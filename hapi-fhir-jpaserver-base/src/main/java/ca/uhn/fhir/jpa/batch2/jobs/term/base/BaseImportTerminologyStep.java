/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.core.JacksonException;

import java.util.concurrent.Callable;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;

public abstract class BaseImportTerminologyStep {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportTerminologyStep.class);

	@Autowired
	protected IJobPersistence myJobPersistence;

	@Autowired
	protected IHapiTransactionService myTransactionService;

	/**
	 * Constructor
	 */
	public BaseImportTerminologyStep() {
		super();
	}

	/**
	 * Constructor
	 */
	public BaseImportTerminologyStep(@Nonnull IJobPersistence theJobPersistence) {
		Validate.notNull(theJobPersistence, "theJobPersistence must not be null");
		myJobPersistence = theJobPersistence;
	}

	protected ImportTerminologyMetadataAttachmentJson getJobMetadata(String theJobInstanceId) {
		return getJobMetadata(theJobInstanceId, myJobPersistence);
	}

	protected static ImportTerminologyMetadataAttachmentJson getJobMetadata(
			String theJobInstanceId, IJobPersistence theJobPersistence) {
		AttachmentDetails jobMetadataAttachment = theJobPersistence.fetchAttachmentByFilename(
				theJobInstanceId, ImportTerminologyMetadataAttachmentJson.ATTACHMENT_FILENAME);
		ImportTerminologyMetadataAttachmentJson jobMetadata;
		try {
			jobMetadata = JsonUtil.deserialize(
					jobMetadataAttachment.getInputStream(), ImportTerminologyMetadataAttachmentJson.class);
		} catch (JacksonException e) {
			throw new JobExecutionFailedException(Msg.code(2940) + "Failed to retrieve job metadata attachment: " + e);
		}
		return jobMetadata;
	}

	protected <T> T executeInNewTransactionWithRetry(
			Callable<T> theFunction, StepExecutionDetails<?, ?> theStepExecutionDetails) {
		int retryCount = 0;
		while (true) {
			try {
				return myTransactionService
						.withSystemRequestOnDefaultPartition()
						.execute(theFunction);
			} catch (ResourceVersionConflictException e) {
				retryCount++;
				int maxRetries = 10;
				if (retryCount > maxRetries) {
					ourLog.atError()
							.setMessage("Failed to saver terminology due to version conflict after {} retries: {}")
							.addArgument(retryCount)
							.addArgument(e.getMessage())
							.log();
					throw e;
				}
				ourLog.atWarn()
						.setMessage("Failed to save terminology for step {}, retry {}/{} in 5 seconds: {}")
						.addArgument(theStepExecutionDetails.getCurrentStepId())
						.addArgument(retryCount)
						.addArgument(maxRetries)
						.addArgument(e.getMessage())
						.log();

				long sleepTime = 5 * DateUtils.MILLIS_PER_SECOND;
				if (HapiSystemProperties.isUnitTestModeEnabled()) {
					sleepTime = 10;
				}

				sleepAtLeast(sleepTime);
			}
		}
	}
}
