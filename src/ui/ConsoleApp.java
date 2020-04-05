package ui;

import java.util.List;

import domein.Aankondiging;
import domein.Gebruiker;
import domein.Media;
import domein.Sessie;
import domein.SessieGebruiker;
import repository.GebruikerDaoJpa;
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

		GenericDaoJpa.closePersistency();
	}

}
