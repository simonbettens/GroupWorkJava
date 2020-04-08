package main;

import gui.LoginSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.ConsoleApp;



/*public class StartUpUI {
	public static void main(String[] args) {
		
		// Console omgeving om te testen
		ConsoleApp consoleApp = new ConsoleApp();
	}*/
	public class StartUpUI extends Application {
		@Override
		public void start(Stage primaryStage) {
			
				LoginSchermController root = new LoginSchermController();
				//Parent root = FXMLLoader.load(getClass().getResource("LoginScherm.fxml"));
				Scene scene = new Scene(root);
				//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setTitle("Login");
				primaryStage.setScene(scene);
				primaryStage.show();
			
		}
		
		public static void main(String[] args) {
			launch(args);
		}
		
}
