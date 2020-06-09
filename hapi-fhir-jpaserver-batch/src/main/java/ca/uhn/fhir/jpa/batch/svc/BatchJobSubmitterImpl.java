package ca.uhn.fhir.jpa.batch.svc;

import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.slf4j.LoggerFactory.getLogger;

public class BatchJobSubmitterImpl implements IBatchJobSubmitter {

	private static final Logger ourLog = getLogger(BatchJobSubmitterImpl.class);

	@Autowired
	private JobLauncher myJobLauncher;

	@Autowired
	private JobRepository myJobRepository;

	@Override
	public JobExecution runJob(Job theJob, JobParameters theJobParameters) throws JobParametersInvalidException{
		try {
			return myJobLauncher.run(theJob, theJobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException theE) {
			ourLog.warn("Job {} was already running, ignoring the call to start.", theJob.getName());
			return myJobRepository.getLastJobExecution(theJob.getName(), theJobParameters);
		} catch (JobParametersInvalidException theE) {
			ourLog.error("Job Parameters passed to this job were invalid: {}", theE.getMessage());
			throw theE;
		}
	}
}
