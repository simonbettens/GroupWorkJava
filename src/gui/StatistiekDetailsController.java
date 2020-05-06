package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.sound.sampled.Port;

import controllers.SessieController;
import controllers.StatistiekController;
import domein.Maand;
import domein.Sessie;
import domein.SessieKalender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class StatistiekDetailsController extends VBox
		implements Initializable, DeelScherm<StatistiekScherm>, PropertyChangeListener {
	@FXML
	private TextField txfJaar1;
	@FXML
	private TextField txfJaar2;
	@FXML
	private TextField txfJaar3;

	private TextField[] txfJaar;
	@FXML
	private TextField txfMaand1;
	@FXML
	private TextField txfMaand2;
	@FXML
	private TextField txfMaand3;

	private TextField[] txfMaand;
	@FXML
	private BarChart<String, Number> aanwezighedenChart;

	@FXML
	private CategoryAxis xAs; // naam van sessies worden hier getoond

	@FXML
	private NumberAxis yAs; // aantal aanwezigheden hier getoond
	private double max;
	private StatistiekController statC;
	private StatistiekScherm parent;

	@Override
	public void buildGui(StatistiekScherm parent) {
		// TODO Auto-generated method stub
		System.out.println("build");
		this.statC = parent.getStatistiekController();
		statC.addPropertyChangeListenerSessie(this);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Statistiek.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		txfJaar = new TextField[] { txfJaar1, txfJaar2, txfJaar3 };
		txfMaand = new TextField[] { txfMaand1, txfMaand2, txfMaand3 };
		List<Sessie> jaar = statC.getBesteSessiesJaar();
		List<Sessie> maand = statC.getBesteSessieMaand();
		int jaarSize = jaar.size();
		int maandSize = maand.size();
		for (int i = 0; i < 3; i++) {
			if (i < jaarSize) {
				txfJaar[i].setText(jaar.get(i).getNaam());
			} else {
				txfJaar[i].setText("");
			}
			if (i < maandSize) {
				txfMaand[i].setText(maand.get(i).getNaam());
			} else {
				txfMaand[i].setText("");
			}
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		System.out.println("initialize");
		vulChart(statC.getBesteSessieMaand());
	}

	public void vulChart(List<Sessie> lijstSessies) {
		System.out.println("verander");
		XYChart.Series<String, Number> dataSet = new XYChart.Series<>();
		dataSet.setName("Sessies");
		max = 20;
		if (lijstSessies.size() != 0) {

			lijstSessies.stream().forEach(s -> {
				max = Math.max(max, (double) s.getMaxCap());
				dataSet.getData().add(new XYChart.Data<String, Number>(s.getNaam(), s.getAantalAanwezige()));
			});
			System.out.println("vernieuw");
			aanwezighedenChart.layout();
			aanwezighedenChart.getData().setAll(dataSet);
		}
		yAs.setUpperBound(max);
	}

	public void clearChart() {
		aanwezighedenChart.getData().clear();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("propchange");
		if (evt.getPropertyName().equals("maand") || evt.getPropertyName().equals("kalender")) {
			List<Sessie> jaar = statC.getBesteSessiesJaar();
			List<Sessie> maand = statC.getBesteSessieMaand();
			int jaarSize = jaar.size();
			int maandSize = maand.size();
			for (int i = 0; i < 3; i++) {
				if (i < jaarSize) {
					txfJaar[i].setText(jaar.get(i).getNaam());
				} else {
					txfJaar[i].setText("");
				}
				if (i < maandSize) {
					txfMaand[i].setText(maand.get(i).getNaam());
				} else {
					txfMaand[i].setText("");
				}
			}
		}
		if (evt.getPropertyName().equals("sessies")) {
			System.out.println("propchange sessies");

			clearChart();

			List<Sessie> sessies = (List<Sessie>) evt.getNewValue();
			System.out.println(sessies.toString());
			vulChart(sessies);

		}
	}

}
