package ca.uhn.fhir.jpa.api.model;

import javax.annotation.Nonnull;

public class StartNewJobParameters {

	/**
	 * The id of the jobdefinition that is to be executed
	 */
	private final String myJobDefinitionId;

	/**
	 * Whether or not to start the job immediately.
	 */
	private final boolean myStartJobImmediately;

	public StartNewJobParameters(@Nonnull String theJobDefinitionId) {
		this(theJobDefinitionId, true);
	}

	public StartNewJobParameters(@Nonnull String theJobDefinitionId,
										  boolean theStartJobImmediately) {
		myJobDefinitionId = theJobDefinitionId;
		myStartJobImmediately = theStartJobImmediately;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public boolean isStartJobImmediately() {
		return myStartJobImmediately;
	}
}
