package ca.uhn.fhir.batch2.jobs.bulkmodify.base;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

public class BulkModifyGenerateReportStep<T extends BaseBulkModifyJobParameters> implements IReductionStepWorker<T, BulkModifyResourcesChunkOutcomeJson, BulkModifyResourcesResultsJson> {

	@Nonnull
	@Override
	public  ChunkOutcome consume(ChunkExecutionDetails<T, BulkModifyResourcesChunkOutcomeJson> theChunkDetails) {
		return null;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<T, BulkModifyResourcesChunkOutcomeJson> theStepExecutionDetails, @NotNull IJobDataSink<BulkModifyResourcesResultsJson> theDataSink) throws JobExecutionFailedException, ReductionStepFailureException {
		return null;
	}
}
