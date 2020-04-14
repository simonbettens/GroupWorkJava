package domein;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class SessieGebruikerId implements Serializable {
	@Column(name="GebruikerId",columnDefinition = "nvarchar")
	private String id;
	
	@Column(name="SessieId")
	private int sessieId;

	public SessieGebruikerId() {
	}

	public SessieGebruikerId(String gebruikerId, int sessieId) {
		this.id = gebruikerId;
		this.sessieId = sessieId;
	}

	public String getGebruikerId() {
		return id;
	}

	public int getSessieId() {
		return sessieId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + sessieId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessieGebruikerId other = (SessieGebruikerId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sessieId != other.sessieId)
			return false;
		return true;
	}
	
	
}
