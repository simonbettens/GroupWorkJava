package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.io.IOException;

import controllers.AankondigingController;
import domein.Aankondiging;
import domein.SessieAankondiging;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class AlgemeneAankondigingLijstController extends VBox implements DeelScherm<AankondigingenDeelScherm> {
	@FXML
	private Button btnGaTerug;
	@FXML
	private TextField txfZoek;
	@FXML
	private Button btnMaakLeeg;
	@FXML
	private Button btnNieuw;
	@FXML
	private TableView<Aankondiging> aankondigingenTable;
	@FXML
	private TableColumn<Aankondiging, String> colVerantwoordelijke;
	@FXML
	private TableColumn<Aankondiging, String> colTijd;
	@FXML
	private TableColumn<Aankondiging, String> colInhoud;
	private AankondigingenDeelScherm parent;
	private AankondigingController ac;
	private String zoekWaarde;

	@Override
	public void buildGui(AankondigingenDeelScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		this.ac = parent.getAankondigingController();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AlgemeneAankondigingLijst.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		ObservableList<Aankondiging> aankondigingen = ac.getAankondigingen();
		aankondigingen.forEach(s -> System.out.println(s.toString()));
		aankondigingenTable.setItems(aankondigingen);
		colVerantwoordelijke.setCellValueFactory(cel -> cel.getValue().getVerantwoordelijkeProperty());
		colTijd.setCellValueFactory(cel -> cel.getValue().getTijdToegevoegdProperty());
		colInhoud.setCellValueFactory(cel -> cel.getValue().getInhoudProperty());
		aankondigingenTable.getSelectionModel().selectedItemProperty().addListener((obs,oldV,newV)->{
			ac.setGeselecteerdeAankondiging(newV);
        });
		this.setVgrow(aankondigingenTable, Priority.SOMETIMES);
	}
	
	// Event Listener on Button[#btnGaTerug].onAction
	@FXML
	public void gaTerug(ActionEvent event) {
		// TODO Autogenerated
		parent.changeBack();
	}
	// Event Listener on TextField[#txfZoek].onKeyReleased
	@FXML
	public void search(KeyEvent event) {
		// TODO Autogenerated
		zoekWaarde = txfZoek.getText();
		btnMaakLeeg.setDisable(false);
		ac.zoekOpAankondiging(zoekWaarde);
	}
	// Event Listener on Button[#btnMaakLeeg].onAction
	@FXML
	public void maakLeeg(ActionEvent event) {
		// TODO Autogenerated
		zoekWaarde = "";
		txfZoek.setText("");
		btnMaakLeeg.setDisable(true);
		ac.zoekOpAankondiging(zoekWaarde);
	}
	// Event Listener on Button[#btnNieuw].onAction
	@FXML
	public void nieuwAankondiging(ActionEvent event) {
		// TODO Autogenerated
		parent.changeBack();
	}	
}
