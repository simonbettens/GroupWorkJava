package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class StatistiekScherm extends AnchorPane implements Initializable, DeelScherm<ApplicatieController>{
	 @FXML
	    private BarChart<?, ?> aanwezighedenChart;

	    @FXML
	    private CategoryAxis xAs;

	    @FXML
	    private NumberAxis yAs;

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			XYChart.Series set = new XYChart.Series<>();
			
			set.getData().add(new XYChart.Data("naamSessie", 15));
			
		}

		@Override
		public void buildGui(ApplicatieController parent) {
			// TODO Auto-generated method stub
			
		}
		
	    
	    
}
