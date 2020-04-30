package domein;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
@Entity
@DiscriminatorValue(value = "SessieAankondiging")
public class SessieAankondiging extends Aankondiging {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne(optional = false)
	@JoinColumn(name = "SessieId", referencedColumnName = "SessieId")
	
	private Sessie sessie;
	//voor jpa
	
	@PostLoad
    private void fillTransientPriorieteit(){
        if (this.getPrioriteitValue() > 0) {
            this.prioriteit = AankondigingPrioriteit.of(this.getPrioriteitValue());
        }
    }
 
    @PrePersist
    private void fillPersistentPriorieteit() {
        if (prioriteit != null) {
            this.setPrioriteitValue(prioriteit.getPriority());
        }
    }
	
	public SessieAankondiging() {
		super();
	}
	//voor instanties
	public SessieAankondiging(Gebruiker verantwoordelijke, LocalDateTime gepost,String inhoud, AankondigingPrioriteit prioriteit,Sessie sessie) {
		super(verantwoordelijke, gepost, inhoud, prioriteit);
		setSessie(sessie);
		
	}
	public Sessie getSessie() {
		return sessie;
	}
	private void setSessie(Sessie sessie) {
		this.sessie = sessie;
	}
	
	public int pasSessieAankondigingAan(String inhoud, AankondigingPrioriteit prioriteit) {
		int verandering = 0;
		if(inhoud != getInhoud()) {
			verandering++;
			setInhoud(inhoud);
		}
		if(prioriteit != this.prioriteit) {
			verandering++;
			setPrioriteit(prioriteit);
		}
		
		return verandering;
	}
	
	@Override
	public String toString() {
		return "Aankondiging met als id :" + super.getAankondigingId() + ", gepost op :" + super.getGepost() 
				+ ", bevat deze inhoud :" + super.getInhoud()+", met prioriteit=" + super.getPrioriteit() + " en is van het type sessieaankondiging en hoort"
				+" bij" + sessie.getSessieId();
	}
	

}