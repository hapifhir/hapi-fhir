package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import jakarta.annotation.Nonnull;

public class ReplaceReferencesQueryIdsStep implements IJobStepWorker<ReplaceReferencesJobParameters, VoidModel, ResourceIdListWorkChunkJson> {
	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReplaceReferencesJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) throws JobExecutionFailedException {
		return null;
	}
}
