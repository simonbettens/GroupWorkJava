package controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import domein.Gebruiker;
import domein.Media;
import domein.MediaType;
import domein.Sessie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import repository.GenericDaoJpa;
import repository.MediaDao;
import repository.MediaDaoJpa;
import repository.SessieDao;

public class MediaController {

	private MediaDao mediaRepository;
	private SessieDao sessieRepository;
	private Gebruiker ingelogdeGebruiker;
	private PropertyChangeSupport subject;
	private Sessie gekozenSessie;

	// media bij een gekozen Sessie;
	private Media gekozenMedia;
	private List<Media> mediaLijst;
	private ObservableList<Media> mediaObservableLijst;
	private FilteredList<Media> filteredMediaLijst;
	private SortedList<Media> sortedMediaLijst;

	public MediaController(Gebruiker ingelogdeGebruiker, SessieDao repo) {
		this.ingelogdeGebruiker = ingelogdeGebruiker;
		this.sessieRepository = repo;
		this.mediaRepository = new MediaDaoJpa();
		this.subject = new PropertyChangeSupport(this);
		this.gekozenSessie = null;
		this.gekozenMedia = null;
	}

	public Gebruiker getIngelogdeGebruiker() {
		return ingelogdeGebruiker;
	}

	public Media getGekozenMedia() {
		return gekozenMedia;
	}

	public Sessie getGekozenSessie() {
		return gekozenSessie;
	}

	public void setGekozenSessie(Sessie gekozenSessie) {
		this.gekozenSessie = gekozenSessie;
		vulLijstMedia();
	}

	// Media methods
	public void vulLijstMedia() {
		System.out.println(gekozenSessie == null ? "null" : "notnull");
		mediaLijst = new ArrayList<>(gekozenSessie.getMedia());
		mediaObservableLijst = FXCollections.observableArrayList(mediaLijst);
		this.filteredMediaLijst = new FilteredList<>(mediaObservableLijst, e -> true);
		this.sortedMediaLijst = new SortedList<Media>(filteredMediaLijst,
				Comparator.comparing(Media::getMediaType).thenComparing(Media::getNaam));
	}

	public ObservableList<Media> getMedia() {
		return sortedMediaLijst;
	}

	public void setGeselecteerdeMedia(Media md) {
		firePropertyChange("media", this.gekozenMedia, md);
		this.gekozenMedia = md;
	}

	public void maakMedia(String naam, String bestandnaam, MediaType type) {
		Media media = new Media(gekozenSessie, bestandnaam, naam, LocalDateTime.now(), type);
		gekozenSessie.addMediaItem(media);
		mediaLijst.add(media);
		mediaObservableLijst.add(media);
		insertMedia(media);
	}

	public void pasMedia(String naam, String bestandnaam, MediaType type) {
		Media media = this.gekozenMedia;
		if (media != null) {
			int ver = media.pasMediaAan(bestandnaam, naam, LocalDateTime.now(), type);
			if (ver != 0) {
				updateMedia(media);
				firePropertyChange("media", this.gekozenMedia, media);
				this.gekozenMedia = media;
			}
		}
	}

	public void zoekOpMedia(String bestandnaam, MediaType type) {
		this.filteredMediaLijst.setPredicate(media -> {
			boolean typeWaardeLeeg = type == null;
			boolean naamWaardeLeeg = bestandnaam == null || bestandnaam.isBlank();

			if (typeWaardeLeeg && naamWaardeLeeg) {
				return true;
			}
			boolean conditieMaand = typeWaardeLeeg ? true : media.getMediaType() == type;
			boolean conditieNaam = naamWaardeLeeg ? true
					: media.getNaam().toLowerCase().contains(bestandnaam)
							|| media.getNaam().toLowerCase().startsWith(bestandnaam);
			return conditieMaand && conditieNaam;
		});
	}

	// ---Media databank operaties
	public void insertMedia(Media media) {
		GenericDaoJpa.startTransaction();
		mediaRepository.insert(media);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
	}

	public void updateMedia(Media media) {
		GenericDaoJpa.startTransaction();
		mediaRepository.update(media);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
	}

	public void deleteMedia() {
		Media teVerwijderenMedia = this.gekozenMedia;
		mediaLijst.remove(teVerwijderenMedia);
		gekozenSessie.removeMediaItem(gekozenMedia);
		mediaObservableLijst.remove(teVerwijderenMedia);
		GenericDaoJpa.startTransaction();
		mediaRepository.delete(teVerwijderenMedia);
		sessieRepository.update(gekozenSessie);
		GenericDaoJpa.commitTransaction();
		firePropertyChange("media", this.gekozenMedia, null);
		this.gekozenMedia = null;
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