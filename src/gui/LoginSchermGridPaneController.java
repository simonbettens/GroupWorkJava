package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class LoginSchermGridPaneController extends GridPane {
	public LoginSchermGridPaneController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginSchermTest.fxml"));
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

    }

    @FXML
    void rightPaneClicked(MouseEvent event) {

    }

}
