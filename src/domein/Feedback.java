package domein;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;

@Entity(name = "Feedback")
@Table(name = "Feedback")
public class Feedback implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	public int id;
	
	@Column(name = "GebruikerUserName")
	public String gebruikerUserName;
	
	@Column(name = "AantalSterren")
	public int aantalSterren;
	
	@Column(name = "Inhoud")
	public String inhoud;
	
	@Column(name = "Voornaam")
	public String voornaam;
	
	@Column(name = "Achternaam")
	public String achternaam;
	
	@Column(name = "TijdToegevoegd")
	public LocalDateTime tijdToegevoegd;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "SessieId", referencedColumnName = "SessieId")
	public Sessie sessie;

	@Transient
	 private final SimpleStringProperty naamProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty tijdToegevoegdProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty aantalSterrenProperty = new SimpleStringProperty();
	public Feedback() {

	}

	public Feedback(int aantalSterren, LocalDateTime tijdToegevoegd, String voornaam, String achternaam,
			String gebruikerUserName, String inhoud) {
		setAantalSterren(aantalSterren);
		setTijdToegevoegd(LocalDateTime.now());
		setInhoud(inhoud);
		setVoornaam(voornaam);
		setAchternaam(achternaam);
		setGebruikerUserName(gebruikerUserName);
	}

	public int getId() {
		return id;
	}

	public String getGebruikerUserName() {
		return gebruikerUserName;
	}

	public int getAantalSterren() {
		return aantalSterren;
	}

	public String getInhoud() {
		return inhoud;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public LocalDateTime getTijdToegevoegd() {
		return tijdToegevoegd;
	}

	private void setId(int id) {
		this.id = id;
	}

	private void setGebruikerUserName(String gebruikerUserName) {
		this.gebruikerUserName = gebruikerUserName;
	}

	private void setAantalSterren(int aantalSterren) {
		this.aantalSterren = aantalSterren;
	}

	private void setInhoud(String inhoud) {
		this.inhoud = inhoud;
	}

	private void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	private void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	private void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		this.tijdToegevoegd = tijdToegevoegd;
	}
	public String getVolledigeNaam() {
		return achternaam + " " + voornaam;
	}

	public SimpleStringProperty getNaamProperty() {
		naamProperty.setValue(getVolledigeNaam());
		return naamProperty;
	}

	public SimpleStringProperty getTijdToegevoegdProperty() {
		tijdToegevoegdProperty.setValue(DatumEnTijdHelper.dateTimeFormat(getTijdToegevoegd()));
		return tijdToegevoegdProperty;
	}

	public SimpleStringProperty getAantalSterrenProperty() {
		aantalSterrenProperty.setValue(String.format("%d", aantalSterren));
		return aantalSterrenProperty;
	}
	

}
