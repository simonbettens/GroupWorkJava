package repository;

import domein.SessieKalender;

public class SessieKalenderDaoJpa extends GenericDaoJpa<SessieKalender> implements SessieKalenderDao{

	public SessieKalenderDaoJpa() {
		super(SessieKalender.class);
	}
}
