package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;

public interface IJobStepWorker {

	void run(JobInstanceStartRequest theStartRequest, IJobDataSink theDataSink);

}
