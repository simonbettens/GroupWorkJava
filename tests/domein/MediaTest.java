package domein;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MediaTest {
	
	//attributen nodig voor sessie (geldige gegevens)
	private static LocalDateTime huidigetijd = LocalDateTime.now();
	private static Gebruiker admin = new Gebruiker("Harm", "De Weirdt", "hdw123456", "adminpass", "harm.deweirdt@hogent.be", 1103720666020L, GebruikerType.HOOFDVERANTWOORDELIJKE, StatusType.ACTIEF);
	private static Sessie testSessie = new Sessie(admin, "The Three Laws of TDD (featuring Kotlin)",
			huidigetijd.plusDays(1).plusHours(1), huidigetijd.plusDays(1).plusHours(2), 20,
			"GSCHB4.026",
			"Testen is moeilijk aan te brengen tijdens je opleiding want je komt toch niet vaak terug op oude code omdat de \"klant\" aanpassing wilt. Maar iedereen heeft al veel tijd verloren omdat er bugs waren, of omdat de code niet goed te lezen was. \n Maar Uncle Bob is terug, en deze keer legt hij de drie wetten van Test - Driven Development uit, en toont ze ook in actie. Dit zijn de drie regels:\n 1.You are not allowed to write any production code unless it is to make a failing unit test pass. \n 2.You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures. \n 3.You are not allowed to write any more production code than is sufficient to pass the one failing unit test. \n Door deze drie regels te volgen garandeer je dat je code altijd doet wat ze moet doen! Als je code bijschrijft of aanpast kan je altijd vertrouwen op je tests.","");
	
	private static Stream<Arguments> geldigeMedia(){
		return Stream.of(

				//Verschillende types
				Arguments.of(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK),
				Arguments.of(testSessie,"https://www.youtube.com/embed/1Rcf8-yk6_o", "Youtbe Linux", huidigetijd, MediaType.YOUTUBEVIDEO),
				Arguments.of(testSessie,"/videos/testVideo.mp4", "Video binary", huidigetijd, MediaType.VIDEO),
				Arguments.of(testSessie,"/images/StockFoto1.jpg", "StockFoto1", huidigetijd, MediaType.AFBEELDING),
				Arguments.of(testSessie,"/documents/Test_document_itlab_sessie.docx", "Word document", huidigetijd, MediaType.WORD),
				Arguments.of(testSessie,"/documents/Test_document_itlab_sessie.pdf", "Pdf document", huidigetijd, MediaType.PDF),
				Arguments.of(testSessie,"/documents/Test_excel.xlsx", "Excel document",huidigetijd, MediaType.EXCEL),
				Arguments.of(testSessie,"/documents/Test_powerpoint.pptx", "Powerpoint document", huidigetijd, MediaType.POWERPOINT),
				Arguments.of(testSessie,"/documents/TestDocumenten.zip", "Gezipte map", huidigetijd, MediaType.ZIP),
				
				//adress
				Arguments.of(testSessie, "t", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK),
				Arguments.of(testSessie, "dit is %£*$ test AKd1&@:;/<>#ηθ|", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK),
				
				//naam
				Arguments.of(testSessie, "https://www.google.be/", "t", huidigetijd, MediaType.LINK),
				Arguments.of(testSessie, "https://www.google.be/", "dit is %£*$ test AKd1&@:;/<>#ηθ|", huidigetijd, MediaType.LINK)
		);	
	}
	
	private static Stream<Arguments> ongeldigeMedia(){
		return Stream.of(

				//adress
				Arguments.of(testSessie, null, "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK),
				Arguments.of(testSessie, "", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK),
				Arguments.of(testSessie, "           ", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK),
				
				//naam
				Arguments.of(testSessie, "https://www.google.be/", null, huidigetijd, MediaType.LINK),
				Arguments.of(testSessie, "https://www.google.be/", "", huidigetijd, MediaType.LINK),
				Arguments.of(testSessie, "https://www.google.be/", "        ", huidigetijd, MediaType.LINK)
		);		
	}
	
	@ParameterizedTest
	@MethodSource("geldigeMedia")
	public void maakGeldigeMediaAan_Slaagt(Sessie sessie, String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		Media media = new Media(sessie, adress, naam, tijdToegevoegd, mediaType);
		Assertions.assertEquals(sessie, media.getSessie());
		Assertions.assertEquals(adress, media.getAdress());
		Assertions.assertEquals(naam, media.getNaam());
		Assertions.assertEquals(tijdToegevoegd, media.getTijdToegevoegd());
		Assertions.assertEquals(mediaType, media.getMediaType());
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeMedia")
	public void maakOngeldigeMediaAan_WerptException(Sessie sessie, String adress, String naam, LocalDateTime tijdToegevoegd, MediaType mediaType) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Media(sessie, adress, naam, tijdToegevoegd, mediaType));
	}
	
	@Test
	public void pasMediaAan_VerschillendeNaam_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.be/", "Klik hier om niet naar google te gaan", MediaType.LINK);
		Assertions.assertEquals("https://www.google.be/", media.getAdress());
		Assertions.assertEquals("Klik hier om niet naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.LINK, media.getMediaType());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasMediaAan_VerschillendeAdress_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.com/", "Klik hier om naar google te gaan", MediaType.LINK);
		Assertions.assertEquals("https://www.google.com/", media.getAdress());
		Assertions.assertEquals("Klik hier om naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.LINK, media.getMediaType());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasMediaAan_VerschillendeMediaType_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.be/", "Klik hier om naar google te gaan", MediaType.YOUTUBEVIDEO);
		Assertions.assertEquals("https://www.google.be/", media.getAdress());
		Assertions.assertEquals("Klik hier om naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.YOUTUBEVIDEO, media.getMediaType());
		Assertions.assertEquals(1, verandering);
	}
	
	@Test
	public void pasMediaAan_VerschillendeNaamEnAdress_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.com/", "Klik hier om niet naar google te gaan", MediaType.LINK);
		Assertions.assertEquals("https://www.google.com/", media.getAdress());
		Assertions.assertEquals("Klik hier om niet naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.LINK, media.getMediaType());
		Assertions.assertEquals(2, verandering);
	}
	
	@Test
	public void pasMediaAan_VerschillendeAdressEnMediaType_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.be/", "Klik hier om niet naar google te gaan", MediaType.YOUTUBEVIDEO);
		Assertions.assertEquals("https://www.google.be/", media.getAdress());
		Assertions.assertEquals("Klik hier om niet naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.YOUTUBEVIDEO, media.getMediaType());
		Assertions.assertEquals(2, verandering);
	}
	
	@Test
	public void pasMediaAan_VerschillendeNaamEnMediaType_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.com/", "Klik hier om naar google te gaan", MediaType.YOUTUBEVIDEO);
		Assertions.assertEquals("https://www.google.com/", media.getAdress());
		Assertions.assertEquals("Klik hier om naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.YOUTUBEVIDEO, media.getMediaType());
		Assertions.assertEquals(2, verandering);
	}
	
	@Test
	public void pasMediaAan_VerschillendeNaamEnAdressEnMediaType_Slaagt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.com/", "Klik hier om niet naar google te gaan", MediaType.YOUTUBEVIDEO);
		Assertions.assertEquals("https://www.google.com/", media.getAdress());
		Assertions.assertEquals("Klik hier om niet naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.YOUTUBEVIDEO, media.getMediaType());
		Assertions.assertEquals(3, verandering);
	}
	
	@Test
	public void pasMediaAan_DezelfdeNaamEnAdressEnMediaType_Faalt() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		int verandering = media.pasMediaAan("https://www.google.be/", "Klik hier om naar google te gaan", MediaType.LINK);
		Assertions.assertEquals("https://www.google.be/", media.getAdress());
		Assertions.assertEquals("Klik hier om naar google te gaan", media.getNaam());
		Assertions.assertEquals(MediaType.LINK, media.getMediaType());
		Assertions.assertEquals(0, verandering);
	}
	
	@Test
	public void pasMediaAan_FoutieveNaam_WerptException() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		Assertions.assertThrows(IllegalArgumentException.class, () -> media.pasMediaAan("", "Klik hier om naar google te gaan", MediaType.LINK));
	}
	
	@Test
	public void pasMediaAan_FoutiefAdress_WerptException() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		Assertions.assertThrows(IllegalArgumentException.class, () -> media.pasMediaAan("https://www.google.be/", "", MediaType.LINK));
	}
	
	@Test
	public void pasMediaAan_FoutieveNaamEnAdress_WerptException() {
		Media media = new Media(testSessie, "https://www.google.be/", "Klik hier om naar google te gaan", huidigetijd, MediaType.LINK);
		Assertions.assertThrows(IllegalArgumentException.class, () -> media.pasMediaAan("", "", MediaType.LINK));
	}
}
