package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	@Transient
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy 'om' hh:mm");
	
	@Basic
	@Column(name="MediaType")
    private int mediaTypeValue;
    @Transient
	private MediaType mediaType;
	
	@PostLoad
    private void fillTransientPriorieteit(){
        if (mediaTypeValue > 0) {
            this.mediaType = mediaType.of(mediaTypeValue);
        }
    }
 
    @PrePersist
    private void fillPersistentPriorieteit() {
        if (mediaType != null) {
            this.mediaTypeValue = mediaType.getMediaTypeValue();
        }
    }
	
	//voor jpa
	public Media() {}
	//voor nieuwe instantie
	public Media(String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		setAdress(adress);
		setNaam(naam);
		setTijdToegevoegd(tijdToegevoegd);
		setMediaType(mediaType);
	}
	public int getId() {
		return id;
	}
	public String getAdress() {
		return adress;
	}
	public String getNaam() {
		return naam;
	}
	public LocalDateTime getTijdToegevoegd() {
		return tijdToegevoegd;
	}
	public MediaType getMediaType() {
		return mediaType;
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
		this.naam = naam;
	}
	private void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		if(tijdToegevoegd.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("je kan geen media toevoegen waarvan de tijd in het verleden ligt");
		}
		this.tijdToegevoegd = tijdToegevoegd;
	}
	private void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", adress=" + adress + ", naam=" + naam + ", tijdToegevoegd=" + tijdToegevoegd
				+ ", mediaTypeValue=" + mediaTypeValue + ", mediaType=" + mediaType + "]";
	}	
}