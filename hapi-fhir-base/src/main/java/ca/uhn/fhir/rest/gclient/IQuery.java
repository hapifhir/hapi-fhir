package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.Bundle;

public interface IQuery {

	IQuery where(ICriterion theCriterion);
	
	IQuery and(ICriterion theCriterion);
	
	Bundle execute();

	IQuery include(Include theIncludeManagingorganization);

	IQuery encodedJson();

	IQuery encodedXml();

	ISort sort();

	IQuery limitTo(int theLimitTo);

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This 
	 * can be useful for debugging, but is generally not desirable in a production situation.
	 */
	IQuery andLogRequestAndResponse(boolean theLogRequestAndResponse);
	
}
