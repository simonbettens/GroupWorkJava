package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatumEnTijdFormater {
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static String dateTimeFormat(LocalDateTime date) {
		String datum = dtf.format(date);
		return String.format("%s om %d:%d", datum,date.getHour(),date.getMinute());
	}
	public static String dateFormat(LocalDate date) {
		return dtf.format(date);
	}
}
