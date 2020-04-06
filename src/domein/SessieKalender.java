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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	@Transient
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy 'om' hh:mm");

	public SessieKalender() {
		
	}

	public SessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		this.sessies = new ArrayList<Sessie>();
	}

	private void setStartDatum(LocalDate startDatum) {
		this.startDatum = startDatum;
	}

	private void setEindDatum(LocalDate eindDatum) {
		this.eindDatum = eindDatum;
	}

	private void setSessies(List<Sessie> sessies) {
		this.sessies = sessies;
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
	
	
}
