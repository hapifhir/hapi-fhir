package ca.uhn.fhir.jpa.batch.svc;

import ca.uhn.fhir.jpa.batch.BaseBatchR4Test;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public class BatchSvcTest extends BaseBatchR4Test {

	@Test
	public void testApplicationContextLoads() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, InterruptedException {
		myJobLauncher.run(myJob, new JobParameters());
	}

}
