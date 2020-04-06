package repository;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.Gebruiker;

public class GebruikerDaoJpa extends GenericDaoJpa<Gebruiker> implements GebruikerDao {

	public GebruikerDaoJpa() {
		super(Gebruiker.class);
	}

	@Override
	public Gebruiker getByUsername(String username) {
		try {
            return em.createNamedQuery("Gebruiker.findByUsername", Gebruiker.class)
                 .setParameter("username", username)
                .getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException();
        } 
	}
}
