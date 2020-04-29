package domein;

import java.util.Arrays;

public enum AankondigingPrioriteit {
	LAAG(1), HOOG(2);
	 
    private int priority;
 
    private AankondigingPrioriteit(int priority) {
        this.priority = priority;
    }
 
    public int getPriority() {
        return priority;
    }
 
    public static AankondigingPrioriteit of(int priority) {
		return Arrays.stream(AankondigingPrioriteit.values())
          .filter(p -> p.getPriority() == priority)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
    
    public static String AankondigingPrioriteitToString(AankondigingPrioriteit prioriteit) {
    	String waarde = "";
    	
    	switch (prioriteit) {
		case LAAG: waarde = "Laag";
			break;
		case HOOG: waarde = "Hoog";
			break;
		default:
			break;
    	}
    	
    	return waarde;
    }
    
    public static AankondigingPrioriteit StringToAankondigingPrioriteit(String prioriteit) {
    	AankondigingPrioriteit waarde = AankondigingPrioriteit.LAAG;
    	
    	switch (prioriteit) {
		case "Laag": waarde = AankondigingPrioriteit.LAAG ;
			break;
		case "Hoog": waarde = AankondigingPrioriteit.HOOG;
			break;
		default:
			break;
    	}
    	return waarde;
    }
}