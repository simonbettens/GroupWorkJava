package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import domein.Gebruiker;
import domein.GebruikerType;
import domein.Maand;
import domein.Sessie;
import domein.SessieKalender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.GenericDaoJpa;
import repository.SessieDao;
import repository.SessieDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class SessieController {

	// Properties
	private SessieKalenderDao sessiekalenderRepository;
	private SessieDao sessieRepository;
	private PropertyChangeSupport subjectSessie;
	private PropertyChangeSupport subjectSessieKalender;
	private Gebruiker ingelogdeGebruiker;
	private Maand gekozenMaand;
	// Lijst van sessieKalenders
	private SessieKalender gekozenSessieKalender;
	private List<SessieKalender> sessieKalenderLijst;
	private ObservableList<String> sessieKalenderObservableLijst;
	private SortedList<SessieKalender> sortedSessieKalenderLijst;
	// Lijst van sessies
	private Sessie sessie;
	private List<Sessie> sessieLijst;
	private ObservableList<Sessie> sessieObservableLijst;
	private FilteredList<Sessie> filteredSessieLijst;
	private SortedList<Sessie> sortedSessieLijst;

	// Constructor
	public SessieController(Gebruiker ingelogdeGebruiker) {
		setSessiekalenderRepository(new SessieKalenderDaoJpa());
		setSessieRepository(new SessieDaoJpa());
		setIngelogdeGebruiker(ingelogdeGebruiker);
		this.subjectSessie = new PropertyChangeSupport(this);
		this.subjectSessieKalender = new PropertyChangeSupport(this);
		setSessieKalender("0");
		gekozenMaand = Maand.of(LocalDate.now().getMonthValue());
		vulLijsten();
		sessie = null;
	}

	private void vulLijsten() {
		this.sessieKalenderLijst = sessiekalenderRepository.getAll();
		this.sessieKalenderObservableLijst = FXCollections.observableArrayList(sessieKalenderLijst.stream().sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed())
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
			this.sortedSessieLijst = new SortedList<Sessie>(filteredSessieLijst,
					Comparator.comparing(Sessie::getStartDatum).thenComparing(Sessie::getNaam));
		}
		else {
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

	public void setSessieRepository(SessieDaoJpa mock) {
		this.sessieRepository = mock;
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
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.insert(sk);
		GenericDaoJpa.commitTransaction();
		sessieKalenderLijst.add(sk);
		sessieKalenderObservableLijst.add(sk.toString());

	}

	public void pasSessieKalender(LocalDate startDatum, LocalDate eindDatum) {
		System.out.println("Start" + startDatum.toString() + " - " + eindDatum.toString());
		SessieKalender sk = this.gekozenSessieKalender;
		sessieKalenderLijst.remove(sk);
		sessieKalenderObservableLijst.remove(sk.toString());
		sk.setStartDatum(startDatum);
		sk.setEindDatum(eindDatum);
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.update(this.gekozenSessieKalender);
		GenericDaoJpa.commitTransaction();
		sessieKalenderLijst.add(sk);
		sessieKalenderObservableLijst.add(sk.toString());
		this.gekozenSessieKalender = sk;
		System.out.println("einde");
		
	}

	public void verwijderSessieKalender(String beginjaar) {
		SessieKalender sk = sessiekalenderRepository.getByBeginjaar(beginjaar);
		GenericDaoJpa.startTransaction();
		sessiekalenderRepository.delete(sk);
		GenericDaoJpa.commitTransaction();
	}

	private List<SessieKalender> getSessieKalenderList() {
		if (sessieKalenderLijst == null) {
			sessieKalenderLijst = sessiekalenderRepository.getAll();
		}
		return sessieKalenderLijst;
	}

	public Sessie getSessie() {
		return sessie;
	}

	public void setSessie(Sessie sessie) {
		firePropertyChange("geselecteerdeSessie", this.sessie, sessie);
		this.sessie = sessie;
	}
	// Sessie methods

	private  void firePropertyChange(String welke, Sessie oude, Sessie nieuwe) {	
			subjectSessie.firePropertyChange(welke, oude, nieuwe);
	}

	public void addPropertyChangeListenerSessie(PropertyChangeListener pcl) {
		subjectSessie.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, "geselecteerdeSessie", null, this.sessie));
	}

	public void removePropertyChangeListenerSessie(PropertyChangeListener pcl) {
		subjectSessie.removePropertyChangeListener(pcl);
	}
	public boolean isKalenderUniek(SessieKalender kalender) {
		boolean overlap = false;
		for (SessieKalender k : sessieKalenderLijst ) {
			overlap = k.getStartDatum().getYear() == kalender.getStartDatum().getYear();
		}
		return overlap;
	}
}
