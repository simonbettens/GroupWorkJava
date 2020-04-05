package domein;

import java.util.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="Gebruiker")
@Table(name = "AspNetUsers")
public class Gebruiker {
	
	@Id
	@Column(name="Id")
	private String id;
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
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "Gebruiker",cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn(name = "Id",referencedColumnName = "Id")
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
		setId(UUID.randomUUID().toString());
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
	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
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

	@Override
	public String toString() {
		return "Gebruiker [id=" + id + ", userName=" + userName + ", idNumber=" + idNumber + ", voornaam=" + voornaam
				+ ", achternaam=" + achternaam + ", inschrijvingen=" + inschrijvingen + ", gebruikersTypeValue="
				+ gebruikersTypeValue + ", gebruikersType=" + gebruikersType + ", statusTypeValue=" + statusTypeValue
				+ ", statusType=" + statusType + "]";
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((achternaam == null) ? 0 : achternaam.hashCode());
		result = prime * result + ((gebruikersType == null) ? 0 : gebruikersType.hashCode());
		result = prime * result + gebruikersTypeValue;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
		result = prime * result + ((inschrijvingen == null) ? 0 : inschrijvingen.hashCode());
		result = prime * result + ((statusType == null) ? 0 : statusType.hashCode());
		result = prime * result + statusTypeValue;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((voornaam == null) ? 0 : voornaam.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gebruiker other = (Gebruiker) obj;
		if (achternaam == null) {
			if (other.achternaam != null)
				return false;
		} else if (!achternaam.equals(other.achternaam))
			return false;
		if (gebruikersType != other.gebruikersType)
			return false;
		if (gebruikersTypeValue != other.gebruikersTypeValue)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idNumber == null) {
			if (other.idNumber != null)
				return false;
		} else if (!idNumber.equals(other.idNumber))
			return false;
		if (inschrijvingen == null) {
			if (other.inschrijvingen != null)
				return false;
		} else if (!inschrijvingen.equals(other.inschrijvingen))
			return false;
		if (statusType != other.statusType)
			return false;
		if (statusTypeValue != other.statusTypeValue)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (voornaam == null) {
			if (other.voornaam != null)
				return false;
		} else if (!voornaam.equals(other.voornaam))
			return false;
		return true;
	}
	
	

	

}