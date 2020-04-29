package repository;

import domein.SessieAankondiging;

public class SessieAankondigingDaoJpa extends GenericDaoJpa<SessieAankondiging> implements SessieAankondigingDao{

	public SessieAankondigingDaoJpa() {
		super(SessieAankondiging.class);
	}

}
