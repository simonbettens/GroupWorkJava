package domein;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Sessie {
	
	private int sessieId;
	private int maxCap;
	private int aantalAanwezigeGebruikers;
	private int aantalIngeschrevenGebruikers;
	private int aantalResterend;
	
	private String naam;
	private String lokaal;
	private String beschrijving;
	private boolean bezig, staatOpen, kanNogInschrijven, gesloten;
	
	private Duration duur;
	private LocalDateTime startDatum, eindDatum;
	private Verantwoordelijke verantwoordelijke;
	
	private List<SessieGebruiker> gebruikersIngeschreven;
	private List<Media> media;
	private List<SessieAankondiging> aankondigingen;
	private List<Feedback> feedback;
	
	//nodig voor jpa
	public Sessie() {}
	
	//algemene constructor voor nieuwe instanties
	public Sessie(Verantwoordelijke verantwoordelijke, String naam, LocalDateTime startDatum, LocalDateTime eindDatum,
			boolean gesloten, int maxCap,String lokaal, String beschrijving) {
		setVerantwoordelijke(verantwoordelijke);
		setNaam(naam);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
		setGesloten(false);
		setMaxCap(maxCap);
		setAantalAanwezigeGebruikers(0);
		setAantalIngeschrevenGebruikers(0);
		setAantalResterend(maxCap);
		setLokaal(lokaal);
		setBeschrijving(beschrijving);
		setStaatOpen(false);
		setKanNogInschrijven(true);
		setDuur(Duration.between(startDatum, eindDatum));
		setBezig(false);
		this.gebruikersIngeschreven = new ArrayList<SessieGebruiker>();
		this.media = new ArrayList<Media>();
		this.aankondigingen = new ArrayList<SessieAankondiging>();
		this.feedback = new ArrayList<Feedback>();
	}
	
	// ----------  getters
	public List<SessieGebruiker> getGebruikersIngeschreven() {
		return Collections.unmodifiableList(gebruikersIngeschreven);
	}
	public Verantwoordelijke getVerantwoordelijke() {
		return verantwoordelijke;
	}
	public List<Media> getMedia() {
		return Collections.unmodifiableList(media);
	}
	public List<SessieAankondiging> getAankondigingen() {
		return Collections.unmodifiableList(aankondigingen);
	}
	public List<Feedback> getFeedback() {
		return Collections.unmodifiableList(feedback);
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
	
	// ------- setters
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
		if(naam == null || naam.equals("")) {
			throw new IllegalArgumentException("Naam moet ingevuld zijn.");
		}
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
		if(lokaal == null || lokaal.equals("")) {
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
	
	// ----- lijst operatie's
	
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
	public Feedback getFeedbackByIndex(int index) {
		return feedback.get(index);
	}
	


	
}