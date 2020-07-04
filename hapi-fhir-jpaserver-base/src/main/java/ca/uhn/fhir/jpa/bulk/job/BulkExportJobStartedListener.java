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

import ca.uhn.fhir.jpa.bulk.model.BulkJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.svc.BulkExportDaoSvc;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Will run before and after a job to set the status to whatever is appropriate.
 */
public class BulkExportJobStartedListener implements StepExecutionListener {

	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	@Override
	public void beforeStep(StepExecution theStepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution theStepExecution) {
		if (theStepExecution.getStatus() == BatchStatus.STARTING) {
			myBulkExportDaoSvc.setJobToStatus(myJobUUID, BulkJobStatusEnum.BUILDING);
		}
		return ExitStatus.EXECUTING;
	}
}
