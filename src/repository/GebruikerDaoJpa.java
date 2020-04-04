package repository;

import domein.Gebruiker;

public class GebruikerDaoJpa extends GenericDaoJpa<Gebruiker> implements GebruikerDao {

	public GebruikerDaoJpa(Class<Gebruiker> type) {
		super(Gebruiker.class);
	}
}
