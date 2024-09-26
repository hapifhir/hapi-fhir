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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ReindexJobStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckPendingReindexWorkStep implements IJobStepWorker<ReindexJobParameters, ReindexResults, VoidModel> {

	@Autowired
	private ReindexJobService myReindexJobService;

	@Nonnull
	@Override
	public RunOutcome run(
		@Nonnull StepExecutionDetails<ReindexJobParameters, ReindexResults> theStepExecutionDetails,
		@Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

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
