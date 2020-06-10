package ca.uhn.fhir.jpa.batch.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

public interface IBatchJobSubmitter {

	/**
	 * Given a {@link Job} and a {@link JobParameters}, execute the job with the given parameters.
	 *
	 * @param theJob the job to run.
	 * @param theJobParameters A collection of key-value pairs that are used to parameterize the job.
	 * @return A {@link JobExecution} representing the job.
	 * @throws JobParametersInvalidException If validation on the parameters fails.
	 */
	JobExecution runJob(Job theJob, JobParameters theJobParameters) throws JobParametersInvalidException;
}
