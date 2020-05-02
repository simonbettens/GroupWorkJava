package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import controllers.SessieController;
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

public class StatistiekController extends VBox implements Initializable, DeelScherm<StatistiekScherm>{
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
    
    private Sessie sessie;
    private StatistiekScherm parent;
    private SessieController sc;
    
    
    @Override
    public void initialize(URL url, ResourceBundle bl) {
    	XYChart.Series set = new XYChart.Series<>();
    	
    	//dummie voor test
    	set.getData().add(new XYChart.Data("SessieNaam", 15));
    	
    	aanwezighedenChart.getData().addAll(set);
    }

		@Override
		public void buildGui(StatistiekScherm parent) {
			// TODO Auto-generated method stub
			this.parent = parent;
			this.sc = parent.getSessieController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Stastistiek.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			
			try {
				loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			cbAcademiejaar.getItems().setAll(sc.getSessieKalenderObservableLijst());
			cbMaand.getSelectionModel().select(Maand.of(LocalDate.now().getMonthValue()).toString());
			cbMaand.getItems().addAll(Arrays.asList(Maand.values()).stream().map(e -> e.toString()).collect(Collectors.toList()));
			SessieKalender sk = sc.getGekozenSessieKalender();
			if(sk != null) {
				cbAcademiejaar.getSelectionModel().select(sk.toString());
			}
			cbAcademiejaar.valueProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
					if (newValue != null) {
						cbMaand.getSelectionModel().clearSelection();
						sc.changeSelectedSessieKalender(newValue);
						
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
			cbSessie.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
					if (newValue != null) {
						sc.changeSelectedSessie(newValue);

					}
				}
			});
		}
		
		public Sessie getSessie() {
			return sessie;
		}
		
}
