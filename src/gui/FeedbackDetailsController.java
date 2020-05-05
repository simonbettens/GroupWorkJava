package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Optional;

import controllers.FeedbackController;
import controllers.SessieController;
import domein.DatumEnTijdHelper;
import domein.Feedback;
import domein.Sessie;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

public class FeedbackDetailsController extends VBox
		implements DeelScherm<SessieKalenderDeelScherm>, PropertyChangeListener {
	@FXML
	private Label lblSessieNaam;
	@FXML
	private Label lblStart;
	@FXML
	private Label lblEind;
	@FXML
	private Label lblNaam;
	@FXML
	private Label lblUsername;
	@FXML
	private TextField txfRating;
	@FXML
	private TextArea txaInhoud;
	@FXML
	private Button btnVerwijder;
	private FeedbackController fc;
	private SessieKalenderDeelScherm parent;
	private SessieController sc;

	@Override
	public void buildGui(SessieKalenderDeelScherm parent) {
		this.parent = parent;
		this.sc = parent.getSessieController();
		this.fc = parent.getFeedbackController();
		Sessie s = sc.getSessie();

		fc.addPropertyChangeListener(this, "feedback");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FeedbackDetails.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		lblSessieNaam.setText(s.getNaam());
		lblStart.setText(DatumEnTijdHelper.dateTimeFormat(s.getStartDatum()));
		lblEind.setText(DatumEnTijdHelper.dateTimeFormat(s.getEindDatum()));
	}

	// Event Listener on Button[#btnVerwijder].onAction
	@FXML
	public void verwijder(ActionEvent event) {
		// TODO Autogenerated
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Feedback verwijderen");
		alert.setHeaderText("Bevestig");
		alert.setContentText("Wens je zeker deze feedback te verwijderen?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		Optional<ButtonType> antwoord = alert.showAndWait();
		if (antwoord.get() == ButtonType.OK) {
			System.out.println("feedback is verwijderd");
			fc.deleteFeedback();
			event.consume();
		} else {
			event.consume();
		}
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals("feedback")) {
			Feedback fb = (Feedback) evt.getNewValue();
			if (fb != null) {
				lblNaam.setText(fb.getVolledigeNaam());
				lblUsername.setText(fb.getGebruikerUserName());
				txfRating.setText(String.format("%d", fb.getAantalSterren()));
				txaInhoud.setText(fb.getInhoud());
			}
		}

	}

}
