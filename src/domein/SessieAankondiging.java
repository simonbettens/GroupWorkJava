package domein;

import java.time.LocalDateTime;

public class SessieAankondiging extends Aankondiging {

	private Sessie sessie;
	private int sessieId;
	//voor jpa
	public SessieAankondiging() {
		super();
	}
	//voor instanties
	public SessieAankondiging(Verantwoordelijke verantwoordelijke, LocalDateTime gepost,String inhoud, AankondigingPrioriteit prioriteit,Sessie sessie) {
		super(verantwoordelijke, gepost, inhoud, prioriteit);
		setSessie(sessie);
		this.sessieId = sessie.getSessieId();
	}
	public Sessie getSessie() {
		return sessie;
	}
	private void setSessie(Sessie sessie) {
		this.sessie = sessie;
	}
	public int getSessieId() {
		return sessieId;
	}
	

}