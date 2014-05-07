package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.Bundle;

public interface IFor {

	IFor where(ICriterion theCriterion);
	
	IFor and(ICriterion theCriterion);
	
	Bundle execute();

	IFor include(Include theIncludeManagingorganization);

	IFor json();
	
}
