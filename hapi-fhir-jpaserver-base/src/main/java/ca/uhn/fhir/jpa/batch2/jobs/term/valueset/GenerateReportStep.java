package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import jakarta.annotation.Nonnull;

public class GenerateReportStep implements IReductionStepWorker<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson, PreExpandValueSetResultJson> {
	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson> theChunkDetails) {
		return null;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson> theStepExecutionDetails, @Nonnull IJobDataSink<PreExpandValueSetResultJson> theDataSink) throws JobExecutionFailedException, ReductionStepFailureException {
		return null;
	}

	@Override
	public IReductionStepWorker<PreExpandValueSetParameters, ExpandValueSetStepOutcomeJson, PreExpandValueSetResultJson> newInstance() {
		return null;
	}
}
