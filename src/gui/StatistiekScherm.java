package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import controllers.AankondigingController;
import controllers.FeedbackController;
import controllers.InschrijvingController;
import controllers.MediaController;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatistiekScherm extends HBox implements DeelScherm<ApplicatieController> {

	private StatistiekController statC;
	private DeelScherm<StatistiekScherm> linkerPaneel;
	private DeelScherm<StatistiekScherm> rechterPaneel;
	private InschrijvingController ic;
	private SessieController sc;

	@Override
	public void buildGui(ApplicatieController parent) {
		// TODO Auto-generated method stub
		this.statC = parent.getStatistiekController();
		this.linkerPaneel = DeelSchermFactory.create(GuiConstant.SessieStatistiekLijstController);
		this.rechterPaneel = DeelSchermFactory.create(GuiConstant.StatistiekDetailsController);
		linkerPaneel.buildGui(this);
		rechterPaneel.buildGui(this);
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();
	}

	public void applyLayout() {

		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.setPadding(new Insets(5, 5, 20, 5));
	}
	public void reload(int linker, int rechter) {
		this.getChildren().remove((Node) linkerPaneel);
		this.getChildren().remove((Node) rechterPaneel);
		this.linkerPaneel = DeelSchermFactory.create(linker);
		this.rechterPaneel = DeelSchermFactory.create(rechter);
		linkerPaneel.buildGui(this);
		rechterPaneel.buildGui(this);
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();
	}
	public SessieController getSessieController() {
		return sc;
	}

	public InschrijvingController getInschrijvingController() {
		return ic;
	}

	public StatistiekController getStatistiekController() {
		// TODO Auto-generated method stub
		return statC;
	}

}
