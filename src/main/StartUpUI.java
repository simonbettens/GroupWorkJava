package main;

import controllers.GebruikerController;
import gui.LoginSchermGridPaneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.GebruikerDaoJpa;
import repository.InitData;
import ui.ConsoleApp;

/*public class StartUpUI {
	public static void main(String[] args) {
		
		// Console omgeving om te testen
		ConsoleApp consoleApp = new ConsoleApp();
	}*/
public class StartUpUI extends Application {
	@Override
	public void start(Stage primaryStage) {
		InitData invoerdata = new InitData();
		// LoginSchermController root = new LoginSchermController();
		LoginSchermGridPaneController root = new LoginSchermGridPaneController();
		// Parent root = FXMLLoader.load(getClass().getResource("LoginScherm.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
