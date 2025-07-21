package ca.uhn.fhir.batch2.jobs.bulkmodify;

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

public class GenerateReportStep implements IReductionStepWorker<BulkModifyJobParameters, ModifyResourcesChunkOutcomeJson, ModifyResourcesResultsJson> {

	@Nonnull
	@Override
	public  ChunkOutcome consume(ChunkExecutionDetails<BulkModifyJobParameters, ModifyResourcesChunkOutcomeJson> theChunkDetails) {
		return null;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkModifyJobParameters, ModifyResourcesChunkOutcomeJson> theStepExecutionDetails, @NotNull IJobDataSink<ModifyResourcesResultsJson> theDataSink) throws JobExecutionFailedException, ReductionStepFailureException {
		return null;
	}
}
