package ca.uhn.fhir.jpa.batch.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

public interface IBatchJobSubmitter {

	JobExecution runJob(Job theJob, JobParameters theJobParameters) throws JobParametersInvalidException;
}
