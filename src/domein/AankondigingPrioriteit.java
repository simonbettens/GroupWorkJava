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
}