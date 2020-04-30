package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import domein.AankondigingPrioriteit;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieAankondiging;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.GenericDaoJpa;
import repository.SessieAankondigingDao;
import repository.SessieAankondigingDaoJpa;
import repository.SessieDao;

public class AankondigingController {
	private SessieAankondigingDao sessieAankondigingRepository;
	private SessieDao sessieRepository;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Sessie gekozenSessie;

	// SessieAankondigingen bij een gekozen Sessie;
	private SessieAankondiging gekozenSessieAankondiging;
	private List<SessieAankondiging> sessieAankondigingenLijst;
	private ObservableList<SessieAankondiging> sessieAankondigingObservableLijst;
	private FilteredList<SessieAankondiging> filteredSessieAankondigingenLijst;
	private SortedList<SessieAankondiging> sortedSessieAankondigingenLijst;

	public AankondigingController(Gebruiker ingelogdeGebruiker, SessieDao sessieRepository) {
		this.sessieRepository = sessieRepository;
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.sessieAankondigingRepository = new SessieAankondigingDaoJpa();
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
