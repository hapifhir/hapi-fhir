package ca.uhn.fhir.batch2.api;

public interface IJobStepWorker {

	/**
	 * Executes a step
	 *
	 * @param theStepExecutionDetails Contains details about the individual execution
	 * @param theDataSink             A data sink for data produced during this step. This may never
	 *                                be used during the final step of a job.
	 */
	void run(StepExecutionDetails theStepExecutionDetails, IJobDataSink theDataSink);

}
