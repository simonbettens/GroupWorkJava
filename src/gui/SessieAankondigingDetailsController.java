package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import controllers.SessieController;
import domein.AankondigingPrioriteit;
import domein.Sessie;
import domein.SessieAankondiging;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.control.ComboBox;

public class SessieAankondigingDetailsController extends VBox
		implements DeelScherm<SessieKalenderDeelScherm>, PropertyChangeListener {
	@FXML
	private Label lblNaam;
	@FXML
	private TextArea txtInhoud;
	@FXML
	private ComboBox<String> cbPrioriteit;
	@FXML
	private Button btnVerwijder;
	@FXML
	private Button btnOpslaan;
	@FXML
	private Button btnAnnuleer;
	private SessieController sc;
	private SessieKalenderDeelScherm parent;
	private boolean isEdit;
	
	@Override
	public void buildGui(SessieKalenderDeelScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		this.sc = parent.getSessieController();
		this.sc.addPropertyChangeListenerSessie(this, "sessieAankondiging");
		isEdit = false;
		Sessie sessie = sc.getSessie();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieAankondigingDetails.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<String> sessieAankondigingLijst = Arrays.asList(AankondigingPrioriteit.values()).stream().sorted().map(ap->AankondigingPrioriteit.AankondigingPrioriteitToString(ap)).collect(Collectors.toList());
		cbPrioriteit.setItems(FXCollections.observableArrayList(sessieAankondigingLijst));
		btnOpslaan.setText("Maak nieuwe aankondiging");
		btnVerwijder.setDisable(true);
		lblNaam.setText(sessie.getNaam());
	}
	// Event Listener on Button[#btnVerwijder].onAction
	@FXML
	public void verwijder(ActionEvent event) {
		// TODO Autogenerated
		sc.deleteSessieAankondiging();
	}
	// Event Listener on Button[#btnOpslaan].onAction
	@FXML
	public void opslaan(ActionEvent event) {
		// TODO Autogenerated
		String inhoud  = txtInhoud.getText();
		AankondigingPrioriteit prioriteit = AankondigingPrioriteit.StringToAankondigingPrioriteit(cbPrioriteit.getSelectionModel().getSelectedItem());
		if(isEdit) {
			sc.pasSessieAankondigingAan(inhoud, prioriteit);
		}else {
			sc.maakSessieAankondiging(inhoud, prioriteit);
		}
	}
	// Event Listener on Button[#btnAnnuleer].onAction
	@FXML
	public void annuleer(ActionEvent event) {
		// TODO Autogenerated
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getPropertyName().equals("sessieAankondiging")) {
			if (evt.getNewValue() != null) {
				btnOpslaan.setText("Wijzigingen opslaan");
				btnVerwijder.setDisable(false);
				isEdit=true;
				SessieAankondiging sa = (SessieAankondiging) evt.getNewValue();
				txtInhoud.setText(sa.getInhoud());
				cbPrioriteit.getSelectionModel().select(AankondigingPrioriteit.AankondigingPrioriteitToString(sa.getPrioriteit()));
			}
		}
	}
	
}
