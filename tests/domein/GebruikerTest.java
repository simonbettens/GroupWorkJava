package domein;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GebruikerTest {

	private static Stream<Arguments> ongeldigeGebruikers(){
		return Stream.of(
				//VOORNAAM
				Arguments.of("", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of(null, "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("p1eter", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdea", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//ACHTERNAAM
				Arguments.of("Pieter", "", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", null, "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carl1er", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdea", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//USERNAME
				Arguments.of("Pieter", "Carlier", "p123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pcelw123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc12345", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc1234567", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", null, "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "@_*123b@_56", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//EMAIL
				Arguments.of("Pieter", "Carlier", "pc123456", "koekjes", "",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "koekjes", null,
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "koekjes", "@@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//IDNUMBER
				Arguments.of("Pieter", "Carlier", "pc123456", "", "pieter.carlier@student.hogent.be",
						12345678912345L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", null, "pieter.carlier@student.hogent.be",
						123456789123L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "", "pieter.carlier@student.hogent.be",
						000000000000L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "", "pieter.carlier@student.hogent.be",
						Long.MIN_VALUE, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "", "pieter.carlier@student.hogent.be",
						Long.MAX_VALUE, GebruikerType.GEBRUIKER, StatusType.ACTIEF)
				);
	}
	
	private static Stream<Arguments> geldigeGebruikers(){
		return Stream.of(
				//VOORNAAM
				Arguments.of("P", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//ACHTERNAAM
				Arguments.of("Pieter", "C", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//USERNAME
				Arguments.of("Pieter", "Carlier", "pc123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pce123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//EMAIL
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "pietercarlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "lucas.van.der.haeghen@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				//IDNUMBER
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "pieter.carlier@student.hogent.be",
						9999999999999L, GebruikerType.GEBRUIKER, StatusType.ACTIEF),
				Arguments.of("Pieter", "Carlier", "pcef123456", "koekjes", "pieter.carlier@student.hogent.be",
						1234567891234L, GebruikerType.GEBRUIKER, StatusType.ACTIEF)
				);
	}
	
	@ParameterizedTest
	@MethodSource("geldigeGebruikers")
	public void maakGeldigeGebruikersAan_Slaagt(String voornaam, String achternaam, String userName, String passwoord, String email, Long idNummer,
			GebruikerType type, StatusType status) {
		Gebruiker gebruiker = new Gebruiker(voornaam, achternaam, userName, passwoord, email, idNummer, type, status);
		Assertions.assertEquals(voornaam, gebruiker.getVoornaam());
		Assertions.assertEquals(achternaam, gebruiker.getAchternaam());
		Assertions.assertEquals(userName, gebruiker.getUserName());
//		try {
//			Assertions.assertEquals(PasswoordHasher.generateStorngPasswordHash(passwoord), gebruiker.getPasswoordHash());
//		}
//		catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			e.printStackTrace();
//		}
		Assertions.assertEquals(email, gebruiker.getEmail());
		Assertions.assertEquals(idNummer, gebruiker.getIdNumber());
	}
	
	@ParameterizedTest
	@MethodSource("ongeldigeGebruikers")
	public void maakOngeldigeGebruikersAan_WerptException(String voornaam, String achternaam, String userName, String passwoord, String email, Long idNummer,
			GebruikerType type, StatusType status) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Gebruiker(voornaam, achternaam, userName, passwoord, email, idNummer, type, status));
	}
}
