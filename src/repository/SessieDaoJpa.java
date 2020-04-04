package repository;

import domein.Sessie;

public class SessieDaoJpa extends GenericDaoJpa<Sessie> implements SessieDao{

	public SessieDaoJpa() {
		super(Sessie.class);
	}
}
