package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
@Entity
@NamedQueries({
	@NamedQuery(name = "SessieKalender.findByBeginJaar",
				query = "select k from SessieKalender k where k.BeginJaar = :beginJaar")
})

public class SessieKalender {

	private int id;
	private LocalDate startDatum;
	private LocalDate eindDatum;
	
	private List<Sessie> sessies;

	public SessieKalender() {
		
	}

	public SessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		this.startDatum = startDatum;
		this.eindDatum = eindDatum;
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
