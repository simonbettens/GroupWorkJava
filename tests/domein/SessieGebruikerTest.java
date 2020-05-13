package domein;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SessieGebruikerTest {

    private static Stream<Arguments> aanwezigheidData(){
        return Stream.of(
                Arguments.of(true, true, "Aanwezig"),
                Arguments.of(false, false, "Niet aanwezig"),
                Arguments.of(true, false, "Niet aanwezig"),
                Arguments.of(false, true, "Aanwezig")
                );
        }
    
    @ParameterizedTest
    @MethodSource("aanwezigheidData")
    public void pasSessieGebruikerAan_Slaagt(boolean aanwezig, boolean aanwezigheidBevestigd, String status) {
        SessieGebruiker sg = new SessieGebruiker();
        sg.pasSessieGebruikerAan(aanwezig, aanwezigheidBevestigd);
        Assertions.assertEquals(status ,sg.getAanwezigProperty().getValue());
    }
}
