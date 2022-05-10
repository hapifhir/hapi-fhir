package ca.uhn.fhir.jpa.batch.api;

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

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

public interface IBatchJobSubmitter {

	/**
	 * Given a {@link Job} and a {@link JobParameters}, execute the job with the given parameters.
	 *
	 * @param theJob           the job to run.
	 * @param theJobParameters A collection of key-value pairs that are used to parameterize the job.
	 * @return A {@link JobExecution} representing the job.
	 * @throws JobParametersInvalidException If validation on the parameters fails.
	 */
	JobExecution runJob(Job theJob, JobParameters theJobParameters) throws JobParametersInvalidException;
}
