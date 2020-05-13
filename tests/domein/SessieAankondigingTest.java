package domein;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SessieAankondigingTest {
	
	private static LocalDateTime huidigetijd = LocalDateTime.now();
	private static Gebruiker admin = new Gebruiker("Harm", "De Weirdt", "hdw123456", "adminpass", "harm.deweirdt@hogent.be", 1103720666020L, GebruikerType.HOOFDVERANTWOORDELIJKE, StatusType.ACTIEF);
	private static Sessie testSessie = new Sessie(admin, "The Three Laws of TDD (featuring Kotlin)",
			huidigetijd.plusDays(1).plusHours(1), huidigetijd.plusDays(1).plusHours(2), 20,
			"GSCHB4.026",
			"Testen is moeilijk aan te brengen tijdens je opleiding want je komt toch niet vaak terug op oude code omdat de \"klant\" aanpassing wilt. Maar iedereen heeft al veel tijd verloren omdat er bugs waren, of omdat de code niet goed te lezen was. \n Maar Uncle Bob is terug, en deze keer legt hij de drie wetten van Test - Driven Development uit, en toont ze ook in actie. Dit zijn de drie regels:\n 1.You are not allowed to write any production code unless it is to make a failing unit test pass. \n 2.You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures. \n 3.You are not allowed to write any more production code than is sufficient to pass the one failing unit test. \n Door deze drie regels te volgen garandeer je dat je code altijd doet wat ze moet doen! Als je code bijschrijft of aanpast kan je altijd vertrouwen op je tests.","");
	
	private static Stream<Arguments> geldigeSessieAankondigingen(){
		return Stream.of(
				//Sessieaankondigingen
				
				//-----------------------
				
				//Gepost
				Arguments.of(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now().plusSeconds(1), "test", AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now().plusDays(1), "test", AankondigingPrioriteit.LAAG, testSessie),
				
				//inhoud
				Arguments.of(admin, LocalDateTime.now(), "t", AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now(), "dit is een testaankondiging", AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now(), "dit is %£*$ test aAn_K.Ond1g&n@:;/<>#ηθ|", AankondigingPrioriteit.LAAG, testSessie),
				
				//prioriteit
				Arguments.of(admin, LocalDateTime.now(), "dit is een testaankondiging", AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now(), "dit is een testaankondiging", AankondigingPrioriteit.HOOG, testSessie)
		);	
	}
	
	private static Stream<Arguments> ongeldigeSessieAankondigingen(){
		return Stream.of(
				//Sessieaankondigingen
				
				//-----------------------
				
				//inhoud
				Arguments.of(admin, LocalDateTime.now(), null, AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now(), "", AankondigingPrioriteit.LAAG, testSessie),
				Arguments.of(admin, LocalDateTime.now(), "        ", AankondigingPrioriteit.LAAG, testSessie)
		);		
	}
	
	@ParameterizedTest
	@MethodSource("geldigeSessieAankondigingen")
	public void maakGeldigeSessieAankondigingAan_Slaagt(Gebruiker gebruiker, LocalDateTime gepost, String inhoud, AankondigingPrioriteit prioriteit, Sessie sessie) {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(gebruiker, gepost, inhoud, prioriteit, sessie);
		Assertions.assertEquals(gebruiker, sessieAankondiging.getVerantwoordelijke());
		Assertions.assertEquals(gepost, sessieAankondiging.getGepost());
		Assertions.assertEquals(inhoud, sessieAankondiging.getInhoud());
		Assertions.assertEquals(prioriteit, sessieAankondiging.getPrioriteit());
		Assertions.assertEquals(sessie, sessieAankondiging.getSessie());
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeSessieAankondigingen")
	public void maakOngeldigeSessieAankondigingenAan_WerptException(Gebruiker gebruiker, LocalDateTime gepost, String inhoud, AankondigingPrioriteit prioriteit, Sessie sessie) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new SessieAankondiging(gebruiker, gepost, inhoud, prioriteit, sessie));
	}
	
	@Test
	public void pasSessieAankondigingAan_VerschillendeInhoud_Slaagt() {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG, testSessie);
		int verandering = sessieAankondiging.pasAankondigingAan("Dit is een test", AankondigingPrioriteit.LAAG);
		Assertions.assertEquals("Dit is een test", sessieAankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.LAAG, sessieAankondiging.getPrioriteit());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasSessieAankondigingAan_VerschillendePrioriteit_Slaagt() {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG, testSessie);
		int verandering = sessieAankondiging.pasAankondigingAan("test", AankondigingPrioriteit.HOOG);
		Assertions.assertEquals("test", sessieAankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.HOOG, sessieAankondiging.getPrioriteit());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasSessieAankondigingAan_VerschillendeInhoudEnPrioriteit_Slaagt() {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG, testSessie);
		int verandering = sessieAankondiging.pasAankondigingAan("dit is een test", AankondigingPrioriteit.HOOG);
		Assertions.assertEquals("dit is een test", sessieAankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.HOOG, sessieAankondiging.getPrioriteit());
		Assertions.assertEquals(2, verandering);
	}
	
	@Test
	public void pasSessieAankondigingAan_DezelfdeInhoudEnPrioriteit_Faalt() {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG, testSessie);
		int verandering = sessieAankondiging.pasAankondigingAan("test", AankondigingPrioriteit.LAAG);
		Assertions.assertEquals("test", sessieAankondiging.getInhoud());
		Assertions.assertEquals(AankondigingPrioriteit.LAAG, sessieAankondiging.getPrioriteit());
		Assertions.assertEquals(0, verandering);
	}
	
	@Test
	public void pasSessieAankondigingAan_FoutieveInhoud_WerptException() {
		SessieAankondiging sessieAankondiging = new SessieAankondiging(admin, LocalDateTime.now(), "test", AankondigingPrioriteit.LAAG, testSessie);
		Assertions.assertThrows(IllegalArgumentException.class, () -> sessieAankondiging.pasAankondigingAan("", AankondigingPrioriteit.LAAG));
	}
}
