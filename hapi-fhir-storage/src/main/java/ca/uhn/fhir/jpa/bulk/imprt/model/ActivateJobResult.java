package ca.uhn.fhir.jpa.bulk.imprt.model;

import javax.annotation.Nullable;

public class ActivateJobResult {

	/**
	 * Whether the job is activated or not
	 */
	public final boolean isActivated;

	/**
	 * The newly created jobid
	 */
	@Nullable
	public final String jobId;

	public ActivateJobResult(boolean theIsActivated,
									 @Nullable String theJobId) {
		isActivated = theIsActivated;
		jobId = theJobId;
	}
}
