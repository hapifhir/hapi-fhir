package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;

public class CalculateValueSetConceptClosureLoadPidsStep implements IJobStepWorker<PreExpandValueSetParameters, VoidModel, GenerateClosurePidsChunkJson> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<GenerateClosurePidsChunkJson> theDataSink) throws JobExecutionFailedException {
		return null;
	}
}
