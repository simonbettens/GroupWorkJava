package gui;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import controllers.GebruikerController;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.PasswoordHasher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import repository.GebruikerDaoJpa;

public class LoginSchermGridPaneController extends GridPane {

    @FXML
    private ImageView imageItLab;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private TextField fieldUsername;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Button buttonLogin;

    @FXML
    private Hyperlink hlForgotPassword;

    @FXML
    private ImageView imageGroup;

    @FXML
    private Text textUserLogin;

    @FXML
    private ImageView imagePerson;

    @FXML
    private ImageView imageLock;
    @FXML
    private Label lblFout;
    
	private GebruikerController gebruikerController;
	
	public LoginSchermGridPaneController() {
		this.gebruikerController = new GebruikerController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginSchermTest.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		lblFout.setText("");
		fieldPassword.setFocusTraversable(true);
		fieldUsername.setOnKeyReleased(e->{
			if(e.getCode() == KeyCode.TAB) {
				lblFout.setText("");
				fieldPassword.requestFocus();
			}
		});
		fieldPassword.setOnKeyReleased(e->{
			if(e.getCode() == KeyCode.ENTER) {
				lblFout.setText("");
				login(null);
			}
		});
	}


    @FXML
    void forgotPassword(MouseEvent event) {

    }

    @FXML
    void hoverExitForgotPassword(MouseEvent event) {

    }

    @FXML
    void hoverExitLock(MouseEvent event) {

    }

    @FXML
    void hoverExitLogin(MouseEvent event) {

    }

    @FXML
    void hoverExitPerson(MouseEvent event) {

    }

    @FXML
    void hoverForgotPassword(MouseEvent event) {

    }

    @FXML
    void hoverLock(MouseEvent event) {

    }

    @FXML
    void hoverLogin(MouseEvent event) {

    }

    @FXML
    void hoverPerson(MouseEvent event) {

    }

    @FXML
    public void login(MouseEvent event) {
    	String gebruikerNaam = this.fieldUsername.getText();
		String wachtwoord = this.fieldPassword.getText();
		Gebruiker gebruiker = null;
		boolean gelijkaardig = false;
		
		if (gebruikerNaam.isEmpty() || gebruikerNaam.isBlank()) {
			System.out.println("Gebruikernaam is niet ingevuld");
			lblFout.setText("Gebruikernaam is niet ingevuld");
			return;
		} else {
			if (wachtwoord.isEmpty() || wachtwoord.isBlank()) {
				System.out.println("Wachtwoordveld is niet ingevuld");
				lblFout.setText("Wachtwoordveld is niet ingevuld");
				return;
			} else {
				try {
					gebruiker = gebruikerController.getGebruikerByUsername(gebruikerNaam);
					if(gebruiker.getType()!=GebruikerType.GEBRUIKER) {
						gelijkaardig = PasswoordHasher.verifyPasswordHash(gebruiker.getPasswoordHash(), wachtwoord);
					}else {
						System.out.println("Enkel (hoofd-)verantwoordelijken kunnen zich inloggen op deze applicatie");
						lblFout.setText("Enkel (hoofd-)verantwoordelijke \nkunnen zich inloggen");
						return;
					}
				}catch(EntityNotFoundException e) {
					System.out.println("Gebruiker bestaat niet");
					lblFout.setText("Gebruiker bestaat niet");
					return;
				}
				catch(Exception e) {
					System.out.println("Er ging iets anders mis" + e.getMessage());
					lblFout.setText("Er ging iets anders mis");
					return;
				}
			}
		}
		if(gelijkaardig) {
			System.out.println("Wachtwoord komt overeen en gebruiker is ingelogd");
			gebruikerController.setIngelogdeGebruiker(gebruiker);
			lblFout.setText("");
			switchScherm();
		}else{
			System.out.println("Gebruiker niet ingelogd");
			lblFout.setText("Wachtwoord is niet correct");
		}

    }

    private void switchScherm() {
        ApplicatieController nss = new ApplicatieController(gebruikerController);
        String s = "It-lab";
        Rectangle2D bounds= Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(nss, bounds.getWidth(), bounds.getHeight());
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setResizable(true);
        stage.setTitle(s);
        stage.setScene(scene);
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setMaximized(true);
        //je kan enkel met dit scherm bezig zijn
        //nieuwSpelStage.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        //nieuwSpelStage.initOwner(achtergrond);
        // Set position of second window, related to primary window.
        //nieuwSpelStage.setX(achtergrond.getX() + 200);
        //nieuwSpelStage.setY(achtergrond.getY() + 100);
        //nieuwSpelScherm.getStylesheets().addAll(this.getClass().getResource("/css/style.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        stage.show();
	}


	@FXML
    void rightPaneClicked(MouseEvent event) {

    }

}
