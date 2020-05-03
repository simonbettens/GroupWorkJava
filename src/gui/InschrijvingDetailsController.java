package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import controllers.InschrijvingController;
import controllers.SessieController;
import domein.AankondigingPrioriteit;
import domein.DatumEnTijdHelper;
import domein.Sessie;
import domein.SessieAankondiging;
import domein.SessieGebruiker;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.CheckBox;

public class InschrijvingDetailsController extends VBox
		implements DeelScherm<SessieKalenderDeelScherm>, PropertyChangeListener {
	@FXML
	private Label lblNaam;
	@FXML
	private Label lblStart;
	@FXML
	private Label lblEind;
	@FXML
	private TextField txfVolledigeNaam;
	@FXML
	private TextField txfGebruikernaam;
	@FXML
	private CheckBox chAanwezig;
	@FXML
	private CheckBox chAanwezigBevestigd;
	@FXML
	private TextField txfEmail;
	@FXML
	private Button btnVerwijder;
	@FXML
	private Button btnOpslaan;
	@FXML
	private Button btnAnnuleer;
	private SessieKalenderDeelScherm parent;
	private SessieController sc;
	private InschrijvingController ic;

	@Override
	public void buildGui(SessieKalenderDeelScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		this.sc = parent.getSessieController();
		this.ic = parent.getInschrijvingController();

		ic.addPropertyChangeListenerSessie(this, "inschrijving");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InschrijvingDetails.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Sessie sessie = sc.getSessie();
		lblNaam.setText(sessie.getNaam());
		lblStart.setText(DatumEnTijdHelper.dateTimeFormat(sessie.getStartDatum()));
		lblEind.setText(DatumEnTijdHelper.dateTimeFormat(sessie.getEindDatum()));
	}

	// Event Listener on Button[#btnVerwijder].onAction
	@FXML
	public void verwijder(ActionEvent event) {
		// TODO Autogenerated
		ic.deleteSessieGebruiker();
	}

	// Event Listener on Button[#btnOpslaan].onAction
	@FXML
	public void opslaan(ActionEvent event) {
		// TODO Autogenerated
		boolean aanwezig = chAanwezig.isSelected();
		boolean aanwezigBevestigd = chAanwezigBevestigd.isSelected();
		ic.pasSessieGebruikerAan(aanwezig, aanwezigBevestigd);
	}

	// Event Listener on Button[#btnAnnuleer].onAction
	@FXML
	public void annuleer(ActionEvent event) {
		// TODO Autogenerated
		SessieGebruiker sa = ic.getGekozenInschrijving();
		if (sa != null) {
			txfVolledigeNaam.setText(sa.getGebruiker().getVolledigeNaam());
			txfGebruikernaam.setText(sa.getUserName());
			txfEmail.setText(sa.getGebruiker().getEmail());
			chAanwezig.setSelected(sa.isAanwezig());
			chAanwezigBevestigd.setSelected(sa.isAanwezigheidBevestigd());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals("inschrijving")) {
			if (evt.getNewValue() != null) {
				btnVerwijder.setDisable(false);

				SessieGebruiker sa = (SessieGebruiker) evt.getNewValue();
				txfVolledigeNaam.setText(sa.getGebruiker().getVolledigeNaam());
				txfGebruikernaam.setText(sa.getUserName());
				txfEmail.setText(sa.getGebruiker().getEmail());
				chAanwezig.setSelected(sa.isAanwezig());
				chAanwezigBevestigd.setSelected(sa.isAanwezigheidBevestigd());
			}
		}
	}

}
