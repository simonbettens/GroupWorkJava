package gui;

import controllers.GebruikerController;
import controllers.SessieController;
import domein.Gebruiker;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class ApplicatieController extends HBox{
	private MenuDeelSchermController menu;
	private DeelScherm deelScherm;
	private GebruikerController gebruikerController;
	private SessieController sessieController;
	private Gebruiker ingelogdeGebruiker;
	public ApplicatieController(GebruikerController gebruikerController) {
		
		this.gebruikerController = gebruikerController;
		ingelogdeGebruiker= gebruikerController.getIngelogdeGebruiker();
		this.sessieController = new SessieController(this.ingelogdeGebruiker);
		//ingelogde gebruiker moet nog mee gegeven worden aan de sessiecontroller
		buildGui(0);
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

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}
	
	
}
