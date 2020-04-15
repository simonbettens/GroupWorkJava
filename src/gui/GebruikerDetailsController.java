package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controllers.GebruikerController;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.StatusType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class GebruikerDetailsController extends VBox implements PropertyChangeListener {
	@FXML
	private TextField txfAchternaam;
	@FXML
	private TextField txfVoornaam;
	@FXML
	private TextField txfEmail;
	@FXML
	private TextField txfWachtwoord;
	@FXML
	private TextField txfUsername;
	@FXML
	private TextField txfIdNummer;
	@FXML
	private Label lblError;
	@FXML
	private ChoiceBox<String> cbStatus;
	private final List<StatusType> statusList = Arrays.asList(StatusType.values());
	private ObservableList<String> statusObs;
	@FXML
	private ChoiceBox<String> cbType;
	private final List<GebruikerType> typeList = Arrays.asList(GebruikerType.values());
	private ObservableList<String> typeObs;
	@FXML
	private Button btnVerwijder;
	@FXML
	private Button btnOpslaan;
	@FXML
	private Button btnAnnuleer;
	private boolean isEdit;
	private GebruikerDeelScherm parent;
	private GebruikerController gc;

	public GebruikerDetailsController(GebruikerDeelScherm parent) {
		this.parent = parent;
		this.gc = parent.getGebruikerController();
		isEdit = true;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikerDetails.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		lblError.setText("");
		typeObs = FXCollections.observableArrayList(
				typeList.stream().distinct().map(t -> GebruikerType.toString(t)).collect(Collectors.toList()));
		statusObs = FXCollections.observableArrayList(
				statusList.stream().distinct().map(t -> StatusType.toString(t)).collect(Collectors.toList()));
		cbStatus.getItems().addAll(statusObs);
		cbType.getItems().addAll(typeObs);
		this.setSpacing(20);
	}

	// Event Listener on Button[#btnVerwijder].onAction
	@FXML
	public void verwijderGebruiker(ActionEvent event) {
		// TODO Autogenerated
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Gebruiker verwijderen");
		alert.setHeaderText("Bevestig");
		alert.setContentText("Wens je zeker deze gebruiker te verwijderen?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		Optional<ButtonType> antwoord = alert.showAndWait();
		if (antwoord.get() == ButtonType.OK) {
			System.out.println("Gebruiker is verwijderd");
			gc.deleteGebruiker();
			bewerkGebruiker();
			event.consume();
		} else {
			bewerkGebruiker();
			event.consume();
		}
	}

	// Event Listener on Button[#btnOpslaan].onAction
	@FXML
	public void opslaan(ActionEvent event) {
		try {
			lblError.setText("");
			int typeIndex = cbType.getSelectionModel().getSelectedIndex();
			int statusIndex = cbStatus.getSelectionModel().getSelectedIndex();
			if(statusIndex == -1) {
				throw new IllegalArgumentException("Je moet de status selecteren");
			}
			if(typeIndex == -1) {
				throw new IllegalArgumentException("Je moet het gebruikerstype selecteren");
			}
			
			
			if (isEdit) {
				gc.pasGebruikerAan(txfVoornaam.getText(), txfAchternaam.getText(), txfUsername.getText(),
						txfWachtwoord.getText(), txfEmail.getText(), Long.valueOf(txfIdNummer.getText()),
						typeList.get(typeIndex),
						statusList.get(statusIndex));
			} else {
				gc.maakGebruiker(txfVoornaam.getText(), txfAchternaam.getText(), txfUsername.getText(),
						txfWachtwoord.getText(), txfEmail.getText(), Long.valueOf(txfIdNummer.getText()),
						typeList.get(typeIndex),
						statusList.get(statusIndex));
			}
			
			bewerkGebruiker();
		} catch (NumberFormatException e) {
			lblError.setText("Idnummer moet een nummer zijn");
			System.out.println("Fout bij het parsen");
		} catch (IllegalArgumentException e) {
			lblError.setText(e.getMessage());
			System.out.println(e.getMessage());
		} finally {

		}
	}

	// Event Listener on Button[#btnAnnuleer].onAction
	@FXML
	public void annuleer(ActionEvent event) {
		if (isEdit) {
			Gebruiker gebruiker = gc.getGeselecteerdeGebruiker();
			txfAchternaam.setText(gebruiker.getAchternaam());
			txfVoornaam.setText(gebruiker.getVoornaam());
			txfEmail.setText(gebruiker.getEmail());
			txfWachtwoord.setText("");
			txfUsername.setText(gebruiker.getUserName());
			txfIdNummer.setText(gebruiker.getIdNumber().toString());
			cbStatus.getSelectionModel().select(StatusType.toString(gebruiker.getStatus()));
			cbType.getSelectionModel().select(GebruikerType.toString(gebruiker.getType()));
		} else {
			bewerkGebruiker();
		}
	}

	public void bewerkGebruiker() {
		isEdit = true;
		lblError.setText("");
		txfAchternaam.setText("");
		txfVoornaam.setText("");
		txfEmail.setText("");
		txfWachtwoord.setText("");
		txfUsername.setText("");
		txfIdNummer.setText("");
		cbStatus.getSelectionModel().clearSelection();
		cbType.getSelectionModel().clearSelection();
		btnOpslaan.setText("Wijzigingen opslaan");
		lblError.setText("");
		verbergButtonVerwijder(!isEdit);
	}

	public void maakGebruiker() {
		isEdit = false;
		lblError.setText("");
		txfAchternaam.setText("");
		txfVoornaam.setText("");
		txfEmail.setText("");
		txfWachtwoord.setText("");
		txfUsername.setText("");
		txfIdNummer.setText("");
		cbStatus.getSelectionModel().clearSelection();
		cbType.getSelectionModel().clearSelection();
		btnOpslaan.setText("Maak gebruiker");
		verbergButtonVerwijder(!isEdit);
	}

	public void verbergButtonVerwijder(boolean waarde) {
		btnVerwijder.setDisable(waarde);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		Gebruiker gebruiker = (Gebruiker) evt.getNewValue();
		if (gebruiker != null) {
			txfAchternaam.setText(gebruiker.getAchternaam());
			txfVoornaam.setText(gebruiker.getVoornaam());
			txfEmail.setText(gebruiker.getEmail());
			txfWachtwoord.setText("");
			txfUsername.setText(gebruiker.getUserName());
			txfIdNummer.setText(gebruiker.getIdNumber().toString());
			cbStatus.getSelectionModel().select(StatusType.toString(gebruiker.getStatus()));
			cbType.getSelectionModel().select(GebruikerType.toString(gebruiker.getType()));

		}
	}
}
