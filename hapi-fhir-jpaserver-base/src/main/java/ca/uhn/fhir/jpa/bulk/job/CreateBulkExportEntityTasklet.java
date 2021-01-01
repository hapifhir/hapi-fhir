package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateBulkExportEntityTasklet implements Tasklet {

	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;

	@Override
	public RepeatStatus execute(StepContribution theStepContribution, ChunkContext theChunkContext) throws Exception {
		Map<String, Object> jobParameters = theChunkContext.getStepContext().getJobParameters();

		//We can leave early if they provided us with an existing job.
		if (jobParameters.containsKey("jobUUID")) {
			addUUIDToJobContext(theChunkContext, (String)jobParameters.get("jobUUID"));
			return RepeatStatus.FINISHED;
		} else {
			String resourceTypes = (String)jobParameters.get("resourceTypes");
			Date since = (Date)jobParameters.get("since");
			String filters = (String)jobParameters.get("filters");
			Set<String> filterSet;
			if (StringUtils.isBlank(filters)) {
				filterSet = null;
			} else {
				filterSet = Arrays.stream(filters.split(",")).collect(Collectors.toSet());
			}
			Set<String> resourceTypeSet = Arrays.stream(resourceTypes.split(",")).collect(Collectors.toSet());

			String outputFormat = (String)jobParameters.get("outputFormat");
			if (StringUtils.isBlank(outputFormat)) {
				outputFormat = Constants.CT_FHIR_NDJSON;
			}

			IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.submitJob(outputFormat, resourceTypeSet, since, filterSet);

			addUUIDToJobContext(theChunkContext, jobInfo.getJobId());
			return RepeatStatus.FINISHED;
		}
	}

	private void addUUIDToJobContext(ChunkContext theChunkContext, String theJobUUID) {
		theChunkContext
			.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext()
			.putString("jobUUID", theJobUUID);
	}
}
