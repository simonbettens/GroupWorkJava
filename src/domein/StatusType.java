package domein;

import java.util.Arrays;

public enum StatusType {
	ACTIEF(1),
	GEBLOKKEERD(2),
	NIETACTIEF(3);
	
	private int statusTypeValue;
	 
    private StatusType(int statusTypeValue) {
        this.statusTypeValue = statusTypeValue;
    }
 
    public int getStatusTypeValue() {
        return statusTypeValue;
    }
 
    public static StatusType of(int statusTypeValue) {
		return Arrays.stream(StatusType.values())
          .filter(p -> p.getStatusTypeValue() == statusTypeValue)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}