package domein;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Sessie {

	private Collection<SessieGebruiker> gebruikersIngeschreven;
	private Verantwoordelijke verantwoordelijke;
	private Collection<Media> media;
	private Collection<SessieAankondiging> aankondigingen;
	private Collection<Feedback> feedback;
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

}