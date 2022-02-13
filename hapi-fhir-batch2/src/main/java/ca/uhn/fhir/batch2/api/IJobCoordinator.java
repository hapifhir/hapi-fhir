package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IJobCoordinator {

	/**
	 * Starts a new job instance
	 *
	 * @param theStartRequest The request, containing the job type and parameters
	 * @return Returns a unique ID for this job execution
	 */
	String startJob(JobInstanceStartRequest theStartRequest);

	/**
	 * Fetch details about a job instance
	 *
	 * @param theInstanceId The instance ID
	 * @return Returns the current instance details
	 * @throws ResourceNotFoundException If the instance ID can not be found
	 */
	JobInstance getInstance(String theInstanceId) throws ResourceNotFoundException;
}
