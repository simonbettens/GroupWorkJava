package domein;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SessieTest {
	private static Stream<Arguments> ongeldigeSessieData(){
		return Stream.of(
				Arguments.of("", LocalDateTime.of(2020, 6, 5, 12, 30), LocalDateTime.of(2020, 6, 5, 12, 30), 30, "B4.012"),
				Arguments.of("titel", LocalDateTime.of(2020, 6, 5, 12, 30), LocalDateTime.of(2020, 6, 5, 13, 00), 30, ""),
				Arguments.of("titel", LocalDateTime.of(2019, 6, 5, 12, 30), LocalDateTime.of(2019, 6, 5, 13, 00), 30, "B4.012"),
				Arguments.of("titel", null, LocalDateTime.of(2020, 6, 5, 13, 00), 30, "B4.012"),
				Arguments.of("titel", LocalDateTime.of(2020, 6, 5, 12, 30), null, 30, "B4.012"),
				Arguments.of("titel", LocalDateTime.of(2020, 6, 5, 12, 30), LocalDateTime.of(2020, 6, 5, 12, 30), 0, "B4.012"),
				Arguments.of("titel", LocalDateTime.of(2020, 6, 5, 13, 40), LocalDateTime.of(2020, 6, 5, 13, 00), 30, "B4.012")
				);
	}
	
	private static Stream<Arguments> geldigeSessieData(){
		return Stream.of(
				Arguments.of("titel", LocalDateTime.of(2020, 6, 5, 12, 30), LocalDateTime.of(2020, 6, 5, 13, 00), 30, "B4.012"),
				Arguments.of("titel", LocalDateTime.of(2020, 6, 12, 12, 30), LocalDateTime.of(2020, 6, 12, 13, 20), 30, "B4.012"));
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeSessieData")
	public void maakOngeldigeSessie_WerptException(String titel, LocalDateTime startuur, LocalDateTime einduur, int maxcap, String lokaal) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Sessie(new Gebruiker(), titel, startuur, einduur, false, maxcap, lokaal, ""));
	}
	
	@ParameterizedTest
	@MethodSource("geldigeSessieData")
	public void maakGeldigeSessie_Slaagt(String titel, LocalDateTime startuur, LocalDateTime einduur, int maxcap, String lokaal) {
		Sessie testSessie = new Sessie(new Gebruiker(), titel, startuur, einduur, false, maxcap, lokaal, "");
		Assertions.assertEquals(titel, testSessie.getTitelProperty().getValue());
	}
	
}
