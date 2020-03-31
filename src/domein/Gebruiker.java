package domein;

import java.util.*;

public class Gebruiker {

	private Collection<SessieGebruiker> sessieGebruikers;
	private Long idNumber;
	private String voornaam;
	private String achternaam;
	private GebruikerType type;
	private StatusType status;

}