package repository;

import domein.Gebruiker;

public interface GebruikerDao extends GenericDao<Gebruiker>{
	Gebruiker getByUsername(String username);
}
