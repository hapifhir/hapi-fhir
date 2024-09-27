package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import jakarta.annotation.Nonnull;

public class CheckPendingReindexWorkStep implements IJobStepWorker<ReindexJobParameters, ReindexResults, VoidModel> {

	private final ReindexJobService myReindexJobService;

	public CheckPendingReindexWorkStep(ReindexJobService theReindexJobService) {
		myReindexJobService = theReindexJobService;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReindexJobParameters, ReindexResults> theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {

		ReindexResults results = theStepExecutionDetails.getData();

		if (!results.getResourceToHasWorkToComplete().isEmpty()) {
			if (myReindexJobService.anyResourceHasPendingReindexWork(results.getResourceToHasWorkToComplete())) {
				/* CHECKSTYLE.OFF: HapiErrorCodeUniqueness
				 * This exception is never fed to users and is only part of our structure
				 * So there's no need to use an error code
				 */
				throw new RetryChunkLaterException(ReindexUtils.getRetryLaterDelay());
				/* CHECKSTYLE.ON: HapiErrorCodeUniqueness */
			}
		}

		return RunOutcome.SUCCESS;
	}
}
