package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import domein.Gebruiker;
import domein.Media;
import domein.Sessie;
import domein.SessieGebruiker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.GenericDaoJpa;
import repository.MediaDao;
import repository.SessieDao;
import repository.SessieGebruikerDao;
import repository.SessieGebruikerDaoJpa;

public class InschrijvingController {
	private SessieGebruikerDao sessieGebruikerRepository;
	private SessieDao sessieRepository;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Sessie gekozenSessie;
	// SessieGebruikers bij een gekozen Sessie;
	private SessieGebruiker gekozenInschrijving;
	private List<SessieGebruiker> inschrijvingenLijst;
	private ObservableList<SessieGebruiker> inschrijvingenObservableLijst;
	private FilteredList<SessieGebruiker> filteredInschrijvingenLijst;
	private SortedList<SessieGebruiker> sortedInschrijvingenLijst;

	public InschrijvingController(Gebruiker ingelogdeGebruiker, SessieDao sessieRepository,SessieGebruikerDao sessieGebruikerRepository) {
		this.sessieGebruikerRepository = sessieGebruikerRepository;
		this.sessieRepository = sessieRepository;
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.subject = new PropertyChangeSupport(this);
		this.gekozenSessie = null;
		this.gekozenInschrijving = null;
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public SessieGebruiker getGekozenInschrijving() {
		return gekozenInschrijving;
	}

	public Sessie getGekozenSessie() {
		return gekozenSessie;
	}

	public void setGekozenSessie(Sessie gekozenSessie) {
		this.gekozenSessie = gekozenSessie;
		vulLijstSessieGebruikers();
	}

	// Inschrijvingen
	public void vulLijstSessieGebruikers() {
		GenericDaoJpa.reload();
		inschrijvingenLijst = sessieGebruikerRepository.getAll().stream().filter(i->i.getSessie().getSessieId()==gekozenSessie.getSessieId()).collect(Collectors.toList());
		inschrijvingenObservableLijst = FXCollections.observableArrayList(inschrijvingenLijst);
		this.filteredInschrijvingenLijst = new FilteredList<>(inschrijvingenObservableLijst, e -> true);
		this.sortedInschrijvingenLijst = new SortedList<SessieGebruiker>(filteredInschrijvingenLijst,
				Comparator.comparing(SessieGebruiker::getVoornaam).thenComparing(SessieGebruiker::getUserName));
	}

	public int getAantalInschrijvingen() {
		return inschrijvingenLijst.size();
	}

	public ObservableList<SessieGebruiker> getSessieGebruikersLijst() {
		return sortedInschrijvingenLijst;
	}

	public void setGeselecteerdeSessieGebruiker(SessieGebruiker sg) {
		firePropertyChange("inschrijving", this.gekozenInschrijving, sg);
		this.gekozenInschrijving = sg;
	}

	public void pasSessieGebruikerAan(boolean aanwezig, boolean aanwezigBevestigd) {
		SessieGebruiker sessieGebruiker = this.gekozenInschrijving;
		if (sessieGebruiker != null) {
			int ver = sessieGebruiker.pasSessieGebruikerAan(aanwezig, aanwezigBevestigd);
			if (ver != 0) {
				updateSessieGebruiker(sessieGebruiker);
				firePropertyChange("inschrijving", this.gekozenInschrijving, sessieGebruiker);
				this.gekozenInschrijving = sessieGebruiker;
			}
		}
	}

	public void zoekOpNaamSessieGebruiker(String naam) {
		this.filteredInschrijvingenLijst.setPredicate(sessieg -> {
			boolean naamWaardeLeeg = naam == null || naam.isBlank();
			if (naamWaardeLeeg) {
				return true;
			}
			boolean conditieNaam = naamWaardeLeeg ? true
					: sessieg.getGebruiker().getVolledigeNaam().toLowerCase().contains(naam)
							|| sessieg.getGebruiker().getVolledigeNaam().toLowerCase().startsWith(naam);
			return conditieNaam;
		});
	}

	// ---Inschrijvingen databank operaties
	public void updateSessieGebruiker(SessieGebruiker sa) {
		GenericDaoJpa.startTransaction();
		sessieGebruikerRepository.update(sa);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
	}

	public void deleteSessieGebruiker() {
		SessieGebruiker teVerwijderenSessieGebruiker = this.gekozenInschrijving;
		inschrijvingenLijst.remove(teVerwijderenSessieGebruiker);
		gekozenSessie.removeInschrijving(teVerwijderenSessieGebruiker);
		inschrijvingenObservableLijst.remove(teVerwijderenSessieGebruiker);
		GenericDaoJpa.startTransaction();
		sessieGebruikerRepository.delete(teVerwijderenSessieGebruiker);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("inschrijving", this.gekozenInschrijving, null);
		this.gekozenInschrijving = null;
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
