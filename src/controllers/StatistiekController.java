package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import domein.Aankondiging;
import domein.Gebruiker;
import domein.GebruikerType;
import domein.Maand;
import domein.Sessie;
import domein.SessieAankondiging;
import domein.SessieGebruiker;
import domein.SessieKalender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.AankondigingDao;
import repository.AankondigingDaoJpa;
import repository.GebruikerDao;
import repository.GenericDaoJpa;
import repository.SessieAankondigingDao;
import repository.SessieAankondigingDaoJpa;
import repository.SessieDao;
import repository.SessieGebruikerDao;
import repository.SessieGebruikerDaoJpa;
import repository.SessieKalenderDao;

public class StatistiekController {

	private SessieDao sessieRepository;
	private GebruikerDao gebruikerRepository;
	private SessieGebruikerDao inschrijvingsRepository;
	private SessieKalenderDao sessiekalenderRepository;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Maand gekozenMaand;
	private List<Sessie> gekozenSessies;
	private SessieKalender gekozenSessieKalender;
	private List<SessieKalender> sessieKalenderLijst;
	private ObservableList<String> sessieKalenderObservableLijst;
	private List<Sessie> besteSessiesJaar;
	private List<Sessie> besteSessieMaand;

	// ==================
	// Sessies
	// ==================

	private List<Sessie> afgelopenSessies;
	private ObservableList<Sessie> obsAfgelopenSessiesList;
	private FilteredList<Sessie> filteredAfgelopenSessiesLijst;
	private SortedList<Sessie> sortedAfgelopenSessiesLijst;

	// ===================
	// Gebruikers
	// ===================
	private Gebruiker gekozenGebruiker;
	private List<Gebruiker> gebruikerLijst;
	private ObservableList<Gebruiker> gebruikerObsLijst;
	private FilteredList<Gebruiker> filteredGebruikerLijst;
	private SortedList<Gebruiker> sortedGebruikerLijst;
	// ====================
	// Lijsten bij de gebruikers
	// ====================
	private List<SessieGebruiker> inschrijvingenGebruiker;
	private List<Sessie> ingeschrevenSessies;
	private ObservableList<SessieGebruiker> ingeschrevenSessiesObs;
	private int aantalInschrijvingen;
	private int aantalAanwezig;
	private double percentage;
	private boolean isOK;

	public StatistiekController(Gebruiker ingelogdeGebruiker, SessieDao sessieRepository,
			SessieGebruikerDao inschrijvingrepo, GebruikerDao gebruikerrepo, SessieKalenderDao sessieKalenderDao) {
		this.sessiekalenderRepository = sessieKalenderDao;
		this.sessieRepository = sessieRepository;
		this.inschrijvingsRepository = inschrijvingrepo;
		this.gebruikerRepository = gebruikerrepo;
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.subject = new PropertyChangeSupport(this);
		this.gekozenSessies = null;
		this.gekozenGebruiker = null;
		setSessieKalender("0");
		vulLijsten();
	}

	// ====================Vullen
	/**
	 * Vult de lijsten met sessiekalenders op met data uit de
	 * sessiekalenderRepository. Roept {@link #vulSessieLijsten() vulSessieLijsten}
	 * aan.
	 */

	public void vulLijsten() {
		GenericDaoJpa.reload();
		this.sessieKalenderLijst = sessiekalenderRepository.getAll();
		this.sessieKalenderObservableLijst = FXCollections.observableArrayList(
				sessieKalenderLijst.stream().sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed())
						.map(SessieKalender::toString).collect(Collectors.toList()));
		vulSessieLijst();
		vulBesteSessieLijsten();
		vulGebruikerLijst();

	}

	private void vulBesteSessieLijsten() {
		this.besteSessiesJaar = new ArrayList<>();
		this.besteSessieMaand = new ArrayList<>();
		List<Sessie> afgelopenSessiesJaar, afgelopenSessiesMaand;
		if (gekozenSessieKalender != null) {

			if (ingelogdeGebruiker.getType() == GebruikerType.HOOFDVERANTWOORDELIJKE) {
				afgelopenSessiesJaar = this.gekozenSessieKalender.getSessies();
			} else {
				afgelopenSessiesJaar = this.gekozenSessieKalender.getSessies().stream()
						.filter(s -> s.getVerantwoordelijke().getId().equals(ingelogdeGebruiker.getId()))
						.collect(Collectors.toList());
			}
			afgelopenSessiesJaar = afgelopenSessiesJaar.stream()
					.filter(s -> s.getEindDatum().isBefore(LocalDateTime.now()) && s.isGesloten() == true)
					.collect(Collectors.toList());
			afgelopenSessiesJaar.sort(Comparator.comparing(Sessie::getAantalInschrijvingen).reversed());
			if (gekozenMaand != null) {
				System.out.println("Maand is niet leeg");
				afgelopenSessiesMaand = afgelopenSessiesJaar.stream()
						.filter(s -> s.getStartDatum().getMonthValue() == gekozenMaand.getWaarde())
						.collect(Collectors.toList());
				afgelopenSessiesMaand.sort(Comparator.comparing(Sessie::getAantalInschrijvingen).reversed());

			} else {
				System.out.println("Maand is leeg");
				afgelopenSessiesMaand = afgelopenSessiesJaar;
			}
			int sizeAfgelopenSessiesJaar = afgelopenSessiesJaar.size();
			int sizeAfgelopenSessiesMaand = afgelopenSessiesMaand.size();
			for (int i = 0; i < 3; i++) {
				if (i < sizeAfgelopenSessiesJaar) {
					besteSessiesJaar.add(afgelopenSessiesJaar.get(i));
				}
				if (i < sizeAfgelopenSessiesMaand) {
					besteSessieMaand.add(afgelopenSessiesMaand.get(i));
				}
			}
		}

	}

	private void vulGebruikerLijst() {
		gebruikerLijst = gebruikerRepository.getAll();
		gebruikerObsLijst = FXCollections.observableArrayList(gebruikerLijst);
		filteredGebruikerLijst = new FilteredList<>(gebruikerObsLijst, e -> true);
		sortedGebruikerLijst = new SortedList<Gebruiker>(filteredGebruikerLijst,
				Comparator.comparing(Gebruiker::getVolledigeNaam).thenComparing(Gebruiker::getUserName)
						.thenComparing(Gebruiker::getType));
	}

	private void vulSessieLijst() {
		if (gekozenSessieKalender != null) {
			if (ingelogdeGebruiker.getType() == GebruikerType.HOOFDVERANTWOORDELIJKE) {
				this.afgelopenSessies = this.gekozenSessieKalender.getSessies();
			} else {
				this.afgelopenSessies = this.gekozenSessieKalender.getSessies().stream()
						.filter(s -> s.getVerantwoordelijke().getId().equals(ingelogdeGebruiker.getId()))
						.collect(Collectors.toList());
			}
			afgelopenSessies = afgelopenSessies.stream()
					.filter(s -> s.getEindDatum().isBefore(LocalDateTime.now()) && s.isGesloten() == true)
					.collect(Collectors.toList());
			this.obsAfgelopenSessiesList = FXCollections.observableArrayList(afgelopenSessies);
			this.filteredAfgelopenSessiesLijst = new FilteredList<>(obsAfgelopenSessiesList, e -> true);
			veranderFilter(Maand.of(LocalDate.now().getMonthValue()));
			this.sortedAfgelopenSessiesLijst = new SortedList<Sessie>(filteredAfgelopenSessiesLijst,
					Comparator.comparing(Sessie::getStartDatum).thenComparing(Sessie::getNaam));
		} else {
			System.out.println("Sessiekalender is null");
		}
	}

	public void vulGebruikerInfo() {
		List<Sessie> afgelopenSessiesLijst;
		isOK = true;

		this.inschrijvingenGebruiker = new ArrayList<>();
		this.ingeschrevenSessies = new ArrayList<>();
		this.ingeschrevenSessiesObs = null;
		if (gekozenSessieKalender != null && gekozenGebruiker != null) {
			afgelopenSessiesLijst = this.gekozenSessieKalender.getSessies();
			afgelopenSessiesLijst = afgelopenSessiesLijst.stream()
					.filter(s -> s.getEindDatum().isBefore(LocalDateTime.now()) && s.isGesloten() == true)
					.collect(Collectors.toList());
			if (gekozenMaand != null) {
				afgelopenSessiesLijst = afgelopenSessiesLijst.stream()
						.filter(s -> s.getStartDatum().getMonth().getValue() == gekozenMaand.getWaarde())
						.collect(Collectors.toList());
			}
			afgelopenSessiesLijst.stream().forEach(s -> {
				SessieGebruiker sg = s.geefInschrijvingVanGebruiker(gekozenGebruiker);
				if (sg != null) {
					this.inschrijvingenGebruiker.add(sg);
					this.ingeschrevenSessies.add(s);
				}
			});
			if (ingeschrevenSessies.size() != 0) {
				this.ingeschrevenSessiesObs = FXCollections.observableArrayList(inschrijvingenGebruiker);
				aantalInschrijvingen = inschrijvingenGebruiker.size();
				
				aantalAanwezig = inschrijvingenGebruiker.stream().filter(sg -> sg.isAanwezigheidBevestigd() == true)
						.collect(Collectors.toList()).size();
				percentage = (double) ((double) aantalAanwezig / (double) aantalInschrijvingen) *100;
				System.out.println((aantalAanwezig / aantalInschrijvingen) *100);
				System.out.println(percentage);
				isOK = (aantalInschrijvingen - aantalAanwezig) <= 3;
			}else {
				aantalInschrijvingen = 0;
				aantalAanwezig = 0;
				percentage = 0;
				isOK = true;
			}
		} else {
			System.out.println("Sessiekalender is null");
		}
	}

	private void veranderFilter(Maand maandWaarde) {
		this.filteredAfgelopenSessiesLijst.setPredicate(sessie -> {
			boolean maandWaardeLeeg = maandWaarde == null;
			if (maandWaardeLeeg) {
				return true;
			}
			boolean conditieMaand = maandWaardeLeeg ? true
					: sessie.getStartDatum().getMonth().getValue() == maandWaarde.getWaarde();
			return conditieMaand;
		});
	}
	public void zoekOpNaam(String zoekwaarde) {
		// TODO Auto-generated method stub
		this.filteredGebruikerLijst.setPredicate(g -> {
			boolean naamWaardeLeeg = zoekwaarde == null || zoekwaarde.isBlank();
			if (naamWaardeLeeg) {
				return true;
			}
			boolean conditieNaam = naamWaardeLeeg ? true
					: g.getVolledigeNaam().toLowerCase().contains(zoekwaarde)
							|| g.getVolledigeNaam().toLowerCase().startsWith(zoekwaarde);

			return conditieNaam;
		});
	}
	public void setGekozenSessies(List<Sessie> gekozenSessies) {
		firePropertyChange("sessies", null, gekozenSessies);
		this.gekozenSessies = gekozenSessies;

	}

	public void setGekozenebruiker(Gebruiker gb) {
		this.gekozenGebruiker = gb;
		vulGebruikerInfo();
		firePropertyChange("gebruiker", null, gb);
	}
	// ==================================== getters

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public List<Sessie> getGekozenSessie() {
		return gekozenSessies;
	}

	public ObservableList<String> getSessieKalenderObservableLijst() {
		// TODO Auto-generated method stub
		return sessieKalenderObservableLijst;
	}

	public ObservableList<Sessie> getObsAfgelopenSessiesList() {
		return sortedAfgelopenSessiesLijst;
	}

	public ObservableList<Gebruiker> getGebruikerObsLijst() {
		return sortedGebruikerLijst;
	}

	public Gebruiker getGekozenGebruiker() {
		return gekozenGebruiker;
	}

	public ObservableList<SessieGebruiker> getIngeschrevenSessiesObs() {
		return ingeschrevenSessiesObs;
	}

	public int getAantalInschrijvingen() {
		return aantalInschrijvingen;
	}

	public int getAantalAanwezig() {
		return aantalAanwezig;
	}

	public double getPercentage() {
		return percentage;
	}

	public boolean isOK() {
		return isOK;
	}

	public List<Sessie> getBesteSessiesJaar() {
		return besteSessiesJaar;
	}

	public List<Sessie> getBesteSessieMaand() {
		return besteSessieMaand;
	}

	public SessieKalender getGekozenSessieKalender() {
		// TODO Auto-generated method stub
		return gekozenSessieKalender;
	}

	// ========================================== SessieKalender
	/**
	 * Verandert de geselecteerde sessiekalender. Roept {@link #vulSessieLijst()
	 * vulSessieLijsten} aan.
	 * 
	 * @param waarde het academiejaar van de sessiekalender
	 * @param sherm  toont van welk scherm het komt
	 */
	public void changeSelectedSessieKalender(String waarde, int scherm) {
		SessieKalender oudKalender = gekozenSessieKalender;
		gekozenSessieKalender = sessieKalenderLijst.stream().filter(sk -> sk.toString().equals(waarde)).findFirst()
				.orElse(null);
		vulSessieLijst();
		if (scherm == 1) {
			vulBesteSessieLijsten();
		} else {
			vulGebruikerInfo();
		}
		firePropertyChange("kalender", oudKalender, gekozenSessieKalender);

	}

	/**
	 * Verandert de geselecteerde maand. Roept {@link #veranderFilter(Maand)
	 * veranderFilter} aan.
	 * 
	 * @param waarde de geselecteerde maand
	 * @param sherm  toont van welk scherm het komt
	 */
	public void changeSelectedMaand(String waarde, int scherm) {
		Maand oudeMaand = gekozenMaand;
		if (waarde == null || waarde.equals("--Alles--")) {
			gekozenMaand = null;
		} else {
			gekozenMaand = Arrays.asList(Maand.values()).stream().filter(sk -> sk.toString().equals(waarde)).findFirst()
					.orElse(null);
		}
		veranderFilter(gekozenMaand);
		if (scherm == 1) {
			vulBesteSessieLijsten();
		} else {
			vulGebruikerInfo();
		}
		firePropertyChange("maand", oudeMaand, gekozenMaand);
	}

	/**
	 * Zoekt een sessiekalender op met het meegegeven beginjaar en stelt deze in als
	 * gekozen sessiekalender. Als beginjaar "0" is, wordt de sessiekalender van het
	 * huidige academiejaar geselecteerd.
	 * 
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
	// ======================= Property support

	private <T> void firePropertyChange(String welke, T oude, T nieuwe) {
		subject.firePropertyChange(welke, oude, nieuwe);
	}

	public void addPropertyChangeListenerSessie(PropertyChangeListener pcl) {
		subject.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, "", null, null));
	}

	public void removePropertyChangeListenerSessie(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}

	

}
