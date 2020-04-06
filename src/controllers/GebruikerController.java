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
		GebruikerDaoJpa.startTransaction();
		gebruikerRepository.delete(gebruiker);
		GebruikerDaoJpa.commitTransaction();
	}
	public void insertGebruiker(Gebruiker gebruiker) {
		GebruikerDaoJpa.startTransaction();
		gebruikerRepository.insert(gebruiker);
		GebruikerDaoJpa.commitTransaction();
	}
	public void rollBack() {
		GebruikerDaoJpa.rollbackTransaction();
	}
	
	
	
}
