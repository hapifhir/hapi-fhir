package ca.uhn.fhir.jpa.api.model;

import javax.annotation.Nonnull;

public class StartNewJobParameters {

	/**
	 * The id of the jobdefinition that is to be executed
	 */
	private final String myJobDefinitionId;

	public StartNewJobParameters(@Nonnull String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}
}
