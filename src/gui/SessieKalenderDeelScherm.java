package gui;

import controllers.SessieController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class SessieKalenderDeelScherm extends HBox implements DeelScherm<ApplicatieController> {

	private SessieController sc;
	private DeelScherm<SessieKalenderDeelScherm> linkerPaneel;
	private DeelScherm<SessieKalenderDeelScherm> rechterPaneel;
	private DeelScherm<SessieKalenderDeelScherm> vorigLinkerPaneel;
	private DeelScherm<SessieKalenderDeelScherm> vorigRechterPaneel;

	@Override
	public void buildGui(ApplicatieController parent) {
		// TODO Auto-generated method stub
		this.sc = parent.getSessieController();
		this.linkerPaneel = DeelSchermFactory.create(5);
		this.rechterPaneel = DeelSchermFactory.create(6);
		this.vorigLinkerPaneel=null;
		this.vorigRechterPaneel=null;
		linkerPaneel.buildGui(this);
		rechterPaneel.buildGui(this);
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();

	}

	public void changeGui(int linkerPaneelChange, int rechterPaneelChange) {
		if (this.vorigLinkerPaneel == null && this.vorigRechterPaneel == null) {
			this.vorigLinkerPaneel = this.linkerPaneel;
			this.vorigRechterPaneel = this.rechterPaneel;
			this.getChildren().removeAll((Node) this.linkerPaneel, (Node) this.rechterPaneel);

			this.linkerPaneel = DeelSchermFactory.create(linkerPaneelChange);
			if (this.vorigLinkerPaneel.getClass().getSimpleName()
					.equals(this.linkerPaneel.getClass().getSimpleName())) {
				this.linkerPaneel = this.vorigLinkerPaneel;
			} else {
				this.linkerPaneel.buildGui(this);
			}
			this.rechterPaneel = DeelSchermFactory.create(rechterPaneelChange);
			if (this.vorigRechterPaneel.getClass().getSimpleName()
					.equals(this.rechterPaneel.getClass().getSimpleName())) {
				this.rechterPaneel = this.vorigRechterPaneel;
			} else {
				this.rechterPaneel.buildGui(this);
			}
			this.getChildren().add((Node) linkerPaneel);
			this.getChildren().add((Node) rechterPaneel);
			applyLayout();
		} else {
			changeBack();
			changeGui(linkerPaneelChange,rechterPaneelChange);
		}
		
	}

	public void changeBack() {
		this.getChildren().removeAll((Node) this.linkerPaneel, (Node) this.rechterPaneel);
		this.linkerPaneel = vorigLinkerPaneel;
		this.rechterPaneel = vorigRechterPaneel;
		this.vorigLinkerPaneel = null;
		this.vorigRechterPaneel = null;
		this.getChildren().add((Node) linkerPaneel);
		this.getChildren().add((Node) rechterPaneel);
		applyLayout();
	}

	public void applyLayout() {
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.setPadding(new Insets(5, 5, 20, 5));
	}

	public SessieController getSessieController() {
		return sc;
	}

}
