package ca.uhn.fhir.jpa.dao;

import java.util.List;

public interface ISearchDao {

	List<Long> search(String theResourceName, SearchParameterMap theParams);
	
}
