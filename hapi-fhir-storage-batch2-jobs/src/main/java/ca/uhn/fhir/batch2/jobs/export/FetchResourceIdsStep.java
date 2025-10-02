/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.svc.BulkExportIdFetchingSvc;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FetchResourceIdsStep implements IFirstJobStepWorker<BulkExportJobParameters, ResourceIdList> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchResourceIdsStep.class);

	@Autowired
	private BulkExportIdFetchingSvc myBulkExportProcessor;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<ResourceIdList> theDataSink)
			throws JobExecutionFailedException {
		BulkExportJobParameters params = theStepExecutionDetails.getParameters();
		ourLog.info(
				"Fetching resource IDs for bulk export job instance[{}]",
				theStepExecutionDetails.getInstance().getInstanceId());

		ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters();
		providerParams.setInstanceId(theStepExecutionDetails.getInstance().getInstanceId());
		providerParams.setChunkId(theStepExecutionDetails.getChunkId());
		providerParams.setFilters(params.getFilters());
		providerParams.setStartDate(params.getSince());
		providerParams.setEndDate(params.getUntil());
		providerParams.setExportStyle(params.getExportStyle());
		providerParams.setGroupId(params.getGroupId());
		providerParams.setPatientIds(params.getPatientIds());
		providerParams.setExpandMdm(params.isExpandMdm());
		providerParams.setPartitionId(params.getPartitionId());
		// This step doesn't use this param. Included here for logging purpose
		providerParams.setIncludeHistory(params.isIncludeHistory());

		/*
		 * we set all the requested resource types here so that
		 * when we recursively fetch resource types for a given patient/group
		 * we don't recurse for types that they did not request
		 */
		providerParams.setRequestedResourceTypes(params.getResourceTypes());

		int submissionCount = 0;
		try {
			submissionCount = myBulkExportProcessor.fetchIds(providerParams, theDataSink::accept);
		} catch (JobExecutionFailedException ex) {
			theDataSink.recoveredError(ex.getMessage());
			// rethrow so it can be properly handled
			throw ex;
		}

		ourLog.info("Submitted {} groups of ids for processing", submissionCount);
		return RunOutcome.SUCCESS;
	}
}
