package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;

import javax.annotation.Nonnull;

public class ExpandResourcesStep implements IJobStepWorker<BulkExportJobParameters, BulkExportIdList, BulkExportExpandedResources> {
	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, BulkExportIdList> theStepExecutionDetails,
								 @Nonnull IJobDataSink<BulkExportExpandedResources> theDataSink) throws JobExecutionFailedException {
		// take each PID
		// fetch the resource
		// add exploded out resource to list
		return null;
	}
}
