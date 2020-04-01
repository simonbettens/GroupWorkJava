package domein;

import java.time.LocalDateTime;

public class Aankondiging {

	private Verantwoordelijke verantwoordelijke;
	private int aankondigingId;
	private LocalDateTime gepost;
	private String inhoud;
	private AankondigingPrioriteit prioriteit;
	
	//voor jpa
	public Aankondiging() {}
	//voor nieuwe instanties
	public Aankondiging(Verantwoordelijke verantwoordelijke, LocalDateTime gepost, String inhoud, AankondigingPrioriteit prioriteit) {
		setVerantwoordelijke(verantwoordelijke);
		setGepost(gepost);
		setInhoud(inhoud);
		setPrioriteit(prioriteit);
	}
	public Verantwoordelijke getVerantwoordelijke() {
		return verantwoordelijke;
	}
	public int getAankondigingId() {
		return aankondigingId;
	}
	public LocalDateTime getGepost() {
		return gepost;
	}
	public String getInhoud() {
		return inhoud;
	}
	public AankondigingPrioriteit getPrioriteit() {
		return prioriteit;
	}
	private void setVerantwoordelijke(Verantwoordelijke verantwoordelijke) {
		this.verantwoordelijke = verantwoordelijke;
	}
	private void setGepost(LocalDateTime gepost) {
		if(gepost.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("tijd gepost is in het verleden.");
		}
		this.gepost = gepost;
	}
	private void setInhoud(String inhoud) {
		if(inhoud == null || inhoud.equals("")) {
			throw new IllegalArgumentException("Inhoud van de aankondiging moet ingevuld zijn");
		}
		this.inhoud = inhoud;
	}
	private void setPrioriteit(AankondigingPrioriteit prioriteit) {
		this.prioriteit = prioriteit;
	}


}