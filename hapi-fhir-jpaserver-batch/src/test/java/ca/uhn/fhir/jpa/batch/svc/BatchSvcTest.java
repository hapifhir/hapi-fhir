package ca.uhn.fhir.jpa.batch.svc;

import ca.uhn.fhir.jpa.batch.BaseBatchR4Test;
import ca.uhn.fhir.jpa.batch.config.BatchJobConfig;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchSvcTest extends BaseBatchR4Test {
	@Autowired
	private BatchJobConfig myBatchJobConfig;

	@Test
	public void testApplicationContextLoads() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, InterruptedException {
		myBatchJobConfig.setExpectedCount(1);
		myJobLauncher.run(myJob, new JobParameters());
		myBatchJobConfig.awaitExpected();
	}

}
