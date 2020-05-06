package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import domein.AankondigingPrioriteit;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.Maand;
import domein.Media;
import domein.MediaType;
import domein.Sessie;
import domein.SessieAankondiging;
import domein.SessieGebruiker;
import domein.SessieKalender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.GebruikerDao;
import repository.GenericDaoJpa;
import repository.MediaDao;
import repository.MediaDaoJpa;
import repository.SessieAankondigingDao;
import repository.SessieAankondigingDaoJpa;
import repository.SessieDao;
import repository.SessieDaoJpa;
import repository.SessieGebruikerDao;
import repository.SessieGebruikerDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class SessieController {

	// Properties
	private SessieKalenderDao sessiekalenderRepository;
	private SessieDao sessieRepository;
	private GebruikerDao gebruikerRepo;
	private Gebruiker ingelogdeGebruiker;
	private List<Gebruiker> verantwoordelijkeLijstBijSessie;
	private Maand gekozenMaand;
	private PropertyChangeSupport subject;
	// SessieKalenders
	private SessieKalender gekozenSessieKalender;
	private List<SessieKalender> sessieKalenderLijst;
	private ObservableList<String> sessieKalenderObservableLijst;
	private SortedList<SessieKalender> sortedSessieKalenderLijst;

	// Sessies
	private Sessie gekozenSessie;
	private List<Sessie> sessieLijst;
	private ObservableList<Sessie> sessieObservableLijst;
	private FilteredList<Sessie> filteredSessieLijst;
	private SortedList<Sessie> sortedSessieLijst;

	//============================================================================================================================================
	// Constructor
	//============================================================================================================================================
	/**
	 * Constructor voor de Sessiecontroller.
	 * Zet gekozen sessiekalender op het huidige academiejaar.
	 * roept {@link #vulLijsten() vulLijsten} aan.
	 * @param ingelogdeGebruiker de ingelogde gebruiker
	 * @param gebruikerrepo gebruikerrepository
	 * @param sessierepo sessierepository
	 */
	public SessieController(Gebruiker ingelogdeGebruiker, GebruikerDao gebruikerrepo, SessieDao sessierepo) {
		this.gebruikerRepo = gebruikerrepo;
		setSessieRepository(sessierepo);
		setSessiekalenderRepository(new SessieKalenderDaoJpa());
		setIngelogdeGebruiker(ingelogdeGebruiker);
		this.subject = new PropertyChangeSupport(this);
		setSessieKalender("0");
		gekozenMaand = Maand.of(LocalDate.now().getMonthValue());
		vulLijsten();
		gekozenSessie = null;
	}

	/**
	 * Vult de lijsten met sessiekalenders op met data uit de sessiekalenderRepository.
	 * Roept {@link #vulSessieLijsten() vulSessieLijsten} aan.
	 */
	private void vulLijsten() {
		this.sessieKalenderLijst = sessiekalenderRepository.getAll();
		this.sessieKalenderObservableLijst = FXCollections.observableArrayList(
				sessieKalenderLijst.stream().sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed())
						.map(SessieKalender::toString).collect(Collectors.toList()));

		vulSessieLijsten();
	}

	
	/**
	 * Vult de lijsten met sessies op als er een sessiekalender geselecteerd is.
	 * Controleert of de ingelogde gebruiker verantwoordelijke of hoofdverantwoordelijke is en 
	 * laadt de gepaste lijsten.
	 */
	private void vulSessieLijsten() {
		if (gekozenSessieKalender != null) {
			if (ingelogdeGebruiker.getType() == GebruikerType.HOOFDVERANTWOORDELIJKE) {
				this.sessieLijst = this.gekozenSessieKalender.getSessies();
			} else {
				this.sessieLijst = this.gekozenSessieKalender.getSessies().stream()
						.filter(s -> s.getVerantwoordelijke().getId().equals(ingelogdeGebruiker.getId()))
						.collect(Collectors.toList());
			}
			this.sessieObservableLijst = FXCollections.observableArrayList(sessieLijst);
			this.filteredSessieLijst = new FilteredList<>(sessieObservableLijst, e -> true);
			veranderFilter(Maand.of(LocalDate.now().getMonthValue()), "");
			this.sortedSessieLijst = new SortedList<Sessie>(filteredSessieLijst,
					Comparator.comparing(Sessie::getStartDatum).thenComparing(Sessie::getNaam));
		} else {
			System.out.println("Sessiekalender is null");
		}
	}

	/**
	 * Roept {@link #veranderFilter(Maand, String) veranderFilter} aan met de meegegeven naamwaarde en de geselecteerde maand
	 * @param naamWaarde filter op de naam van sessies
	 */
	public void zoekOpNaam(String naamWaarde) {
		veranderFilter(gekozenMaand, naamWaarde);
	}

	/**
	 * Verandert de filter van de gefilterde sessie lijst
	 * @param maandWaarde de geselecteerde maand
	 * @param naamWaarde de filter op de naam van de sessie
	 */
	private void veranderFilter(Maand maandWaarde, String naamWaarde) {
		this.filteredSessieLijst.setPredicate(sessie -> {
			boolean maandWaardeLeeg = maandWaarde == null;
			boolean naamWaardeLeeg = naamWaarde == null || naamWaarde.isBlank();

			if (maandWaardeLeeg && naamWaardeLeeg) {
				return true;
			}
			boolean conditieMaand = maandWaardeLeeg ? true
					: sessie.getStartDatum().getMonth().getValue() == maandWaarde.getWaarde();
			boolean conditieNaam = naamWaardeLeeg ? true
					: sessie.getNaam().toLowerCase().contains(naamWaarde)
							|| sessie.getNaam().toLowerCase().startsWith(naamWaarde);
			return conditieMaand && conditieNaam;
		});
	}

	//============================================================================================================================================
	// Setters
	//============================================================================================================================================

	public void setIngelogdeGebruiker(Gebruiker ingelogdeGebruiker) {
		this.ingelogdeGebruiker = ingelogdeGebruiker;
	}

	public void setSessiekalenderRepository(SessieKalenderDaoJpa mock) {
		this.sessiekalenderRepository = mock;
	}

	public void setSessieRepository(SessieDao mock) {
		this.sessieRepository = mock;
	}

	/**
	 * Verandert de geselecteerde sessiekalender.
	 * Roept  {@link #vulSessieLijsten() vulSessieLijsten} aan.
	 * @param waarde het academiejaar van de sessiekalender
	 */
	public void changeSelectedSessieKalender(String waarde) {
		gekozenSessieKalender = sessieKalenderLijst.stream().filter(sk -> sk.toString().equals(waarde)).findFirst()
				.orElse(null);
		vulSessieLijsten();
	}

	/**
	 * Verandert de geselecteerde maand.
	 * Roept {@link #veranderFilter(Maand, String) veranderFilter} aan.
	 * @param waarde de geselecteerde maand
	 */
	public void changeSelectedMaand(String waarde) {
		gekozenMaand = Arrays.asList(Maand.values()).stream().filter(sk -> sk.toString().equals(waarde)).findFirst()
				.orElse(null);
		veranderFilter(gekozenMaand, "");
	}

	/**
	 * Verandert de geselecteerde sessie.
	 * @param waarde toString nieuwe sessie
	 */
	public void changeSelectedSessie(String waarde) {
		gekozenSessie = sessieLijst.stream().filter(s -> s.toString().equals(waarde)).findFirst().orElse(null);
	}

	/**
	 * Zet de meegegeven sessie als geselecteerde sessie.
	 * @param sessie de meegegeven sessie
	 */
	public void setSessie(Sessie sessie) {
		if (sessie != null) {
			setVerantwoordelijkeLijstBijSessie(sessie.getVerantwoordelijke());
		}
		firePropertyChange("sessie", this.gekozenSessie, sessie);
		this.gekozenSessie = sessie;
	}
	
	//============================================================================================================================================
	// getters
	//============================================================================================================================================
	
	/**
	 * @return de lijst met alle sessiekalenders
	 */
	public List<SessieKalender> getSessieKalenderLijst() {
		return sessieKalenderLijst;
	}
	/**
	 * 
	 * @return sessieKalender repository
	 */
	public SessieKalenderDao getSessiekalenderRepository() {
		return sessiekalenderRepository;
	}

	/**
	 * @return de gekozen sessiekalender
	 */
	public SessieKalender getGekozenSessieKalender() {
		return gekozenSessieKalender;
	}

	/**
	 * @return de ObservableList met sessiekalenders
	 */
	public ObservableList<String> getSessieKalenderObservableLijst() {
		return sessieKalenderObservableLijst;
	}

	/**
	 * @return een lijst van sessies van de geselecteerde sessiekalender
	 */
	public List<Sessie> getSessieLijst() {
		return sessieLijst;
	}

	/**
	 * @return een gesorteerde ObservableList van sessies van de geselecteerde sessiekalender.
	 */
	public ObservableList<Sessie> getSessieObservableLijst() {
		return this.sortedSessieLijst;
	}

	/**
	 * @return de ingelogde gebruiker
	 */
	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	//============================================================================================================================================
	// Sessiekalender methods
	//============================================================================================================================================
	
	/**
	 * Zoekt een sessiekalender op met het meegegeven beginjaar en stelt deze in als gekozen sessiekalender.
	 * Als beginjaar "0" is, wordt de sessiekalender van het huidige academiejaar geselecteerd.
	 * @param beginjaar het beginjaar van de sessiekalender
	 */
	public void setSessieKalender(String beginjaar) {
		try {
			if (beginjaar == "0") {
				String jaar;
				if (LocalDate.now().getMonth().getValue() < Maand.September.getWaarde()) {
					jaar = String.format("%d", LocalDate.now().getYear() - 1);
				} else {
					jaar = String.format("%d", LocalDate.now().getYear());
				}
				this.gekozenSessieKalender = sessiekalenderRepository.getByBeginjaar(jaar);
			} else {
				this.gekozenSessieKalender = sessiekalenderRepository.getByBeginjaar(beginjaar);
			}
		} catch (Exception e) {
			// TODO
			System.err.printf("Kalender niet gevonden.\n");
			// e.printStackTrace();
		}
	}

	/**
	 * Zoekt de jongste sessiekalender en geeft deze terug.
	 * @return de jongste sessiekalender
	 */
	public SessieKalender getLaasteSessieKalender() {
		return this.sessieKalenderLijst.stream().sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed())
				.findFirst().orElse(null);
	}

	/**
	 * Maakt een nieuwe sessiekalender aan met de meegegeven startdatum en einddatum.
	 * Controleert op overlap met bestaande sessiekalenders.
	 * Roept {@link #insertSessieKalender(SessieKalender) insertSessieKalender} aan.
	 * @param startDatum de startdatum van de sessiekalender
	 * @param eindDatum de einddatum van de sessiekalender
	 */
	public void maakNieuweSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = new SessieKalender(startDatum, eindDatum);
		SessieKalender overlap = sessieKalenderLijst.stream().filter(
				s -> sk.getStartDatum().isAfter(s.getStartDatum()) && sk.getStartDatum().isBefore(s.getEindDatum())
						|| sk.getEindDatum().isAfter(s.getStartDatum()) && sk.getEindDatum().isBefore(sk.getEindDatum())
						|| sk.getStartDatum().isBefore(s.getStartDatum())
								&& sk.getEindDatum().isAfter(s.getStartDatum()))
				.findFirst().orElse(null);
		if (overlap == null) {
			insertSessieKalender(sk);
		} else {
			String fout = String.format("Ingeven sessiekalender overlapt met %s - %s",
					overlap.getStartDatum().toString(), overlap.getEindDatum().toString());
			System.out.println(fout);
			throw new IllegalArgumentException(fout);
		}
	}

	/**
	 * Wijzigt startdatum en einddatum de gekozen sessiekalender met de meegegeven startdatum en einddatum.
	 * Controleert op overlap met bestaande sessiekalenders.
	 * Roept {@link #updateSessieKalender(SessieKalender) updateSessieKalender} aan.
	 * @param startDatum de nieuwe startdatum van de sessiekalender
	 * @param eindDatum de nieuwe einddatum van de sessiekalender
	 */
	public void pasSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = this.gekozenSessieKalender;
		SessieKalender overlap = sessieKalenderLijst.stream().filter(
				s -> sk.getStartDatum().isAfter(s.getStartDatum()) && sk.getStartDatum().isBefore(s.getEindDatum())
						|| sk.getEindDatum().isAfter(s.getStartDatum()) && sk.getEindDatum().isBefore(sk.getEindDatum())
						|| sk.getStartDatum().isBefore(s.getStartDatum())
								&& sk.getEindDatum().isAfter(s.getStartDatum()))
				.findFirst().orElse(null);
		if (overlap == null) {
			sessieKalenderLijst.remove(sk);
			sessieKalenderObservableLijst.remove(sk.toString());
			sk.setStartDatum(startDatum);
			sk.setEindDatum(eindDatum);
			updateSessieKalender(sk);
			sessieKalenderLijst.add(sk);
			sessieKalenderObservableLijst.add(sk.toString());
			this.gekozenSessieKalender = sk;
		} else {
			String fout = String.format("Ingeven sessiekalender overlapt met %s - %s",
					overlap.getStartDatum().toString(), overlap.getEindDatum().toString());
			System.out.println(fout);
			throw new IllegalArgumentException(fout);
		}
	}

	//============================================================================================================================================
	// Sessie methods
	//============================================================================================================================================
	
	
	/**
	 * @return de geselecteerde sessie
	 */
	public Sessie getSessie() {
		return this.gekozenSessie;
	}

	/**
	 * @return een ObservableList met alle namen van de mogelijke verantwoordelijken bij de geselecteerde sessie
	 */
	public ObservableList<String> getVerantwoordelijkeNamen() {
		return FXCollections.observableArrayList(
				verantwoordelijkeLijstBijSessie.stream().map(Gebruiker::getVolledigeNaam).collect(Collectors.toList()));
	}

	/**
	 * Voegt een lijst met verantwoordelijken toe bij de geselecteerde sessie.
	 * Als de ingelogde gebruiker geen hoofdverantwoordelijke is wordt enkel hij toegevoegd.
	 * Anders wordt een lijst met alle mogelijke verantwoordelijken toegevoegd.
	 * @param verantwoordelijkeBijSessie de verantwoordelijke van de geselecteerde sessie
	 */
	public void setVerantwoordelijkeLijstBijSessie(Gebruiker verantwoordelijkeBijSessie) {
		// verantwoordelijke van de sessie kan op dit moment geen verantwoordelijke meer
		// zijn en moet daarom bij de lijst gestoken worden maar als hij niet meer
		// bestaat dan niet :
		if (ingelogdeGebruiker.getType() == GebruikerType.HOOFDVERANTWOORDELIJKE) {
			List<Gebruiker> huidigeVerantwoordelijkeList = gebruikerRepo.getByDiscriminator("Verantwoordelijke");
			if (verantwoordelijkeBijSessie != null) {
				boolean zitErIn = huidigeVerantwoordelijkeList.stream()
						.filter(v -> v.getId().equals(verantwoordelijkeBijSessie.getId())).findFirst()
						.orElse(null) != null;
				if (zitErIn) {
					this.verantwoordelijkeLijstBijSessie = huidigeVerantwoordelijkeList;
				} else {
					List<Gebruiker> lijst = new ArrayList<>(huidigeVerantwoordelijkeList);
					lijst.add(verantwoordelijkeBijSessie);
					this.verantwoordelijkeLijstBijSessie = lijst;
				}
			} else {
				this.verantwoordelijkeLijstBijSessie = huidigeVerantwoordelijkeList;
			}
		} else {
			List<Gebruiker> huidigeVerantwoordelijkeList = new ArrayList<>();
			huidigeVerantwoordelijkeList.add(ingelogdeGebruiker);
			this.verantwoordelijkeLijstBijSessie = huidigeVerantwoordelijkeList;
		}
	}

	/**
	 * Maakt een nieuwe sessie aan.
	 * Roept {@link #insertSessie(Sessie) insertSessie} aan
	 * @param titel titel van de sessie
	 * @param lokaal lokaal waarin de sessie doorgaat
	 * @param gastSpreker gastspreker die de sessie geeft
	 * @param startuur uur waarop de sessie start
	 * @param startmin minuut waarop de sessie start
	 * @param einduur uur waarop de sessie eindigt
	 * @param eindmin minuut waarop de sessie eindigt
	 * @param volledigeNaamVerantwoordelijke volledige naam van de verantwoordelijke van de sessie
	 * @param beschrijving beschrijving van de sessie
	 * @param maxCap maximaal aantal personen dat de sessie mag bijwonen
	 * @param start datum waarop de sessie begint
	 * @param einde datum waarop de sessie eindigt
	 */
	public void maakSessieAan(String titel, String lokaal, String gastSpreker, String startuur, String startmin,
			String einduur, String eindmin, String volledigeNaamVerantwoordelijke, String beschrijving, String maxCap,
			LocalDate start, LocalDate einde) {

		if (start == null) {
			System.out.println("datum is null");
			String fout = String.format("Voer een geldige datum in bij het %sdatum", "start");
			System.out.println(fout);
			throw new IllegalArgumentException(fout);

		}
		LocalDateTime startDatum = maakLocalDateTime(start, startuur, startmin, "start");
		if (einde == null) {
			einde = start;
		}
		LocalDateTime eindDatum = maakLocalDateTime(einde, einduur, eindmin, "eind");
		Gebruiker verantwoordelijke = verantwoordelijkeLijstBijSessie.stream()
				.filter(v -> v.getVolledigeNaam().equals(volledigeNaamVerantwoordelijke)).findFirst().orElse(null);
		boolean correctFormaat = true;
		int maxCapaciteit;
		Pattern p = Pattern.compile("[0-9]{1,3}");
		Matcher m = p.matcher(String.valueOf(maxCap));
		correctFormaat = m.matches();
		if (correctFormaat) {
			maxCapaciteit = Integer.parseInt(maxCap);
		} else {
			String fout = String.format("%s is geen geldige waarde bij maximum capaciteit", maxCap);
			throw new NumberFormatException(fout);
		}
		Sessie nieuweSessie = new Sessie(verantwoordelijke, titel, startDatum, eindDatum, maxCapaciteit, lokaal,
				beschrijving, gastSpreker);
		insertSessie(nieuweSessie);
		sessieLijst.add(nieuweSessie);
		sessieObservableLijst.add(nieuweSessie);
	}

	/**
	 * Past de geselecteerde sessie aan met de meegegeven parameters.
	 * @param titel titel van de sessie
	 * @param lokaal lokaal waarin de sessie doorgaat
	 * @param gastSpreker gastspreker die de sessie geeft
	 * @param startuur uur waarop de sessie start
	 * @param startmin minuut waarop de sessie start
	 * @param einduur uur waarop de sessie eindigt
	 * @param eindmin minuut waarop de sessie eindigt
	 * @param volledigeNaamVerantwoordelijke volledige naam van de verantwoordelijke van de sessie
	 * @param beschrijving beschrijving van de sessie
	 * @param maxCap maximaal aantal personen dat de sessie mag bijwonen
	 * @param staatOpen true als de sessie open staat
	 * @param gesloten true als de sessie gesloten is
	 * @param start datum waarop de sessie begint
	 * @param einde datum waarop de sessie eindigt
	 */
	public void pasSessieAan(String titel, String lokaal, String gastSpreker, String startuur, String startmin,
			String einduur, String eindmin, String volledigeNaamVerantwoordelijke, String beschrijving, String maxCap,
			boolean staatOpen, boolean gesloten, LocalDate start, LocalDate einde) {
		System.out.println("pas aan");
		if (this.gekozenSessie != null) {
			if (start == null) {
				System.out.println("datum is null");
				String fout = String.format("Voer een geldige datum in bij het %sdatum", "start");
				throw new IllegalArgumentException(fout);
			}
			LocalDateTime startDatum = maakLocalDateTime(start, startuur, startmin, "start");

			if (einde == null) {
				einde = start;
			}
			LocalDateTime eindDatum = maakLocalDateTime(einde, einduur, eindmin, "eind");
			Gebruiker verantwoordelijke = verantwoordelijkeLijstBijSessie.stream()
					.filter(v -> v.getVolledigeNaam().equals(volledigeNaamVerantwoordelijke)).findFirst().orElse(null);
			boolean correctFormaat = true;
			int maxCapaciteit;
			Pattern p = Pattern.compile("[0-9]{1,3}");
			Matcher m = p.matcher(String.valueOf(maxCap));
			correctFormaat = m.matches();
			if (correctFormaat) {
				maxCapaciteit = Integer.parseInt(maxCap);
			} else {
				String fout = String.format("%s is geen geldige waarde bij maximum capaciteit", maxCap);
				throw new NumberFormatException(fout);
			}
			Sessie sessie = this.gekozenSessie;
			int veranderingen = sessie.pasSessieAan(verantwoordelijke, titel, startDatum, eindDatum, staatOpen,
					gesloten, maxCapaciteit, lokaal, beschrijving, gastSpreker);
			if (veranderingen > 0) {
				updateSessie(this.gekozenSessie);
				firePropertyChange("sessie", this.gekozenSessie, sessie);
				this.gekozenSessie = sessie;
			}
		}
	}

	/**
	 * Maakt een nieuw LocalDateTime object met meegegeven datum, uur en minuten.
	 * @param datum LocalDate object met de datum
	 * @param uur uur
	 * @param min minuten
	 * @param welke indiceert of het over begin of eind gaat van een sessie
	 * @return een LocalDateTime object op de geselecteerde datum en tijd.
	 */
	private LocalDateTime maakLocalDateTime(LocalDate datum, String uur, String min, String welke) {
		// strings naar localdatetimes
		int uurInt, minInt;

		if (!(uur.isEmpty() || min.isEmpty() || uur.isBlank() || min.isBlank())) {
			boolean correctFormaat = true;
			Pattern p = Pattern.compile("[0-9]{1,2}");
			Matcher m = p.matcher(String.valueOf(uur));
			correctFormaat = m.matches();
			if (correctFormaat) {
				uurInt = Integer.parseInt(uur);
			} else {
				String fout = String.format("Voer een geldig formaat in bij als %s uur", welke);
				throw new NumberFormatException(fout);
			}

			m = p.matcher(String.valueOf(min));
			correctFormaat = m.matches();
			if (correctFormaat) {
				minInt = Integer.parseInt(min);
			} else {
				String fout = String.format("Voer een geldig formaat in bij als %s minuten", welke);
				throw new NumberFormatException(fout);
			}
			if (uurInt <= 0 || uurInt > 24) {
				String fout = String
						.format("Voer een geldig formaat in bij het %suur, het uur moet tussen 1 - 24 liggen", welke);
				throw new IllegalArgumentException(fout);
			}
			if (uurInt < 0 || uurInt > 59) {
				String fout = String.format(
						"Voer een geldig formaat in bij het %suur, de minuten moet tussen 0 - 59 liggen", welke);
				throw new IllegalArgumentException(fout);
			}
			LocalTime tijd = LocalTime.of(uurInt, minInt);
			return LocalDateTime.of(datum, tijd);
		} else {
			System.out.println(welke + " : " + datum.toString() + " " + uur + " " + min);
			String fout = String.format("Voer een geldig formaat in bij het %suur", welke);
			throw new IllegalArgumentException(fout);
		}
	}

	//============================================================================================================================================
	// Databank operaties
	// --- Sessiekalender databank operaties
	//============================================================================================================================================

	/**
	 * Voegt een nieuwe sessiekalender toe aan de databank en sessiekalender lijsten.
	 * @param sk toe te voegen sessiekalender
	 */
	public void insertSessieKalender(SessieKalender sk) {
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.insert(sk);
		GenericDaoJpa.commitTransaction();
		sessieKalenderLijst.add(sk);
		sessieKalenderObservableLijst.add(sk.toString());
	}

	/**
	 * Update een bestaande sessiekalender in de databank.
	 * @param sk nieuwe versie van de sessiekalender
	 */
	public void updateSessieKalender(SessieKalender sk) {
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.update(sk);
		GenericDaoJpa.commitTransaction();
	}
	
	//============================================================================================================================================
	// ---Sessie databank operaties
	//============================================================================================================================================
	
	/**
	 * verwijdert de geselecteerde sessie uit de databank en sessielijsten.
	 * Maakt de geselecteerde sessie null.
	 */
	public void deleteSessie() {
		Sessie teVerwijderenSessie = this.gekozenSessie;
		sessieLijst.remove(teVerwijderenSessie);
		sessieObservableLijst.remove(teVerwijderenSessie);
		GenericDaoJpa.startTransaction();
		sessieRepository.delete(teVerwijderenSessie);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("sessie", this.gekozenSessie, null);
		this.gekozenSessie = null;
	}

	/**
	 * Voegt een nieuwe sessie toe aan de databank.
	 * @param sessie toe te voegen sessie
	 */
	public void insertSessie(Sessie sessie) {
		GenericDaoJpa.startTransaction();
		sessieRepository.insert(sessie);
		GenericDaoJpa.commitTransaction();
	}

	/**
	 * Update een bestaande sessie in de databank.
	 * @param sessie nieuwe versie van de sessie.
	 */
	public void updateSessie(Sessie sessie) {
		GenericDaoJpa.startTransaction();
		sessieRepository.update(sessie);
		GenericDaoJpa.commitTransaction();
	}

	public void rollBack() {
		GenericDaoJpa.rollbackTransaction();
	}

	//============================================================================================================================================
	// changeSupport
	//============================================================================================================================================

	private <T> void firePropertyChange(String welke, T oude, T nieuwe) {
		subject.firePropertyChange(welke, oude, nieuwe);
	}

	public void addPropertyChangeListenerSessie(PropertyChangeListener pcl, String welke) {
		subject.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, welke, null, null));
	}

	public void removePropertyChangeListenerSessie(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}

}
