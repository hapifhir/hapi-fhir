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
import org.slf4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Will run before and after a job to set the status to whatever is appropriate.
 */
public class BulkExportJobCloser implements Tasklet {
	private static final Logger ourLog = getLogger(BulkExportJobCloser.class);


	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	@Override
	public RepeatStatus execute(StepContribution theStepContribution, ChunkContext theChunkContext) throws Exception {
		if (theChunkContext.getStepContext().getStepExecution().getJobExecution().getStatus() == BatchStatus.STARTED) {
			ourLog.warn("Job Closer is setting job to COMPLETE!");
			myBulkExportDaoSvc.setJobToStatus(myJobUUID, BulkJobStatusEnum.COMPLETE);
		} else {
			ourLog.error("Job Closer is setting job to ERROR!");
			myBulkExportDaoSvc.setJobToStatus(myJobUUID, BulkJobStatusEnum.ERROR);
		}
		return RepeatStatus.FINISHED;
	}
}
