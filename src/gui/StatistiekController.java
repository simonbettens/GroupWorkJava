package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class StatistiekController extends VBox implements Initializable, DeelScherm<StatistiekScherm>{
	@FXML
    private ComboBox<?> cbAcademiejaar; //linkse cb

    @FXML
    private ComboBox<?> cbMaand; //middel cb

    @FXML
    private ComboBox<?> cbSessie; //laatste cb

    @FXML
    private BarChart<?, ?> aanwezighedenChart;

    @FXML
    private CategoryAxis xAs; //naam van sessies worden hier getoond

    @FXML
    private NumberAxis yAs; // aantal aanwezigheden hier getoond
    
    
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
			
		}
}
