package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domein.Gebruiker;
import domein.GebruikerType;
import domein.StatusType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

public class GebruikerController {
	private List<Gebruiker> gebruikerLijst;
	private ObservableList<String> gebruikerObsLijst;
	private Gebruiker ingelogdeGebruiker;
	private Gebruiker geselecteerdeGebruiker;
	private PropertyChangeSupport subject;
	public GebruikerDaoJpa gebruikerRepository;

	public GebruikerController(GebruikerDaoJpa gebruikerRepository) {
		this.gebruikerRepository = gebruikerRepository;
		gebruikerLijst = new ArrayList<>();
		subject = new PropertyChangeSupport(this);
		vulLijsten();
	}

	private void vulLijsten() {
		gebruikerLijst = gebruikerRepository.getAll();
		setObservableList();
	}

	private void setObservableList() {
		gebruikerObsLijst = FXCollections.observableArrayList(getGebruikers());
	}

	private List<String> getGebruikers() {
		return gebruikerLijst.stream().distinct().map(Gebruiker::getVolledigeNaam).collect(Collectors.toList());
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public Gebruiker getGeselecteerdeGebruiker() {
		return geselecteerdeGebruiker;
	}

	public void setIngelogdeGebruiker(Gebruiker ingelogdeGebruiker) {
		this.ingelogdeGebruiker = ingelogdeGebruiker;
	}

	public void setGeselecteerdeGebruiker(String volledigeNaam) {
		Gebruiker geselecteerdeGebruiker = gebruikerLijst.stream()
				.filter(g -> g.getVolledigeNaam().equals(volledigeNaam)).findFirst().orElse(null);
		firePropertyChange("geselecteerdeGebruiker", this.geselecteerdeGebruiker, geselecteerdeGebruiker);
		this.geselecteerdeGebruiker = geselecteerdeGebruiker;
	}

	public Gebruiker getGebruikerById(String id) {
		return gebruikerRepository.get(id);
	}

	public Gebruiker getGebruikerByUsername(String username) {
		return gebruikerRepository.getByUsername(username);
	}

	public void deleteGebruiker() {
		Gebruiker teVerwijderenGebruiker = this.geselecteerdeGebruiker;
		gebruikerLijst.remove(teVerwijderenGebruiker);
		gebruikerObsLijst.remove(teVerwijderenGebruiker.getVolledigeNaam());
		GenericDaoJpa.startTransaction();
		gebruikerRepository.delete(teVerwijderenGebruiker);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("geselecteerdeGebruiker", this.geselecteerdeGebruiker, null);
		this.geselecteerdeGebruiker = null;
	}

	public void insertGebruiker(Gebruiker gebruiker) {
		GenericDaoJpa.startTransaction();
		gebruikerRepository.insert(gebruiker);
		GenericDaoJpa.commitTransaction();
	}

	public void rollBack() {
		GenericDaoJpa.rollbackTransaction();
	}

	public void maakGebruiker(String voornaam, String achternaam, String userName, String passwoord, String email,
			Long idNummer, GebruikerType type, StatusType status) {
		Gebruiker gebruiker = new Gebruiker(voornaam, achternaam, userName, passwoord, email, idNummer, type, status);
		// kijken of de gebruikerid al bestaat en dan een nieuwe laten genereren
		while (gebruikerRepository.exists(gebruiker.getId())) {
			gebruiker.genereerNieuwId();
		}
		insertGebruiker(gebruiker);
		gebruikerLijst.add(gebruiker);
		gebruikerObsLijst.add(gebruiker.getVolledigeNaam());
	}

	public void pasGebruikerAan(String voornaam, String achternaam, String userName, String passwoord, String email,
			Long idNummer, GebruikerType type, StatusType status) {
		Gebruiker gebruiker = this.geselecteerdeGebruiker;
		int veranderingen = gebruiker.pasGebruikerAan(voornaam, achternaam, userName, passwoord, email, idNummer, type,
				status);
		if (veranderingen > 0) {
			GenericDaoJpa.startTransaction();
			gebruikerRepository.update(this.geselecteerdeGebruiker);
			GenericDaoJpa.commitTransaction();
			firePropertyChange("geselecteerdeGebruiker", this.geselecteerdeGebruiker, gebruiker);
		}
	}
	private void firePropertyChange(String welke,Gebruiker oude,Gebruiker nieuwe) {
		subject.firePropertyChange(welke,oude, nieuwe);
	}
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		subject.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, "geselecteerdeGebruiker", null, this.geselecteerdeGebruiker));
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}

}
