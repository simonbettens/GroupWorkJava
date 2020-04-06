package ui;

import java.time.LocalDate;
import java.util.List;

import controllers.GebruikerController;
import controllers.SessieController;
import domein.Aankondiging;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.Media;
import domein.Sessie;
import domein.SessieGebruiker;
import domein.SessieKalender;
import domein.StatusType;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;
import repository.SessieDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class ConsoleApp {

	public ConsoleApp() {
		run();
	}

	private void run() {
		maakEenTestGebruiker();
		inloggen();
		//databankTest();
		sessieControllerTest();
		
	}

	private void maakEenTestGebruiker() {
		// TODO Auto-generated method stub
		GebruikerController gc = new GebruikerController(new GebruikerDaoJpa());
		Gebruiker nieuwegebruiker = new Gebruiker("testVoornaam", "testAchternaam", "tst1234", "test", "test.test@student.hogent.be", 123L,
				GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		gc.insertGebruiker(nieuwegebruiker);
	}

	private void inloggen() {
		
	}

	private void databankTest() {
		System.out.println("Begin overlopen alles");
		System.out.printf("Aankondingingen : %n%n");
		GenericDaoJpa<Aankondiging> aankondigingRepo = new GenericDaoJpa<>(Aankondiging.class);
		List<Aankondiging> aankondingenLijst = aankondigingRepo.getAll();
		aankondingenLijst.forEach(a -> System.out.println(a.toString()));
		System.out.printf("%n%nMedia : %n%n");
		GenericDaoJpa<Media> mediaRepo = new GenericDaoJpa<>(Media.class);
		List<Media> mediaLijst = mediaRepo.getAll();
		mediaLijst.forEach(m -> System.out.println(m.toString()));
		System.out.printf("%n%nSessieGebruiker : %n%n");
		GenericDaoJpa<SessieGebruiker> sessiegebruikerRepo = new GenericDaoJpa<>(SessieGebruiker.class);
		List<SessieGebruiker> sessiegebruikerLijst = sessiegebruikerRepo.getAll();
		sessiegebruikerLijst.forEach(a -> System.out.println(a.toString()));
		System.out.printf("%n%nGebruiker : %n%n");
		GebruikerDaoJpa gebruikerDaoJpa = new GebruikerDaoJpa();
		List<Gebruiker> gebruikers = gebruikerDaoJpa.getAll();
		gebruikers.forEach(g -> System.out.println(g.toString()));
		System.out.printf("%n%nSessies : %n%n");
		SessieDaoJpa sessieDaoJpa = new SessieDaoJpa();
		List<Sessie> sessies = sessieDaoJpa.getAll();
		sessies.forEach(s -> System.out.println(s.toString()));
	}
	
	private void sessieControllerTest() {
		SessieController sc = new SessieController();
		SessieKalender sk = sc.geefSessieKalender("0");
		System.out.println(sk.getStartDatum() + "-" + sk.getEindDatum());
		sk = sc.geefSessieKalender("2020");
		System.out.println(sk.getStartDatum() + "-" + sk.getEindDatum());
	}
	
	private void sluitDatabank() {
		GenericDaoJpa.closePersistency();
	}

}
