package gui;

import controllers.GebruikerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class GebruikerDeelScherm extends HBox implements DeelScherm {
	private GebruikerController gebruikerController;
	private GebruikerLijstController glc;
	private GebruikerDetailsController gdc;
	
	@Override
	public void buildGui(ApplicatieController parent) {
		this.gebruikerController = parent.getGebruikerController();
		this.gdc = new GebruikerDetailsController(this);
		this.glc = new GebruikerLijstController(this,gdc);
		
		this.setAlignment(Pos.BOTTOM_CENTER);
		this.getChildren().add(glc);
		this.getChildren().add(gdc);
		this.setSpacing(10);
        this.setPadding(new Insets(5, 5, 20, 5));
		gebruikerController.addPropertyChangeListener(gdc);
	}

	public GebruikerController getGebruikerController() {
		return gebruikerController;
	}
	

}
