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
package ca.uhn.fhir.batch2.jobs.export.v3;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportWorkPackageJson;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// FIXME: find the 2 FetchResourceIdsV1Step tests and make V2 versions too
public class FetchResourceIdsV3Step
		implements IJobStepWorker<BulkExportJobParameters, BulkExportWorkPackageJson, ResourceIdList> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchResourceIdsV3Step.class);

	@Autowired
	private IBulkExportProcessor<?> myBulkExportProcessor;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, BulkExportWorkPackageJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ResourceIdList> theDataSink)
			throws JobExecutionFailedException {
		BulkExportJobParameters params = theStepExecutionDetails.getParameters();
		BulkExportWorkPackageJson data = theStepExecutionDetails.getData();
		RequestPartitionId partitionId = data.getPartitionId();

		ourLog.info(
				"Fetching resource IDs for bulk export job instance[{}] for partition(s): {}",
				theStepExecutionDetails.getInstance().getInstanceId(),
				partitionId);

		ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters();
		providerParams.setInstanceId(theStepExecutionDetails.getInstance().getInstanceId());
		providerParams.setChunkId(theStepExecutionDetails.getChunkId());
		providerParams.setFilters(params.getFilters());
		providerParams.setStartDate(params.getSince());
		providerParams.setEndDate(params.getUntil());
		providerParams.setExportStyle(params.getExportStyle());
		if (params.getExportStyle() == BulkExportJobParameters.ExportStyle.GROUP) {
			providerParams.setGroupId(data.getGroupId());
			providerParams.setPatientIds(data.getPatientIds());
		} else {
			providerParams.setGroupId(params.getGroupId());
			providerParams.setPatientIds(params.getPatientIds());
		}
		providerParams.setExpandMdm(params.isExpandMdm());
		providerParams.setPartitionId(partitionId);
		// This step doesn't use this param. Included here for logging purpose
		providerParams.setIncludeHistory(params.isIncludeHistory());

		/*
		 * we set all the requested resource types here so that
		 * when we recursively fetch resource types for a given patient/group
		 * we don't recurse for types that they did not request
		 */
		providerParams.setRequestedResourceTypes(params.getResourceTypes());

		int submissionChunkCount = 0;
		int submissionResourceCount = 0;
		try {
			Set<TypedPidJson> submittedBatchResourceIds = new HashSet<>();

			/*
			 * We will fetch ids for each resource type in the ResourceTypes (_type filter).
			 */
			String resourceType = data.getResourceType();
			providerParams.setResourceType(resourceType);

			// filters are the filters for searching
			ourLog.info(
					"Running FetchResourceIdsStep for resource type: {} with params: {}", resourceType, providerParams);
			@SuppressWarnings("unchecked")
			Iterator<IResourcePersistentId<?>> pidIterator =
					(Iterator<IResourcePersistentId<?>>) myBulkExportProcessor.getResourcePidIterator(providerParams);
			List<TypedPidJson> idsToSubmit = new ArrayList<>();

			int estimatedChunkSize = 0;

			if (!pidIterator.hasNext()) {
				ourLog.debug("Bulk Export generated an iterator with no results!");
			}
			while (pidIterator.hasNext()) {
				IResourcePersistentId<?> pid = pidIterator.next();

				TypedPidJson batchResourceId;
				if (pid.getResourceType() != null) {
					batchResourceId = new TypedPidJson(pid.getResourceType(), pid);
				} else {
					batchResourceId = new TypedPidJson(resourceType, pid);
				}

				if (!submittedBatchResourceIds.add(batchResourceId)) {
					continue;
				}

				idsToSubmit.add(batchResourceId);
				submissionResourceCount++;

				if (estimatedChunkSize > 0) {
					// Account for comma between array entries
					estimatedChunkSize++;
				}
				estimatedChunkSize += batchResourceId.estimateSerializedSize();

				// Make sure resources stored in each batch does not go over the max capacity
				if (idsToSubmit.size() >= myStorageSettings.getBulkExportFileMaximumCapacity()
						|| estimatedChunkSize >= myStorageSettings.getBulkExportFileMaximumSize()) {
					submitWorkChunk(partitionId, resourceType, idsToSubmit, theDataSink);
					submissionChunkCount++;
					idsToSubmit = new ArrayList<>();
					estimatedChunkSize = 0;
				}
			}

			// if we have any other Ids left, submit them now
			if (!idsToSubmit.isEmpty()) {
				submitWorkChunk(partitionId, resourceType, idsToSubmit, theDataSink);
				submissionChunkCount++;
			}

		} catch (Exception ex) {
			ourLog.error(ex.getMessage(), ex);

			theDataSink.recoveredError(ex.getMessage());

			throw new JobExecutionFailedException(Msg.code(2239) + " : " + ex.getMessage());
		}

		ourLog.info("Submitted {} chunks with {} ids for processing", submissionChunkCount, submissionResourceCount);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(
			RequestPartitionId thePartitionId,
			String theResourceType,
			List<TypedPidJson> theBatchResourceIds,
			IJobDataSink<ResourceIdList> theDataSink) {
		ResourceIdList idList = new ResourceIdList();
		idList.setPartitionId(thePartitionId);
		idList.setResourceType(theResourceType);
		idList.setIds(theBatchResourceIds);

		theDataSink.accept(idList);
	}

	@VisibleForTesting
	public void setBulkExportProcessorForUnitTest(IBulkExportProcessor<?> theBulkExportProcessor) {
		myBulkExportProcessor = theBulkExportProcessor;
	}
}
