package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.jpa.api.model.Batch2JobInfo;
import ca.uhn.fhir.jpa.api.model.StartNewJobParameters;

public interface IBatch2JobRunner {

	/**
	 * Start the job with the given parameters
	 * @param theParameters
	 * @return  returns the job id
	 */
	String startNewJob(StartNewJobParameters theParameters);

	/**
	 * Returns information about a provided job.
	 * @param theJobId - the job id
	 * @return - the batch2 job info
	 */
	Batch2JobInfo getJobInfo(String theJobId);
}
