package ca.uhn.fhir.jpa.bulk.imprt.job;

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
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

/**
 * This class will prevent a job from running if the UUID does not exist or is invalid.
 */
public class BulkImportJobParameterValidator implements JobParametersValidator {

	@Autowired
	private IBulkImportJobDao myBulkImportJobDao;
	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {
		if (theJobParameters == null) {
			throw new JobParametersInvalidException(Msg.code(784) + "This job needs Parameters: [jobUUID]");
		}

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		String errorMessage = txTemplate.execute(tx -> {
			StringBuilder errorBuilder = new StringBuilder();
			String jobUUID = theJobParameters.getString(BatchConstants.JOB_UUID_PARAMETER);
			Optional<BulkImportJobEntity> oJob = myBulkImportJobDao.findByJobId(jobUUID);
			if (!StringUtils.isBlank(jobUUID) && !oJob.isPresent()) {
				errorBuilder.append("There is no persisted job that exists with UUID: ");
				errorBuilder.append(jobUUID);
				errorBuilder.append(". ");
			}

			return errorBuilder.toString();
		});

		if (!StringUtils.isEmpty(errorMessage)) {
			throw new JobParametersInvalidException(Msg.code(785) + errorMessage);
		}
	}
}
