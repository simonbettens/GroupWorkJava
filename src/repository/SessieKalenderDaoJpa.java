package repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.SessieKalender;


public class SessieKalenderDaoJpa extends GenericDaoJpa<SessieKalender> implements SessieKalenderDao{

	 private List<SessieKalender> sessieKalenders = new ArrayList<>();
	 private SessieKalender sessieKalender;
	public SessieKalenderDaoJpa() {
		super(SessieKalender.class);
		this.sessieKalender = new SessieKalender();
	}


	@Override
	public SessieKalender getByAcademieJaar(String academiejaar) throws EntityNotFoundException{
		try {
			return em.createNamedQuery("SessieKalender.getByBeginJaar", SessieKalender.class)
					.setParameter("academiejaar", academiejaar).getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}
