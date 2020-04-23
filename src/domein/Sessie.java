package domein;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;

@Entity(name="Sessie")
@Table(name = "Sessie")
public class Sessie implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SessieId")
	private int sessieId;
	@Column(name="MaxCap")
	private int maxCap;
	@Column(name="Naam")
	private String naam;
	@Column(name="Lokaal")
	private String lokaal;
	@Column(name="Beschrijving",length = 1000)
	private String beschrijving;
	@Transient
	private boolean bezig;
	@Column(name="StaatOpen")
	private boolean staatOpen;
	@Column(name="Gesloten")
	private boolean gesloten;
	@Transient
	private Duration duur;
	@Column(name="StartDatum")
	private LocalDateTime startDatum;
	@Column(name="EindDatum")
	private LocalDateTime eindDatum;
	@Transient
	 private final SimpleStringProperty titelProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty verantwoordelijkeNaamProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty startDatumProperty = new SimpleStringProperty();
	@Transient
	private final SimpleStringProperty duurProperty = new SimpleStringProperty();
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "VerantwoordelijkeId",referencedColumnName = "Id")
	private Gebruiker verantwoordelijke;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "Sessie",cascade = CascadeType.ALL)
	private List<SessieGebruiker> gebruikersIngeschreven;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "Sessie")
	@JoinColumn(name = "SessieId", referencedColumnName = "SessieId")
	private List<Media> media;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "Sessie")
	@JoinColumn(name = "SessieId", referencedColumnName = "SessieId")
	private List<SessieAankondiging> aankondigingen;
	@Transient
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy 'om' hh:mm");
	//nodig voor jpa
	public Sessie() {}
	
	//algemene constructor voor nieuwe instanties
	public Sessie(Gebruiker verantwoordelijke, String naam, LocalDateTime startDatum, LocalDateTime eindDatum,
			boolean gesloten, int maxCap,String lokaal, String beschrijving) {
		setVerantwoordelijke(verantwoordelijke);
		setNaam(naam);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setGesloten(false);
		setMaxCap(maxCap);
		setLokaal(lokaal);
		setBeschrijving(beschrijving);
		setStaatOpen(false);
		setDuur(Duration.between(startDatum, eindDatum));
		setBezig(false);
		this.gebruikersIngeschreven = new ArrayList<SessieGebruiker>();
		this.media = new ArrayList<Media>();
		this.aankondigingen = new ArrayList<SessieAankondiging>();
	}
	
	// ----------  getters
	public List<SessieGebruiker> getGebruikersIngeschreven() {
		return Collections.unmodifiableList(gebruikersIngeschreven);
	}
	public Gebruiker getVerantwoordelijke() {
		return verantwoordelijke;
	}
	public List<Media> getMedia() {
		return Collections.unmodifiableList(media);
	}
	public List<SessieAankondiging> getAankondigingen() {
		return Collections.unmodifiableList(aankondigingen);
	}
	public String getNaam() {
		return naam;
	}
	public int getSessieId() {
		return sessieId;
	}
	public LocalDateTime getStartDatum() {
		return startDatum;
	}
	public LocalDateTime getEindDatum() {
		return eindDatum;
	}
	public boolean isGesloten() {
		return gesloten;
	}
	public int getMaxCap() {
		return maxCap;
	}
	public String getLokaal() {
		return lokaal;
	}
	public String getBeschrijving() {
		return beschrijving;
	}
	public boolean isStaatOpen() {
		return staatOpen;
	}
	public Duration getDuur() {
		if(duur == null) {
			setDuur(Duration.between(startDatum, eindDatum));
		}
		return duur;
	}
	public boolean isBezig() {
		//telkens als je kijkt naar de attribut moet je zien of de waarde veranderd moet worden (wordt dynamish aangepast)
		boolean vernieuwBezig = (LocalDateTime.now().isAfter(startDatum)&&LocalDateTime.now().isBefore(eindDatum))|| LocalDateTime.now().isEqual(startDatum) ;
		setBezig(vernieuwBezig);
		return bezig;
	}
	
	// ------- setters
	private void setGebruikersIngeschreven(List<SessieGebruiker> gebruikersIngeschreven) {
		this.gebruikersIngeschreven = gebruikersIngeschreven;
	}
	private void setVerantwoordelijke(Gebruiker verantwoordelijke) {
		verantwoordelijkeNaamProperty.set(verantwoordelijke.getVolledigeNaam());
		this.verantwoordelijke = verantwoordelijke;
	}
	private void setMedia(List<Media> media) {
		this.media = media;
	}
	private void setAankondigingen(List<SessieAankondiging> aankondigingen) {
		this.aankondigingen = aankondigingen;
	}
	private void setNaam(String naam) {
		if(naam.isEmpty() || naam.isBlank()) {
			throw new IllegalArgumentException("Naam moet ingevuld zijn.");
		}
		titelProperty.set(naam);
		this.naam = naam;
	}
	private void setStartDatum(LocalDateTime startDatum) {
		if(startDatum == null) {
			throw new IllegalArgumentException("Startdatum moet ingevuld zijn");
		}
		if(startDatum.isBefore(LocalDateTime.now()))  {
			throw new IllegalArgumentException("Datum is in het verleden.");
		}
		startDatumProperty.set(dtf.format(startDatum));
		this.startDatum = startDatum;
	}
	private void setEindDatum(LocalDateTime eindDatum) {
		if(eindDatum == null) {
			throw new IllegalArgumentException("Einddatum moet ingevuld zijn");
		}
		if(startDatum.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Datum is in het verleden.");
		}
		this.eindDatum = eindDatum;
	}
	private void setGesloten(boolean gesloten) {
		this.gesloten = gesloten;
	}
	private void setMaxCap(int maxCap) {
		if(maxCap <= 0) {
			throw new IllegalArgumentException("MaxCap moet groter dan 0 zijn.");
		}
		this.maxCap = maxCap;
	}
	private void setLokaal(String lokaal) {
		if(lokaal.isEmpty() || lokaal.isBlank()) {
			throw new IllegalArgumentException("Lokaal moet ingevuld zijn.");
		}
		this.lokaal = lokaal;
	}
	private void setBeschrijving(String beschrijving) {
		// mag en kan null zijn
		this.beschrijving = beschrijving;
	}
	private void setStaatOpen(boolean staatOpen) {
		this.staatOpen = staatOpen;
	}
	private void setDuur(Duration duur) {
		if(duur.isNegative() || duur.isZero()) {
			throw new IllegalArgumentException("Duur mag niet 0 of negatief zijn.");
		}
		duurProperty.set(String.format("%d minuten", duur.toMinutesPart()));
		this.duur = duur;
	}
	private void setBezig(boolean bezig) {
		this.bezig = bezig;
	}
	
	// ----- lijst operatie's
	public void addInschrijving (SessieGebruiker sg) {
		this.gebruikersIngeschreven.add(sg);
	}
	public void addMediaItem(Media nieuwMedia) {
		media.add(nieuwMedia);
	}
	public void addAankondiging(SessieAankondiging nieuwAankondiging) {
		aankondigingen.add(nieuwAankondiging);
	}
	public void removeMediaItem(Media teVerwijderenMedia) {
		media.remove(teVerwijderenMedia);
	}
	public void removeAankondiging(SessieAankondiging teVerwijderenAankondiging) {
		aankondigingen.remove(teVerwijderenAankondiging);
	}
	public Media getMediaByIndex(int index) {
		return media.get(index);
	}
	public SessieAankondiging getAankondigingByIndex(int index) {
		return aankondigingen.get(index);
	}
	public SessieGebruiker getInschrijvingByIndex(int index) {
		return gebruikersIngeschreven.get(index);
	}

	public SimpleStringProperty getTitelProperty() {
		titelProperty.set(getNaam());
		return titelProperty;
	}

	public SimpleStringProperty getVerantwoordelijkeNaamProperty() {
		verantwoordelijkeNaamProperty.set(verantwoordelijke.getVolledigeNaam());
		return verantwoordelijkeNaamProperty;
	}

	public SimpleStringProperty getStartDatumProperty() {
		startDatumProperty.set(dtf.format(getStartDatum()));
		return startDatumProperty;
	}

	public SimpleStringProperty getDuurProperty() {
		if(duur==null) {
			setDuur(Duration.between(startDatum, eindDatum));
		}
		if(duur.toHoursPart()==0) {
			duurProperty.set(String.format("%d minuten", duur.toMinutesPart()));
		}else {
			duurProperty.set(String.format("%d uur, %d minuten", duur.toHoursPart(),duur.toMinutesPart()));
		}
		
		return duurProperty;
	}

	@Override
	public String toString() {
		return "Sessie [sessieId=" + sessieId + ", maxCap=" + maxCap + ", naam=" + naam + ", lokaal=" + lokaal
				+ ", beschrijving=" + beschrijving + ", bezig=" + bezig + ", staatOpen=" + staatOpen + ", gesloten="
				+ gesloten + ", duur=" + duur + ", startDatum=" + startDatum + ", eindDatum=" + eindDatum
				+ ", verantwoordelijke=" + verantwoordelijke + "]";
	}
	//nodig voor de mapping
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aankondigingen == null) ? 0 : aankondigingen.hashCode());
		result = prime * result + ((beschrijving == null) ? 0 : beschrijving.hashCode());
		result = prime * result + (bezig ? 1231 : 1237);
		result = prime * result + ((duur == null) ? 0 : duur.hashCode());
		result = prime * result + ((eindDatum == null) ? 0 : eindDatum.hashCode());
		result = prime * result + ((gebruikersIngeschreven == null) ? 0 : gebruikersIngeschreven.hashCode());
		result = prime * result + (gesloten ? 1231 : 1237);
		result = prime * result + ((lokaal == null) ? 0 : lokaal.hashCode());
		result = prime * result + maxCap;
		result = prime * result + ((media == null) ? 0 : media.hashCode());
		result = prime * result + ((naam == null) ? 0 : naam.hashCode());
		result = prime * result + sessieId;
		result = prime * result + (staatOpen ? 1231 : 1237);
		result = prime * result + ((startDatum == null) ? 0 : startDatum.hashCode());
		result = prime * result + ((verantwoordelijke == null) ? 0 : verantwoordelijke.hashCode());
		return result;
	}
	//nodig voor de mapping
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sessie other = (Sessie) obj;
		if (aankondigingen == null) {
			if (other.aankondigingen != null)
				return false;
		} else if (!aankondigingen.equals(other.aankondigingen))
			return false;
		if (beschrijving == null) {
			if (other.beschrijving != null)
				return false;
		} else if (!beschrijving.equals(other.beschrijving))
			return false;
		if (bezig != other.bezig)
			return false;
		if (duur == null) {
			if (other.duur != null)
				return false;
		} else if (!duur.equals(other.duur))
			return false;
		if (eindDatum == null) {
			if (other.eindDatum != null)
				return false;
		} else if (!eindDatum.equals(other.eindDatum))
			return false;
		if (gebruikersIngeschreven == null) {
			if (other.gebruikersIngeschreven != null)
				return false;
		} else if (!gebruikersIngeschreven.equals(other.gebruikersIngeschreven))
			return false;
		if (gesloten != other.gesloten)
			return false;
		if (lokaal == null) {
			if (other.lokaal != null)
				return false;
		} else if (!lokaal.equals(other.lokaal))
			return false;
		if (maxCap != other.maxCap)
			return false;
		if (media == null) {
			if (other.media != null)
				return false;
		} else if (!media.equals(other.media))
			return false;
		if (naam == null) {
			if (other.naam != null)
				return false;
		} else if (!naam.equals(other.naam))
			return false;
		if (sessieId != other.sessieId)
			return false;
		if (staatOpen != other.staatOpen)
			return false;
		if (startDatum == null) {
			if (other.startDatum != null)
				return false;
		} else if (!startDatum.equals(other.startDatum))
			return false;
		if (verantwoordelijke == null) {
			if (other.verantwoordelijke != null)
				return false;
		} else if (!verantwoordelijke.equals(other.verantwoordelijke))
			return false;
		return true;
	}
	
}