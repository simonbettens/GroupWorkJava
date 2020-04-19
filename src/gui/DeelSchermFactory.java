package gui;

public class DeelSchermFactory {
	public static DeelScherm create(int welke) {
		DeelScherm deelScherm = null;
		switch(welke) {
		case 1:deelScherm = new SessieKalenderDeelScherm();break;
		case 2:break;
		case 3:deelScherm = new GebruikerDeelScherm();break;
		case 4:break;
		default: 
			deelScherm = new StartDeelSchermController();
		}
		return deelScherm;
	}
}
