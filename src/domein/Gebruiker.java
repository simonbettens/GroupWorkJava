package domein;

import java.util.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="AspNetUsers")
@Table(name = "AspNetUsers")
public class Gebruiker {
	
	@Id
	@Column(name="Id")
	private UUID id;
	// bv sb12356
	@Column(name="UserName")
	private String userName;
	//de asp.net guid
	@Column(name="IdNumber")
	private Long idNumber;
	@Column(name="Voornaam")
	private String voornaam;
	@Column(name="Achternaam")
	private String achternaam;
	
	@OneToMany
	private List<SessieGebruiker> inschrijvingen;
	
	@Basic
	@Column(name="Type")
    private int gebruikersTypeValue;
    @Transient
	private GebruikerType gebruikersType;
	
    @Basic
	@Column(name="Status")
    private int statusTypeValue;
    @Transient
	private StatusType statusType;
    
	@PostLoad
    private void fillTransientEnums(){
        if (gebruikersTypeValue > 0) {
            this.gebruikersType = gebruikersType.of(gebruikersTypeValue);
        }
        
        if (statusTypeValue > 0) {
            this.statusType = statusType.of(statusTypeValue);
        }
    }
 
    @PrePersist
    private void fillPersistentEnums() {
        if (gebruikersType != null) {
            this.gebruikersTypeValue = gebruikersType.getGebruikersTypeValue();
        }
        
        if (gebruikersType != null) {
            this.statusTypeValue = statusType.getStatusTypeValue();
        }
    }
	
	//voor jpa 
	public Gebruiker() {}
	//voor nieuwe instantie
	public Gebruiker( String voornaam, String achternaam,GebruikerType type, StatusType status) {
		this.inschrijvingen = new ArrayList<SessieGebruiker>();
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setType(type);
		setStatus(status);
	}
	public List<SessieGebruiker> getInschrijvingen() {
		return inschrijvingen;
	}
	public Long getIdNumber() {
		return idNumber;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public String getAchternaam() {
		return achternaam;
	}
	public GebruikerType getType() {
		return gebruikersType;
	}
	public StatusType getStatus() {
		return statusType;
	}
	public String getUserName() {
		return userName;
	}
	public UUID getId() {
		return id;
	}
	
	
	public void setId(UUID id) {
		this.id = id;
	}
	private void setSessieGebruikers(List<SessieGebruiker> inschrijvingen) {
		this.inschrijvingen = inschrijvingen;
	}
	private void setVoornaam(String voornaam) {
		if(voornaam == null || voornaam.equals("")) {
			throw new IllegalArgumentException("voornaam van de gebruiker moet ingevuld zijn.");
		}
		this.voornaam = voornaam;
	}
	private void setAchternaam(String achternaam) {
		if(achternaam == null || achternaam.equals("")) {
			throw new IllegalArgumentException("achternaam van de gebruiker moet ingevuld zijn.");
		}
		this.achternaam = achternaam;
	}
	private void setType(GebruikerType type) {
		this.gebruikersType = type;
	}
	private void setStatus(StatusType status) {
		this.statusType = status;
	}

	

}