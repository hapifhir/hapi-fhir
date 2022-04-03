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
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Will run before and after a job to set the status to whatever is appropriate.
 */
public class BulkImportJobCloser implements Tasklet {

	@Value("#{jobParameters['" + BatchConstants.JOB_UUID_PARAMETER + "']}")
	private String myJobUUID;

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Override
	public RepeatStatus execute(StepContribution theStepContribution, ChunkContext theChunkContext) {
		BatchStatus executionStatus = theChunkContext.getStepContext().getStepExecution().getJobExecution().getStatus();
		if (executionStatus == BatchStatus.STARTED) {
			myBulkDataImportSvc.setJobToStatus(myJobUUID, BulkImportJobStatusEnum.COMPLETE);
			myBulkDataImportSvc.deleteJobFiles(myJobUUID);
		} else {
			myBulkDataImportSvc.setJobToStatus(myJobUUID, BulkImportJobStatusEnum.ERROR, "Found job in status: " + executionStatus);
			myBulkDataImportSvc.deleteJobFiles(myJobUUID);
		}
		return RepeatStatus.FINISHED;
	}
}
