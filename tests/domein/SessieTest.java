package domein;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
	
	private static Stream<Arguments> pasSessieAanData(){
		return Stream.of(
				Arguments.of("titel", LocalDateTime.of(2020, 6, 5, 12, 30), LocalDateTime.of(2020, 6, 5, 13, 00), 30, "B4.012", "titel2", LocalDateTime.of(2020, 6, 5, 12, 30), LocalDateTime.of(2020, 6, 5, 13, 00), 35, "B4.013", 4),
				Arguments.of("titel", LocalDateTime.of(2020, 6, 12, 12, 30), LocalDateTime.of(2020, 6, 12, 13, 20), 30, "B4.012", "titel", LocalDateTime.of(2020, 6, 4, 12, 30), LocalDateTime.of(2020, 6, 4, 13, 00), 30, "B4.012", 3)
				);
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeSessieData")
	public void maakOngeldigeSessie_WerptException(String titel, LocalDateTime startuur, LocalDateTime einduur, int maxcap, String lokaal) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Sessie(new Gebruiker(), titel, startuur, einduur, maxcap, lokaal, "",""));
	}
	
	@ParameterizedTest
	@MethodSource("geldigeSessieData")
	public void maakGeldigeSessie_Slaagt(String titel, LocalDateTime startuur, LocalDateTime einduur, int maxcap, String lokaal) {
		Sessie testSessie = new Sessie(new Gebruiker(), titel, startuur, einduur, maxcap, lokaal, "","");
		Assertions.assertEquals(titel, testSessie.getTitelProperty().getValue());
	}
	
	
	@Test
	public void testIsBezig_returnsTrue() {
		Sessie testSessie=new Sessie(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(29));
		Assertions.assertTrue(testSessie.isBezig());
	}
	
	@Test
	public void testIsBezig_returnsFalse() {
		Sessie testSessie=new Sessie(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(31));
		Assertions.assertFalse(testSessie.isBezig());
	}
	
	//pas sessie aan

	@ParameterizedTest
	@MethodSource("pasSessieAanData")
	public void pasSessieAanTest(String titel, LocalDateTime startuur, LocalDateTime einduur, int maxcap, String lokaal, String titel2, LocalDateTime startuur2, LocalDateTime einduur2, int maxcap2, String lokaal2, int changes) {
		Gebruiker gebruiker= new Gebruiker("Piet", "Piraat", "Ppr123456", "randompass", "piet.piraat@student.hogent.be", 1234567890123l, GebruikerType.GEBRUIKER, StatusType.ACTIEF);
		Sessie sessie = new Sessie(gebruiker, titel, startuur, einduur, maxcap, lokaal, "","");
		Assertions.assertEquals(
				changes, 
				sessie.pasSessieAan(gebruiker, titel2, startuur2, einduur2, false, true, maxcap2, lokaal2, "","")
				);
	}
}
