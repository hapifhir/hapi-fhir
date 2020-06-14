package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.context.RuntimeSearchParam;

public interface ISearchParamRetriever {
	/**
	 * @return Returns {@literal null} if no match
	 */
	RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName);
}
