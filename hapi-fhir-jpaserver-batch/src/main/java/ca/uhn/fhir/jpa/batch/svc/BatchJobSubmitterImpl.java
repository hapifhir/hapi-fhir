package ca.uhn.fhir.jpa.batch.svc;

import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchJobSubmitterImpl implements IBatchJobSubmitter {

	@Autowired
	private JobLauncher myJobLauncher;

	@Override
	public JobExecution runJob(Job theJob, JobParameters theJobParameters) {
		try {
			System.out.println("WTF");
			return myJobLauncher.run(theJob, theJobParameters);
		} catch (JobExecutionAlreadyRunningException theE) {
			//FIXME properly handle these
			theE.printStackTrace();
		} catch (JobRestartException theE) {
			theE.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException theE) {
			theE.printStackTrace();
		} catch (JobParametersInvalidException theE) {
			theE.printStackTrace();
		}
		return null;

	}
}
