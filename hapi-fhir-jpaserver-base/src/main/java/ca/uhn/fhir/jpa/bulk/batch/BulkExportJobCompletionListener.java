package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.jpa.bulk.BulkJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

public class BulkExportJobCompletionListener implements JobExecutionListener {
	@Value("#{jobParameters['jobUUID']}")
	private String myJobUUID;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Override
	public void beforeJob(JobExecution theJobExecution) {

	}

	@Override
	public void afterJob(JobExecution theJobExecution) {
		if (theJobExecution.getStatus() == BatchStatus.COMPLETED) {
			Optional<BulkExportJobEntity> byJobId = myBulkExportJobDao.findByJobId(myJobUUID);
			if (byJobId.isPresent()) {
				BulkExportJobEntity bulkExportJobEntity = byJobId.get();
				bulkExportJobEntity.setStatus(BulkJobStatusEnum.COMPLETE);
				myBulkExportJobDao.save(bulkExportJobEntity);
			}

		}

	}
}
