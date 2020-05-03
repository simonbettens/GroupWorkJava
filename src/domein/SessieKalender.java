package domein;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name="SessieKalender")
@NamedQueries({
	@NamedQuery(name = "SessieKalender.getByBeginjaar",
				query = "SELECT k FROM SessieKalender k WHERE EXTRACT(YEAR from k.startDatum) = :beginjaar")
})
@Table(name="SessieKalender")
public class SessieKalender implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="SessieKalenderId")
	private int id;
	@Column(name="StartDatum")
	private LocalDate startDatum;
	@Column(name="EindDatum")
	private LocalDate eindDatum;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Sessie> sessies;
	

	public SessieKalender() {
		this.sessies = new ArrayList<Sessie>();
	}

	public SessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		this.sessies = new ArrayList<Sessie>();
	}

	public final void setStartDatum(LocalDate startDatum) {
		if(startDatum.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Datum is in het verleden");
		}
		this.startDatum = startDatum;
	}
	
	public final void setStartDatumInitData(LocalDate startDatum) {
		this.startDatum = startDatum;
	}

	public final void setEindDatum(LocalDate eindDatum) {
		if(startDatum == null || eindDatum.isBefore(LocalDate.now()) || eindDatum.isAfter(LocalDate.of(startDatum.getYear()+1, 9, 30)))
			throw new IllegalArgumentException("Einddatum is ongeldig.");
		this.eindDatum = eindDatum;
	}

	public int getId() {
		return id;
	}

	public LocalDate getStartDatum() {
		return startDatum;
	}

	public LocalDate getEindDatum() {
		return eindDatum;
	}

	public List<Sessie> getSessies() {
		return sessies;
	}
	public void addSessie(Sessie sessie) {
		if(sessie.getStartDatum().toLocalDate().isBefore(this.startDatum) || sessie.getEindDatum().toLocalDate().isAfter(this.eindDatum))
			throw new IllegalArgumentException("Sessie valt de buiten de sessiekalender");
		this.sessies.add(sessie);
	}

	@Override
	public String toString() {
		return DatumEnTijdHelper.dateFormat(getStartDatum()) + " - " + DatumEnTijdHelper.dateFormat(getEindDatum());
	} 
	
	
}
