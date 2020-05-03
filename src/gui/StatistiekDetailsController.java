package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import controllers.SessieController;
import controllers.StatistiekController;
import domein.Maand;
import domein.Sessie;
import domein.SessieKalender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class StatistiekDetailsController extends VBox implements Initializable, DeelScherm<StatistiekScherm>{
	@FXML
    private ComboBox<String> cbAcademiejaar; //linkse cb

    @FXML
    private ComboBox<String> cbMaand; //middel cb

    @FXML
    private ComboBox<String> cbSessie; //laatste cb

    @FXML
    private BarChart<?, ?> aanwezighedenChart;

    @FXML
    private CategoryAxis xAs; //naam van sessies worden hier getoond

    @FXML
    private NumberAxis yAs; // aantal aanwezigheden hier getoond

	private StatistiekController statC;
	private StatistiekScherm parent;

	@Override
	public void buildGui(StatistiekScherm parent) {
		// TODO Auto-generated method stub
		this.statC = parent.getStatistiekController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Statistiek.fxml"));
	loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// String sessieNaam = statC.getGekozenSessie().getNaam();
		// double aanwezigHeden = statC.getGekozenSessie().getGebruikersIngeschreven().stream()
		//		 .filter(s -> s.isAanwezigheidBevestigd() == true ).collect(Collectors.toList()).size(); 
		XYChart.Series set1 = new XYChart.Series<>();
		set1.getData().add(new XYChart.Data("test",15));
		set1.getData().add(new XYChart.Data("test2",5));
		
		aanwezighedenChart.getData().addAll(set1);
	}
    

		
}
