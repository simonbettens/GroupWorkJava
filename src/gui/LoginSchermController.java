package gui;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import controllers.GebruikerController;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.PasswoordHasher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class LoginSchermController extends AnchorPane {
	private GebruikerController gebruikerController;
	public LoginSchermController(GebruikerController gController) {

		this.gebruikerController = gController;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScherm.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
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
	    void login(MouseEvent event) {
	    	String gebruikerNaam = this.fieldUsername.getText();
			String wachtwoord = this.fieldPassword.getText();
			Gebruiker gebruiker = null;
			boolean gelijkaardig = false;
			if (gebruikerNaam.isEmpty() || gebruikerNaam.isBlank()) {
				System.out.println("Gebruikernaam is niet ingevuld");
			} else {
				if (wachtwoord.isEmpty() || wachtwoord.isBlank()) {
					System.out.println("Wachtwoordveld is niet ingevuld");
				} else {
					try {
						gebruiker = gebruikerController.getGebruikerByUsername(gebruikerNaam);
						if(gebruiker.getType()!=GebruikerType.GEBRUIKER) {
							gelijkaardig = PasswoordHasher.verifyPasswordHash(gebruiker.getPasswoordHash(), wachtwoord);
						}else {
							System.out.println("Enkel (hoofd-)verantwoordelijken kunnen zich inloggen op deze applicatie");
						}
					}catch(EntityNotFoundException e) {
						System.out.println("Gebruiker bestaat niet");
					}
					catch(Exception e) {
						System.out.println("Er ging iets anders mis" + e.getMessage());
					}
				}
			}
			if(gelijkaardig) {
				System.out.println("Wachtwoord komt overeen en gebruiker is ingelogd");
				gebruikerController.setIngelogdeGebruiker(gebruiker);
			}else{
				System.out.println("Gebruiker niet ingelogd");
			}

	    }

	    @FXML
	    void rightPaneClicked(MouseEvent event) {

	    }

	}

