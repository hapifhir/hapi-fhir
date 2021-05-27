package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeleteExpungeJobTest extends BaseJpaR4Test {
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.DELETE_EXPUNGE_JOB_NAME)
	private Job myDeleteExpungeJob;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Test
	public void testDeleteExpunge() throws Exception {
		// setup
		JobParameters jobParameters = DeleteExpungeParamUtil.buildJobParameters("Patient?address=memory", "Patient?name=smith");
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myDeleteExpungeJob, jobParameters);

		// execute
		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// validate
		// FIXME KHS
	}
}
