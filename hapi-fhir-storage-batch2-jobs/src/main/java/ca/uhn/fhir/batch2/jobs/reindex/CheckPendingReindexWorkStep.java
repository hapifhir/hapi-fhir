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
				// give time for reindex work to complete
				throw new RetryChunkLaterException(ReindexUtils.getRetryLaterDelay());
			}
		}

		return RunOutcome.SUCCESS;
	}
}
