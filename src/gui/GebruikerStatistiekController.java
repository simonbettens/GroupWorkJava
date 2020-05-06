package gui;

import java.io.IOException;

import domein.Sessie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;

import javafx.scene.control.Label;

public class GebruikerStatistiekController extends VBox implements DeelScherm<StatistiekScherm> {
	@FXML
	private TextField txfNaam;
	@FXML
	private TextField txfUsername;
	@FXML
	private TextField txfEmail;
	@FXML
	private ListView<Sessie> lvSessies;
	@FXML
	private TextField txfInschrijving;
	@FXML
	private TextField txfAanwezig;
	@FXML
	private TextField txfRatio;
	@FXML
	private Label lblOk;
	
	@Override
	public void buildGui(StatistiekScherm parent) {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GebruikerStatistiek.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
