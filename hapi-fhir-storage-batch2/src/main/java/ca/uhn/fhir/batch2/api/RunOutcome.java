package ca.uhn.fhir.batch2.api;

/**
 * Return type for {@link IJobStepWorker#run(StepExecutionDetails, IJobDataSink)}
 */
public class RunOutcome {

	/**
	 * RunOutcome with 0 records processed
	 */
	public static final RunOutcome SUCCESS = new RunOutcome(0);

	private final int myRecordsProcessed;

	/**
	 * Constructor
	 *
	 * @param theRecordsProcessed The number of records processed by this step. This number is not used for anything
	 *                            other than calculating the total number of records processed by the job. Therefore in many
	 *                            cases it will make sense to return a count of 0 for all steps except for the final
	 *                            step. For example, if you have a step that fetches files and a step that saves them,
	 *                            you might choose to only return a non-zero count indicating the number of saved files
	 *                            (or even the number of records within those files) so that the ultimate total
	 *                            reflects the real total.
	 */
	public RunOutcome(int theRecordsProcessed) {
		myRecordsProcessed = theRecordsProcessed;
	}

	public int getRecordsProcessed() {
		return myRecordsProcessed;
	}
}
