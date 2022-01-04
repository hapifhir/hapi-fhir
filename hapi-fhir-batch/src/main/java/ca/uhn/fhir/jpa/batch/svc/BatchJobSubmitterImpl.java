package ca.uhn.fhir.jpa.batch.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

public class BatchJobSubmitterImpl implements IBatchJobSubmitter {

	private static final Logger ourLog = getLogger(BatchJobSubmitterImpl.class);

	@Autowired
	private JobLauncher myJobLauncher;

	@Autowired
	private JobRepository myJobRepository;

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public JobExecution runJob(Job theJob, JobParameters theJobParameters) throws JobParametersInvalidException {
		try {
			return myJobLauncher.run(theJob, theJobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
			ourLog.warn("Job {} was already running, ignoring the call to start: {}", theJob.getName(), e.toString());
			return myJobRepository.getLastJobExecution(theJob.getName(), theJobParameters);
		} catch (JobParametersInvalidException e) {
			ourLog.error("Job Parameters passed to this job were invalid: {}", e.getMessage());
			throw e;
		}
	}
}
