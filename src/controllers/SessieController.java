package controllers;

import domein.SessieKalender;
import repository.SessieKalenderDao;

public class SessieController {

	private SessieKalenderDao sessieKalenderRepository;
	private String academiejaar;
	private SessieKalender sessieKalender;

	public SessieController(SessieKalenderDao sessieKalenderRepository) {
		this.sessieKalenderRepository = sessieKalenderRepository;
	}
	
	public SessieKalender geefSessieKalender(String academiejaar) {
		try {
			if (academiejaar.isBlank() || academiejaar == null) {
				//TODO kalender van huidig jaar
				
			} else return null;
		} catch (Exception e) {
			// TODO 
			System.err.printf("Kalender niet gevonden.\n");
			e.printStackTrace();
		}
		throw new java.lang.UnsupportedOperationException("Not supported yet.");
	}
	
	
}
