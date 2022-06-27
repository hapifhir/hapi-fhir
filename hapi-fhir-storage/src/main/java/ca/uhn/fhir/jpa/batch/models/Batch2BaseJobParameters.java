package ca.uhn.fhir.jpa.batch.models;

import javax.annotation.Nonnull;

/**
 * Base parameters for StartJob as well as other requests
 */
public class Batch2BaseJobParameters {

	/**
	 * The id of the jobdefinition that is to be executed
	 */
	private final String myJobDefinitionId;

	/**
	 * If true, will search for existing jobs
	 * first and return any that have already completed or are inprogress/queued or cancelled first
	 */
	private boolean myUseExistingJobsFirst;

	public Batch2BaseJobParameters(@Nonnull String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public boolean isUseExistingJobsFirst() {
		return myUseExistingJobsFirst;
	}

	public void setUseExistingJobsFirst(boolean theUseExistingJobsFirst) {
		myUseExistingJobsFirst = theUseExistingJobsFirst;
	}
}
