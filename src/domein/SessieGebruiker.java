package domein;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name="SessieGebruiker")
@Table(name = "SessieGebruiker")
public class SessieGebruiker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private SessieGebruikerId combindedId;
	
	@Column(name="Aanwezig")
	private boolean aanwezig;
	@Column(name="AanwezigBevestiged")
	private boolean aanwezigheidBevestigd;
	
	@MapsId(value = "id")
	@ManyToOne(optional = false)
	@JoinColumn(name = "GebruikerId",referencedColumnName = "Id")
	private Gebruiker gebruiker;
	
	
	@MapsId(value = "sessieId")
	@ManyToOne(optional = false)
	@JoinColumn(name = "SessieId",referencedColumnName = "SessieId")
	private Sessie sessie;
	
	
	//voor jpa
	public SessieGebruiker() {}
	//nieuwe instanties
	public SessieGebruiker(Gebruiker gebruiker, Sessie sessie, boolean aanwezig, boolean aanwezigBev) {
		setGebruiker(gebruiker);
		setSessie(sessie);
		this.combindedId = new SessieGebruikerId(gebruiker.getId(),sessie.getSessieId());
		setAanwezig(aanwezig);
		setAanwezigheidBevestigd(aanwezigBev);
	}
	public Gebruiker getGebruiker() {
		return gebruiker;
	}
	public Sessie getSessie() {
		return sessie;
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
	
	@Override
	public String toString() {
		return "SessieGebruiker [aanwezig=" + aanwezig + ", aanwezigheidBevestigd=" + aanwezigheidBevestigd
				+ ", gebruikerId=" + combindedId.getGebruikerId() + ", sessieId="
				+ combindedId.getSessieId() + "]";
	}
	
}