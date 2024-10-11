package ca.uhn.fhir.batch2.jobs.reindex.v2;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils;
import ca.uhn.fhir.batch2.jobs.reindex.models.ReindexResults;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.i18n.Msg;
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
				throw new RetryChunkLaterException(Msg.code(2553), ReindexUtils.getRetryLaterDelay());
			}
		}

		return RunOutcome.SUCCESS;
	}
}
