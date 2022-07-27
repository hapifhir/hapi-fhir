package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static org.slf4j.LoggerFactory.getLogger;

public class FetchPartitionedFilesStep implements IFirstJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult> {
	private static final Logger ourLog = getLogger(FetchPartitionedFilesStep.class);

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<Batch2BulkImportPullJobParameters, VoidModel> theStepExecutionDetails,
		@NotNull IJobDataSink<BulkImportFilePartitionResult> theDataSink
	) throws JobExecutionFailedException {
		String jobId = theStepExecutionDetails.getParameters().getJobId();

		ourLog.info("Start FetchPartitionedFilesStep for jobID {} ", jobId);

		BulkImportJobJson job = myBulkDataImportSvc.fetchJob(jobId);

		for (int i = 0; i < job.getFileCount(); i++) {
			String fileDescription = myBulkDataImportSvc.getFileDescription(jobId, i);

			BulkImportFilePartitionResult result = new BulkImportFilePartitionResult();
			result.setFileIndex(i);
			result.setProcessingMode(job.getProcessingMode());
			result.setFileDescription(fileDescription);
			result.setJobDescription(job.getJobDescription());

			theDataSink.accept(result);
		}

		ourLog.info("FetchPartitionedFilesStep complete for jobID {}", jobId);

		return RunOutcome.SUCCESS;
	}
}
