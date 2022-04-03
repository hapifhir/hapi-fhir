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
import ca.uhn.fhir.jpa.bulk.export.job.CreateBulkExportEntityTasklet;
import ca.uhn.fhir.util.ValidateUtil;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map;

public class CreateBulkImportEntityTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution theStepContribution, ChunkContext theChunkContext) throws Exception {
		Map<String, Object> jobParameters = theChunkContext.getStepContext().getJobParameters();

		//We can leave early if they provided us with an existing job.
		ValidateUtil.isTrueOrThrowInvalidRequest(jobParameters.containsKey(BatchConstants.JOB_UUID_PARAMETER), "Job doesn't have a UUID");
		CreateBulkExportEntityTasklet.addUUIDToJobContext(theChunkContext, (String) jobParameters.get(BatchConstants.JOB_UUID_PARAMETER));
		return RepeatStatus.FINISHED;
	}

}
