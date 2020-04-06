package controllers;

import domein.SessieKalender;
import repository.SessieDao;
import repository.SessieDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class SessieController {

	//Properties
	private SessieKalenderDao sessiekalenderRepository;
	private SessieDao sessieRepository;

	//Constructor
	public SessieController() {
		setSessiekalenderRepository(new SessieKalenderDaoJpa());
	}
	
	
	//Setters
	public void setSessiekalenderRepository(SessieKalenderDaoJpa mock) {
		this.sessiekalenderRepository = mock;
	}
	public void setSessieRepository(SessieDaoJpa mock) {
		this.sessieRepository = mock;
	}
	
	
	//Sessiekalender methods
	public SessieKalender geefSessieKalender(String beginjaar) {
		try {
			if (beginjaar == "0") {
				return sessiekalenderRepository.getByBeginjaar("2020");
			} else {
				return sessiekalenderRepository.getByBeginjaar(beginjaar);
			}
		} catch (Exception e) {
			// TODO 
			System.err.printf("Kalender niet gevonden.\n");
			e.printStackTrace();
		}
		return null;
	}
	
	
	//Sessie methods
	
}
