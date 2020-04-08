package controllers;

import java.util.ArrayList;
import java.util.List;

import domein.Gebruiker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

public class GebruikerController {
	public List<Gebruiker> gebruikerLijst;
	public ObservableList<Gebruiker> gebruikerObsLijst;
	public GebruikerDaoJpa gebruikerRepository;
	
	public GebruikerController(GebruikerDaoJpa gebruikerRepository) {
		this.gebruikerRepository = gebruikerRepository;
		gebruikerLijst = new ArrayList<>();
		vulLijsten();
	}
	private void vulLijsten() {
		gebruikerLijst = gebruikerRepository.getAll();
		gebruikerObsLijst = FXCollections.observableArrayList(gebruikerLijst);
	}
	public Gebruiker getGebruikerById(String id) {
		return gebruikerRepository.get(id);
	}
	public Gebruiker getGebruikerByUsername(String username) {
		return gebruikerRepository.getByUsername(username);
	}
	public void deleteGebruiker(Gebruiker gebruiker) {
		gebruikerLijst.remove(gebruiker);
		GenericDaoJpa.startTransaction();
		gebruikerRepository.delete(gebruiker);
		GenericDaoJpa.commitTransaction();
	}
	public void insertGebruiker(Gebruiker gebruiker) {
		GenericDaoJpa.startTransaction();
		gebruikerRepository.insert(gebruiker);
		GenericDaoJpa.commitTransaction();
	}
	public void rollBack() {
		GenericDaoJpa.rollbackTransaction();
	}
	
	
	
}
