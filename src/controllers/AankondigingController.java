package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import domein.Aankondiging;
import domein.AankondigingPrioriteit;
import domein.Gebruiker;
import domein.MailHelper;
import domein.MediaType;
import domein.Sessie;
import domein.SessieAankondiging;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.AankondigingDao;
import repository.AankondigingDaoJpa;
import repository.GebruikerDao;
import repository.GenericDaoJpa;
import repository.SessieAankondigingDao;
import repository.SessieAankondigingDaoJpa;
import repository.SessieDao;

public class AankondigingController {
	private SessieAankondigingDao sessieAankondigingRepository;
	private AankondigingDao aankondigingRepository;
	private SessieDao sessieRepository;
	private GebruikerDao gebruikerDao;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Sessie gekozenSessie;

	// SessieAankondigingen bij een gekozen Sessie;
	private SessieAankondiging gekozenSessieAankondiging;
	private List<SessieAankondiging> sessieAankondigingenLijst;
	private ObservableList<SessieAankondiging> sessieAankondigingObservableLijst;
	private FilteredList<SessieAankondiging> filteredSessieAankondigingenLijst;
	private SortedList<SessieAankondiging> sortedSessieAankondigingenLijst;
	
	// Algemene aankondigingen
	private Aankondiging gekozenAankondiging;
	private List<Aankondiging> aankondigingenLijst;
	private ObservableList<Aankondiging> aankondigingObservableLijst;
	private FilteredList<Aankondiging> filteredAankondigingenLijst;
	private SortedList<Aankondiging> sortedAankondigingenLijst;

	public AankondigingController(Gebruiker ingelogdeGebruiker, SessieDao sessieRepository,GebruikerDao gebruikerDao) {
		this.sessieRepository = sessieRepository;
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.gebruikerDao = gebruikerDao;
		this.sessieAankondigingRepository = new SessieAankondigingDaoJpa();
		this.aankondigingRepository = new AankondigingDaoJpa();
		this.subject = new PropertyChangeSupport(this);
		this.gekozenSessie = null;
		this.gekozenSessieAankondiging =null;
	}
	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}
	public SessieAankondiging getGekozenSessieAankondiging() {
		return gekozenSessieAankondiging;
	}
	public Aankondiging getGekozenAankondiging() {
		return gekozenAankondiging;
	}
	public Sessie getGekozenSessie() {
		return gekozenSessie;
	}
	public void setGekozenSessie(Sessie gekozenSessie) {
		this.gekozenSessie = gekozenSessie;
		vulLijstSessieAankondigingen();
	}

	// SessieAankondiging methodes
	public void vulLijstSessieAankondigingen() {
		System.out.println(gekozenSessie == null ? "null" : "notnull");
		sessieAankondigingenLijst = new ArrayList<>(gekozenSessie.getAankondigingen());
		sessieAankondigingObservableLijst = FXCollections.observableArrayList(sessieAankondigingenLijst);
		System.out.println(sessieAankondigingObservableLijst.size());
		this.filteredSessieAankondigingenLijst = new FilteredList<>(sessieAankondigingObservableLijst, e -> true);
		this.sortedSessieAankondigingenLijst = new SortedList<SessieAankondiging>(filteredSessieAankondigingenLijst,
				Comparator.comparing(SessieAankondiging::getPrioriteit).thenComparing(SessieAankondiging::getGepost));
	}

	public ObservableList<SessieAankondiging> getSessieAankondigingen() {
		return sortedSessieAankondigingenLijst;
	}

	public void setGeselecteerdeSessieAankondiging(SessieAankondiging sa) {
		firePropertyChange("sessieAankondiging", this.gekozenSessieAankondiging, sa);
		this.gekozenSessieAankondiging = sa;
	}

	public void maakSessieAankondiging(String inhoud, AankondigingPrioriteit prioriteit) {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(ingelogdeGebruiker, LocalDateTime.now(), inhoud,
				prioriteit, gekozenSessie);
		gekozenSessie.addAankondiging(sessieAankondiging);
		sessieAankondigingenLijst.add(sessieAankondiging);
		sessieAankondigingObservableLijst.add(sessieAankondiging);
		insertSessieAankondiging(sessieAankondiging);
	}

	public void pasSessieAankondigingAan(String inhoud, AankondigingPrioriteit prioriteit) {
		SessieAankondiging sessieAankondiging = this.gekozenSessieAankondiging;
		if (sessieAankondiging != null) {
			int ver = sessieAankondiging.pasSessieAankondigingAan(inhoud, prioriteit);
			if (ver != 0) {
				updateSessieAankondiging(sessieAankondiging);
				firePropertyChange("sessieAankondiging", this.gekozenSessieAankondiging, sessieAankondiging);
				this.gekozenSessieAankondiging = sessieAankondiging;
			}
		}
	}
	
	public void zoekOpSessieAankondiging(String sav) {
		this.filteredSessieAankondigingenLijst.setPredicate(sessieAankondiging -> {
			boolean naamWaardeLeeg = sav == null || sav.isBlank();

			if (naamWaardeLeeg) {
				return true;
			}
			
			boolean conditieNaam = naamWaardeLeeg ? true
					: sessieAankondiging.getVerantwoordelijke().getVolledigeNaam().toLowerCase().contains(sav)
							|| sessieAankondiging.getVerantwoordelijke().getVolledigeNaam().startsWith(sav);
			return conditieNaam;
		});
	}
	
	//Aankondiging methodes
	public void vulLijstAankondigingen() {
		aankondigingenLijst = new ArrayList<>(aankondigingRepository.getAll().stream().filter(a -> a.getClass().getSimpleName().equalsIgnoreCase("Aankondiging")).collect(Collectors.toList()));
		aankondigingObservableLijst = FXCollections.observableArrayList(aankondigingenLijst);
		System.out.println(aankondigingObservableLijst.size());
		this.filteredAankondigingenLijst = new FilteredList<>(aankondigingObservableLijst, e -> true);
		this.sortedAankondigingenLijst = new SortedList<Aankondiging>(filteredAankondigingenLijst,
				Comparator.comparing(Aankondiging::getPrioriteit).thenComparing(Aankondiging::getGepost));
	}
	
	public ObservableList<Aankondiging> getAankondigingen() {
		vulLijstAankondigingen();
		return sortedAankondigingenLijst;
	}
	
	public void setGeselecteerdeAankondiging(Aankondiging a) {
		firePropertyChange("aankondiging", this.gekozenAankondiging, a);
		this.gekozenAankondiging = a;
	}

	public void maakAankondiging(String inhoud, AankondigingPrioriteit prioriteit) {
		Aankondiging aankondiging = new Aankondiging(ingelogdeGebruiker, LocalDateTime.now(), inhoud,
				prioriteit);
		aankondigingenLijst.add(aankondiging);
		aankondigingObservableLijst.add(aankondiging);
		insertAankondiging(aankondiging);
		this.gekozenAankondiging = aankondiging;
		firePropertyChange("aankondiging", null, aankondiging);
	}
	
	public void pasAankondigingAan(String inhoud, AankondigingPrioriteit prioriteit) {
		Aankondiging aankondiging = this.gekozenAankondiging;
		if (aankondiging != null) {
			int ver = aankondiging.pasAankondigingAan(inhoud, prioriteit);
			if (ver != 0) {
				updateAankondiging(aankondiging);
				firePropertyChange("aankondiging", this.gekozenAankondiging, aankondiging);
				this.gekozenAankondiging = aankondiging;
			}
		}
	}
	
	public void zoekOpAankondiging(String av) {
		this.filteredAankondigingenLijst.setPredicate(aankondiging -> {
			boolean naamWaardeLeeg = av == null || av.isBlank();

			if (naamWaardeLeeg) {
				return true;
			}
			
			boolean conditieNaam = naamWaardeLeeg ? true
					: aankondiging.getVerantwoordelijke().getVolledigeNaam().toLowerCase().contains(av)
							|| aankondiging.getVerantwoordelijke().getVolledigeNaam().startsWith(av);
			return conditieNaam;
		});
	}
	
	//--- email operaties 
	public void verstuurMailAankondiging(String pass) throws MessagingException {
		List<String> emails = new ArrayList<String>();
		List<Gebruiker> gebruikers = gebruikerDao.getAll();
		gebruikers.stream().forEach(g->emails.add(g.getEmail()));
	    MailHelper.verstuurMailAankondiging(gekozenAankondiging.getVerantwoordelijke().getEmail(), pass, emails,gekozenAankondiging);
	}
	public void verstuurMailSessieAankondiging(String pass) throws MessagingException {
		List<String> emails = new ArrayList<>();
		List<Gebruiker> gebruikers = new ArrayList<>();
		gekozenSessieAankondiging.getSessie().getGebruikersIngeschreven().stream().forEach(in->gebruikers.add(in.getGebruiker()));
		gebruikers.stream().forEach(g->emails.add(g.getEmail()));
	    MailHelper.verstuurMailSessieAankondiging(gekozenSessieAankondiging.getVerantwoordelijke().getEmail(), pass, emails,gekozenSessieAankondiging);
	}

	// ---SessieAankondiging databank operaties
	public void insertSessieAankondiging(SessieAankondiging sa) {
		GenericDaoJpa.startTransaction();
		sessieAankondigingRepository.insert(sa);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
	}

	public void updateSessieAankondiging(SessieAankondiging sa) {
		GenericDaoJpa.startTransaction();
		sessieAankondigingRepository.update(sa);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
	}

	public void deleteSessieAankondiging() {
		SessieAankondiging teVerwijderenSessieAankondiging = this.gekozenSessieAankondiging;
		sessieAankondigingenLijst.remove(teVerwijderenSessieAankondiging);
		gekozenSessie.removeAankondiging(teVerwijderenSessieAankondiging);
		sessieAankondigingObservableLijst.remove(teVerwijderenSessieAankondiging);
		GenericDaoJpa.startTransaction();
		sessieAankondigingRepository.delete(teVerwijderenSessieAankondiging);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("sessieAankondiging", this.gekozenSessieAankondiging, null);
		this.gekozenSessieAankondiging = null;
	}
	
	// ---Aankondiging databank operaties
	public void insertAankondiging(Aankondiging a) {
		GenericDaoJpa.startTransaction();
		aankondigingRepository.insert(a);
		GenericDaoJpa.commitTransaction();
	}

	public void updateAankondiging(Aankondiging a) {
		GenericDaoJpa.startTransaction();
		aankondigingRepository.update(a);
		GenericDaoJpa.commitTransaction();
	}

	public void deleteAankondiging() {
		Aankondiging teVerwijderenAankondiging = this.gekozenAankondiging;
		aankondigingenLijst.remove(teVerwijderenAankondiging);
		aankondigingObservableLijst.remove(teVerwijderenAankondiging);
		GenericDaoJpa.startTransaction();
		aankondigingRepository.delete(teVerwijderenAankondiging);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("aankondiging", this.gekozenAankondiging, null);
		this.gekozenAankondiging = null;
	}

	// changeSupport
	private <T> void firePropertyChange(String welke, T oude, T nieuwe) {
		subject.firePropertyChange(welke, oude, nieuwe);
	}

	public void addPropertyChangeListenerSessie(PropertyChangeListener pcl, String welke) {
		subject.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, welke, null, null));
	}

	public void removePropertyChangeListenerSessie(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}
	

}
