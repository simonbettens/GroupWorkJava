package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class StartDeelSchermController extends GridPane implements DeelScherm{
	@FXML
	private Label lblWelkom;
	@FXML
	private Label lblNaam;
	private ApplicatieController parent;
	
	@Override
	public void buildGui(ApplicatieController parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StartDeelScherm.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		lblNaam.setText(parent.getIngelogdeGebruiker().getVolledigeNaam());
	}

}
