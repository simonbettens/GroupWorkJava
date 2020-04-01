package domein;

import java.util.*;

public class Verantwoordelijke extends Gebruiker {

	private List<Sessie> beheerdeSessies;

	//jpa
	public Verantwoordelijke() {
		super();
	}
	//voor nieuwe instanties
	public Verantwoordelijke(String voornaam, String achternaam, GebruikerType type, StatusType status) {
		super(voornaam, achternaam, type, status);
		// TODO Auto-generated constructor stub
		beheerdeSessies = new ArrayList<>();
	}

	public List<Sessie> getBeheerdeSessies() {
		return beheerdeSessies;
	}

	private void setBeheerdeSessies(List<Sessie> beheerdeSessies) {
		this.beheerdeSessies = beheerdeSessies;
	}

}