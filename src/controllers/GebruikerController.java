package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import domein.Gebruiker;
import domein.GebruikerType;
import domein.Maand;
import domein.StatusType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

public class GebruikerController {
	private List<Gebruiker> gebruikerLijst;
	private ObservableList<Gebruiker> gebruikerObsLijst;
	private FilteredList<Gebruiker> filteredGebruikerLijst;
	private SortedList<Gebruiker> sortedGebruikerLijst;
	private Gebruiker ingelogdeGebruiker;
	private Gebruiker geselecteerdeGebruiker;
	private PropertyChangeSupport subject;
	private GebruikerDaoJpa gebruikerRepository;

	public GebruikerController() {
		setGebruikerRepository(new GebruikerDaoJpa());
		gebruikerLijst = new ArrayList<>();
		subject = new PropertyChangeSupport(this);
		vulLijsten();
	}

	private void vulLijsten() {
		gebruikerLijst = gebruikerRepository.getAll();
		gebruikerObsLijst = FXCollections.observableArrayList(gebruikerLijst);
		filteredGebruikerLijst = new FilteredList<>(gebruikerObsLijst, e -> true);
		sortedGebruikerLijst = new SortedList<Gebruiker>(filteredGebruikerLijst,
				Comparator.comparing(Gebruiker::getVolledigeNaam).thenComparing(Gebruiker::getUserName)
						.thenComparing(Gebruiker::getType));
	}
	public List<Gebruiker> getVerantwoordelijkeList(){
		return gebruikerRepository.getByDiscriminator("Verantwoordelijke");
		
	}
	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public Gebruiker getGeselecteerdeGebruiker() {
		return geselecteerdeGebruiker;
	}

	public ObservableList<Gebruiker> getSortedGebruikerLijst() {
		return sortedGebruikerLijst;
	}

	private void setGebruikerRepository(GebruikerDaoJpa gebruikerRepository) {
		this.gebruikerRepository = gebruikerRepository;
	}

	public void setIngelogdeGebruiker(Gebruiker ingelogdeGebruiker) {
		this.ingelogdeGebruiker = ingelogdeGebruiker;
	}

	public void setGeselecteerdeGebruiker(Gebruiker gebruiker) {
		firePropertyChange("geselecteerdeGebruiker", this.geselecteerdeGebruiker, gebruiker);
		this.geselecteerdeGebruiker = gebruiker;
	}

	public Gebruiker getGebruikerById(String id) {
		return gebruikerRepository.get(id);
	}

	public Gebruiker getGebruikerByUsername(String username) {
		return gebruikerRepository.getByUsername(username);
	}

	public GebruikerDaoJpa getGebruikerRepository() {
		return gebruikerRepository;
	}

	public void deleteGebruiker() {
		Gebruiker teVerwijderenGebruiker = this.geselecteerdeGebruiker;
		gebruikerLijst.remove(teVerwijderenGebruiker);
		gebruikerObsLijst.remove(teVerwijderenGebruiker);
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
	public void zoekOpNaam(String zoekwaarde) {
		veranderFilter(zoekwaarde);
		
	}
	private void veranderFilter( String naamWaarde) {
		this.filteredGebruikerLijst.setPredicate(g -> {
			boolean naamWaardeLeeg = naamWaarde == null || naamWaarde.isBlank();
			if (naamWaardeLeeg) {
				return true;
			}
			boolean conditieNaam = naamWaardeLeeg ? true
					: g.getVolledigeNaam().toLowerCase().contains(naamWaarde)
							|| g.getVolledigeNaam().toLowerCase().startsWith(naamWaarde);

			return conditieNaam;
		});
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
		gebruikerObsLijst.add(gebruiker);
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
			this.geselecteerdeGebruiker = gebruiker;
			firePropertyChange("geselecteerdeGebruiker", this.geselecteerdeGebruiker, gebruiker);
		}
	}

	private void firePropertyChange(String welke, Gebruiker oude, Gebruiker nieuwe) {
		subject.firePropertyChange(welke, oude, nieuwe);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		subject.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, "geselecteerdeGebruiker", null, this.geselecteerdeGebruiker));
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}

	

}
