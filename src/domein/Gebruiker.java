package domein;

import java.util.*;

public class Gebruiker {
	// bv sb12356
	private String userName;
	//de asp.net guid
	private String gebruikerId;
	private Long idNumber;
	private String voornaam, achternaam;
	private GebruikerType type;
	private StatusType status;
	private List<SessieGebruiker> inschrijvingen;
	
	//voor jpa 
	public Gebruiker() {}
	//voor nieuwe instantie
	public Gebruiker( String voornaam, String achternaam,GebruikerType type, StatusType status) {
		this.inschrijvingen = new ArrayList<SessieGebruiker>();
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setType(type);
		setStatus(status);
	}
	public List<SessieGebruiker> getInschrijvingen() {
		return inschrijvingen;
	}
	public Long getIdNumber() {
		return idNumber;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public String getAchternaam() {
		return achternaam;
	}
	public GebruikerType getType() {
		return type;
	}
	public StatusType getStatus() {
		return status;
	}
	public String getUserName() {
		return userName;
	}
	public String getGebruikerId() {
		return gebruikerId;
	}
	
	private void setSessieGebruikers(List<SessieGebruiker> inschrijvingen) {
		this.inschrijvingen = inschrijvingen;
	}
	private void setVoornaam(String voornaam) {
		if(voornaam == null || voornaam.equals("")) {
			throw new IllegalArgumentException("voornaam van de gebruiker moet ingevuld zijn.");
		}
		this.voornaam = voornaam;
	}
	private void setAchternaam(String achternaam) {
		if(achternaam == null || achternaam.equals("")) {
			throw new IllegalArgumentException("achternaam van de gebruiker moet ingevuld zijn.");
		}
		this.achternaam = achternaam;
	}
	private void setType(GebruikerType type) {
		this.type = type;
	}
	private void setStatus(StatusType status) {
		this.status = status;
	}

	

}