package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;

import javax.annotation.Nonnull;

public class FetchResourceIdsStep implements IFirstJobStepWorker<BulkExportJobParameters, BulkExportIdList> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
								 @Nonnull IJobDataSink<BulkExportIdList> theDataSink) throws JobExecutionFailedException {

		// steps
		// get the pidlist from the reader
		// and create chunks that are each 1000 pids/resource types
		// and submit those
		// comment - this is going to be a 'slow' step (and we can refactor
		// that later)

		// iterate through the reader PIDs so
		// as to add them to (invent object here)
		// that are then added using accept to the data sync
		return null;
	}
}
