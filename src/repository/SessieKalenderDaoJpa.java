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
	public SessieKalender getByBeginjaar(String beginjaar) throws EntityNotFoundException{
		try {
			return em.createNamedQuery("SessieKalender.getByBeginjaar", SessieKalender.class)
					.setParameter("beginjaar", beginjaar)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}
