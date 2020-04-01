package domein;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Sessie {

	private List<SessieGebruiker> gebruikersIngeschreven;
	private Verantwoordelijke verantwoordelijke;
	private List<Media> media;
	private List<SessieAankondiging> aankondigingen;
	private List<Feedback> feedback;
	private String naam;
	private int sessieId;
	private LocalDateTime startDatum;
	private LocalDateTime eindDatum;
	private boolean gesloten;
	private int maxCap;
	private int aantalAanwezigeGebruikers;
	private int aantalIngeschrevenGebruikers;
	private int aantalResterend;
	private String lokaal;
	private String beschrijving;
	private boolean staatOpen;
	private boolean kanNogInschrijven;
	private Duration duur;
	private boolean bezig;
	public Sessie(Verantwoordelijke verantwoordelijke, String naam, LocalDateTime startDatum, LocalDateTime eindDatum,
			boolean gesloten, int maxCap, int aantalAanwezigeGebruikers, int aantalIngeschrevenGebruikers,
			int aantalResterend, String lokaal, String beschrijving, boolean staatOpen, boolean kanNogInschrijven,
			Duration duur, boolean bezig) {
		this.verantwoordelijke = verantwoordelijke;
		this.naam = naam;
		this.startDatum = startDatum;
		this.eindDatum = eindDatum;
		this.gesloten = gesloten;
		this.maxCap = maxCap;
		this.aantalAanwezigeGebruikers = aantalAanwezigeGebruikers;
		this.aantalIngeschrevenGebruikers = aantalIngeschrevenGebruikers;
		this.aantalResterend = aantalResterend;
		this.lokaal = lokaal;
		this.beschrijving = beschrijving;
		this.staatOpen = staatOpen;
		this.kanNogInschrijven = kanNogInschrijven;
		this.duur = duur;
		this.bezig = bezig;
		this.gebruikersIngeschreven = new ArrayList<SessieGebruiker>();
		this.media = new ArrayList<Media>();
		this.aankondigingen = new ArrayList<SessieAankondiging>();
		this.feedback = new ArrayList<Feedback>();
	}
	public List<SessieGebruiker> getGebruikersIngeschreven() {
		return gebruikersIngeschreven;
	}
	public Verantwoordelijke getVerantwoordelijke() {
		return verantwoordelijke;
	}
	public List<Media> getMedia() {
		return media;
	}
	public List<SessieAankondiging> getAankondigingen() {
		return aankondigingen;
	}
	public List<Feedback> getFeedback() {
		return feedback;
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
	public int getAantalAanwezigeGebruikers() {
		return aantalAanwezigeGebruikers;
	}
	public int getAantalIngeschrevenGebruikers() {
		return aantalIngeschrevenGebruikers;
	}
	public int getAantalResterend() {
		return aantalResterend;
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
	public boolean isKanNogInschrijven() {
		return kanNogInschrijven;
	}
	public Duration getDuur() {
		return duur;
	}
	public boolean isBezig() {
		return bezig;
	}
	private void setGebruikersIngeschreven(List<SessieGebruiker> gebruikersIngeschreven) {
		this.gebruikersIngeschreven = gebruikersIngeschreven;
	}
	private void setVerantwoordelijke(Verantwoordelijke verantwoordelijke) {
		this.verantwoordelijke = verantwoordelijke;
	}
	private void setMedia(List<Media> media) {
		this.media = media;
	}
	private void setAankondigingen(List<SessieAankondiging> aankondigingen) {
		this.aankondigingen = aankondigingen;
	}
	private void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}
	private void setNaam(String naam) {
		this.naam = naam;
	}
	private void setStartDatum(LocalDateTime startDatum) {
		if(startDatum.isBefore(LocalDateTime.now()))  {
			throw new IllegalArgumentException("Datum is in het verleden.");
		}
		this.startDatum = startDatum;
	}
	private void setEindDatum(LocalDateTime eindDatum) {
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
	private void setAantalAanwezigeGebruikers(int aantalAanwezigeGebruikers) {
		this.aantalAanwezigeGebruikers = aantalAanwezigeGebruikers;
	}
	private void setAantalIngeschrevenGebruikers(int aantalIngeschrevenGebruikers) {
		this.aantalIngeschrevenGebruikers = aantalIngeschrevenGebruikers;
	}
	private void setAantalResterend(int aantalResterend) {
		this.aantalResterend = aantalResterend;
	}
	private void setLokaal(String lokaal) {
		this.lokaal = lokaal;
	}
	private void setBeschrijving(String beschrijving) {
		this.beschrijving = beschrijving;
	}
	private void setStaatOpen(boolean staatOpen) {
		this.staatOpen = staatOpen;
	}
	private void setKanNogInschrijven(boolean kanNogInschrijven) {
		this.kanNogInschrijven = kanNogInschrijven;
	}
	private void setDuur(Duration duur) {
		if(duur.isNegative() || duur.isZero()) {
			throw new IllegalArgumentException("Duur mag niet 0 of negatief zijn.");
		}
		this.duur = duur;
	}
	private void setBezig(boolean bezig) {
		this.bezig = bezig;
	}
	


	
}