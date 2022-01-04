package ca.uhn.fhir.jpa.bulk.export.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

/**
 * This class will prevent a job from running if the UUID does not exist or is invalid.
 */
public class BulkExportJobParameterValidator implements JobParametersValidator {

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException(Msg.code(793) + "This job needs Parameters: [readChunkSize], [jobUUID], [filters], [outputFormat], [resourceTypes]");
		}

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		String errorMessage = txTemplate.execute(tx -> {
			StringBuilder errorBuilder = new StringBuilder();
			Long readChunkSize = theJobParameters.getLong(BatchConstants.READ_CHUNK_PARAMETER);
			if (readChunkSize == null || readChunkSize < 1) {
				errorBuilder.append("There must be a valid number for readChunkSize, which is at least 1. ");
			}
			String jobUUID = theJobParameters.getString(BatchConstants.JOB_UUID_PARAMETER);
			Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(jobUUID);
			if (!StringUtils.isBlank(jobUUID) && !oJob.isPresent()) {
				errorBuilder.append("There is no persisted job that exists with UUID: " + jobUUID + ". ");
			}


			boolean hasExistingJob = oJob.isPresent();
			//Check for to-be-created parameters.
			if (!hasExistingJob) {
				String resourceTypes = theJobParameters.getString(BatchConstants.JOB_RESOURCE_TYPES_PARAMETER);
				if (StringUtils.isBlank(resourceTypes)) {
					errorBuilder.append("You must include [").append(BatchConstants.JOB_RESOURCE_TYPES_PARAMETER).append("] as a Job Parameter");
				} else {
					String[] resourceArray = resourceTypes.split(",");
					Arrays.stream(resourceArray).filter(resourceType -> resourceType.equalsIgnoreCase("Binary"))
						.findFirst()
						.ifPresent(resourceType -> errorBuilder.append("Bulk export of Binary resources is forbidden"));
				}

				String outputFormat = theJobParameters.getString("outputFormat");
				if (!StringUtils.isBlank(outputFormat) && !Constants.CT_FHIR_NDJSON.equals(outputFormat)) {
					errorBuilder.append("The only allowed format for Bulk Export is currently " + Constants.CT_FHIR_NDJSON);
				}
			}

			return errorBuilder.toString();
		});

		if (!StringUtils.isEmpty(errorMessage)) {
			throw new JobParametersInvalidException(Msg.code(794) + errorMessage);
		}
	}
}
