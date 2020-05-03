package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import controllers.AankondigingController;
import controllers.SessieController;
import domein.AankondigingPrioriteit;
import domein.SessieAankondiging;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class SessieAankondigingLijstController extends VBox implements DeelScherm<SessieKalenderDeelScherm> {
	@FXML
	private Label lblSessieNaam;
	@FXML
	private Button btnGaTerug;
	@FXML
	private TextField txfZoek;
	@FXML
	private Button btnMaakLeeg;
	@FXML
	private Button btnNieuw;
	@FXML
	private TableView<SessieAankondiging> aankondigingenTable;
	@FXML
	private TableColumn<SessieAankondiging, String> colVerantwoordelijke;
	@FXML
	private TableColumn<SessieAankondiging, String> colTijd;
	@FXML
	private TableColumn<SessieAankondiging, String> colInhoud;
	private SessieController sc;
	private SessieKalenderDeelScherm parent;
	private AankondigingController ac;

	@Override
	public void buildGui(SessieKalenderDeelScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		this.sc= parent.getSessieController();
		this.ac = parent.getAankondigingController();
		ac.setGekozenSessie(sc.getSessie());
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SessieAankondigingLijst.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		lblSessieNaam.setText(sc.getSessie().getNaam());
		//List<String> sessieAankondigingLijst = Arrays.asList(AankondigingPrioriteit.values()).stream().sorted().map(sa->AankondigingPrioriteit.AankondigingPrioriteitToString(sa)).collect(Collectors.toList());
		ObservableList<SessieAankondiging> testje = ac.getSessieAankondigingen();
		testje.forEach(s -> System.out.println(s.toString()));
		aankondigingenTable.setItems(testje);
		colVerantwoordelijke.setCellValueFactory(cel -> cel.getValue().getVerantwoordelijkeProperty());
		colTijd.setCellValueFactory(cel -> cel.getValue().getTijdToegevoegdProperty());
		colInhoud.setCellValueFactory(cel -> cel.getValue().getInhoudProperty());
		aankondigingenTable.getSelectionModel().selectedItemProperty().addListener((obs,oldV,newV)->{
			ac.setGeselecteerdeSessieAankondiging(newV);
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
	}
	// Event Listener on Button[#btnMaakLeeg].onAction
	@FXML
	public void maakLeeg(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on Button[#btnNieuw].onAction
	@FXML
	public void nieuwMedia(ActionEvent event) {
		// TODO Autogenerated
	}
	
}
