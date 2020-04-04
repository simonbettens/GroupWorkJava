package ui;

import java.util.List;

import domein.Aankondiging;
import domein.Media;
import domein.Sessie;
import repository.GenericDaoJpa;
import repository.SessieDaoJpa;

public class ConsoleApp {

	public ConsoleApp() {
		run();
	}
	private void run() {
		inloggen();
		databankTest();
	}
	private void inloggen() {
		
	}
	private void databankTest() {
		
		/*
		 * GenericDaoJpa<Aankondiging> aankondigingRepo = new
		 * GenericDaoJpa<>(Aankondiging.class); List<Aankondiging> aankondingenLijst=
		 * aankondigingRepo.getAll();
		 * aankondingenLijst.forEach(a->System.out.println(a.toString()));
		 * 
		 * GenericDaoJpa<Media> mediaRepo = new GenericDaoJpa<>(Media.class);
		 * List<Media> mediaLijst= mediaRepo.getAll(); mediaLijst.forEach(m->
		 * System.out.println(m.toString()));
		 */
		
		SessieDaoJpa sessieDaoJpa = new SessieDaoJpa();
		List<Sessie> sessies = sessieDaoJpa.getAll();
		sessies.forEach(s -> System.out.println(s.toString()));
		
		GenericDaoJpa.closePersistency();
	}
	
	
}
