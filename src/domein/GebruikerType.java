package domein;

import java.util.Arrays;

public enum GebruikerType {
	GEBRUIKER(1),
	VERANTWOORDELIJKE(2),
	HOOFDVERANTWOORDELIJKE(3);
	
	private int gebruikersTypeValue;
	 
    private GebruikerType(int gebruikersTypeValue) {
        this.gebruikersTypeValue = gebruikersTypeValue;
    }
 
    public int getGebruikersTypeValue() {
        return gebruikersTypeValue;
    }
 
    public static GebruikerType of(int gebruikersTypeValue) {
		return Arrays.stream(GebruikerType.values())
          .filter(p -> p.getGebruikersTypeValue() == gebruikersTypeValue)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
    public static String toString(GebruikerType t) {
    	String enumStringLowerCase = t.toString().toLowerCase();
		return enumStringLowerCase.substring(0, 1).toUpperCase() + enumStringLowerCase.substring(1);
    }
}