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
}