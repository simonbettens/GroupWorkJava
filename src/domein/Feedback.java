package domein;

import java.time.LocalDateTime;

public class Feedback {

	private int id, aantalSterren;
	private String inhoud, voornaam, achternaam, username;
	private LocalDateTime tijdToegevoegd;
	//voor jpa
	public Feedback() {}
	//voor nieuwe instaties
	public Feedback(String username, int aantalSterren, String inhoud, String voornaam, String achternaam,
			LocalDateTime tijdToegevoegd) {
		setUsername(username);
		setAantalSterren(aantalSterren);
		setInhoud(inhoud);
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setTijdToegevoegd(tijdToegevoegd);
	}
	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public int getAantalSterren() {
		return aantalSterren;
	}
	public String getInhoud() {
		return inhoud;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public String getAchternaam() {
		return achternaam;
	}
	public LocalDateTime getTijdToegevoegd() {
		return tijdToegevoegd;
	}
	public void setUsername(String username) {
		if(username == null|| username.equals("")) {
			throw new IllegalArgumentException("username moet ingevuld zijn");
		}
		this.username = username;
	}
	public void setAantalSterren(int aantalSterren) {
		if(aantalSterren<=0 || aantalSterren>5) {
			throw new IllegalArgumentException("aantalsterren moet tussen 0 en 5 liggen");
		}
		this.aantalSterren = aantalSterren;
	}
	public void setInhoud(String inhoud) {
		this.inhoud = inhoud;
	}
	public void setVoornaam(String voornaam) {
		if(voornaam == null|| voornaam.equals("")) {
			throw new IllegalArgumentException("voornaam moet ingevuld zijn");
		}
		this.voornaam = voornaam;
	}
	public void setAchternaam(String achternaam) {
		if(achternaam == null|| achternaam.equals("")) {
			throw new IllegalArgumentException("achternaam moet ingevuld zijn");
		}
		this.achternaam = achternaam;
	}
	public void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		if(tijdToegevoegd.isBefore(LocalDateTime.now())){
			throw new IllegalArgumentException("tijd mag niet in het verleden liggen");
		}
		this.tijdToegevoegd = tijdToegevoegd;
	}
	

}