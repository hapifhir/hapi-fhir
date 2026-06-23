package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

public class IncludeOrExcludeConceptsStep<OT extends IModelJson> implements IJobStepWorker<PreExpandValueSetParameters, ExpansionWorkChunkJson, OT> {
	private final boolean myInclude;

	public IncludeOrExcludeConceptsStep(boolean theInclude) {
		myInclude = theInclude;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, ExpansionWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<OT> theDataSink) throws JobExecutionFailedException {
		return null;
	}
}
