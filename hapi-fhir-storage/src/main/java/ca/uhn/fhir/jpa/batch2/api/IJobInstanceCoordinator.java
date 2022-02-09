package ca.uhn.fhir.jpa.batch2.api;

import ca.uhn.fhir.jpa.batch2.model.JobInstanceStartRequest;

public interface IJobInstanceCoordinator {


	void startJob(JobInstanceStartRequest theStartRequest);
}
