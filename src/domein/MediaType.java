package domein;

import java.util.Arrays;

public enum MediaType {
	LINK(1),
	VIDEO(2),
	YOUTUBEVIDEO(3),
	AFBEELDING(4),
	EXCEL(5),
	WORD(6),
	PDF(7),
	POWERPOINT(8),
	ZIP(9),
	ANDERDOCUMENT(10);
	
	private int mediaTypeValue;
	 
    private MediaType(int mediaTypeValue) {
        this.mediaTypeValue = mediaTypeValue;
    }
 
    public int getMediaTypeValue() {
        return mediaTypeValue;
    }
 
    public static MediaType of(int mediaTypeValue) {
		return Arrays.stream(MediaType.values())
          .filter(p -> p.getMediaTypeValue() == mediaTypeValue)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
    public static String MediaTypeToString(MediaType type) {
    	String waarde = "";
    	switch (type) {
		case AFBEELDING: waarde = "Afbeelding";
			break;
		case ANDERDOCUMENT: waarde = "Ander document";
			break;
		case EXCEL:waarde = "Excel";
			break;
		case LINK:waarde = "Link";
			break;
		case PDF:waarde = "Pdf";
			break;
		case POWERPOINT:waarde = "Powerpoint";
			break;
		case VIDEO:waarde = "Video";
			break;
		case WORD:waarde = "Word";
			break;
		case YOUTUBEVIDEO:waarde = "Youtube Video";
			break;
		case ZIP:waarde = "Zip";
			break;
		default:
			break;
    	}
    	return waarde;
    }
}