package ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import controllers.GebruikerController;
import controllers.SessieController;
import domein.Aankondiging;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.Media;
import domein.PasswoordHasher;
import domein.Sessie;
import domein.SessieGebruiker;
import domein.SessieKalender;
import domein.StatusType;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;
import repository.SessieDaoJpa;

public class ConsoleApp {
	private Scanner sc = new Scanner(System.in);
	public GebruikerController gc;
	public ConsoleApp() {
		gc = new GebruikerController();
		run();
	}

	private void run() {
		//maakEenTestGebruiker();
		//inloggen();
		//databankTest();
		sessieControllerTest();
		sluitDatabank();
	}

	private void maakEenTestGebruiker() {
		// TODO Auto-generated method stub
		
		Gebruiker nieuwegebruiker = new Gebruiker("testVoornaam", "testAchternaam", "test123", "testpass", "test.test@student.hogent.be", 1L,
				GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		
		gc.insertGebruiker(nieuwegebruiker);
		System.out.println(nieuwegebruiker.toString());
		/*
		System.out.println("Wachten op gebruikerinput voor verwijderen ");
		sc.nextLine();
		gc.deleteGebruiker(nieuwegebruiker);
		System.out.println("gebruiker verwijderd");
		*/
	}

	private void inloggen() {
		Gebruiker opgehaaldeGebruiker = gc.getGebruikerByUsername("test123");
		System.out.println("Geef het passwoord in voor de volgende gebruiker" + opgehaaldeGebruiker.toString());
		String ingegevenPass = sc.nextLine();
		boolean gelijkaardig = PasswoordHasher.verifyPasswordHash(opgehaaldeGebruiker.getPasswoordHash(), ingegevenPass);
		System.out.println("Gelijkheid?" + gelijkaardig);
		
		System.out.println("Gebruiker verwijderen?(druk enter)");
		sc.nextLine();
		gc.deleteGebruiker();
		
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
		
		sc.maakNieuweSessieKalender(LocalDate.now().minusYears(2), LocalDate.now().minusYears(1).minusDays(5));
		SessieKalender sk2 = sc.geefSessieKalender("2018");
		System.out.println(sk2.getStartDatum());
		sc.verwijderSessieKalender("2018");
	}
	
	private void sluitDatabank() {
		GenericDaoJpa.closePersistency();
	}

}
