package domein;

import java.time.LocalDateTime;
// verzamelklasse van media :(document/afbeelding/link/video)
public class Media {
	
	 // ----- properties
	
	 public int id;
     public String adress;
     public String naam; 
     public LocalDateTime tijdToegevoegd;
     //enumklasse 
     public MediaType mediaType;
     //waarde van de enum
     private int mediaTypeWaarde;
     //de discriminator van de databank
     public String typeMedia;
     
     // ----- Constructors
     
     public Media( String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType,
 			String typeMedia) {
 		this.adress = adress;
 		this.naam = naam;
 		this.tijdToegevoegd = tijdToegevoegd;
 		this.mediaType = mediaType;
 		this.typeMedia = typeMedia;
 	}
     
	public Media(int id, String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType,
			String typeMedia) {
		this.id = id;
		this.adress = adress;
		this.naam = naam;
		this.tijdToegevoegd = tijdToegevoegd;
		this.mediaType = mediaType;
		this.typeMedia = typeMedia;
	}
	
	// ----- Getters and setters
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public LocalDateTime getTijdToegevoegd() {
		return tijdToegevoegd;
	}
	public void setTijdToegevoegd(LocalDateTime tijdToegevoegd) {
		this.tijdToegevoegd = tijdToegevoegd;
	}
	public MediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	public int getMediaTypeWaarde() {
		return mediaTypeWaarde;
	}
	public void setMediaTypeWaarde(int mediaTypeWaarde) {
		this.mediaTypeWaarde = mediaTypeWaarde;
	}
	public String getTypeMedia() {
		return typeMedia;
	}
	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}
	
	// ----- other methodes
     
}
