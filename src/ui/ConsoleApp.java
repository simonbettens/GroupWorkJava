package ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controllers.GebruikerController;
import controllers.SessieController;
import domein.Aankondiging;
import domein.AankondigingPrioriteit;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.MailHelper;
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
		
	}
	public static void main(String[] args) {
		System.out.println("Voor mail verstuurd");
		Gebruiker gebruiker = new Gebruiker("Bettens", "Simon", "sb123456", "test", "simon.bettens@student.hogent.be", 1103720666030L, GebruikerType.VERANTWOORDELIJKE, StatusType.ACTIEF);
		Aankondiging aankondiging = new Aankondiging(gebruiker, LocalDateTime.now().plusMinutes(2), "Test aankondiging", AankondigingPrioriteit.LAAG);
		List<String> mailAdressen = new ArrayList<String>();
		mailAdressen.add("simon.bettens@live.be");
		
		
		MailHelper.verstuurMailAankondiging("simon.bettens@student.hogent.be","...",mailAdressen ,aankondiging);
		System.out.println("Na mail verstuurd");
	}
	

}
