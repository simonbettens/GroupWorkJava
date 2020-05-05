package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import domein.Feedback;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieAankondiging;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.FeedbackDao;
import repository.FeedbackDaoJpa;
import repository.GenericDaoJpa;
import repository.SessieAankondigingDao;
import repository.SessieDao;

public class FeedbackController {
	private FeedbackDaoJpa feedbackRepository;
	private SessieDao sessieRepository;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Sessie gekozenSessie;

	// Feedback bij een gekozen Sessie;
	private Feedback gekozenFeedback;
	private List<Feedback> feedbackLijst;
	private ObservableList<Feedback> feedbackObservableLijst;
	private FilteredList<Feedback> filteredFeedbackLijst;
	private SortedList<Feedback> sortedFeedbackLijst;

	public FeedbackController(Gebruiker ingelogdeGebruiker, SessieDao sessieRepository) {
		this.sessieRepository = sessieRepository;
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.feedbackRepository = new FeedbackDaoJpa();
		this.subject = new PropertyChangeSupport(this);
		this.gekozenSessie = null;
		this.gekozenFeedback = null;
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public Feedback getGekozenFeedback() {
		return gekozenFeedback;
	}

	public Sessie getGekozenSessie() {
		return gekozenSessie;
	}

	public void setGekozenSessie(Sessie gekozenSessie) {
		this.gekozenSessie = gekozenSessie;
		vulLijstFeedback();
	}
	
	public ObservableList<Feedback> getFeedbackObservableLijst() {
		return feedbackObservableLijst;
	}

	public void setGekozenFeedback(Feedback gekozenFeedback) {
		firePropertyChange("feedback", this.gekozenFeedback, gekozenFeedback);
		this.gekozenFeedback = gekozenFeedback;
	}

	private void vulLijstFeedback() {
		// TODO Auto-generated method stub
		if (gekozenSessie != null) {
			feedbackRepository.reload();
			feedbackLijst =feedbackRepository.getAll().stream().filter(f->f.getSessie().getSessieId()==gekozenSessie.getSessieId()).collect(Collectors.toList());
			feedbackObservableLijst = FXCollections.observableArrayList(feedbackLijst);
			this.filteredFeedbackLijst = new FilteredList<>(feedbackObservableLijst, e -> true);
			this.sortedFeedbackLijst = new SortedList<Feedback>(filteredFeedbackLijst,
					Comparator.comparing(Feedback::getVolledigeNaam).thenComparing(Feedback::getTijdToegevoegd));
		}
	}
	public void zoekOpNaam(String naamWaarde) {
		this.filteredFeedbackLijst.setPredicate(s -> {
			boolean naamWaardeLeeg = naamWaarde == null || naamWaarde.isBlank();
			if (naamWaardeLeeg) {
				return true;
			}
			boolean conditieNaam = naamWaardeLeeg ? true
					: s.getVolledigeNaam().toLowerCase().contains(naamWaarde)
							|| s.getVolledigeNaam().toLowerCase().startsWith(naamWaarde);

			return conditieNaam;
		});
	}
	public void deleteFeedback() {
		Feedback teVerwijderenFeedback= this.gekozenFeedback;
		feedbackLijst.remove(teVerwijderenFeedback);
		gekozenSessie.removeFeedback(teVerwijderenFeedback);
		feedbackObservableLijst.remove(teVerwijderenFeedback);
		GenericDaoJpa.startTransaction();
		feedbackRepository.delete(teVerwijderenFeedback);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("feedback", this.gekozenFeedback, null);
		this.gekozenFeedback = null;
	}
	
	private <T> void firePropertyChange(String welke, T oude, T nieuwe) {
		subject.firePropertyChange(welke, oude, nieuwe);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl, String welke) {
		subject.addPropertyChangeListener(pcl);
		pcl.propertyChange(new PropertyChangeEvent(pcl, welke, null, null));
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}
	

}
