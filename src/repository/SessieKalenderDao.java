package repository;

import java.util.List;

import domein.SessieKalender;

public interface SessieKalenderDao extends GenericDao<SessieKalender>{
	
	public SessieKalender getByBeginjaar(String beginjaar);
}
