package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

import domein.Sessie;
import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;

public class SessieStatistiekLijstController extends VBox implements DeelScherm<StatistiekScherm> {
	@FXML
	private Button btnGebruikers;
	@FXML
	private ComboBox<String> chSessiekalender;
	@FXML
	private ComboBox<String> chMaand;
	@FXML
	private TableView<Sessie> tbSessies;
	@FXML
	private TableColumn<Sessie,String> colStart;
	@FXML
	private TableColumn<Sessie,String> colTitel;
	@FXML
	private TableColumn<Sessie,String> colVerantwoordelijke;
	private StatistiekScherm parent;
	@Override
	public void buildGui(StatistiekScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SessieStatistiekLijst.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	// Event Listener on Button[#btnGebruikers].onAction
	@FXML
	public void switchGebruikers(ActionEvent event) {
		// TODO Autogenerated
		parent.reload(20, 22);
	}
	// Event Listener on ComboBox[#chSessiekalender].onAction
	@FXML
	public void switchSessiekalender(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on ComboBox[#chMaand].onAction
	@FXML
	public void switchMaand(ActionEvent event) {
		// TODO Autogenerated
	}
	
}
