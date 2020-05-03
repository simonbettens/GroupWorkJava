package repository;

import domein.Aankondiging;

public class AankondigingDaoJpa extends GenericDaoJpa<Aankondiging> implements AankondigingDao{

	public AankondigingDaoJpa() {
		super(Aankondiging.class);
	}

}
