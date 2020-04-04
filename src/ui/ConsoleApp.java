package ui;

import java.util.List;

import domein.Aankondiging;
import repository.GenericDaoJpa;

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
		
		GenericDaoJpa<Aankondiging> repo = new GenericDaoJpa<>(Aankondiging.class);
		List<Aankondiging> aankondingenLijst= repo.getAll();
		aankondingenLijst.forEach(a->System.out.println(a.toString()));
	}
	
	
}
