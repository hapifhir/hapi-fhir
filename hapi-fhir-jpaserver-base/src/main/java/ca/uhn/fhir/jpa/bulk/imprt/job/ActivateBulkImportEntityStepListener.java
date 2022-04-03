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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Will run before and after a job to set the status to whatever is appropriate.
 */
public class ActivateBulkImportEntityStepListener implements StepExecutionListener {

	@Autowired
	private IBulkDataImportSvc myBulkImportDaoSvc;

	@Override
	public void beforeStep(StepExecution theStepExecution) {
		String jobUuid = theStepExecution.getJobExecution().getJobParameters().getString(BatchConstants.JOB_UUID_PARAMETER);
		if (jobUuid != null) {
			myBulkImportDaoSvc.setJobToStatus(jobUuid, BulkImportJobStatusEnum.RUNNING);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution theStepExecution) {
		return ExitStatus.EXECUTING;
	}
}
