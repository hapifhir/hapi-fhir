package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static org.slf4j.LoggerFactory.getLogger;

public class FinalBatchExportCallback implements IJobCompletionHandler<BulkExportJobParameters> {
	private static final Logger ourLog = getLogger(FinalBatchExportCallback.class);

	@Autowired
	private IBulkExportProcessor myBulkIdProcessor;

	@Override
	public void jobComplete(JobCompletionDetails<BulkExportJobParameters> theDetails) {
		BulkExportJobParameters params = theDetails.getParameters();
		String jobId = params.getJobId();

		ourLog.info(jobId + " has completed");

		BulkExportJobStatusEnum status = myBulkIdProcessor.getJobStatus(jobId);

		// we don't want to set it if it's in ERROR
		if (status != BulkExportJobStatusEnum.ERROR) {
			myBulkIdProcessor.setJobStatus(jobId, BulkExportJobStatusEnum.COMPLETE, null);
		}
	}
}
