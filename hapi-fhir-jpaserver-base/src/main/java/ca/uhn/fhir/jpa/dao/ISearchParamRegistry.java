package ca.uhn.fhir.jpa.dao;

import java.util.Map;

import ca.uhn.fhir.context.RuntimeSearchParam;

public interface ISearchParamRegistry {

	void forceRefresh();

	Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams();

	Map<String,RuntimeSearchParam> getActiveSearchParams(String theResourceName);

}
