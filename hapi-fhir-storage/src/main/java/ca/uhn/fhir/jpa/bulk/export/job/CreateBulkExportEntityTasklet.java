package ca.uhn.fhir.jpa.bulk.export.job;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
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

	@Autowired private IBulkDataExportSvc myBulkDataExportSvc;

	public static void addUUIDToJobContext(ChunkContext theChunkContext, String theJobUUID) {
		theChunkContext
			.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext()
			.putString(BatchConstants.JOB_UUID_PARAMETER, theJobUUID);
	}

	@Override
	public RepeatStatus execute(StepContribution theStepContribution, ChunkContext theChunkContext) throws Exception {
		Map<String, Object> jobParameters = theChunkContext.getStepContext().getJobParameters();

		//We can leave early if they provided us with an existing job.
		if (jobParameters.containsKey(BatchConstants.JOB_UUID_PARAMETER)) {
			addUUIDToJobContext(theChunkContext, (String) jobParameters.get(BatchConstants.JOB_UUID_PARAMETER));
			return RepeatStatus.FINISHED;
		} else {
			String resourceTypes = (String) jobParameters.get("resourceTypes");
			Date since = (Date) jobParameters.get("since");
			String filters = (String) jobParameters.get("filters");
			Set<String> filterSet;
			if (StringUtils.isBlank(filters)) {
				filterSet = null;
			} else {
				filterSet = Arrays.stream(filters.split(",")).collect(Collectors.toSet());
			}
			Set<String> resourceTypeSet = Arrays.stream(resourceTypes.split(",")).collect(Collectors.toSet());

			String outputFormat = (String) jobParameters.get("outputFormat");
			if (StringUtils.isBlank(outputFormat)) {
				outputFormat = Constants.CT_FHIR_NDJSON;
			}

			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setOutputFormat(outputFormat);
			bulkDataExportOptions.setResourceTypes(resourceTypeSet);
			bulkDataExportOptions.setSince(since);
			bulkDataExportOptions.setFilters(filterSet);

			//Set export style
			String exportStyle = (String)jobParameters.get("exportStyle");
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.valueOf(exportStyle));

			//Set group id if present
			String groupId = (String)jobParameters.get("groupId");
			bulkDataExportOptions.setGroupId(new IdDt(groupId));

			IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

			addUUIDToJobContext(theChunkContext, jobInfo.getJobId());
			return RepeatStatus.FINISHED;
		}
	}
}
