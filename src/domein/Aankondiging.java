package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="Aankondiging")
@Table(name = "Aankondiging")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Discriminator")
@DiscriminatorValue(value = "Aankondiging")
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
	@Transient
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy 'om' hh:mm");
	
	@Basic
	@Column(name="Prioriteit")
    private int prioriteitValue;
    @Transient
	private AankondigingPrioriteit prioriteit;
    @JoinColumn(name="VerantwoordelijkeId",referencedColumnName = "Id" )
    @ManyToOne(fetch = FetchType.EAGER)
	private Gebruiker verantwoordelijke;
	
	@PostLoad
    private void fillTransientPriorieteit(){
        if (prioriteitValue > 0) {
            this.prioriteit = AankondigingPrioriteit.of(prioriteitValue);
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
	public Aankondiging(Gebruiker verantwoordelijke, LocalDateTime gepost, String inhoud, AankondigingPrioriteit prioriteit) {
		setVerantwoordelijke(verantwoordelijke);
		setGepost(gepost);
		setInhoud(inhoud);
		setPrioriteit(prioriteit);
	}
	
	public Gebruiker getVerantwoordelijke() {
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
	private void setVerantwoordelijke(Gebruiker verantwoordelijke) {
		this.verantwoordelijke = verantwoordelijke;
	}
	private void setGepost(LocalDateTime gepost) {
		if(gepost.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("tijd gepost is in het verleden.");
		}
		this.gepost = gepost;
	}
	private void setInhoud(String inhoud) {
		if(inhoud.isEmpty() || inhoud.isBlank()) {
			throw new IllegalArgumentException("Inhoud van de aankondiging moet ingevuld zijn");
		}
		this.inhoud = inhoud;
	}
	private void setPrioriteit(AankondigingPrioriteit prioriteit) {
		this.prioriteit = prioriteit;
	}
	
	@Override
	public String toString() {
		return "Aankondiging met als id :" + aankondingId + ", gepost op :" + gepost + ", bevat deze inhoud :" + inhoud+", met prioriteit=" + prioriteit + " en is van het type aankondiging";
	}
	
	


}