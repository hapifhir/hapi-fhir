package ca.uhn.fhir.jpa.batch.svc;

import ca.uhn.fhir.jpa.batch.BaseBatchR4Test;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchSvcTest extends BaseBatchR4Test {
	@Autowired
	protected JobLauncher myJobLauncher;
	@Autowired
	protected Job myJob;

	@Test
	public void testApplicationContextLoads() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, InterruptedException {
		myJobLauncher.run(myJob, new JobParameters());
	}
}
