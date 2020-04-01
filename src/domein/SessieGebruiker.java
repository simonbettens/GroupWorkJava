package domein;

public class SessieGebruiker {

	private Gebruiker gebruiker;
	private Sessie sessie;
	private String gebruikerId;
	private int sessieId;
	private Long idNumber;
	private String username;
	private String voornaam;
	private String achternaam;
	private boolean aanwezig;
	private boolean aanwezigheidBevestigd;
	public SessieGebruiker(Gebruiker gebruiker, Sessie sessie, String gebruikerId, int sessieId, Long idNumber,
			String username, String voornaam, String achternaam, boolean aanwezig, boolean aanwezigheidBevestigd) {
		this.gebruiker = gebruiker;
		this.sessie = sessie;
		this.gebruikerId = gebruikerId;
		this.sessieId = sessieId;
		this.idNumber = idNumber;
		this.username = username;
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.aanwezig = aanwezig;
		this.aanwezigheidBevestigd = aanwezigheidBevestigd;
	}
	public Gebruiker getGebruiker() {
		return gebruiker;
	}
	public Sessie getSessie() {
		return sessie;
	}
	public String getGebruikerId() {
		return gebruikerId;
	}
	public int getSessieId() {
		return sessieId;
	}
	public Long getIdNumber() {
		return idNumber;
	}
	public String getUsername() {
		return username;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public String getAchternaam() {
		return achternaam;
	}
	public boolean isAanwezig() {
		return aanwezig;
	}
	public boolean isAanwezigheidBevestigd() {
		return aanwezigheidBevestigd;
	}
	private void setGebruiker(Gebruiker gebruiker) {
		this.gebruiker = gebruiker;
	}
	private void setSessie(Sessie sessie) {
		this.sessie = sessie;
	}
	private void setUsername(String username) {
		this.username = username;
	}
	private void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	private void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}
	private void setAanwezig(boolean aanwezig) {
		this.aanwezig = aanwezig;
	}
	private void setAanwezigheidBevestigd(boolean aanwezigheidBevestigd) {
		this.aanwezigheidBevestigd = aanwezigheidBevestigd;
	}
	

	
}