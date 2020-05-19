package gui;

import controllers.AankondigingController;
import controllers.FeedbackController;
import controllers.GebruikerController;
import controllers.InschrijvingController;
import controllers.MediaController;
import controllers.SessieController;
import controllers.StatistiekController;
import domein.Gebruiker;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;
import repository.SessieDao;
import repository.SessieDaoJpa;
import repository.SessieGebruikerDaoJpa;

public class ApplicatieController extends HBox{
	private MenuDeelSchermController menu;
	private DeelScherm deelScherm;
	private GebruikerController gebruikerController;
	private SessieController sessieController;
	private MediaController mediaController;
	private AankondigingController aankondigingController;
	private InschrijvingController inschrijvingController;
	private FeedbackController feedbackController;
	private Gebruiker ingelogdeGebruiker;
	private StatistiekController statistiekController;
	public ApplicatieController(GebruikerController gebruikerController) {
		this.gebruikerController = gebruikerController;
		ingelogdeGebruiker= gebruikerController.getIngelogdeGebruiker();
		SessieDao sessierepo = new SessieDaoJpa();
		sessieController = new SessieController(this.ingelogdeGebruiker,gebruikerController.getGebruikerRepository(),sessierepo);
		mediaController = new MediaController(ingelogdeGebruiker, sessierepo);
		aankondigingController = new AankondigingController(ingelogdeGebruiker, sessierepo,gebruikerController.getGebruikerRepository());
		SessieGebruikerDaoJpa inschrijvingrepo = new SessieGebruikerDaoJpa();
		inschrijvingController = new InschrijvingController(ingelogdeGebruiker,sessierepo,inschrijvingrepo);
		feedbackController = new FeedbackController(ingelogdeGebruiker, sessierepo);
		statistiekController = new StatistiekController(ingelogdeGebruiker, sessierepo,inschrijvingrepo,
				gebruikerController.getGebruikerRepository(),sessieController.getSessiekalenderRepository());
		buildGui(1);
	}

	public void buildGui(int welkeDeelScherm) {
		this.menu = new MenuDeelSchermController(this);
		this.deelScherm = DeelSchermFactory.create(welkeDeelScherm);
		this.deelScherm.buildGui(this);
		this.getChildren().add(menu);
		this.getChildren().add((Node) deelScherm);
		this.setHgrow((Node)deelScherm, Priority.ALWAYS);
		this.setAlignment(Pos.CENTER_LEFT);
		this.setId("background");
		
	}
	public void update(int welkeDeelScherm) {
		this.getChildren().removeAll(menu,(Node) deelScherm);
		buildGui(welkeDeelScherm);
	}
	
	public void logout() {
		this.getChildren().removeAll(menu,(Node) deelScherm);
		LoginSchermGridPaneController lgpc = new LoginSchermGridPaneController();
        String s = "inloggen";
        Rectangle2D bounds= Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(lgpc, 700, 500);
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle(s);
        stage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
		stage.setResizable(false);
		stage.setMaximized(false);
		stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
		stage.setWidth(700);
        stage.setHeight(500);
        stage.show();
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
	
	public FeedbackController getFeedbackController() {
		return feedbackController;
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public StatistiekController getStatistiekController() {
		return statistiekController;
	}
	
	
}
