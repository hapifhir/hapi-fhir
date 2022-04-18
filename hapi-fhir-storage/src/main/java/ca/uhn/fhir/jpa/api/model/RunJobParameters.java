package ca.uhn.fhir.jpa.api.model;

import javax.annotation.Nonnull;

public class RunJobParameters {

	/**
	 * The id of the jobdefinition that is to be executed
	 */
	private final String myJobDefinitionId;

	public RunJobParameters(@Nonnull String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}
}
