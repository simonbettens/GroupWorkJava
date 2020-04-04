package domein;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name="Aankondiging")
@Table(name = "Aankondiging")
public class Aankondiging implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="AankondingId")
	private int aankondingId;
	@Column(name="Gepost")
	private LocalDateTime gepost;
	@Column(name="Inhoud")
	private String inhoud;
	
	@Basic
	@Column(name="Prioriteit")
    private int prioriteitValue;
    @Transient
	private AankondigingPrioriteit prioriteit;
    @Transient
	private Verantwoordelijke verantwoordelijke;
	
	@PostLoad
    private void fillTransientPriorieteit(){
        if (prioriteitValue > 0) {
            this.prioriteit = prioriteit.of(prioriteitValue);
        }
    }
 
    @PrePersist
    private void fillPersistentPriorieteit() {
        if (prioriteit != null) {
            this.prioriteitValue = prioriteit.getPriority();
        }
    }

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
		return aankondingId;
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
	
	@Override
	public String toString() {
		return "Aankondiging [aankondigingId=" + aankondingId + ", gepost=" + gepost + ", inhoud=" + inhoud
				+ ", prioriteitValue=" + prioriteitValue + ", prioriteit=" + prioriteit + "]";
	}
	
	


}