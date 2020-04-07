package controllers;

import java.time.LocalDate;
import java.util.List;

import domein.SessieKalender;
import repository.GenericDaoJpa;
import repository.SessieDao;
import repository.SessieDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class SessieController {

	//Properties
	private SessieKalenderDao sessiekalenderRepository;
	private SessieDao sessieRepository;
	private List<SessieKalender> sessieKalenderList;

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
	
	public void maakNieuweSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = new SessieKalender(startDatum, eindDatum);
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.insert(sk);
		GenericDaoJpa.commitTransaction();
	}
	
	public void verwijderSessieKalender(String beginjaar) {
		SessieKalender sk = sessiekalenderRepository.getByBeginjaar(beginjaar);
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.delete(sk);
		GenericDaoJpa.commitTransaction();
	}
	
	private List<SessieKalender> getSessieKalenderList(){
		if(sessieKalenderList==null) {
			sessieKalenderList=sessiekalenderRepository.getAll();
		}
		return sessieKalenderList;
	}
	
	//Sessie methods
	
}
