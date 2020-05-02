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

	// Constructor
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

	private void vulLijsten() {
		this.sessieKalenderLijst = sessiekalenderRepository.getAll();
		this.sessieKalenderObservableLijst = FXCollections.observableArrayList(
				sessieKalenderLijst.stream().sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed())
						.map(SessieKalender::toString).collect(Collectors.toList()));

		vulSessieLijsten();
	}

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

	public void zoekOpNaam(String naamWaarde) {
		veranderFilter(gekozenMaand, naamWaarde);
	}

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

	// Setters
	public void setIngelogdeGebruiker(Gebruiker ingelogdeGebruiker) {
		this.ingelogdeGebruiker = ingelogdeGebruiker;
	}

	public void setSessiekalenderRepository(SessieKalenderDaoJpa mock) {
		this.sessiekalenderRepository = mock;
	}

	public void setSessieRepository(SessieDao mock) {
		this.sessieRepository = mock;
	}

	public void changeSelectedSessieKalender(String waarde) {
		gekozenSessieKalender = sessieKalenderLijst.stream().filter(sk -> sk.toString().equals(waarde)).findFirst()
				.orElse(null);
		vulSessieLijsten();
	}

	public void changeSelectedMaand(String waarde) {
		gekozenMaand = Arrays.asList(Maand.values()).stream().filter(sk -> sk.toString().equals(waarde)).findFirst()
				.orElse(null);
		veranderFilter(gekozenMaand, "");
	}
	public void changeSelectedSessie(String waarde) {
		gekozenSessie = sessieLijst.stream().filter(s -> s.toString().equals(waarde)).findFirst().orElse(null);
		//TODO roep functie voor gegevens?
	}

	public void setSessie(Sessie sessie) {
		if (sessie != null) {
			setVerantwoordelijkeLijstBijSessie(sessie.getVerantwoordelijke());
		}
		firePropertyChange("sessie", this.gekozenSessie, sessie);
		this.gekozenSessie = sessie;
	}

	// getters
	public List<SessieKalender> getSessieKalenderLijst() {
		return sessieKalenderLijst;
	}

	public SessieKalender getGekozenSessieKalender() {
		return gekozenSessieKalender;
	}

	public ObservableList<String> getSessieKalenderObservableLijst() {
		return sessieKalenderObservableLijst;
	}

	public List<Sessie> getSessieLijst() {
		return sessieLijst;
	}

	public ObservableList<Sessie> getSessieObservableLijst() {
		return this.sortedSessieLijst;
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	private List<SessieKalender> getSessieKalenderList() {
		if (sessieKalenderLijst == null) {
			sessieKalenderLijst = sessiekalenderRepository.getAll();
		}
		return sessieKalenderLijst;
	}

	// Sessiekalender methods
	public void setSessieKalender(String beginjaar) {
		try {
			if (beginjaar == "0") {
				String jaar;
				if (LocalDate.now().getMonth().getValue() < Maand.September.getWaarde()) {
					jaar = String.format("%d", LocalDate.now().getYear() - 1);
				} else {
					jaar = String.format("%d", LocalDate.now().getYear());
				}
				System.out.println(jaar);
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

	public SessieKalender getLaasteSessieKalender() {
		return this.sessieKalenderLijst.stream().sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed())
				.findFirst().orElse(null);
	}

	public void maakNieuweSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = new SessieKalender(startDatum, eindDatum);
		insertSessieKalender(sk);
	}

	public void pasSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = this.gekozenSessieKalender;
		sessieKalenderLijst.remove(sk);
		sessieKalenderObservableLijst.remove(sk.toString());
		sk.setStartDatum(startDatum);
		sk.setEindDatum(eindDatum);
		updateSessieKalender(sk);
		sessieKalenderLijst.add(sk);
		sessieKalenderObservableLijst.add(sk.toString());
		this.gekozenSessieKalender = sk;
	}

	public boolean isKalenderUniek(SessieKalender kalender) {
		boolean overlap = false;
		for (SessieKalender k : sessieKalenderLijst) {
			overlap = k.getStartDatum().getYear() == kalender.getStartDatum().getYear();
		}
		return overlap;
	}

	// Sessie methods

	public Sessie getSessie() {
		return this.gekozenSessie;
	}

	public ObservableList<String> getVerantwoordelijkeNamen() {
		return FXCollections.observableArrayList(
				verantwoordelijkeLijstBijSessie.stream().map(Gebruiker::getVolledigeNaam).collect(Collectors.toList()));
	}

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

	public void pasSessieAan(String titel, String lokaal, String gastSpreker, String startuur, String startmin,
			String einduur, String eindmin, String volledigeNaamVerantwoordelijke, String beschrijving, String maxCap,
			boolean staatOpen, boolean gesloten, LocalDate start, LocalDate einde) {
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
			if(uurInt<=0||uurInt>24) {
				String fout = String.format("Voer een geldig formaat in bij het %suur, het uur moet tussen 1 - 24 liggen", welke);
				throw new IllegalArgumentException(fout);
			}
			if(uurInt<0||uurInt>59) {
				String fout = String.format("Voer een geldig formaat in bij het %suur, de minuten moet tussen 0 - 59 liggen", welke);
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

	// Databank operaties
	// --- Sessiekalender databank operaties

	public void insertSessieKalender(SessieKalender sk) {
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.insert(sk);
		GenericDaoJpa.commitTransaction();
		sessieKalenderLijst.add(sk);
		sessieKalenderObservableLijst.add(sk.toString());
	}

	public void updateSessieKalender(SessieKalender sk) {
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.update(sk);
		GenericDaoJpa.commitTransaction();
	}

	// ---Sessie databank operaties
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

	public void insertSessie(Sessie sessie) {
		GenericDaoJpa.startTransaction();
		sessieRepository.insert(sessie);
		GenericDaoJpa.commitTransaction();
	}

	public void updateSessie(Sessie sessie) {
		GenericDaoJpa.startTransaction();
		sessieRepository.update(sessie);
		GenericDaoJpa.commitTransaction();
	}

	public void rollBack() {
		GenericDaoJpa.rollbackTransaction();
	}

	// changeSupport
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
