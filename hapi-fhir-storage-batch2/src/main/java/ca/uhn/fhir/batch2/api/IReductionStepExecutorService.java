package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobWorkCursor;

public interface IReductionStepExecutorService {
	void triggerReductionStep(String theInstanceId, JobWorkCursor<?, ?, ?> theJobWorkCursor);

	void reducerPass();
}
