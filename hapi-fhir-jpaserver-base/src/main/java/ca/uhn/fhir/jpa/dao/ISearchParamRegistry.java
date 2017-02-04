package ca.uhn.fhir.jpa.dao;

import java.util.Collection;
import java.util.Map;

import ca.uhn.fhir.context.RuntimeSearchParam;

public interface ISearchParamRegistry {

	Map<String,RuntimeSearchParam> getActiveSearchParams(String theResourceName);

	Collection<RuntimeSearchParam> getAllSearchParams(String theResourceName);

	void forceRefresh();

}
