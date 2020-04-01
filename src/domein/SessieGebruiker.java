package domein;

public class SessieGebruiker {

	private Long idNumber;
	private String username, voornaam, achternaam;
	private boolean aanwezig, aanwezigheidBevestigd;
	
	private Gebruiker gebruiker;
	private Sessie sessie;
	private String gebruikerId;
	private int sessieId;
	//voor jpa
	public SessieGebruiker() {}
	//nieuwe instanties
	public SessieGebruiker(Gebruiker gebruiker, Sessie sessie, boolean aanwezig, boolean aanwezigBev) {
		setGebruiker(gebruiker);
		setSessie(sessie);
		this.gebruikerId = gebruiker.getGebruikerId();
		this.sessieId = sessie.getSessieId();
		this.idNumber = gebruiker.getIdNumber();
		this.username = gebruiker.getUserName();
		this.voornaam = gebruiker.getUserName();
		this.achternaam = gebruiker.getAchternaam();
		setAanwezig(aanwezig);
		setAanwezigheidBevestigd(aanwezigBev);
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
		if(gebruiker ==null) {
			throw new IllegalArgumentException("Gebruiker bestaat niet");
		}
		this.gebruiker = gebruiker;
	}
	private void setSessie(Sessie sessie) {
		if(sessie == null) {
			throw new IllegalArgumentException("Sessie bestaat niet");
		}
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