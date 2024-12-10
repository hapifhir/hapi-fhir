package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;

public class ReplaceReferenceUpdateTaskStep
		implements IJobStepWorker<ReplaceReferencesJobParameters, ReplaceReferenceResults, VoidModel> {
	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<ReplaceReferencesJobParameters, ReplaceReferenceResults>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {
		// FIXME KHS
		RunOutcome retval = new RunOutcome(0);
		return retval;
	}
}
