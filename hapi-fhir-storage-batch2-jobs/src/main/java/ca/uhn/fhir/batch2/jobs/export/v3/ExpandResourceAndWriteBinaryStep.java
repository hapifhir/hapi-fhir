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
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.svcs.BinaryCreator;
import ca.uhn.fhir.batch2.jobs.export.svcs.ExpandResourcesConsumer;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.batch2.jobs.imprt.BulkImportAppCtx.PARAM_MAXIMUM_BATCH_SIZE_DEFAULT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourceAndWriteBinaryStep
		implements IJobStepWorker<BulkExportJobParameters, ResourceIdList, BulkExportBinaryFileId> {
	private static final Logger ourLog = getLogger(ExpandResourceAndWriteBinaryStep.class);

	// small limit to account for the possible large size of history versions
	private static final int MAX_HISTORY_PAGE_SIZE = 10;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	@Autowired
	private IBulkExportProcessor<?> myBulkExportProcessor;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private InterceptorService myInterceptorService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private IBulkDataExportHistoryHelper myExportHelper;

	@Autowired
	private ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	/**
	 * Constructor
	 */
	public ExpandResourceAndWriteBinaryStep() {
		super();
	}

	/**
	 * Note on the design of this step:
	 * This step takes a list of resource PIDs as input, fetches those
	 * resources (or their history if requested), applies a bunch of filtering/consent/MDM/etc. modifications
	 * on them, serializes the result as NDJSON files, and then persists those
	 * NDJSON files as Binary resources.
	 * <p>
	 * We want to avoid writing files which exceed the configured maximum
	 * file size, and we also want to avoid keeping too much in memory
	 * at any given time, so this class works a bit like a stream processor
	 * (although not using Java streams).
	 * <p>
	 * The {@link #fetchResourcesByIdAndConsumeThem(ResourceIdList, BulkExportJobParameters, Consumer, StepExecutionDetails)}
	 * method loads the resources by ID, {@link ExpandResourcesConsumer} handles
	 * the filtering and batching, then the {@link BinaryCreator}
	 * ultimately writes them.
	 */
	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportBinaryFileId> theDataSink)
			throws JobExecutionFailedException {

		ResourceIdList idList = theStepExecutionDetails.getData();
		BulkExportJobParameters parameters = theStepExecutionDetails.getParameters();

		ExpandResourcesConsumer resourceListConsumer =
				getExpandResourcesConsumer(theStepExecutionDetails, theDataSink, parameters);

		// search the resources
		fetchResourcesByIdAndConsumeThem(idList, parameters, resourceListConsumer, theStepExecutionDetails);

		int resourcesConsumed = resourceListConsumer.getConsumedResourceCount();
		return new RunOutcome(resourcesConsumed);
	}

	private ExpandResourcesConsumer getExpandResourcesConsumer(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			IJobDataSink<BulkExportBinaryFileId> theDataSink,
			BulkExportJobParameters theParameters) {
		ExpandResourcesConsumer resourceListConsumer = new ExpandResourcesConsumer(
				myFhirContext,
				myBulkExportProcessor,
				myInterceptorService,
				myStorageSettings,
				myInMemoryResourceMatcher,
				myResponseTerminologyTranslationSvc,
				getBinaryCreator(theStepExecutionDetails, theDataSink),
				theStepExecutionDetails);
		// V3 does not support MDM expansion — the feature was rolled back to V2 only.
		// BulkExportJobParameters still carries isExpandMdm() because V2 and V3 share the
		// parameters shape, so this is pinned false rather than removed, to make the
		// rollback explicit rather than looking like an oversight.
		resourceListConsumer.setDoExpandMDM(isV2Job() && theParameters.isExpandMdm());
		return resourceListConsumer;
	}

	private BinaryCreator getBinaryCreator(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			IJobDataSink<BulkExportBinaryFileId> theDataSink) {
		return new BinaryCreator(myFhirContext, myDaoRegistry, theStepExecutionDetails, theDataSink);
	}

	private void fetchResourcesByIdAndConsumeThem(
			ResourceIdList theIds,
			BulkExportJobParameters theJobParameters,
			Consumer<List<IBaseResource>> theResourceListConsumer,
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails) {

		Map<RequestPartitionId, ArrayListMultimap<String, TypedPidJson>> partitionTotypeToIds = new HashMap<>();

		for (TypedPidJson id : theIds.getIds()) {
			Integer partitionId = id.getPartitionId();
			RequestPartitionId requestPartitionId;
			if (partitionId == null) {
				requestPartitionId = RequestPartitionId.defaultPartition(myPartitionSettings);
			} else {
				requestPartitionId = RequestPartitionId.fromPartitionId(partitionId);
			}
			ArrayListMultimap<String, TypedPidJson> typeToPids =
					partitionTotypeToIds.computeIfAbsent(requestPartitionId, k -> ArrayListMultimap.create());
			typeToPids.put(id.getResourceType(), id);
		}

		for (Map.Entry<RequestPartitionId, ArrayListMultimap<String, TypedPidJson>> entry :
				partitionTotypeToIds.entrySet()) {
			RequestPartitionId requestPartitionId = entry.getKey();
			ArrayListMultimap<String, TypedPidJson> typeToPids = entry.getValue();

			if (theJobParameters.isIncludeHistory()) {
				adjustJobParameters(theJobParameters, theStepExecutionDetails);
				processHistoryResources(theResourceListConsumer, typeToPids, requestPartitionId, theJobParameters);
			} else {
				processResources(theResourceListConsumer, typeToPids, requestPartitionId);
			}
		}
	}

	/**
	 * Processes historical resources by resource type in batches to avoid exceeding
	 * maximum file capacity or query size limits. This method handles pagination and
	 * resource accumulation for history-enabled bulk exports.
	 *
	 * @param theResourceListConsumer Consumer to process batches of resources
	 * @param theTypeToIds            Multimap of resource types to their corresponding PIDs
	 * @param theRequestPartitionId   Partition ID for the request
	 * @param theJobParameters        the batch job parameters
	 */
	@VisibleForTesting
	public void processHistoryResources(
			Consumer<List<IBaseResource>> theResourceListConsumer,
			ArrayListMultimap<String, TypedPidJson> theTypeToIds,
			RequestPartitionId theRequestPartitionId,
			BulkExportJobParameters theJobParameters) {

		// for each resource type
		for (String resourceType : theTypeToIds.keySet()) {
			List<TypedPidJson> typePidJsonList = theTypeToIds.get(resourceType);

			List<IResourcePersistentId<?>> idList = new ArrayList<>();
			for (TypedPidJson typePid : typePidJsonList) {
				@SuppressWarnings("unchecked")
				IResourcePersistentId<?> persistentId = typePid.toPersistentId(myIdHelperService);
				idList.add(persistentId);
			}

			consumeHistoryInBatches(
					resourceType, idList, theRequestPartitionId, theJobParameters, theResourceListConsumer);
		}
	}

	@VisibleForTesting
	public void adjustJobParameters(
			BulkExportJobParameters theJobParameters,
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails) {

		// History export requires a valid `until` param with date not after the history querying start to
		// cover for the case of new history records being created during export, which would generate
		// pagination inconsistencies
		Date jobStartTime = theStepExecutionDetails.getInstance().getStartTime();
		if (theJobParameters.getUntil() == null || theJobParameters.getUntil().after(jobStartTime)) {
			theJobParameters.setUntil(jobStartTime);
			ourLog.info(
					"Until time adjusted to match job start time: {}, as required by history type export.",
					jobStartTime);
		}
	}

	private void consumeHistoryInBatches(
			String theResourceType,
			List<IResourcePersistentId<?>> theIdList,
			RequestPartitionId theRequestPartitionId,
			BulkExportJobParameters theJobParameters,
			Consumer<List<IBaseResource>> theResourceListConsumer) {

		// only fileMaxResourceCount considered here because consumer takes care of fileMaximumSize

		final int fileMaxResourceCount = myStorageSettings.getBulkExportFileMaximumCapacity();
		final int pageSize = Math.min(MAX_HISTORY_PAGE_SIZE, fileMaxResourceCount);

		IBundleProvider resHistoryProvider =
				searchForResourcesHistory(theResourceType, theIdList, theRequestPartitionId, theJobParameters);

		int currentIndex = 0;
		List<IBaseResource> resourcesToConsume = new ArrayList<>();

		while (true) {
			ourLog.debug(
					"Fetching history page from index {} to {} for resource type {}",
					currentIndex,
					currentIndex + pageSize,
					theResourceType);

			List<IBaseResource> page = resHistoryProvider.getResources(currentIndex, currentIndex + pageSize);
			ourLog.debug("Retrieved {} history resources from page starting at index {}", page.size(), currentIndex);

			if (page.isEmpty()) {
				ourLog.debug("No more history resources found, breaking pagination loop");
				break;
			}

			resourcesToConsume.addAll(page);

			// process complete batches
			while (resourcesToConsume.size() >= fileMaxResourceCount) {
				List<IBaseResource> batch = new ArrayList<>(resourcesToConsume.subList(0, fileMaxResourceCount));
				theResourceListConsumer.accept(batch);
				ourLog.debug("Sent batch of {} history resources to consumer", batch.size());
				resourcesToConsume.subList(0, fileMaxResourceCount).clear();
			}

			currentIndex += page.size();

			// If we got fewer results than requested, we've reached the end
			if (page.size() < pageSize) {
				ourLog.debug(
						"Retrieved {} resources (less than page size: {}), reached end of history",
						page.size(),
						pageSize);
				break;
			}
		}

		// consume possible remaining resources
		if (!resourcesToConsume.isEmpty()) {
			theResourceListConsumer.accept(resourcesToConsume);
			ourLog.debug("Sent batch of {} history resources to consumer", resourcesToConsume.size());
		}
	}

	/**
	 * Processes current version resources (non-history) by resource type in batches.
	 * This method handles pagination and resource retrieval for standard bulk exports.
	 *
	 * @param theResourceListConsumer Consumer to process batches of resources
	 * @param typeToIds Multimap of resource types to their corresponding PIDs
	 * @param requestPartitionId Partition ID for the request
	 */
	private void processResources(
			Consumer<List<IBaseResource>> theResourceListConsumer,
			ArrayListMultimap<String, TypedPidJson> typeToIds,
			RequestPartitionId requestPartitionId) {

		final int maxResourcesPerBatches = myStorageSettings.getBulkExportFileMaximumCapacity();

		for (String resourceType : typeToIds.keySet()) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
			List<TypedPidJson> allIds = typeToIds.get(resourceType);
			List<List<TypedPidJson>> batches = ListUtils.partition(allIds, maxResourcesPerBatches);
			for (List<TypedPidJson> consumingBatch : batches) {

				List<IBaseResource> batchResources = new ArrayList<>(maxResourcesPerBatches);

				// Break each batch up into query sub-batches in order to make sure we don't exceed the
				// limit of 800 variables per SQL statement
				for (List<TypedPidJson> queryBatch :
						ListUtils.partition(consumingBatch, PARAM_MAXIMUM_BATCH_SIZE_DEFAULT)) {
					List<String> idList = convertToStringIds(requestPartitionId, resourceType, queryBatch);
					IBundleProvider outcome = searchForResources(dao, idList, requestPartitionId);
					batchResources.addAll(outcome.getAllResources());
				}

				theResourceListConsumer.accept(batchResources);
			}
		}
	}

	/**
	 * Converts a batch of typed PID JSON objects to their corresponding string IDs,
	 * handling both forced IDs and numeric PIDs.
	 *
	 * @param theRequestPartitionId Partition ID for the request
	 * @param theResourceType Type of resources being processed
	 * @param theQueryBatch Batch of typed PID JSON objects to convert
	 * @return List of string IDs corresponding to the input PIDs
	 */
	@SuppressWarnings("rawtypes")
	private @NotNull List<String> convertToStringIds(
			RequestPartitionId theRequestPartitionId, String theResourceType, List<TypedPidJson> theQueryBatch) {

		Set<IResourcePersistentId> queryBatchPids = theQueryBatch.stream()
				.map(t -> myIdHelperService.newPidFromStringIdAndResourceName(
						t.getPartitionId(), t.getPid(), theResourceType))
				.collect(Collectors.toSet());

		@SuppressWarnings("unchecked")
		PersistentIdToForcedIdMap nextBatchOfResourceIds = myTransactionService
				.withSystemRequestOnPartition(theRequestPartitionId)
				.execute(() -> myIdHelperService.translatePidsToForcedIds(queryBatchPids));

		List<String> idList = new ArrayList<>();
		for (IResourcePersistentId nextPid : queryBatchPids) {
			@SuppressWarnings("unchecked")
			Optional<String> resourceId = nextBatchOfResourceIds.get(nextPid);
			idList.add(resourceId.orElse(nextPid.getId().toString()));
		}
		return idList;
	}

	/**
	 * Searches for historical versions of resources by their IDs using the bulk export history helper.
	 *
	 * @param theResourceType       Type of resources to search for
	 * @param theIdList             The resource IDs which history must be fetched
	 * @param theRequestPartitionId Partition ID for the request
	 * @param theJobParameters		The job parameters
	 * @return Bundle provider containing historical versions of the resources
	 */
	private IBundleProvider searchForResourcesHistory(
			String theResourceType,
			List<IResourcePersistentId<?>> theIdList,
			RequestPartitionId theRequestPartitionId,
			BulkExportJobParameters theJobParameters) {

		return myExportHelper.fetchHistoryForResourceIds(
				theResourceType,
				theIdList,
				theRequestPartitionId,
				theJobParameters.getSince(),
				theJobParameters.getUntil());
	}

	/**
	 * Searches for current versions of resources by their IDs using the resource DAO.
	 *
	 * @param theDao Resource DAO to perform the search
	 * @param theIdList List of resource IDs to search for
	 * @param theRequestPartitionId Partition ID for the request
	 * @return Bundle provider containing the current versions of the resources
	 */
	private IBundleProvider searchForResources(
			IFhirResourceDao<?> theDao, List<String> theIdList, RequestPartitionId theRequestPartitionId) {

		TokenOrListParam idListParam = new TokenOrListParam();
		theIdList.forEach(idListParam::add);

		SearchParameterMap spMap = SearchParameterMap.newSynchronous().add(PARAM_ID, idListParam);
		return theDao.search(spMap, new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId));
	}

	@VisibleForTesting
	public void setIdHelperServiceForUnitTest(IIdHelperService<?> theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}

	/**
	 * Overridden in the V2 step
	 * Always false since the mdmexpansion was rolled back form
	 * V2 for V3
	 */
	protected boolean isV2Job() {
		return false;
	}
}
