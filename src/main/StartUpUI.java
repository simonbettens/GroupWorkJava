package main;

import gui.LoginSchermGridPaneController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import repository.GenericDaoJpa;
import repository.InitData;
/*public class StartUpUI {
	public static void main(String[] args) {
		
		// Console omgeving om te testen
		ConsoleApp consoleApp = new ConsoleApp();
	}*/
public class StartUpUI extends Application {
	@Override
	public void start(Stage primaryStage) {
		new InitData();
		// LoginSchermController root = new LoginSchermController();
		LoginSchermGridPaneController root = new LoginSchermGridPaneController();
		// Parent root = FXMLLoader.load(getClass().getResource("LoginScherm.fxml"));
		Scene scene = new Scene(root, 700, 500);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/itLabIcon.jpg")));
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(event -> {
			GenericDaoJpa.closePersistency();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
