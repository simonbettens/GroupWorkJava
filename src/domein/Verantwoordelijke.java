package domein;

import java.util.*;

public class Verantwoordelijke extends Gebruiker {

	private List<Sessie> beheerdeSessies;

	public Verantwoordelijke( String voornaam, String achternaam,
			GebruikerType type, StatusType status) {
		super( voornaam, achternaam, type, status);
		// TODO Auto-generated constructor stub
	}

	public List<Sessie> getBeheerdeSessies() {
		return beheerdeSessies;
	}

	private void setBeheerdeSessies(List<Sessie> beheerdeSessies) {
		this.beheerdeSessies = beheerdeSessies;
	}
	

}