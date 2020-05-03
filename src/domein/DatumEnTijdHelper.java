package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatumEnTijdHelper {
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static String dateTimeFormat(LocalDateTime date) {
		String datum = dtf.format(date);
		return String.format("%s om %d:%d", datum,date.getHour(),date.getMinute());
	}
	public static String dateFormat(LocalDate date) {
		return dtf.format(date);
	}
	public static boolean vergelijk(LocalDateTime dt1,LocalDateTime dt2) {
		if(dt1.getDayOfMonth()!=dt2.getDayOfMonth()) {
			return false;
		}
		if(dt1.getMonthValue()!=dt2.getMonthValue()) {
			return false;
		}
		if(dt1.getYear() != dt2.getYear()) {
			return false;
		}
		if(dt1.getHour() != dt2.getHour()) {
			return false;
		}
		if(dt1.getMinute() != dt2.getMinute()) {
			return false;
		}
		return true;
		
	}
}
