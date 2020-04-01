package domein;

import java.util.*;

public class Gebruiker {

	private List<SessieGebruiker> sessieGebruikers;
	private Long idNumber;
	private String voornaam;
	private String achternaam;
	private GebruikerType type;
	private StatusType status;
	public Gebruiker( String voornaam, String achternaam,
			GebruikerType type, StatusType status) {
		this.sessieGebruikers = new ArrayList<SessieGebruiker>();
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.type = type;
		this.status = status;
	}
	public List<SessieGebruiker> getSessieGebruikers() {
		return sessieGebruikers;
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
	private void setSessieGebruikers(List<SessieGebruiker> sessieGebruikers) {
		this.sessieGebruikers = sessieGebruikers;
	}
	private void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	private void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}
	private void setType(GebruikerType type) {
		this.type = type;
	}
	private void setStatus(StatusType status) {
		this.status = status;
	}

	

}