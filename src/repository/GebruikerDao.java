package repository;

import java.util.List;

import domein.Gebruiker;

public interface GebruikerDao extends GenericDao<Gebruiker>{
	Gebruiker getByUsername(String username);
	List<Gebruiker> getByDiscriminator(String discriminator);
}
