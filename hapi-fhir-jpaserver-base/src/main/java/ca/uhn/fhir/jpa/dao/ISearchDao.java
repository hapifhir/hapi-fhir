package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.rest.server.IBundleProvider;

public interface ISearchDao {

	public static final String FULL_TEXT_PARAM_NAME = "fullTextSearch";
	
	IBundleProvider search(SearchParameterMap theParams);
	
}
