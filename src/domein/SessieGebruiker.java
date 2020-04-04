package domein;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="SessieGebruiker")
@Table(name = "SessieGebruiker")
public class SessieGebruiker {

	@Column(name="Aanwezig")
	private boolean aanwezig;
	@Column(name="AanwezigheidBevestigd")
	private boolean aanwezigheidBevestigd;
	@ManyToOne
	private Gebruiker gebruiker;
	@ManyToOne
	private Sessie sessie;
	@Column(name="GebruikerId")
	private UUID gebruikerId;
	@Column(name="SessieId")
	private int sessieId;
	//voor jpa
	public SessieGebruiker() {}
	//nieuwe instanties
	public SessieGebruiker(Gebruiker gebruiker, Sessie sessie, boolean aanwezig, boolean aanwezigBev) {
		setGebruiker(gebruiker);
		setSessie(sessie);
		this.gebruikerId = gebruiker.getId();
		this.sessieId = sessie.getSessieId();
		setAanwezig(aanwezig);
		setAanwezigheidBevestigd(aanwezigBev);
	}
	public Gebruiker getGebruiker() {
		return gebruiker;
	}
	public Sessie getSessie() {
		return sessie;
	}
	public UUID getGebruikerId() {
		return gebruikerId;
	}
	public int getSessieId() {
		return sessieId;
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
	private void setAanwezig(boolean aanwezig) {
		this.aanwezig = aanwezig;
	}
	private void setAanwezigheidBevestigd(boolean aanwezigheidBevestigd) {
		this.aanwezigheidBevestigd = aanwezigheidBevestigd;
	}
	
}