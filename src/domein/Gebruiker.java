package domein;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity(name = "Gebruiker")
@NamedQueries({
		@NamedQuery(name = "Gebruiker.findByUsername", query = "select g from Gebruiker g where g.userName = :username"),
		@NamedQuery(name = "Gebruiker.findByDiscriminator", query = "select g from Gebruiker g where g.discriminator = :discriminator"),
})

@Table(name = "AspNetUsers")
public class Gebruiker {
	// de asp.net guid
	@Id
	@Column(name = "Id", columnDefinition = "uniqueidentifier", nullable = false)
	private String id;
	// bv sb12356
	@Column(name = "UserName", columnDefinition = "nvarchar(256) not null", nullable = false)
	private String userName;
	@Column(name = "NormalizedUserName", columnDefinition = "nvarchar(256)")
	private String normalizedUserName;
	@Column(name = "IdNumber", columnDefinition = "bigint not null", nullable = false)
	private Long idNumber;
	@Column(name = "Voornaam", columnDefinition = "nvarchar(max) not null", nullable = false)
	private String voornaam;
	@Column(name = "Achternaam", columnDefinition = "nvarchar(max) not null", nullable = false)
	private String achternaam;
	@Column(name = "PasswordHash", columnDefinition = "nvarchar(MAX)")
	private String passwoordHash;
	@Column(name = "JavaPasswoord")
	private String javaPasswoord;
	@Column(name = "SecurityStamp", columnDefinition = "nvarchar(MAX)")
	private String securityStamp;
	@Column(name = "ConcurrencyStamp", columnDefinition = "nvarchar(MAX)")
	private String concurrencyStamp;
	@Column(name = "Discriminator", columnDefinition = "nvarchar(MAX) not null", nullable = false)
	private String discriminator;
	@Column(name = "Email", columnDefinition = "nvarchar(256)")
	private String email;
	@Column(name = "NormalizedEmail", columnDefinition = "nvarchar(256)")
	private String normalizedEmail;
	@Column(name = "EmailConfirmed ", nullable = false)
	private boolean emailConfirmed;
	@Column(name = "PhoneNumberConfirmed", nullable = false)
	private boolean phoneNumberConfirmed;
	@Column(name = "TwoFactorEnabled", nullable = false)
	private boolean twoFactorEnabled;
	@Column(name = "LockoutEnabled", nullable = false)
	private boolean lockoutEnabled;
	@Column(name = "AccessFailedCount")
	private int accessFailedCount;
	@Column(name = "PhoneNumber", columnDefinition = "nvarchar(256)")
	private String phoneNumber = null;
	@Column(name = "LockoutEnd")
	private LocalDateTime lockoutEnd = null;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "Gebruiker", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn(name = "Id", referencedColumnName = "Id")
	private List<SessieGebruiker> inschrijvingen;

	@Basic
	@Column(name = "Type", nullable = false)
	private int gebruikersTypeValue;
	@Transient
	private GebruikerType gebruikersType;

	@Basic
	@Column(name = "Status", nullable = false)
	private int statusTypeValue;
	@Transient
	private StatusType statusType;
	@Transient
	 private final SimpleStringProperty volledigeNaamProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty gebruikerNaamProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty typeProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty statusProperty = new SimpleStringProperty();
	
	@PostLoad
	private void fillTransientEnums() {
		if (gebruikersTypeValue > 0) {
			this.gebruikersType = GebruikerType.of(gebruikersTypeValue);
		}

		if (statusTypeValue > 0) {
			this.statusType = StatusType.of(statusTypeValue);
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
	
	// voor jpa
	public Gebruiker() {
	}

	// voor nieuwe instantie
	public Gebruiker(String voornaam, String achternaam, String userName, String passwoord, String email, Long idNummer,
			GebruikerType type, StatusType status) {
		setId(UUID.randomUUID().toString().toUpperCase());
		this.inschrijvingen = new ArrayList<SessieGebruiker>();
		setUserName(userName);
		setNormalizedUserName(userName);
		setIdNumber(idNummer);
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setType(type);
		setStatus(status);
		setEmail(email);
		setNormalizedEmail(email);
		setSecurityStamp(UUID.randomUUID().toString());
		setConcurrencyStamp(UUID.randomUUID().toString());
		setEmailConfirmed(true);
		setPhoneNumberConfirmed(false);
		setLockoutEnabled(false);
		setTwoFactorEnabled(false);
		setAccessFailedCount(0);
		if (type.equals(GebruikerType.GEBRUIKER)) {
			this.discriminator = "Gebruiker";
		} else {
			this.discriminator = "Verantwoordelijke";
		}
		try {
			String pass = PasswoordHasher.generateStorngPasswordHash(passwoord);
			setPasswoordHash(pass);
			setJavaPasswoord(pass);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNormalizedUserName() {
		return normalizedUserName;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public String getEmail() {
		return email;
	}

	public String getNormalizedEmail() {
		return normalizedEmail;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public boolean isPhoneNumberConfirmed() {
		return phoneNumberConfirmed;
	}

	public boolean isTwoFactorEnabled() {
		return twoFactorEnabled;
	}

	public boolean isLockoutEnabled() {
		return lockoutEnabled;
	}

	public int getAccessFailedCount() {
		return accessFailedCount;
	}

	public String getPasswoordHash() {
		return passwoordHash;
	}

	public String getJavaPasswoord() {
		return javaPasswoord;
	}

	

	public String getSecurityStamp() {
		return securityStamp;
	}

	public String getConcurrencyStamp() {
		return concurrencyStamp;
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
	public String getVolledigeNaam() {
		return getVoornaam() + " " + getAchternaam();
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
	public SimpleStringProperty getVolledigeNaamProperty() {
		volledigeNaamProperty.set(getVolledigeNaam());
		return volledigeNaamProperty;
	}

	public SimpleStringProperty getGebruikerNaamProperty() {
		gebruikerNaamProperty.set(getUserName());
		return gebruikerNaamProperty;
	}

	public SimpleStringProperty getTypeProperty() {
		typeProperty.set(GebruikerType.toString(getType()));
		return typeProperty;
	}

	public SimpleStringProperty getStatusProperty() {
		statusProperty.set(StatusType.toString(getStatus()));
		return statusProperty;
	}

	private void setSessieGebruikers(List<SessieGebruiker> inschrijvingen) {
		this.inschrijvingen = inschrijvingen;
	}

	private void setVoornaam(String voornaam) {
		boolean correctFormaat = true;
		if(voornaam != null ) {
			Pattern p = Pattern.compile("[a-zA-Z]{1,100}");
			Matcher m = p.matcher(String.valueOf(voornaam));
			correctFormaat = m.matches();
		}
		
		if (voornaam == null || voornaam.isEmpty() || voornaam.isBlank() || !correctFormaat) {
			throw new IllegalArgumentException("voornaam van de gebruiker moet ingevuld zijn en van het juiste formaat zijn.");
		}
		this.voornaam = voornaam;
		volledigeNaamProperty.set(getVolledigeNaam());
	}

	private void setAchternaam(String achternaam) {
		boolean correctFormaat = true;
		if(achternaam != null ) {
			Pattern p = Pattern.compile("[a-zA-Z\\s]{1,100}");
			Matcher m = p.matcher(String.valueOf(achternaam));
			correctFormaat = m.matches();
		}
		
		if (achternaam == null || achternaam.isEmpty() || achternaam.isBlank() || !correctFormaat) {
			throw new IllegalArgumentException("achternaam van de gebruiker moet ingevuld zijn en van het juiste formaat zijn.");
		}
		this.achternaam = achternaam;
		volledigeNaamProperty.set(getVolledigeNaam());
	}

	private void setType(GebruikerType type) {
		this.gebruikersType = type;
		this.gebruikersTypeValue = type.getGebruikersTypeValue();
		typeProperty.set(GebruikerType.toString(getType()));

	}

	private void setStatus(StatusType status) {
		this.statusType = status;
		this.statusTypeValue = status.getStatusTypeValue();
		statusProperty.set(StatusType.toString(getStatus()));

	}

	public void setIdNumber(Long idNumber) {
		boolean correctFormaat = true;
		if(idNumber != null ) {
			Pattern p = Pattern.compile("[0-9]{13}");
			Matcher m = p.matcher(String.valueOf(idNumber));
			correctFormaat = m.matches();
		}
				
		if(idNumber == null || !correctFormaat) {
			throw new IllegalArgumentException("idNumber moet ingevuld zijn en van het juiste formaat zijn.");
		}
		
		this.idNumber = idNumber;
	}

	public void setUserName(String userName) {
		boolean correctFormaat = true;
		if(userName != null ) {
			Pattern p = Pattern.compile("[a-zA-Z]{2,4}[0-9]{6}");
			Matcher m = p.matcher(String.valueOf(userName));
			correctFormaat = m.matches();
		}
			
		if (userName == null || userName.isEmpty() || userName.isBlank() || !correctFormaat) {
			throw new IllegalArgumentException("Username van de gebruiker moet ingevuld zijn.");
		}
		this.userName = userName;
		gebruikerNaamProperty.set(getUserName());
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setEmail(String email) {
		boolean correctFormaat = true;
		if(email != null ) {
			Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\""
					+ "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\"
					+ "x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)"
					+ "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
					+ "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\"
					+ "x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
			Matcher m = p.matcher(String.valueOf(email));
			correctFormaat = m.matches();
		}
		
		if (email == null || email.isEmpty() || email.isBlank() || !correctFormaat) {
			throw new IllegalArgumentException("email van de gebruiker moet ingevuld zijn en van het juiste formaat zijn.");
		}
		this.email = email;
	}

	public void setNormalizedEmail(String normalizedEmail) {
		this.normalizedEmail = this.email;
	}

	private void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	private void setPhoneNumberConfirmed(boolean phoneNumberConfirmed) {
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}

	private void setLockoutEnabled(boolean lockoutEnabled) {
		this.lockoutEnabled = lockoutEnabled;
	}

	private void setTwoFactorEnabled(boolean twoFactorEnabled) {
		this.twoFactorEnabled = twoFactorEnabled;
	}

	private void setAccessFailedCount(int accessFailedCount) {
		this.accessFailedCount = accessFailedCount;
	}

	private void setPasswoordHash(String passwoordHash) {
		this.passwoordHash = passwoordHash;
	}
	private void setJavaPasswoord(String javaPasswoord) {
		this.javaPasswoord = javaPasswoord;
	}

	private void setNormalizedUserName(String normalizedUserName) {
		this.normalizedUserName = this.userName;
	}

	private void setSecurityStamp(String securityStamp) {
		this.securityStamp = securityStamp;
	}

	private void setConcurrencyStamp(String concurrencyStamp) {
		this.concurrencyStamp = concurrencyStamp;
	}
	
	public void addInschrijvingen(SessieGebruiker sessieGebruiker) {
		this.inschrijvingen.add(sessieGebruiker);
	}

	public int pasGebruikerAan(String voornaam, String achternaam, String userName, String passwoord, String email,
			Long idNummer, GebruikerType type, StatusType status) {
		int veranderingen = 0;
		if (!userName.equals(getUserName())) {
			setUserName(userName);
			setNormalizedUserName(userName);
			veranderingen++;
		}
		if (!idNummer.equals(getIdNumber())) {
			setIdNumber(idNummer);
			veranderingen++;

		}
		if (!voornaam.equals(getVoornaam())) {
			setVoornaam(voornaam);
			veranderingen++;

		}
		if (!achternaam.equals(getAchternaam())) {
			setAchternaam(achternaam);
			veranderingen++;

		}
		if (!type.equals(getType())) {
			setType(type);
			if (type.equals(GebruikerType.GEBRUIKER)) {
				this.discriminator = "Gebruiker";
			} else {
				this.discriminator = "Verantwoordelijke";
			}
			veranderingen++;

		}
		if (!status.equals(getStatus())) {
			setStatus(status);
			veranderingen++;

		}
		if (!email.equals(getEmail())) {
			setEmail(email);
			setNormalizedEmail(email);
			setEmailConfirmed(true);
			veranderingen++;
		}
		setPhoneNumberConfirmed(false);
		setLockoutEnabled(false);
		setTwoFactorEnabled(false);
		setAccessFailedCount(0);
		if (passwoord.isEmpty() || passwoord.isBlank()) {

		} else {
			try {
				String pass = PasswoordHasher.generateStorngPasswordHash(passwoord);
				setPasswoordHash(pass);
				setJavaPasswoord(pass);
				setSecurityStamp(UUID.randomUUID().toString());
				veranderingen++;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (veranderingen > 0) {
			setConcurrencyStamp(UUID.randomUUID().toString());
		}
		return veranderingen;
	}
	public void genereerNieuwId() {
		setId(UUID.randomUUID().toString().toUpperCase());
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