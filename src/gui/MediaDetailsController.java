package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controllers.MediaController;
import controllers.SessieController;
import domein.Media;
import domein.MediaType;
import domein.Sessie;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

public class MediaDetailsController extends VBox
		implements DeelScherm<SessieKalenderDeelScherm>, PropertyChangeListener {
	@FXML
	private Label lblNaam;
	@FXML
	private Label lblStart;
	@FXML
	private Label lblEind;
	@FXML
	private TextField txtNaam;
	@FXML
	private ComboBox<String> cbType;
	@FXML
	private TextField txfBestandNaam;
	@FXML
	private Button btnVerwijder;
	@FXML
	private Button btnOpslaan;
	@FXML
	private Button btnAnnuleer;
	private SessieController sc;
	private MediaController mc;
	private SessieKalenderDeelScherm parent;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy 'om' hh:mm");
	private boolean isEdit;

	@Override
	public void buildGui(SessieKalenderDeelScherm parent) {
		this.parent = parent;
		this.sc = parent.getSessieController();
		this.mc = parent.getMediaController();
		this.mc.addPropertyChangeListenerSessie(this, "media");
		isEdit = false;
		Sessie sessie = mc.getGekozenSessie();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("MediaDetails.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<String> mediaLijst = Arrays.asList(MediaType.values()).stream().sorted()
				.map(m -> MediaType.MediaTypeToString(m)).collect(Collectors.toList());
		cbType.setItems(FXCollections.observableArrayList(mediaLijst));
		btnOpslaan.setText("Maak nieuwe media");
		btnVerwijder.setDisable(true);
		lblNaam.setText(sessie.getNaam());
		lblStart.setText(dtf.format(sessie.getStartDatum()));
		lblEind.setText(dtf.format(sessie.getEindDatum()));
	}

	// Event Listener on Button[#btnVerwijder].onAction
	@FXML
	public void verwijder(ActionEvent event) {
		// TODO Autogenerated
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Media verwijderen");
		alert.setHeaderText("Bevestig");
		alert.setContentText("Wens je zeker deze media item te verwijderen?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		Optional<ButtonType> antwoord = alert.showAndWait();
		if (antwoord.get() == ButtonType.OK) {
			System.out.println("media is verwijderd");
			mc.deleteMedia();
			txtNaam.setText("");
			txfBestandNaam.setText("");
			cbType.getSelectionModel().select("");
			event.consume();
		} else {
			event.consume();
		}
	}

	// Event Listener on Button[#btnOpslaan].onAction
	@FXML
	public void opslaan(ActionEvent event) {
		// TODO Autogenerated
		String naam = txtNaam.getText();
		MediaType type = MediaType.StringToMediaType(cbType.getSelectionModel().getSelectedItem());
		if (type != null) {
			String bestandNaam = veranderVanBestandNaam(txfBestandNaam.getText(), type);
			if (isEdit) {
				mc.pasMedia(naam, bestandNaam, type);
			} else {
				mc.maakMedia(naam, bestandNaam, type);
			}
		}
	}

	// Event Listener on Button[#btnAnnuleer].onAction
	@FXML
	public void annuleer(ActionEvent event) {
		// TODO Autogenerated
		if (isEdit) {
			Media m = mc.getGekozenMedia();
			if (m != null) {
				txtNaam.setText(m.getNaam());
				txfBestandNaam.setText(veranderNaarBestandNaam(m.getAdress(), m.getMediaType()));
				cbType.getSelectionModel().select(MediaType.MediaTypeToString(m.getMediaType()));
			} else {
				txtNaam.setText("");
				txfBestandNaam.setText("");
				cbType.getSelectionModel().select("");
			}
		} else {
			txtNaam.setText("");
			txfBestandNaam.setText("");
			cbType.getSelectionModel().select("");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals("media")) {
			if (evt.getNewValue() != null) {
				btnOpslaan.setText("Wijzigingen opslaan");
				btnVerwijder.setDisable(false);
				isEdit = true;
				Media m = (Media) evt.getNewValue();
				txtNaam.setText(m.getNaam());
				txfBestandNaam.setText(veranderNaarBestandNaam(m.getAdress(), m.getMediaType()));
				cbType.getSelectionModel().select(MediaType.MediaTypeToString(m.getMediaType()));
			}
		}
	}

	private String veranderNaarBestandNaam(String naam, MediaType type) {
		String waarde = "";
		switch (type) {
		case AFBEELDING:
			waarde = naam.replaceAll("/images/", "");
			break;
		case VIDEO:
			waarde = naam.replaceAll("/videos/", "");
			break;
		case YOUTUBEVIDEO:
		case LINK:
			waarde = naam;
			break;
		case EXCEL:
		case PDF:
		case POWERPOINT:
		case WORD:
		case ZIP:
		case ANDERDOCUMENT:
			waarde = naam.replaceAll("/documents/", "");
			break;
		default:
			waarde = naam;
			break;
		}
		return waarde;
	}

	private String veranderVanBestandNaam(String bestandNaam, MediaType type) {
		String waarde = "";
		switch (type) {
		case AFBEELDING:
			waarde = String.format("/images/%s", bestandNaam);
			break;
		case VIDEO:
			waarde = String.format("/videos/%s", bestandNaam);
			break;
		case YOUTUBEVIDEO:
		case LINK:
			waarde = bestandNaam;
			break;
		case EXCEL:
		case PDF:
		case POWERPOINT:
		case WORD:
		case ZIP:
		case ANDERDOCUMENT:
			waarde = String.format("/documents/%s", bestandNaam);
			break;
		default:
			waarde = bestandNaam;
			break;
		}
		return waarde;
	}
}
