/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

		ActivityCounter activityCounter = new ActivityCounter();
		try {
			Set<TypedPidJson> submittedBatchResourceIds = new HashSet<>();
			BulkExportWorkPackageJson data = theStepExecutionDetails.getData();

			/*
			 * We will fetch ids for each resource type in the ResourceTypes (_type filter).
			 */
			for (String resourceType : data.getResourceTypes()) {
				fetchResourceIdsAndSubmitWorkChunksForResourceType(
						theDataSink, theStepExecutionDetails, resourceType, submittedBatchResourceIds, activityCounter);
			}

		} catch (Exception ex) {
			ourLog.error(ex.getMessage(), ex);

			theDataSink.recoveredError(ex.getMessage());

			throw new JobExecutionFailedException(Msg.code(2839) + " : " + ex.getMessage());
		}

		ourLog.info(
				"Submitted {} chunks with {} ids for processing",
				activityCounter.getSubmissionChunkCount(),
				activityCounter.getSubmissionResourceCount());
		return RunOutcome.SUCCESS;
	}

	private void fetchResourceIdsAndSubmitWorkChunksForResourceType(
			@Nonnull IJobDataSink<ResourceIdList> theDataSink,
			StepExecutionDetails<BulkExportJobParameters, BulkExportWorkPackageJson> theStepExecutionDetails,
			String theResourceType,
			Set<TypedPidJson> submittedBatchResourceIds,
			ActivityCounter theActivityCounter) {
		BulkExportWorkPackageJson data = theStepExecutionDetails.getData();
		RequestPartitionId partitionId = data.getPartitionId();

		BulkExportJobParameters params = theStepExecutionDetails.getParameters();

		ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters();
		providerParams.setRequestDetails(theStepExecutionDetails.newSystemRequestDetails());
		providerParams.setInstanceId(theStepExecutionDetails.getInstance().getInstanceId());
		providerParams.setChunkId(theStepExecutionDetails.getChunkId());
		providerParams.setFilters(params.getFilters());
		providerParams.setStartDate(params.getSince());
		providerParams.setEndDate(params.getUntil());
		providerParams.setExportStyle(params.getExportStyle());
		if (params.getExportStyle() == BulkExportJobParameters.ExportStyle.GROUP) {
			providerParams.setGroupId(data.getGroupId());
			providerParams.setPatientIds(data.getPatientIds());
		} else if (params.getExportStyle() == BulkExportJobParameters.ExportStyle.PATIENT) {
			if (data.getPatientIds() != null && !data.getPatientIds().isEmpty()) {
				providerParams.setPatientIds(data.getPatientIds());
				providerParams.setExpandedPatientIdsForPatientExport(Set.copyOf(data.getPatientIds()));
			}
		} else {
			providerParams.setGroupId(data.getGroupId());
			providerParams.setPatientIds(data.getPatientIds());
		}
		providerParams.setPartitionId(partitionId);

		/// We do MDM expansion in {@link GenerateWorkPackagesV3Step}, no need to do it again in this step
		providerParams.setExpandMdm(false);

		/*
		 * we set all the requested resource types here so that
		 * when we recursively fetch resource types for a given patient/group
		 * we don't recurse for types that they did not request
		 */
		providerParams.setRequestedResourceTypes(params.getResourceTypes());

		providerParams.setResourceType(theResourceType);

		ourLog.info(
				"Running {} for InstanceID[{}] ChunkID[{}] with ResourceType[{}] Partition[{}]",
				getClass().getSimpleName(),
				theResourceType,
				theStepExecutionDetails.getInstance().getInstanceId(),
				theStepExecutionDetails.getChunkId(),
				data.getPartitionId());

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
				batchResourceId = new TypedPidJson(theResourceType, pid);
			}

			if (!submittedBatchResourceIds.add(batchResourceId)) {
				continue;
			}

			idsToSubmit.add(batchResourceId);
			theActivityCounter.incrementSubmissionResourceCount();

			if (estimatedChunkSize > 0) {
				// Account for comma between array entries
				estimatedChunkSize++;
			}
			estimatedChunkSize += batchResourceId.estimateSerializedSize();

			// Make sure resources stored in each batch does not go over the max capacity
			if (idsToSubmit.size() >= myStorageSettings.getBulkExportFileMaximumCapacity()
					|| estimatedChunkSize >= myStorageSettings.getBulkExportFileMaximumSize()) {
				submitWorkChunk(partitionId, theResourceType, idsToSubmit, theDataSink);
				theActivityCounter.incrementSubmissionChunkCount();
				idsToSubmit = new ArrayList<>();
				estimatedChunkSize = 0;
			}
		}

		// if we have any other Ids left, submit them now
		if (!idsToSubmit.isEmpty()) {
			submitWorkChunk(partitionId, theResourceType, idsToSubmit, theDataSink);
			theActivityCounter.incrementSubmissionChunkCount();
		}
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

	private static class ActivityCounter {
		private int mySubmissionChunkCount;
		private int mySubmissionResourceCount;

		private void incrementSubmissionChunkCount() {
			mySubmissionChunkCount++;
		}

		private void incrementSubmissionResourceCount() {
			mySubmissionResourceCount++;
		}

		private int getSubmissionChunkCount() {
			return mySubmissionChunkCount;
		}

		private int getSubmissionResourceCount() {
			return mySubmissionResourceCount;
		}
	}
}
