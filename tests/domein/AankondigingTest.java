package domein;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AankondigingTest {
	
	private static Gebruiker admin = new Gebruiker("Harm", "De Weirdt", "hdw123456", "adminpass", "harm.deweirdt@hogent.be", 1103720666020L, GebruikerType.HOOFDVERANTWOORDELIJKE, StatusType.ACTIEF);
	
	private static Stream<Arguments> geldigeAankondigingen(){
		return Stream.of(
				//Algemene aankondigingen
				
				//-----------------------
				
				//Gepost
				Arguments.of(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now().plusSeconds(1), "test", AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now().plusDays(1), "test", AankondigingPrioriteit.LAAG),
				
				//inhoud
				Arguments.of(admin, LocalDateTime.now(), "t", AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now(), "dit is een testaankondiging", AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now(), "dit is %£*$ test aAn_K.Ond1g&n@:;/<>#ηθ|", AankondigingPrioriteit.LAAG),
				
				//prioriteit
				Arguments.of(admin, LocalDateTime.now(), "dit is een testaankondiging", AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now(), "dit is een testaankondiging", AankondigingPrioriteit.HOOG)
		);	
	}
	
	private static Stream<Arguments> ongeldigeAankondigingen(){
		return Stream.of(
				//Algemene aankondigingen
				
				//-----------------------
				
				//inhoud
				Arguments.of(admin, LocalDateTime.now(), null, AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now(), "", AankondigingPrioriteit.LAAG),
				Arguments.of(admin, LocalDateTime.now(), "        ", AankondigingPrioriteit.LAAG)
		);		
	}
	
	@ParameterizedTest
	@MethodSource("geldigeAankondigingen")
	public void maakGeldigeAankondigingAan_Slaagt(Gebruiker gebruiker, LocalDateTime gepost, String inhoud, AankondigingPrioriteit prioriteit) {
		Aankondiging aankondiging = new Aankondiging(gebruiker, gepost, inhoud, prioriteit);
		Assertions.assertEquals(gebruiker, aankondiging.getVerantwoordelijke());
		Assertions.assertEquals(gepost, aankondiging.getGepost());
		Assertions.assertEquals(inhoud, aankondiging.getInhoud());
		Assertions.assertEquals(prioriteit, aankondiging.getPrioriteit());
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeAankondigingen")
	public void maakOngeldigeAankondigingenAan_WerptException(Gebruiker gebruiker, LocalDateTime gepost, String inhoud, AankondigingPrioriteit prioriteit) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Aankondiging(gebruiker, gepost, inhoud, prioriteit));
	}
	
	@Test
	public void pasAankondigingAan_VerschillendeInhoud_Slaagt() {
		Aankondiging aankondiging = new Aankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG);
		int verandering = aankondiging.pasAankondigingAan("Dit is een test", AankondigingPrioriteit.LAAG);
		Assertions.assertEquals("Dit is een test", aankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.LAAG, aankondiging.getPrioriteit());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasAankondigingAan_VerschillendePrioriteit_Slaagt() {
		Aankondiging aankondiging = new Aankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG);
		int verandering = aankondiging.pasAankondigingAan("test", AankondigingPrioriteit.HOOG);
		Assertions.assertEquals("test", aankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.HOOG, aankondiging.getPrioriteit());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasAankondigingAan_VerschillendeInhoudEnPrioriteit_Slaagt() {
		Aankondiging aankondiging = new Aankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG);
		int verandering = aankondiging.pasAankondigingAan("dit is een test", AankondigingPrioriteit.HOOG);
		Assertions.assertEquals("dit is een test", aankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.HOOG, aankondiging.getPrioriteit());
		Assertions.assertEquals(2, verandering);
	}
	
	@Test
	public void pasAankondigingAan_DezelfdeInhoudEnPrioriteit_Faalt() {
		Aankondiging aankondiging = new Aankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG);
		int verandering = aankondiging.pasAankondigingAan("test", AankondigingPrioriteit.LAAG);
		Assertions.assertEquals("test", aankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.LAAG, aankondiging.getPrioriteit());
		Assertions.assertEquals(0, verandering);
	}
	
	@Test
	public void pasAankondigingAan_FoutieveInhoud_WerptException() {
		Aankondiging aankondiging = new Aankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG);
		Assertions.assertThrows(IllegalArgumentException.class, () -> aankondiging.pasAankondigingAan("", AankondigingPrioriteit.LAAG));
	}
}
