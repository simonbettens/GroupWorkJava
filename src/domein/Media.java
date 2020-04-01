package domein;

import java.time.LocalDateTime;

public class Media {

	private int id;
	private String adress;
	private String naam;
	private LocalDateTime tijdToegevoegd;
	private MediaType mediaType;
	public Media(String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		super();
		this.adress = adress;
		this.naam = naam;
		this.tijdToegevoegd = tijdToegevoegd;
		this.mediaType = mediaType;
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
		this.adress = adress;
	}
	private void setNaam(String naam) {
		this.naam = naam;
	}
	private void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		this.tijdToegevoegd = tijdToegevoegd;
	}
	private void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}


}