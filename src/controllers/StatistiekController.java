package controllers;

import java.beans.PropertyChangeSupport;
import java.util.List;

import domein.Aankondiging;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieAankondiging;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.AankondigingDao;
import repository.AankondigingDaoJpa;
import repository.SessieAankondigingDao;
import repository.SessieAankondigingDaoJpa;
import repository.SessieDao;

public class StatistiekController {

	private SessieDao sessieRepository;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Sessie gekozenSessie;

	public StatistiekController(Gebruiker ingelogdeGebruiker, SessieDao sessieRepository) {
		this.sessieRepository = sessieRepository;
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.subject = new PropertyChangeSupport(this);
		this.gekozenSessie = null;
	}
	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}
	public Sessie getGekozenSessie() {
		return gekozenSessie;
	}
	public void setGekozenSessie(Sessie gekozenSessie) {
		this.gekozenSessie = gekozenSessie;
		maakGrafiek();
	}
	private void maakGrafiek() {
		// TODO Auto-generated method stub
		
	}
}
