package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;

public class Step1CreateExpansionWorkChunks implements IJobStepWorker<PreExpandValueSetParameters, VoidModel, ExpansionWorkChunkJson> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ExpansionWorkChunkJson> theDataSink) throws JobExecutionFailedException {
		return null;
	}

}
