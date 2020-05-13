package gui;

import controllers.AankondigingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class AankondigingenDeelScherm extends HBox implements DeelScherm<ApplicatieController> {

	private AankondigingController ac;
	private DeelScherm<AankondigingenDeelScherm> linkerPaneel;
	private DeelScherm<AankondigingenDeelScherm> rechterPaneel;
	
	@Override
	public void buildGui(ApplicatieController parent) {
		// TODO Auto-generated method stub
		this.ac = parent.getAankondigingController();
		this.linkerPaneel = DeelSchermFactory.create(GuiConstant.AlgemeneAankondigingLijstController);
		this.rechterPaneel = DeelSchermFactory.create(GuiConstant.AlgemeneAankondigingDetailsController);
		linkerPaneel.buildGui(this);
		rechterPaneel.buildGui(this);
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();
	}
	
	public void changeBack() {
		this.getChildren().removeAll((Node) this.linkerPaneel, (Node) this.rechterPaneel);
		this.linkerPaneel = DeelSchermFactory.create(GuiConstant.AlgemeneAankondigingLijstController);
		this.linkerPaneel.buildGui(this);
		this.rechterPaneel = DeelSchermFactory.create(GuiConstant.AlgemeneAankondigingDetailsController);
		this.rechterPaneel.buildGui(this);
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();
	}

	public void applyLayout() {	
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.setPadding(new Insets(5, 5, 20, 5));
	}
	
	public void rebuildGui() {
		this.getChildren().removeAll((Node) this.linkerPaneel, (Node) this.rechterPaneel);
		this.linkerPaneel = DeelSchermFactory.create(GuiConstant.AlgemeneAankondigingLijstController);
		this.rechterPaneel = DeelSchermFactory.create(GuiConstant.AlgemeneAankondigingDetailsController);
		linkerPaneel.buildGui(this);
		rechterPaneel.buildGui(this);
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();
	}
	
	public AankondigingController getAankondigingController() {
		return ac;
	}

}
