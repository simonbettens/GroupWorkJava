package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;

@Entity(name="Media")
@Table(name = "Media")
public class Media implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "SessieId", referencedColumnName = "SessieId")
	private Sessie sessie;
	@Column(name="Adress")
	private String adress;
	@Column(name="Naam")
	private String naam;
	@Column(name="TijdToegevoegd")
	private LocalDateTime tijdToegevoegd;
	
	@Basic
	@Column(name="MediaType")
    private int mediaTypeValue;
    @Transient
	private MediaType mediaType;
    
	@Column(name="TypeMedia")
	@Enumerated(EnumType.STRING)
    private TypeMedia specifiekeKlasse;
	
	@PostLoad
    private void fillTransientPriorieteit(){
        if (mediaTypeValue > 0) {
            this.mediaType = MediaType.of(mediaTypeValue);
        }
    }
 
    @PrePersist
    private void fillPersistentPriorieteit() {
        if (mediaType != null) {
            this.mediaTypeValue = mediaType.getMediaTypeValue();
        }
    }
    @Transient
	 private final SimpleStringProperty naamProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty tijdToegevoegdProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty typeProperty = new SimpleStringProperty();
	//voor jpa
	public Media() {}
	//voor nieuwe instantie
	public Media(Sessie sessie,String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		setNaam(naam);
		setAdress(adress);
		setTijdToegevoegd(tijdToegevoegd);
		setMediaType(mediaType);
		this.sessie = sessie;
		switch(mediaType) {
		case LINK:setSpecifiekeKlasse(TypeMedia.Link);
			break;
		case AFBEELDING: setSpecifiekeKlasse(TypeMedia.Afbeelding);
			break;
		case YOUTUBEVIDEO:
		case VIDEO:setSpecifiekeKlasse(TypeMedia.Video);
			break;
		case EXCEL:
		case PDF:
		case POWERPOINT:
		case WORD:
		case ZIP:
		case ANDERDOCUMENT:
		default: setSpecifiekeKlasse(TypeMedia.Document);
			break;
			}
		
	}
	public int getId() {
		return id;
	}
	public String getAdress() {
		return adress;
	}
	public String getNaam() {
		naamProperty.setValue(this.naam);
		return naam;
	}
	public LocalDateTime getTijdToegevoegd() {
		typeProperty.setValue(DatumEnTijdHelper.dateTimeFormat(tijdToegevoegd));
		return tijdToegevoegd;
	}
	public MediaType getMediaType() {
		typeProperty.setValue(MediaType.MediaTypeToString(mediaType));
		return mediaType;
	}
	public TypeMedia getSpecifiekeKlasse() {
		return specifiekeKlasse;
	}
	
	
	public SimpleStringProperty getNaamProperty() {
		naamProperty.setValue(naam);
		return naamProperty;
	}

	public SimpleStringProperty getTijdToegevoegdProperty() {
		tijdToegevoegdProperty.setValue(DatumEnTijdHelper.dateTimeFormat(tijdToegevoegd));
		return tijdToegevoegdProperty;
	}

	public SimpleStringProperty getTypeProperty() {
		typeProperty.setValue(MediaType.MediaTypeToString(mediaType));
		return typeProperty;
	}

	private void setAdress(String adress) {
		if(adress.isEmpty()|| adress.isBlank()) {
			throw new IllegalArgumentException("adress moet ingevuld zijn");
		}
		this.adress = adress;
	}
	private void setNaam(String naam) {
		if(naam.isEmpty() || naam.isBlank()) {
			throw new IllegalArgumentException("naam moet ingevuld zijn");
		}
		naamProperty.setValue(naam);
		this.naam = naam;
	}
	private void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		if(tijdToegevoegd.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("je kan geen media toevoegen waarvan de tijd in het verleden ligt");
		}
		typeProperty.setValue(DatumEnTijdHelper.dateTimeFormat(tijdToegevoegd));
		this.tijdToegevoegd = tijdToegevoegd;
	}
	private void setMediaType(MediaType mediaType) {
		typeProperty.setValue(MediaType.MediaTypeToString(mediaType));
		this.mediaType = mediaType;
	}
	private void setSpecifiekeKlasse(TypeMedia specifiekeKlasse) {
		this.specifiekeKlasse = specifiekeKlasse;
	}
	
	public int pasMediaAan(String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		int verandering = 0;
		if(adress != this.adress) {
			verandering++;
			setAdress(adress);
		}
		if(naam != this.naam) {
			verandering++;
			setNaam(naam);
		}
		if(tijdToegevoegd != this.tijdToegevoegd) {
			verandering++;
			setTijdToegevoegd(tijdToegevoegd);
		}
		if(mediaType != this.mediaType) {
			verandering++;
			setMediaType(mediaType);
		}
		
		return verandering;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", adress=" + adress + ", naam=" + naam + ", tijdToegevoegd=" + tijdToegevoegd
				+ ", mediaTypeValue=" + mediaTypeValue + ", mediaType=" + mediaType + "]";
	}	
}