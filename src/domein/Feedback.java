package domein;

import java.time.LocalDateTime;

public class Feedback {

	private int id;
	private String username;
	private int aantalSterren;
	private String inhoud;
	private String voornaam;
	private String achternaam;
	private LocalDateTime tijdToegevoegd;
	public Feedback( String username, int aantalSterren, String inhoud, String voornaam, String achternaam,
			LocalDateTime tijdToegevoegd) {
		this.username = username;
		this.aantalSterren = aantalSterren;
		this.inhoud = inhoud;
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.tijdToegevoegd = tijdToegevoegd;
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
		this.username = username;
	}
	public void setAantalSterren(int aantalSterren) {
		this.aantalSterren = aantalSterren;
	}
	public void setInhoud(String inhoud) {
		this.inhoud = inhoud;
	}
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}
	public void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		this.tijdToegevoegd = tijdToegevoegd;
	}
	

}