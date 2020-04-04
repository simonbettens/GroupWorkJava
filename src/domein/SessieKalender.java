package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	
	
}
