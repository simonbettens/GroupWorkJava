package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

import domein.GebruikerType;
import javafx.event.ActionEvent;

public class MenuDeelSchermController extends VBox {

	@FXML
	private Button btnSessieKalender;
	@FXML
	private Button btnBeheerSessies;
	@FXML
	private Button btnRaadPleegStat;
	@FXML
	private Button btnBeheerGebruiker;
	@FXML
	private Button btnAankondiging;
	@FXML
	private Button btnStart;

	private ApplicatieController parent;

	public MenuDeelSchermController(ApplicatieController parent) {
		this.parent = parent;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuDeelScherm.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (parent.getIngelogdeGebruiker().getType() != GebruikerType.HOOFDVERANTWOORDELIJKE) {
			btnBeheerGebruiker.setDisable(true);
			btnBeheerGebruiker.setVisible(false);
		}

	}
	// Event Listener on Button[#btnStart].onAction
		@FXML
		public void switchStart(ActionEvent event) {
			// TODO Autogenerated
			System.out.println("Start");
			parent.update(0);
		}
	// Event Listener on Button[#btnSessieKalender].onAction
	@FXML
	public void switchSessieKalender(ActionEvent event) {
		// TODO Autogenerated
		System.out.println("SessieKalender");
	}

	// Event Listener on Button[#btnBeheerSessies].onAction
	@FXML
	public void switchSessies(ActionEvent event) {
		// TODO Autogenerated
		System.out.println("Sessies");

	}

	// Event Listener on Button[#btnRaadPleegStat].onAction
	@FXML
	public void switchRaadPleegStat(ActionEvent event) {
		// TODO Autogenerated
		System.out.println("Statistieken");

	}

	// Event Listener on Button[#btnBeheerGebruiker].onAction
	@FXML
	public void switchGebruikers(ActionEvent event) {
		// TODO Autogenerated
		System.out.println("Gebruikers");
		parent.update(4);
	}

	// Event Listener on Button[#btnAankondiging].onAction
	@FXML
	public void switchAankondiging(ActionEvent event) {
		// TODO Autogenerated
		System.out.println("Aankondigingen");

	}
}
