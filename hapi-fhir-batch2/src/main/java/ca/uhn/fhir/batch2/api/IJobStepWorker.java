package ca.uhn.fhir.batch2.api;

public interface IJobStepWorker {

	/**
	 * Executes a step
	 *
	 * @param theStepExecutionDetails Contains details about the individual execution
	 * @param theDataSink             A data sink for data produced during this step. This may never
	 *                                be used during the final step of a job.
	 * @throws JobExecutionFailedException This exception indicates an unrecoverable failure. If a
	 *                                     step worker throws this exception, processing for the
	 *                                     job will be aborted.
	 */
	RunOutcome run(StepExecutionDetails theStepExecutionDetails, IJobDataSink theDataSink) throws JobExecutionFailedException;

	/**
	 * Return type for {@link #run(StepExecutionDetails, IJobDataSink)}
	 */
	class RunOutcome {

		private final int myRecordsProcessed;

		public RunOutcome(int theRecordsProcessed) {
			myRecordsProcessed = theRecordsProcessed;
		}

		public int getRecordsProcessed() {
			return myRecordsProcessed;
		}
	}


}
