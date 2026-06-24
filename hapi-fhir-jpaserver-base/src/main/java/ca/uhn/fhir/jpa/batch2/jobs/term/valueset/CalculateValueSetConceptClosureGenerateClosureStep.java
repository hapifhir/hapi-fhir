package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.*;
import jakarta.annotation.Nonnull;

public class CalculateValueSetConceptClosureGenerateClosureStep implements IJobStepWorker<PreExpandValueSetParameters, GenerateClosurePidsChunkJson, ExpandValueSetStepOutcomeJson> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, GenerateClosurePidsChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<ExpandValueSetStepOutcomeJson> theDataSink) throws JobExecutionFailedException {
		return null;
	}
}
