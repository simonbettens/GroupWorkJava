package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import controllers.SessieController;
import domein.Maand;
import domein.Sessie;
import domein.SessieKalender;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

public class SessieLijstController extends VBox
		implements DeelScherm<SessieKalenderDeelScherm> {
	@FXML
	private Button btnNieuweSessieKalender;
	@FXML
	private Button btnPasSessieKalender;
	@FXML
	private Button btnMaakLeeg;
	@FXML
	private TextField txfZoek;
	@FXML
	private ComboBox<String> cbAcademiejaar;
	@FXML
	private ChoiceBox<String> cbMaand;
	@FXML
	private TableView<Sessie> tblSessies;
	@FXML
	private TableColumn<Sessie, String> clnTitel;
	@FXML
	private TableColumn<Sessie, String> clnVerantwoordelijke;
	@FXML
	private TableColumn<Sessie, String> clnStartdatum;
	@FXML
	private TableColumn<Sessie, String> clnDuur;

	private SessieController sc;
	private SessieKalenderDeelScherm parent;

	@Override
	public void buildGui(SessieKalenderDeelScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		this.sc = parent.getSessieController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieLijst.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		cbAcademiejaar.getItems().setAll(sc.getSessieKalenderObservableLijst());
		cbMaand.getSelectionModel().select(Maand.of(LocalDate.now().getMonthValue()).toString());
		cbMaand.getItems()
				.addAll(Arrays.asList(Maand.values()).stream().map(e -> e.toString()).collect(Collectors.toList()));
		SessieKalender sk = sc.getGekozenSessieKalender();
		if (sk != null) {
			cbAcademiejaar.getSelectionModel().select(sk.toString());
		}
		cbAcademiejaar.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
				if (newValue != null) {
					cbMaand.getSelectionModel().clearSelection();
					sc.changeSelectedSessieKalender(newValue);
					vulTabel();
				}
			}

		});
		cbMaand.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
				if (newValue != null) {
					sc.changeSelectedMaand(newValue);

				}
			}
		});
		vulTabel();

	}

	private void vulTabel() {
		// TODO Auto-generated method stub
		tblSessies.setItems(sc.getSessieObservableLijst());
		clnTitel.setCellValueFactory(cel -> cel.getValue().getTitelProperty());
		clnVerantwoordelijke.setCellValueFactory(cel -> cel.getValue().getVerantwoordelijkeNaamProperty());
		clnStartdatum.setCellValueFactory(cel -> cel.getValue().getStartDatumProperty());
		clnDuur.setCellValueFactory(cel -> cel.getValue().getDuurProperty());
		tblSessies.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			sc.setSessie(newV);
		});
	}

	// Event Listener on Button[#btnNieuweSessieKalender].onAction
	@FXML
	public void maakNieuweSessiekalender(ActionEvent event) {
		// TODO Autogenerated
		parent.changeGui(5, 8);
	}

	// Event Listener on Button[#btnVerwijderSessieKalender].onAction
	@FXML
	public void pasSessieKalender(ActionEvent event) {
		// TODO Autogenerated
		parent.changeGui(5, 7);
	}

	@FXML
	public void search(KeyEvent e) {
		String zoekwaarde = txfZoek.getText();
		if (!zoekwaarde.isEmpty() || !zoekwaarde.isBlank()) {
			sc.zoekOpNaam(zoekwaarde);
			this.btnMaakLeeg.setDisable(false);
		}
	}

	@FXML
	public void maakLeeg(ActionEvent event) {
		sc.zoekOpNaam("");
		txfZoek.setText("");
		this.btnMaakLeeg.setDisable(true);
	}

	

}
