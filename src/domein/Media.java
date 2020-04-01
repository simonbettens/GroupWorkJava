package domein;

import java.time.LocalDateTime;

public class Media {

	private int id;
	private String adress, naam;
	private LocalDateTime tijdToegevoegd;
	private MediaType mediaType;
	
	//voor jpa
	public Media() {}
	//voor nieuwe instantie
	public Media(String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		setAdress(adress);
		setNaam(naam);
		setTijdToegevoegd(tijdToegevoegd);
		setMediaType(mediaType);
	}
	public int getId() {
		return id;
	}
	public String getAdress() {
		return adress;
	}
	public String getNaam() {
		return naam;
	}
	public LocalDateTime getTijdToegevoegd() {
		return tijdToegevoegd;
	}
	public MediaType getMediaType() {
		return mediaType;
	}
	private void setAdress(String adress) {
		if(adress ==null || adress.equals("")) {
			throw new IllegalArgumentException("adress moet ingevuld zijn");
		}
		this.adress = adress;
	}
	private void setNaam(String naam) {
		if(naam == null || naam.equals("")) {
			throw new IllegalArgumentException("naam moet ingevuld zijn");
		}
		this.naam = naam;
	}
	private void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		if(tijdToegevoegd.isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("je kan geen media toevoegen waarvan de tijd in het verleden ligt");
		}
		this.tijdToegevoegd = tijdToegevoegd;
	}
	private void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	

}