package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.jpa.bulk.model.BulkJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.svc.BulkExportDaoSvc;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Will run before and after a job to set the status to whatever is appropriate.
 */
public class BulkExportJobStatusChangeListener implements JobExecutionListener {

	@Value("#{jobParameters['jobUUID']}")
	private String myJobUUID;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	@Override
	public void beforeJob(JobExecution theJobExecution) {
		if (theJobExecution.getStatus() == BatchStatus.STARTING) {
			myBulkExportDaoSvc.setJobToStatus(myJobUUID, BulkJobStatusEnum.BUILDING);
		}

	}

	@Override
	public void afterJob(JobExecution theJobExecution) {
		if (theJobExecution.getStatus() == BatchStatus.COMPLETED) {
			myBulkExportDaoSvc.setJobToStatus(myJobUUID, BulkJobStatusEnum.COMPLETE);
		} else {
			myBulkExportDaoSvc.setJobToStatus(myJobUUID, BulkJobStatusEnum.ERROR);
		}
	}
}
