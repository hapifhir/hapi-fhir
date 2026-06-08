/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.merge.MergeJobParameters;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.PartitionedTransactionPartialFailureException;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.CrossPartitionReplaceReferencesResult;
import ca.uhn.fhir.jpa.provider.CrossPartitionReplaceReferencesSvc;
import ca.uhn.fhir.merge.MergeResourceHelper;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.merge.MergeAppCtx.JOB_MERGE;
import static ca.uhn.fhir.merge.MergeResourceHelper.addInfoToOperationOutcome;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_202_ACCEPTED;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;

/**
 * Service for the FHIR Patient/$merge and [ResourceType]/$hapi.fhir.merge (generic merge) operations.
 */
public class ResourceMergeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceMergeService.class);

	private final FhirContext myFhirContext;
	private final JpaStorageSettings myStorageSettings;
	private final DaoRegistry myDaoRegistry;
	private final ReplaceReferencesPatchBundleSvc myReplaceReferencesPatchBundleSvc;
	private final IResourceLinkDao myResourceLinkDao;
	private final IHapiTransactionService myHapiTransactionService;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final IFhirResourceDao<Task> myTaskDao;
	private final IJobCoordinator myJobCoordinator;
	private final MergeResourceHelper myMergeResourceHelper;
	private final Batch2TaskHelper myBatch2TaskHelper;
	private final MergeValidationService myMergeValidationService;
	private final CrossPartitionReplaceReferencesSvc myCrossPartitionReplaceReferencesSvc;
	private final PartitionSettings myPartitionSettings;
	private final CrossPartitionMergeRollbackService myCrossPartitionMergeRollbackService;

	public ResourceMergeService(
			JpaStorageSettings theStorageSettings,
			DaoRegistry theDaoRegistry,
			ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc,
			IResourceLinkDao theResourceLinkDao,
			IHapiTransactionService theHapiTransactionService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			IJobCoordinator theJobCoordinator,
			Batch2TaskHelper theBatch2TaskHelper,
			MergeValidationService theMergeValidationService,
			MergeResourceHelper theMergeResourceHelper,
			CrossPartitionReplaceReferencesSvc theCrossPartitionReplaceReferencesSvc,
			PartitionSettings thePartitionSettings,
			CrossPartitionMergeRollbackService theCrossPartitionMergeRollbackService) {
		myStorageSettings = theStorageSettings;
		myDaoRegistry = theDaoRegistry;

		myTaskDao = theDaoRegistry.getResourceDao(Task.class);
		myReplaceReferencesPatchBundleSvc = theReplaceReferencesPatchBundleSvc;
		myResourceLinkDao = theResourceLinkDao;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myJobCoordinator = theJobCoordinator;
		myBatch2TaskHelper = theBatch2TaskHelper;
		myFhirContext = theDaoRegistry.getFhirContext();
		myHapiTransactionService = theHapiTransactionService;
		myMergeResourceHelper = theMergeResourceHelper;
		myMergeValidationService = theMergeValidationService;
		myCrossPartitionReplaceReferencesSvc = theCrossPartitionReplaceReferencesSvc;
		myPartitionSettings = thePartitionSettings;
		myCrossPartitionMergeRollbackService = theCrossPartitionMergeRollbackService;
	}

	/**
	 * Perform the $merge operation. Operation can be performed synchronously or asynchronously depending on
	 * the prefer-async request header.
	 * If the operation is requested to be performed synchronously and the number of
	 * resources to be changed exceeds the provided batch size,
	 * and error is returned indicating that operation needs to be performed asynchronously. See the
	 * <a href="https://build.fhir.org/patient-operation-merge.html">Patient $merge spec</a>
	 * for details on what the difference is between synchronous and asynchronous mode.
	 *
	 * @param theMergeOperationParameters the merge operation parameters
	 * @param theRequestDetails           the request details
	 * @return the merge outcome containing OperationOutcome and HTTP status code
	 */
	public MergeOperationOutcome merge(
			MergeOperationInputParameters theMergeOperationParameters, RequestDetails theRequestDetails) {

		MergeOperationOutcome mergeOutcome = new MergeOperationOutcome();
		IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.newInstance(myFhirContext);
		mergeOutcome.setOperationOutcome(operationOutcome);
		// default to 200 OK, would be changed to another code during processing as required
		mergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);
		try {
			validateAndMerge(theMergeOperationParameters, theRequestDetails, mergeOutcome);
		} catch (Exception e) {
			ourLog.error("Resource merge failed", e);
			if (e instanceof BaseServerResponseException) {
				mergeOutcome.setHttpStatusCode(((BaseServerResponseException) e).getStatusCode());
			} else {
				mergeOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);
			}
			OperationOutcomeUtil.addIssue(myFhirContext, operationOutcome, "error", e.getMessage(), null, "exception");
		}
		return mergeOutcome;
	}

	private void validateAndMerge(
			MergeOperationInputParameters theMergeOperationParameters,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		// TODO KHS remove the outparameter and instead accumulate issues in the validation result
		MergeValidationResult mergeValidationResult =
				myMergeValidationService.validate(theMergeOperationParameters, theRequestDetails, theMergeOutcome);

		if (mergeValidationResult.isValid) {
			IBaseResource sourceResource = mergeValidationResult.sourceResource;
			IBaseResource targetResource = mergeValidationResult.targetResource;

			validateCrossPartitionAsyncNotSupported(sourceResource, targetResource, theRequestDetails);

			if (theMergeOperationParameters.getPreview()) {
				handlePreview(
						sourceResource,
						targetResource,
						theMergeOperationParameters,
						theRequestDetails,
						theMergeOutcome);
			} else {
				doMerge(
						theMergeOperationParameters,
						sourceResource,
						targetResource,
						theRequestDetails,
						theMergeOutcome);
			}
		} else {
			theMergeOutcome.setHttpStatusCode(mergeValidationResult.httpStatusCode);
		}
	}

	private void handlePreview(
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			MergeOperationInputParameters theMergeOperationParameters,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		Integer referencingResourceCount = countResourcesReferencingResource(
				theSourceResource.getIdElement().toVersionless(), theRequestDetails);

		// in preview mode, we should also return what the target would look like
		IBaseResource resultResource = theMergeOperationParameters.getResultResource();
		IBaseResource targetPatientAsIfUpdated = myMergeResourceHelper.prepareTargetResourceForUpdate(
				theTargetResource, theSourceResource, resultResource, theMergeOperationParameters.getDeleteSource());
		theMergeOutcome.setUpdatedTargetResource(targetPatientAsIfUpdated);

		// adding +2 because the source and the target resources would be updated as well
		String diagnosticsMsg = String.format("Merge would update %d resources", referencingResourceCount + 2);
		String detailsText = "Preview only merge operation - no issues detected";
		addInfoToOperationOutcome(myFhirContext, theMergeOutcome.getOperationOutcome(), diagnosticsMsg, detailsText);
	}

	private void doMerge(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(theTargetResource.getIdElement()));

		if (theRequestDetails.isPreferAsync()) {
			doMergeAsync(
					theMergeOperationParameters,
					theSourceResource,
					theTargetResource,
					theRequestDetails,
					theMergeOutcome,
					partitionId);
		} else {
			doMergeSync(
					theMergeOperationParameters,
					theSourceResource,
					theTargetResource,
					theRequestDetails,
					theMergeOutcome,
					partitionId);
		}
	}

	private void doMergeSync(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			RequestPartitionId partitionId) {

		Date startTime = new Date();

		boolean crossPartition = isCrossPartitionMerge(theSourceResource, theTargetResource);

		validateResourceLimit(theMergeOperationParameters, theSourceResource, theRequestDetails, crossPartition);

		if (crossPartition) {
			// Cross-partition writes can commit independently per partition (changing partitions opens its own
			// transaction under REQUIRES_NEW, e.g. MegaScale), so the outer transaction cannot guarantee
			// atomicity. doMergeSyncCrossPartition owns the transaction boundary and the rollback,
			// and populates the outcome itself.
			doMergeSyncCrossPartition(
					theMergeOperationParameters,
					theSourceResource,
					theTargetResource,
					theRequestDetails,
					theMergeOutcome,
					startTime);
		} else {
			// Same partition: the whole merge runs in one transaction, so a failure rolls everything back
			// automatically and no rollback is needed.
			// The outer tx is intentionally not pinned to a partition: per-operation partition resolution
			// handles writes (per-bundle-entry resolution in transactionNested).
			myHapiTransactionService
					.withRequest(theRequestDetails)
					.execute(() -> doMergeSyncSamePartition(
							theMergeOperationParameters,
							theSourceResource,
							theTargetResource,
							partitionId,
							theRequestDetails,
							theMergeOutcome,
							startTime));

			addInfoToOperationOutcome(
					myFhirContext,
					theMergeOutcome.getOperationOutcome(),
					null,
					"Merge operation completed successfully.");
		}
	}

	private void doMergeSyncSamePartition(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestPartitionId thePartitionId,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime) {

		List<Bundle> responseBundles = replaceReferencesInNestedTransaction(
				theSourceResource,
				theTargetResource,
				theMergeOperationParameters.getResourceLimit(),
				thePartitionId,
				theRequestDetails);
		List<IIdType> changedResourceIds = ReplaceReferencesProvenanceSvc.extractChangedResourceIds(responseBundles);

		finalizeMerge(
				theMergeOperationParameters,
				theSourceResource,
				theTargetResource,
				changedResourceIds,
				null,
				List.of(),
				theRequestDetails,
				theMergeOutcome,
				theStartTime,
				null);
	}

	private void doMergeSyncCrossPartition(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime) {

		boolean deleteSource = theMergeOperationParameters.getDeleteSource();
		MergeRollbackContext rollbackContext = new MergeRollbackContext(deleteSource);

		try {
			myHapiTransactionService
					.withRequest(theRequestDetails)
					.execute(() -> doCrossPartitionForwardSteps(
							theMergeOperationParameters,
							theSourceResource,
							theTargetResource,
							theRequestDetails,
							theMergeOutcome,
							theStartTime,
							rollbackContext));
		} catch (Exception forwardFailure) {
			ourLog.error("Cross-partition merge failed; rolling back", forwardFailure);
			rollbackContext.setFailureCause(forwardFailure);
			if (!myHapiTransactionService.isRequiresNewTransactionWhenChangingPartitions()) {
				// Partition changes joined the outer transaction, which has already rolled the whole merge back, so
				// there is nothing to revert — just report it.
				myCrossPartitionMergeRollbackService.reportFullyRolledBackByOuterTransaction(
						rollbackContext, theMergeOutcome);
				return;
			}
			// Partition changes committed independently, so the committed steps are durable and must be reverted. If
			// the data bundle itself partially committed before failing, its committed entries are carried on the
			// exception rather than recorded in the context, so harvest them here.
			if (forwardFailure instanceof PartitionedTransactionPartialFailureException partialFailure) {
				extractCommittedResourceIdsByPartition(partialFailure, theRequestDetails)
						.forEach(rollbackContext::addCommittedResourceIds);
			}
			myCrossPartitionMergeRollbackService.rollbackPartialCrossPartitionMerge(
					rollbackContext, theRequestDetails, theMergeOutcome);
			return;
		}

		addInfoToOperationOutcome(
				myFhirContext, theMergeOutcome.getOperationOutcome(), null, "Merge operation completed successfully.");
	}

	private void doCrossPartitionForwardSteps(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime,
			MergeRollbackContext theRollbackContext) {

		String provenanceGroupId = generateProvenanceGroupId(theSourceResource, theTargetResource);

		// Step 1: Data bundle — copy compartment resources and replace references
		CrossPartitionReplaceReferencesResult copyResult =
				myCrossPartitionReplaceReferencesSvc.copyCompartmentResourcesAndReplaceReferences(
						theSourceResource, theTargetResource, theRequestDetails);

		Map<RequestPartitionId, List<IIdType>> committedByPartition = copyResult.getCommittedResourcesByPartition();
		// The originals will be deleted in Step 5, which bumps each version by one. Anticipate that post-delete version
		// once
		// here and reuse it for the per-partition Provenances (Step 2) and the rollback context.
		Map<RequestPartitionId, List<IIdType>> deletedResourceIdsByPartition =
				newMapWithVersionIncremented(copyResult.getCopiedResourceOriginalIdsByPartition());

		// Record what committed so a failure in a later step can be rolled back, keeping the partition each group
		// was written to (the copy result already groups by partition). The originals are recorded at their
		// anticipated tombstone version even though they are not deleted until Step 5 — the rollback only undeletes
		// them if the delete bundle committed.
		committedByPartition.forEach(theRollbackContext::addCommittedResourceIds);
		deletedResourceIdsByPartition.forEach(theRollbackContext::addDeletedOriginalResourceIds);

		// Step 2: Per-partition Provenances (sequential, continue-on-failure).
		// Each includes [tgt, src, partition-specific committed + tombstone refs].
		// Source and target are referenced at their current versions (pre-update) since
		// the src/tgt update hasn't happened yet at this point.
		if (theMergeOperationParameters.getCreateProvenance()) {
			IIdType sourceId = theSourceResource.getIdElement();
			IIdType targetId = theTargetResource.getIdElement();

			Set<RequestPartitionId> allPartitions = new LinkedHashSet<>();
			allPartitions.addAll(committedByPartition.keySet());
			allPartitions.addAll(deletedResourceIdsByPartition.keySet());

			for (RequestPartitionId partition : allPartitions) {
				List<IIdType> partitionRefs = new ArrayList<>();

				List<IIdType> committedInPartition = committedByPartition.getOrDefault(partition, List.of());
				partitionRefs.addAll(committedInPartition);

				partitionRefs.addAll(deletedResourceIdsByPartition.getOrDefault(partition, List.of()));

				IIdType subProvenanceId = myMergeResourceHelper.createProvenance(
						sourceId,
						targetId,
						partitionRefs,
						provenanceGroupId,
						theRequestDetails,
						theStartTime,
						theMergeOperationParameters.getProvenanceAgents(),
						List.of());
				if (subProvenanceId != null) {
					theRollbackContext.getCreatedSubProvenanceIds().add(subProvenanceId);
				}
			}
		}

		// Step 3-5: Update source/target, create main Provenance, delete originals (the source-side compartment copies
		// here; finalizeMerge adds the source itself when deleteSource).
		finalizeMerge(
				theMergeOperationParameters,
				theSourceResource,
				theTargetResource,
				List.of(),
				provenanceGroupId,
				copyResult.getCopiedResourceOriginalIds(),
				theRequestDetails,
				theMergeOutcome,
				theStartTime,
				theRollbackContext);
	}

	/**
	 * Recovers the versioned ids of the resources that committed before a partitioned data bundle failed partway,
	 * from the {@link PartitionedTransactionPartialFailureException} that carries the committed response entries,
	 * grouped by the partition they were committed to. The exception already groups its response entries per
	 * partition (one inner list per committed sub-bundle), and every entry in an inner list shares a partition — so
	 * the partition is derived once per list (from its first ref) rather than per resource. No-op responses
	 * (no change) are skipped — nothing committed for those.
	 */
	private Map<RequestPartitionId, List<IIdType>> extractCommittedResourceIdsByPartition(
			PartitionedTransactionPartialFailureException theFailure, RequestDetails theRequestDetails) {
		Map<RequestPartitionId, List<IIdType>> committedByPartition = new LinkedHashMap<>();
		for (List<IBase> partitionEntries : theFailure.getCommittedResponseEntriesPerPartition()) {
			// Collect the versioned id of every resource this partition actually changed, skipping entries
			// with no location or a no-change outcome (nothing was committed for those).
			List<IIdType> committedRefsInPartition = new ArrayList<>();
			for (IBase entry : partitionEntries) {
				ReplaceReferencesProvenanceSvc.extractChangedResourceId((Bundle.BundleEntryComponent) entry)
						.ifPresent(committedRefsInPartition::add);
			}
			if (!committedRefsInPartition.isEmpty()) {
				// One inner list per committed sub-bundle, and the partitioning groups entries by partition, so
				// each partition appears at most once here — we can set the list directly.
				RequestPartitionId partition = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
						theRequestDetails, committedRefsInPartition.get(0).toUnqualifiedVersionless());
				committedByPartition.put(partition, committedRefsInPartition);
			}
		}
		return committedByPartition;
	}

	private void finalizeMerge(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			List<IIdType> theProvenanceTargetIds,
			@Nullable String theProvenanceGroupId,
			List<IIdType> theAdditionalResourceIdsToDelete,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime,
			@Nullable MergeRollbackContext theRollbackContext) {

		DaoMethodOutcome outcome = myMergeResourceHelper.updateMergedResourcesAfterReferencesReplaced(
				theSourceResource,
				theTargetResource,
				theMergeOperationParameters.getResultResource(),
				theMergeOperationParameters.getDeleteSource(),
				theRequestDetails);

		theMergeOutcome.setUpdatedTargetResource(outcome.getResource());

		// The source's post-merge versioned ref: its post-update version when kept, or its post-delete version
		// (bumped by one) when deleteSource, since the delete happens later in Step 5.
		IIdType sourcePostMergeId = theSourceResource.getIdElement();
		if (theMergeOperationParameters.getDeleteSource()) {
			sourcePostMergeId =
					sourcePostMergeId.withVersion(Long.toString(sourcePostMergeId.getVersionIdPartAsLong() + 1));
		}

		// Record the post-merge source resource and target resource (with the partition each lives on) so a
		// later-step failure can restore them.
		if (theRollbackContext != null) {
			theRollbackContext.setTargetPostMergeId(
					outcome.getResource().getIdElement(),
					RequestPartitionId.getPartitionFromUserDataIfPresent(theTargetResource)
							.orElse(null));
			theRollbackContext.setSourcePostMergeId(
					sourcePostMergeId,
					RequestPartitionId.getPartitionFromUserDataIfPresent(theSourceResource)
							.orElse(null));
		}

		if (theMergeOperationParameters.getCreateProvenance()) {
			IIdType targetPostMergeId = outcome.getResource().getIdElement();

			List<IBaseResource> containedResources =
					List.of(theMergeOperationParameters.getOriginalInputParameters(), outcome.getOperationOutcome());

			IIdType mainProvenanceId = myMergeResourceHelper.createProvenance(
					sourcePostMergeId,
					targetPostMergeId,
					theProvenanceTargetIds,
					theProvenanceGroupId,
					theRequestDetails,
					theStartTime,
					theMergeOperationParameters.getProvenanceAgents(),
					containedResources);
			if (theRollbackContext != null) {
				theRollbackContext.setMainProvenanceId(mainProvenanceId);
			}
		}

		// Step 5: delete the originals copied across partitions (if any) plus the source itself when deleteSource.
		List<IIdType> resourceIdsToDelete = new ArrayList<>(theAdditionalResourceIdsToDelete);
		if (theMergeOperationParameters.getDeleteSource()) {
			resourceIdsToDelete.add(theSourceResource.getIdElement());
		}
		if (!resourceIdsToDelete.isEmpty()) {
			deleteResources(resourceIdsToDelete, theRequestDetails);
			if (theRollbackContext != null) {
				theRollbackContext.markDeletesCommitted();
			}
		}
	}

	/**
	 * Returns a copy of the given per-partition ids with each id's version incremented by one, preserving partition
	 * order. Used to anticipate the post-delete version of resources a cross-partition merge will delete.
	 */
	private static Map<RequestPartitionId, List<IIdType>> newMapWithVersionIncremented(
			Map<RequestPartitionId, List<IIdType>> theIdsByPartition) {
		Map<RequestPartitionId, List<IIdType>> result = new LinkedHashMap<>();
		for (Map.Entry<RequestPartitionId, List<IIdType>> entry : theIdsByPartition.entrySet()) {
			List<IIdType> incrementedIds = new ArrayList<>();
			for (IIdType id : entry.getValue()) {
				incrementedIds.add(id.withVersion(Long.toString(id.getVersionIdPartAsLong() + 1)));
			}
			result.put(entry.getKey(), incrementedIds);
		}
		return result;
	}

	private static String generateProvenanceGroupId(IBaseResource theSourceResource, IBaseResource theTargetResource) {
		String src = theSourceResource.getIdElement().getIdPart();
		String tgt = theTargetResource.getIdElement().getIdPart();
		String resourceType = theSourceResource.getIdElement().getResourceType();
		String uuid = UUID.randomUUID().toString();
		return "merge-" + resourceType + "-" + src + "-" + tgt + "-" + uuid;
	}

	private void doMergeAsync(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			RequestPartitionId thePartitionId) {

		String operationName = theRequestDetails.getOperation();
		MergeJobParameters mergeJobParameters = theMergeOperationParameters.asMergeJobParameters(
				myFhirContext, myStorageSettings, theSourceResource, theTargetResource, thePartitionId, operationName);

		Task task = myBatch2TaskHelper.startJobAndCreateAssociatedTask(
				myTaskDao, theRequestDetails, myJobCoordinator, JOB_MERGE, mergeJobParameters);

		task.setIdElement(task.getIdElement().toUnqualifiedVersionless());
		task.getMeta().setVersionId(null);
		theMergeOutcome.setTask(task);
		theMergeOutcome.setHttpStatusCode(STATUS_HTTP_202_ACCEPTED);

		String detailsText = "Merge request is accepted, and will be processed asynchronously. See"
				+ " task resource returned in this response for details.";
		addInfoToOperationOutcome(myFhirContext, theMergeOutcome.getOperationOutcome(), null, detailsText);
	}

	private Integer countResourcesReferencingResource(IIdType theResourceId, RequestDetails theRequestDetails) {
		return myHapiTransactionService
				.withRequest(theRequestDetails)
				.execute(() -> myResourceLinkDao.countResourcesTargetingFhirTypeAndFhirId(
						theResourceId.getResourceType(), theResourceId.getIdPart()));
	}

	private List<Bundle> replaceReferencesInNestedTransaction(
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			int theResourceLimit,
			RequestPartitionId thePartitionId,
			RequestDetails theRequestDetails) {
		ReplaceReferencesRequest replaceReferencesRequest = new ReplaceReferencesRequest(
				theSourceResource.getIdElement(),
				theTargetResource.getIdElement(),
				theResourceLimit,
				thePartitionId,
				false,
				null);
		List<IdDt> resourceIds;
		try (Stream<IdDt> stream = myResourceLinkDao.streamSourceIdsForTargetFhirId(
				replaceReferencesRequest.sourceId.getResourceType(), replaceReferencesRequest.sourceId.getIdPart())) {
			resourceIds = stream.toList();
		}
		Bundle result = myReplaceReferencesPatchBundleSvc.patchReferencingResourcesInNestedTransaction(
				replaceReferencesRequest, resourceIds, theRequestDetails);
		return List.of(result);
	}

	private void deleteResources(List<IIdType> theResourcesToDelete, RequestDetails theRequestDetails) {
		BundleBuilder deleteBuilder = new BundleBuilder(myFhirContext);
		for (IIdType id : theResourcesToDelete) {
			deleteBuilder.addTransactionDeleteEntry(id);
		}
		myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, deleteBuilder.getBundle());
	}

	private void validateCrossPartitionAsyncNotSupported(
			IBaseResource theSourceResource, IBaseResource theTargetResource, RequestDetails theRequestDetails) {
		if (isCrossPartitionMerge(theSourceResource, theTargetResource) && theRequestDetails.isPreferAsync()) {
			throw new NotImplementedOperationException(
					Msg.code(2881) + "Cross-partition merge does not support asynchronous processing.");
		}
	}

	private void validateResourceLimit(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			RequestDetails theRequestDetails,
			boolean theCrossPartition) {
		Integer referencingResourceCount = countResourcesReferencingResource(
				theSourceResource.getIdElement().toVersionless(), theRequestDetails);
		if (referencingResourceCount > theMergeOperationParameters.getResourceLimit()) {
			String message = "Number of resources with references to "
					+ theSourceResource.getIdElement().toVersionless()
					+ " exceeds the resource-limit "
					+ theMergeOperationParameters.getResourceLimit();
			if (!theCrossPartition) {
				message += ". Submit the request asynchronously by adding the HTTP Header 'Prefer: respond-async'.";
			}
			throw new PreconditionFailedException(Msg.code(2880) + message);
		}
	}

	private boolean isCrossPartitionMerge(IBaseResource theSourceResource, IBaseResource theTargetResource) {
		if (!myPartitionSettings.isPartitioningEnabled()) {
			return false;
		}
		Optional<RequestPartitionId> srcPart = RequestPartitionId.getPartitionFromUserDataIfPresent(theSourceResource);
		Optional<RequestPartitionId> tgtPart = RequestPartitionId.getPartitionFromUserDataIfPresent(theTargetResource);
		return srcPart.isPresent() && tgtPart.isPresent() && !srcPart.get().equals(tgtPart.get());
	}
}
