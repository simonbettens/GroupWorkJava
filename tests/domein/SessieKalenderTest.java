package domein;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class SessieKalenderTest {

	private static Stream<Arguments> geldigeAcademiejaarData(){
		return Stream.of(
				Arguments.of(LocalDate.of(2017, 9, 25), LocalDate.of(2018, 9, 23)),
				Arguments.of(LocalDate.of(2018, 9, 24), LocalDate.of(2029, 9, 22)),
				Arguments.of(LocalDate.of(2019, 9, 23), LocalDate.of(2020, 9, 20))
				);
	}
	
	private static Stream<Arguments> ongeldigeAcademiejaarData(){
		return Stream.of(
				Arguments.of(LocalDate.of(2019, 9, 25), LocalDate.of(2018, 9, 23)),
				Arguments.of(LocalDate.of(1000, 9, 25), LocalDate.of(2018, 9, 23)),
				Arguments.of(LocalDate.MIN, LocalDate.MAX),
				Arguments.of(LocalDate.of(2019, 9, 25), LocalDate.of(2019, 9, 26))
				);
	}
	
	@ParameterizedTest
	@MethodSource("geldigeAcademiejaarData")
	public void maakGeldigeSessieKalender_Slaagt(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = new SessieKalender(startDatum, eindDatum);
		Assertions.assertEquals(startDatum, sk.getStartDatum());
		Assertions.assertEquals(eindDatum, sk.getEindDatum());
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeAcademiejaarData")
	public void maakOngeldigeSessieKalender_WerptException(LocalDate startDatum, LocalDate eindDatum) {
		SessieKalender sk = new SessieKalender();
		Assertions.assertThrows(IllegalArgumentException.class, () -> sk.setStartDatum(startDatum));
		Assertions.assertThrows(IllegalArgumentException.class, () -> sk.setEindDatum(eindDatum));
	}
}
