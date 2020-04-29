package gui;

public class DeelSchermFactory {
	public static DeelScherm create(int welke) {
		DeelScherm deelScherm = null;
		switch(welke) {
		case 1:deelScherm = new SessieKalenderDeelScherm();break;
		case 2:break;
		case 3:deelScherm = new GebruikerDeelScherm();break;
		case 4:break;
		case 5:deelScherm = new SessieLijstController();break;
		case 6:deelScherm = new SessieDetailsController();break;
		case 7:deelScherm = new SessieKalenderDetailsController(false);break;
		case 8:deelScherm = new SessieKalenderDetailsController(true);break;
		case 9: deelScherm = new MediaLijstController(); break;
		case 10: deelScherm = new MediaDetailsController(); break;
		case 11:break;
		case 12:break;
		default: 
			System.out.println("Fout bij het laden van het juiste component :"+welke+" bestaat niet");
			deelScherm = new SessieKalenderDeelScherm();
		}
		return deelScherm;
	}
}
