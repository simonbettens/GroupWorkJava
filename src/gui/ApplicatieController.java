package gui;

import controllers.AankondigingController;
import controllers.GebruikerController;
import controllers.InschrijvingController;
import controllers.MediaController;
import controllers.SessieController;
import domein.Gebruiker;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import repository.SessieDao;
import repository.SessieDaoJpa;

public class ApplicatieController extends HBox{
	private MenuDeelSchermController menu;
	private DeelScherm deelScherm;
	private GebruikerController gebruikerController;
	private SessieController sessieController;
	private MediaController mediaController;
	private AankondigingController aankondigingController;
	private InschrijvingController inschrijvingController;
	private Gebruiker ingelogdeGebruiker;
	public ApplicatieController(GebruikerController gebruikerController) {
		this.gebruikerController = gebruikerController;
		ingelogdeGebruiker= gebruikerController.getIngelogdeGebruiker();
		SessieDao sessierepo = new SessieDaoJpa();
		sessieController = new SessieController(this.ingelogdeGebruiker,gebruikerController.getGebruikerRepository(),sessierepo);
		mediaController = new MediaController(ingelogdeGebruiker, sessierepo);
		aankondigingController = new AankondigingController(ingelogdeGebruiker, sessierepo);
		inschrijvingController = new InschrijvingController(ingelogdeGebruiker,sessierepo);
		buildGui(1);
	}

	public void buildGui(int welkeDeelScherm) {
		this.menu = new MenuDeelSchermController(this);
		this.deelScherm = DeelSchermFactory.create(welkeDeelScherm);
		this.deelScherm.buildGui(this);
		this.getChildren().add(menu);
		this.getChildren().add((Node) deelScherm);
		this.setAlignment(Pos.CENTER_LEFT);
		this.setId("background");
		
	}
	public void update(int welkeDeelScherm) {
		this.getChildren().removeAll(menu,(Node) deelScherm);
		buildGui(welkeDeelScherm);
	}
	
	public GebruikerController getGebruikerController() {
		return gebruikerController;
	}
	
	public SessieController getSessieController() {
		return sessieController;
	}
	
	public MediaController getMediaController() {
		return mediaController;
	}
	
	public AankondigingController getAankondigingController() {
		return aankondigingController;
	}

	public InschrijvingController getInschrijvingController() {
		return inschrijvingController;
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}
	
	
}
