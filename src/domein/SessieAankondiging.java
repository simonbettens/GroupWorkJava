package domein;

import java.time.LocalDateTime;

public class SessieAankondiging extends Aankondiging {

	private Sessie sessie;
	private int sessieId;
	public SessieAankondiging(Verantwoordelijke verantwoordelijke, int aankondigingId, LocalDateTime gepost,
			String inhoud, AankondigingPrioriteit prioriteit) {
		super(verantwoordelijke, aankondigingId, gepost, inhoud, prioriteit);
		// TODO Auto-generated constructor stub
	}

}