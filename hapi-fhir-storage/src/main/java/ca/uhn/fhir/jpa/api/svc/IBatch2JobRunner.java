package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.jpa.api.model.RunJobParameters;

public interface IBatch2JobRunner {

	/**
	 * Start the job with the given parameters
	 * @param theParameters
	 */
	void startJob(RunJobParameters theParameters);
}
