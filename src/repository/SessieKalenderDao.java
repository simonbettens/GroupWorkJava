package repository;

import domein.SessieKalender;

public interface SessieKalenderDao extends GenericDao<SessieKalender>{
	
	public SessieKalender getByBeginjaar(String beginjaar);
}
