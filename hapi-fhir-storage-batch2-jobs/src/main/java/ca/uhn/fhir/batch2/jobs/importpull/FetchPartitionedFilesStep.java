package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.batch2.importpull.svc.IBulkImportPullSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class FetchPartitionedFilesStep implements IFirstJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult> {

	@Autowired
	private IBulkImportPullSvc myBulkImportPullSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<Batch2BulkImportPullJobParameters, VoidModel> theStepExecutionDetails,
		@NotNull IJobDataSink<BulkImportFilePartitionResult> theDataSink
	) throws JobExecutionFailedException {
		String jobId = theStepExecutionDetails.getParameters().getJobId();

		BulkImportJobJson job = myBulkImportPullSvc.fetchJobById(jobId);

		for (int i = 0; i < job.getFileCount(); i++) {
			String fileDescription = myBulkImportPullSvc.fetchJobDescription(jobId, i);

			BulkImportFilePartitionResult result = new BulkImportFilePartitionResult();
			result.setFileIndex(i);
			result.setProcessingMode(job.getProcessingMode());
			result.setFileDescription(fileDescription);
			result.setJobDescription(job.getJobDescription());

			theDataSink.accept(result);
		}

		return RunOutcome.SUCCESS;
	}
}
