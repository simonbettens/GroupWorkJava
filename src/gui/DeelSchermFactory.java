package gui;

public class DeelSchermFactory {
	public static DeelScherm create(int welke) {
		DeelScherm deelScherm = null;
		switch(welke) {
		case 1:deelScherm = new SessieKalenderDeelScherm();break;
		case 2:deelScherm = new StatistiekScherm();break;
		
		case 3:deelScherm = new GebruikerDeelScherm();break;
		case 4:deelScherm =  new AankondigingenDeelScherm();break;
		
		case 5:deelScherm = new SessieLijstController();break;
		case 6:deelScherm = new SessieDetailsController();break;
		
		case 7:deelScherm = new SessieKalenderDetailsController(false);break;
		case 8:deelScherm = new SessieKalenderDetailsController(true);break;
		
		case 9: deelScherm = new MediaLijstController(); break;
		case 10: deelScherm = new MediaDetailsController(); break;
		
		case 11: deelScherm = new SessieAankondigingLijstController(); break;
		case 12: deelScherm = new SessieAankondigingDetailsController(); break;

		case 13: deelScherm = new InschrijvingLijstController(); break;
		case 14: deelScherm = new InschrijvingDetailsController(); break;
		
		case 15: deelScherm = new FeedbackLijstController(); break;
		case 16: deelScherm = new FeedbackDetailsController(); break;
		
		case 17: deelScherm = new AlgemeneAankondigingLijstController(); break;
		case 18: deelScherm = new AlgemeneAankondigingDetailsController(); break;
		
		case 19: deelScherm = new StatistiekDetailsController();break;
		case 20 : deelScherm = new GebruikerStatistiekLijstController();break;
		case 21 : deelScherm = new SessieStatistiekLijstController();break;
		case 22 :deelScherm = new GebruikerStatistiekController();break;
		default: 
			System.out.println("Fout bij het laden van het juiste component :"+welke+" bestaat niet");
			deelScherm = new SessieKalenderDeelScherm();
		}
		return deelScherm;
	}
}
