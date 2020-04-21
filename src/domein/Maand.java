package domein;

import java.util.Arrays;

public enum Maand {
	Januari(1),
	Februari(2),
	Maart(3),
	April(4),
	Mei(5),
	Juni(6),
	Juli(7),
	Augustus(8),
    September(9),
    Oktober(10),
    November(11),
    December(12);
	
	private int nummer;
	 
    private Maand(int nummer) {
        this.nummer = nummer;
    }
    public int getWaarde() {
        return nummer;
    }
 
    public static Maand of(int nummer) {
		return Arrays.stream(Maand.values())
          .filter(p -> p.getWaarde() == nummer)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
