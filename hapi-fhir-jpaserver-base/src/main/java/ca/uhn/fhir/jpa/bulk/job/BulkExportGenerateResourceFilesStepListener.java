package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.jpa.bulk.model.BulkJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.svc.BulkExportDaoSvc;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class sets the job status to ERROR if any failures occur while actually
 * generating the export files.
 */
public class BulkExportGenerateResourceFilesStepListener implements StepExecutionListener {

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	@Override
	public void beforeStep(@Nonnull StepExecution stepExecution) {
		// nothing
	}

	@Override
	public ExitStatus afterStep(StepExecution theStepExecution) {
		if (theStepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
			String jobUuid = theStepExecution.getJobExecution().getJobParameters().getString("jobUUID");
			assert isNotBlank(jobUuid);
			String exitDescription = theStepExecution.getExitStatus().getExitDescription();
			myBulkExportDaoSvc.setJobToStatus(jobUuid, BulkJobStatusEnum.ERROR, exitDescription);
		}
		return theStepExecution.getExitStatus();
	}
}
