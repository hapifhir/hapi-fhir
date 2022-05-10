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

import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.jsr.RetryListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.ExhaustedRetryException;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class sets the job status to ERROR if any failures occur while actually
 * generating the export files.
 */
public class BulkImportStepListener implements StepExecutionListener, RetryListener {

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Override
	public void beforeStep(@Nonnull StepExecution stepExecution) {
		// nothing
	}

	@Override
	public ExitStatus afterStep(StepExecution theStepExecution) {
		if (theStepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
			//Try to fetch it from the parameters first, and if it doesn't exist, fetch it from the context.
			String jobUuid = theStepExecution.getJobExecution().getJobParameters().getString(BatchConstants.JOB_UUID_PARAMETER);
			if (jobUuid == null) {
				jobUuid = theStepExecution.getJobExecution().getExecutionContext().getString(BatchConstants.JOB_UUID_PARAMETER);
			}
			assert isNotBlank(jobUuid);

			StringBuilder message = new StringBuilder();
			message.append("Job: ").append(theStepExecution.getExecutionContext().getString(BulkImportPartitioner.JOB_DESCRIPTION)).append("\n");
			message.append("File: ").append(theStepExecution.getExecutionContext().getString(BulkImportPartitioner.FILE_DESCRIPTION)).append("\n");
			for (Throwable next : theStepExecution.getFailureExceptions()) {
				if (next instanceof ExhaustedRetryException) {
					next = next.getCause(); // ExhaustedRetryException is a spring exception that wraps the real one
				}
				String nextErrorMessage = next.toString();
				message.append("Error: ").append(nextErrorMessage).append("\n");
			}

			theStepExecution.addFailureException(new RuntimeException(message.toString()));

			myBulkDataImportSvc.setJobToStatus(jobUuid, BulkImportJobStatusEnum.ERROR, message.toString());

			ExitStatus exitStatus = ExitStatus.FAILED.addExitDescription(message.toString());
			theStepExecution.setExitStatus(exitStatus);

			// Replace the built-in error message with a better one
			return exitStatus;
		}

		return theStepExecution.getExitStatus();
	}
}
