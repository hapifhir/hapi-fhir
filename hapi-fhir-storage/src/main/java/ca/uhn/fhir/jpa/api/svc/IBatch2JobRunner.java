package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.jpa.api.model.Batch2JobInfo;
import ca.uhn.fhir.jpa.batch.models.Batch2BaseJobParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;

public interface IBatch2JobRunner {

	/**
	 * Start the job with the given parameters
	 * @param theParameters
	 * @return  returns the job id
	 */
	Batch2JobStartResponse startNewJob(Batch2BaseJobParameters theParameters);

	/**
	 * Returns information about a provided job.
	 * @param theJobId - the job id
	 * @return - the batch2 job info
	 */
	Batch2JobInfo getJobInfo(String theJobId);
}
