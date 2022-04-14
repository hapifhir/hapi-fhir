package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;

import javax.annotation.Nonnull;

public class WriteBinaryStep implements IJobStepWorker<BulkExportJobParameters, BulkExportExpandedResources, VoidModel> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, BulkExportExpandedResources> theStepExecutionDetails,
								 @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
		// Write the binary
		// save the binary file name to the db (on the BulkExportJobEntity...
		return null;
	}
}
