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
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.svcs.ISearchLimiterSvc;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FetchResourceIdsStep implements IFirstJobStepWorker<BulkExportJobParameters, ResourceIdList> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchResourceIdsStep.class);

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private ISearchLimiterSvc mySearchLimiterSvc;

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

		/*
		 * we set all the requested resource types here so that
		 * when we recursively fetch resource types for a given patient/group
		 * we don't recurse for types that they did not request
		 */
		List<String> resourceTypesToFetch;
		if (theStepExecutionDetails.getParameters().getExportStyle() == BulkExportJobParameters.ExportStyle.PATIENT) {
			Collection<String> omitted =
					mySearchLimiterSvc.getResourcesToOmitForOperationSearches(ProviderConstants.OPERATION_EXPORT);
			resourceTypesToFetch = params.getResourceTypes().stream()
					.filter(r -> !omitted.contains(r))
					.toList();
		} else {
			resourceTypesToFetch = params.getResourceTypes().stream().toList();
		}
		providerParams.setRequestedResourceTypes(resourceTypesToFetch);

		int submissionCount = 0;
		try {
			Set<TypedPidJson> submittedBatchResourceIds = new HashSet<>();

			/*
			 * We will fetch ids for each resource type in the ResourceTypes (_type filter).
			 */
			for (String resourceType : resourceTypesToFetch) {
				providerParams.setResourceType(resourceType);

				// filters are the filters for searching
				ourLog.info(
						"Running FetchResourceIdsStep for resource type: {} with params: {}",
						resourceType,
						providerParams);
				Iterator<IResourcePersistentId> pidIterator =
						myBulkExportProcessor.getResourcePidIterator(providerParams);
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

					if (estimatedChunkSize > 0) {
						// Account for comma between array entries
						estimatedChunkSize++;
					}
					estimatedChunkSize += batchResourceId.estimateSerializedSize();

					// Make sure resources stored in each batch does not go over the max capacity
					if (idsToSubmit.size() >= myStorageSettings.getBulkExportFileMaximumCapacity()
							|| estimatedChunkSize >= myStorageSettings.getBulkExportFileMaximumSize()) {
						submitWorkChunk(idsToSubmit, resourceType, theDataSink);
						submissionCount++;
						idsToSubmit = new ArrayList<>();
						estimatedChunkSize = 0;
					}
				}

				// if we have any other Ids left, submit them now
				if (!idsToSubmit.isEmpty()) {
					submitWorkChunk(idsToSubmit, resourceType, theDataSink);
					submissionCount++;
				}
			}
		} catch (Exception ex) {
			ourLog.error(ex.getMessage(), ex);

			theDataSink.recoveredError(ex.getMessage());

			throw new JobExecutionFailedException(Msg.code(2239) + " : " + ex.getMessage());
		}

		ourLog.info("Submitted {} groups of ids for processing", submissionCount);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(
			List<TypedPidJson> theBatchResourceIds, String theResourceType, IJobDataSink<ResourceIdList> theDataSink) {
		ResourceIdList idList = new ResourceIdList();

		idList.setIds(theBatchResourceIds);

		idList.setResourceType(theResourceType);

		theDataSink.accept(idList);
	}

	@VisibleForTesting
	public void setBulkExportProcessorForUnitTest(IBulkExportProcessor theBulkExportProcessor) {
		myBulkExportProcessor = theBulkExportProcessor;
	}
}
