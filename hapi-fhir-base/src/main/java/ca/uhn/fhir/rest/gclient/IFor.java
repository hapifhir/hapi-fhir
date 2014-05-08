package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.Bundle;

public interface IFor {

	IFor where(ICriterion theCriterion);
	
	IFor and(ICriterion theCriterion);
	
	Bundle execute();

	IFor include(Include theIncludeManagingorganization);

	IFor encodedJson();

	IFor encodedXml();

	ISort sort();

	IFor limitTo(int theLimitTo);

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This 
	 * can be useful for debugging, but is generally not desirable in a production situation.
	 */
	IFor andLogRequestAndResponse(boolean theLogRequestAndResponse);
	
}
