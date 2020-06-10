package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * This class will prevent a job from running if the UUID does not exist or is invalid.
 */
public class JobExistsParameterValidator implements JobParametersValidator {
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException("This job requires Parameters: [readChunkSize] and [jobUUID]");
		}

		Long readChunkSize = theJobParameters.getLong("readChunkSize");
		String errorMessage = "";
		if (readChunkSize == null || readChunkSize < 1) {
			errorMessage += "There must be a valid number for readChunkSize, which is at least 1. ";
		}
		String jobUUID = theJobParameters.getString("jobUUID");
		if (StringUtils.isBlank(jobUUID)) {
			errorMessage += "Missing jobUUID Job parameter. ";
		}

		Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(jobUUID);
		if (!oJob.isPresent()) {
			errorMessage += "There is no persisted job that exists with UUID: " + jobUUID + ". ";
		}
		if (!StringUtils.isEmpty(errorMessage)) {
			throw new JobParametersInvalidException(errorMessage);
		}
	}
}
