package gui;

public class DeelSchermFactory {
	public static DeelScherm create(int welke) {
		DeelScherm deelScherm = null;
		switch(welke) {
		case 1: break;
		case 2:break;
		case 3:break;
		case 4:deelScherm = new GebruikerDeelScherm();break;
		case 5:break;
		default: 
			deelScherm = new StartDeelSchermController();
		}
		return deelScherm;
	}
}
