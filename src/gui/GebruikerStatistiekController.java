package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import controllers.StatistiekController;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieGebruiker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;

public class GebruikerStatistiekController extends VBox
		implements DeelScherm<StatistiekScherm>, PropertyChangeListener {
	@FXML
	private TextField txfNaam;
	@FXML
	private TextField txfUsername;
	@FXML
	private TextField txfEmail;
	@FXML
	private TableView<SessieGebruiker> tbSessies;
	@FXML
	private TableColumn<SessieGebruiker, String> colNaam;
	@FXML
	private TableColumn<SessieGebruiker, String> colAanwezigheid;
	@FXML
	private TextField txfInschrijving;
	@FXML
	private TextField txfAanwezig;
	@FXML
	private TextField txfRatio;
	@FXML
	private Label lblOk;
	private StatistiekScherm parent;
	private StatistiekController sc;

	@Override
	public void buildGui(StatistiekScherm parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		this.sc = parent.getStatistiekController();
		this.sc.addPropertyChangeListenerSessie(this);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GebruikerStatistiek.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getPropertyName().equals("maand") || evt.getPropertyName().equals("kalender")||evt.getPropertyName().equals("gebruiker")) {
			if(evt.getPropertyName().equals("gebruiker")) {
				Gebruiker gekozenGebruiker = sc.getGekozenGebruiker();
				if (gekozenGebruiker != null) {
					txfNaam.setText(gekozenGebruiker.getVolledigeNaam());
					txfUsername.setText(gekozenGebruiker.getUserName());
					txfEmail.setText(gekozenGebruiker.getEmail());
					
				}
			}

			tbSessies.setItems(sc.getIngeschrevenSessiesObs());
			colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
			colAanwezigheid.setCellValueFactory(cel -> cel.getValue().getAanwezigProperty());
			
			txfInschrijving.setText(String.format("%d", sc.getAantalInschrijvingen()));
			txfAanwezig.setText(String.format("%d", sc.getAantalAanwezig()));
			txfRatio.setText(String.format("%.2f%%", sc.getPercentage()));
			
			if(!sc.isOK()) {
				lblOk.setText("NIET OK");
				lblOk.setTextFill(Color.web("#ad2f13"));
			}else {
				lblOk.setText("OK");
				lblOk.setTextFill(Color.web("#1da318"));
			}
		}
		
		
	}

}
