package gui;

import controllers.SessieController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class SessieKalenderDeelScherm extends HBox implements DeelScherm {

	private SessieController sc;
	private SessieLijstController slc;

	@Override
	public void buildGui(ApplicatieController parent) {
		// TODO Auto-generated method stub
		this.sc = parent.getSessieController();
		this.slc = new SessieLijstController(this);
		
		this.setAlignment(Pos.CENTER);
		this.getChildren().add(slc);
		this.setSpacing(10);
		this.setPadding(new Insets(5, 5, 20, 5));
	}

	public SessieController getSessieController() {
		return sc;
	}
	
}
