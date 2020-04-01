package domein;

import java.time.LocalDateTime;

public class Aankondiging {

	private Verantwoordelijke verantwoordelijke;
	private int aankondigingId;
	private LocalDateTime gepost;
	private String inhoud;
	private AankondigingPrioriteit prioriteit;
	public Aankondiging(Verantwoordelijke verantwoordelijke, int aankondigingId, LocalDateTime gepost, String inhoud,
			AankondigingPrioriteit prioriteit) {
		this.verantwoordelijke = verantwoordelijke;
		this.aankondigingId = aankondigingId;
		this.gepost = gepost;
		this.inhoud = inhoud;
		this.prioriteit = prioriteit;
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
		this.gepost = gepost;
	}
	private void setInhoud(String inhoud) {
		this.inhoud = inhoud;
	}
	private void setPrioriteit(AankondigingPrioriteit prioriteit) {
		this.prioriteit = prioriteit;
	}


}