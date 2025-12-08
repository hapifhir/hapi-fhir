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
package ca.uhn.fhir.batch2.jobs.bulkmodify.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexWarningProcessor;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.batch2.jobs.reindex.v2.ReindexResults;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.ReindexOutcome;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReindexV3ModifyResourcesStep extends BaseBulkModifyResourcesStep<ReindexJobParameters, Void> {

	private static final Logger ourLog = LoggerFactory.getLogger(ReindexV3ModifyResourcesStep.class);
	private final ReindexJobService myReindexJobService;

	/**
	 * Constructor
	 */
	public ReindexV3ModifyResourcesStep(ReindexJobService theReindexJobService) {
		myReindexJobService = theReindexJobService;
	}

	@Override
	protected void processPidsOutsideTransaction(
			String theInstanceId,
			String theChunkId,
			ReindexJobParameters theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails,
			IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {
		/*
		 * This logic is ported from the V2 reindex task, and is used to ensure that we don't
		 * reindex CodeSystem resources if they are pending CodeSystem backend indexing. We really
		 * need to move CS extraction and VS expansion into Batch2 jobs which have some sort of
		 * guarding against collisions, so we can do this in a less convoluted way.
		 */
		Map<String, Boolean> resourceTypesToCheckFlag = new HashMap<>();
		thePids.forEach(id -> {
			// we don't really care about duplicates; we check by resource type
			resourceTypesToCheckFlag.put(id.getResourceType(), true);
		});
		if (myReindexJobService.anyResourceHasPendingReindexWork(resourceTypesToCheckFlag)) {
			throw new RetryChunkLaterException(Msg.code(2830), ReindexUtils.getRetryLaterDelay());
		}
	}

	@Override
	protected void processPidsInTransaction(
			String theInstanceId,
			String theChunkId,
			ReindexJobParameters theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails,
			IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {

		// TODO: this whole construction with a "warning processor" getting attached to the sink
		// is weirdly complex - all it does is massage specific warning messages. This logic
		// should just be moved into this class
		theDataSink.setWarningProcessor(new ReindexWarningProcessor());

		// Convert JSON TypedPids into Persistent IDs
		List<? extends IResourcePersistentId<?>> persistentIds = thePids.stream()
				.map(TypedPidAndVersionJson::toTypedPid)
				.map(t -> t.toPersistentId(myIdHelperService))
				.toList();

		StopWatch sw = new StopWatch();
		ReindexResults reindexResults = new ReindexResults();

		// Prefetch Resources from DB
		boolean reindexSearchParameters =
				theJobParameters.getReindexSearchParameters() != ReindexParameters.ReindexSearchParametersEnum.NONE;
		mySystemDao.preFetchResources(persistentIds, reindexSearchParameters);
		ourLog.info(
				"Prefetched {} resources in {} - Instance[{}] Chunk[{}]",
				persistentIds.size(),
				sw,
				theInstanceId,
				theChunkId);

		ReindexParameters parameters = new ReindexParameters()
				.setReindexSearchParameters(theJobParameters.getReindexSearchParameters())
				.setOptimizeStorage(theJobParameters.getOptimizeStorage())
				.setOptimisticLock(theJobParameters.getOptimisticLock())
				.setCorrectCurrentVersion(theJobParameters.getCorrectCurrentVersion());

		// Reindex

		sw.restart();
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		for (int i = 0; i < persistentIds.size(); i++) {

			TypedPidAndVersionJson nextPid = thePids.get(i);
			String nextResourceType = persistentIds.get(i).getResourceType();
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextResourceType);
			IResourcePersistentId<?> resourcePersistentId = persistentIds.get(i);
			try {

				ReindexOutcome outcome =
						dao.reindex(resourcePersistentId, parameters, requestDetails, theTransactionDetails);

				theState.setResourceIdForPid(nextPid, outcome.getResourceId());
				theState.moveToState(nextPid, StateEnum.CHANGED_PENDING);
				outcome.getWarnings().forEach(theDataSink::recoveredError);
				reindexResults.addResourceTypeToCompletionStatus(nextResourceType, outcome.isHasPendingWork());

			} catch (BaseServerResponseException | DataFormatException e) {
				String resourceForcedId = myIdHelperService
						.translatePidIdToForcedIdWithCache(resourcePersistentId)
						.orElse(resourcePersistentId.toString());
				String resourceId = nextResourceType + "/" + resourceForcedId;
				ourLog.error("Failure during reindexing {}", resourceId, e);
				theDataSink.recoveredError("Failure reindexing " + resourceId + ": " + e.getMessage());
				theState.setResourceIdForPid(
						nextPid, myFhirContext.getVersion().newIdType(nextResourceType, resourceId));
			}
		}
	}

	@Override
	protected String getJobNameForLogging() {
		return ProviderConstants.OPERATION_REINDEX;
	}
}
