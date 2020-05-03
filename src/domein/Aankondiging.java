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

import javafx.beans.property.SimpleStringProperty;

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
	
	@Basic
	@Column(name="Prioriteit")
    private int prioriteitValue;
    @Transient
	protected AankondigingPrioriteit prioriteit;
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

    @Transient
	 private final SimpleStringProperty verantwoordelijkeProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty tijdToegevoegdProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty prioriteitProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty inhoudProperty = new SimpleStringProperty();
    
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
		verantwoordelijkeProperty.setValue(verantwoordelijke.getVoornaam() + verantwoordelijke.getAchternaam());
		return verantwoordelijke;
	}
	
	public int getAankondigingId() {
		return aankondingId;
	}
	public LocalDateTime getGepost() {
		tijdToegevoegdProperty.setValue(DatumEnTijdHelper.dateTimeFormat(gepost));
		return gepost;
	}
	public String getInhoud() {
		inhoudProperty.setValue(inhoud);
		return inhoud;
	}
	public AankondigingPrioriteit getPrioriteit() {
		//prioriteitProperty.setValue(AankondigingPrioriteit.AankondigingPrioriteitToString(prioriteit));
		return prioriteit;
	}
	
	
	public int getPrioriteitValue() {
		return prioriteitValue;
	}
	
	public void setPrioriteitValue(int prioriteitValue) {
		this.prioriteitValue = prioriteitValue;
	}

	private void setVerantwoordelijke(Gebruiker verantwoordelijke) {
		verantwoordelijkeProperty.setValue(verantwoordelijke.getVoornaam() + verantwoordelijke.getAchternaam());
		this.verantwoordelijke = verantwoordelijke;
	}
	private void setGepost(LocalDateTime gepost) {
		if(gepost.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("tijd gepost is in het verleden.");
		}
		tijdToegevoegdProperty.setValue(DatumEnTijdHelper.dateTimeFormat(gepost));
		this.gepost = gepost;
	}
	protected void setInhoud(String inhoud) {
		if(inhoud.isEmpty() || inhoud.isBlank()) {
			throw new IllegalArgumentException("Inhoud van de aankondiging moet ingevuld zijn");
		}
		inhoudProperty.setValue(inhoud);
		this.inhoud = inhoud;
	}
	protected void setPrioriteit(AankondigingPrioriteit prioriteit) {
		prioriteitProperty.setValue(AankondigingPrioriteit.AankondigingPrioriteitToString(prioriteit));
		setPrioriteitValue(prioriteit.getPriority());
		this.prioriteit = prioriteit;
	}
	
	public SimpleStringProperty getVerantwoordelijkeProperty() {
		verantwoordelijkeProperty.setValue(verantwoordelijke.getVoornaam() + verantwoordelijke.getAchternaam());
		return verantwoordelijkeProperty;
	}

	public SimpleStringProperty getTijdToegevoegdProperty() {
		tijdToegevoegdProperty.setValue(DatumEnTijdHelper.dateTimeFormat(gepost));
		return tijdToegevoegdProperty;
	}

	public SimpleStringProperty getPrioriteitProperty() {
		prioriteitProperty.setValue(AankondigingPrioriteit.AankondigingPrioriteitToString(prioriteit));
		return prioriteitProperty;
	}

	public SimpleStringProperty getInhoudProperty() {
		inhoudProperty.setValue(inhoud);
		return inhoudProperty;
	}
	
	@Override
	public String toString() {
		return "Aankondiging met als id :" + aankondingId + ", gepost op :" + gepost + ", bevat deze inhoud :" + inhoud+", met prioriteit=" + prioriteit + " en is van het type aankondiging";
	}
	
	


}