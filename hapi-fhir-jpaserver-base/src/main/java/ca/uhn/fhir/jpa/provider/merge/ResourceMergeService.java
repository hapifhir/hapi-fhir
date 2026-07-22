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
import ca.uhn.fhir.merge.MergeProvenanceGroupIdUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.merge.MergeAppCtx.JOB_MERGE;
import static ca.uhn.fhir.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.merge.MergeResourceHelper.addInfoToOperationOutcome;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_202_ACCEPTED;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * Service for the FHIR Patient/$merge and [ResourceType]/$hapi.fhir.merge (generic merge) operations.
 */
public class ResourceMergeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceMergeService.class);

	private static final String ISSUE_TYPE_EXCEPTION = "exception";

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
			PartitionSettings thePartitionSettings) {
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
			// transaction under REQUIRES_NEW), so the outer transaction cannot guarantee
			// atomicity. doMergeSyncCrossPartition owns the transaction boundary and populates the outcome itself,
			// reporting anything a partial failure left committed.
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
			doMergeSyncSamePartition(
					theMergeOperationParameters,
					theSourceResource,
					theTargetResource,
					partitionId,
					theRequestDetails,
					theMergeOutcome,
					startTime);
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

		// The outer tx is intentionally not pinned to a partition: per-operation partition resolution
		// handles writes (per-bundle-entry resolution in transactionNested).
		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			List<Bundle> responseBundles = replaceReferencesInNestedTransaction(
					theSourceResource,
					theTargetResource,
					theMergeOperationParameters.getResourceLimit(),
					thePartitionId,
					theRequestDetails);
			List<IIdType> changedResourceIds =
					ReplaceReferencesProvenanceSvc.extractChangedResourceIds(responseBundles);

			// Finalize the merge: update source/target, create the Provenance, delete the source when
			// requested. The whole merge runs in this one transaction, so there is no rollback bookkeeping here.
			DaoMethodOutcome outcome = myMergeResourceHelper.updateMergedResourcesAfterReferencesReplaced(
					theSourceResource,
					theTargetResource,
					theMergeOperationParameters.getResultResource(),
					theMergeOperationParameters.getDeleteSource(),
					theRequestDetails);
			theMergeOutcome.setUpdatedTargetResource(outcome.getResource());

			if (theMergeOperationParameters.getCreateProvenance()) {
				// The source is referenced at its post-merge version: its post-update version when kept, or its
				// post-delete version (bumped by one) when deleteSource, since the delete happens below.
				IIdType sourcePostMergeId = theSourceResource.getIdElement();
				if (theMergeOperationParameters.getDeleteSource()) {
					sourcePostMergeId = sourcePostMergeId.withVersion(
							Long.toString(sourcePostMergeId.getVersionIdPartAsLong() + 1));
				}
				List<IBaseResource> containedResources = List.of(
						theMergeOperationParameters.getOriginalInputParameters(), outcome.getOperationOutcome());
				myMergeResourceHelper.createProvenance(
						sourcePostMergeId,
						outcome.getResource().getIdElement(),
						changedResourceIds,
						null,
						theRequestDetails,
						theStartTime,
						theMergeOperationParameters.getProvenanceAgents(),
						containedResources);
			}

			if (theMergeOperationParameters.getDeleteSource()) {
				deleteResources(List.of(theSourceResource.getIdElement()), theRequestDetails);
			}
		});

		addMergeCompletedSuccessfully(theMergeOutcome);
	}

	private void addMergeCompletedSuccessfully(MergeOperationOutcome theMergeOutcome) {
		addInfoToOperationOutcome(
				myFhirContext, theMergeOutcome.getOperationOutcome(), null, "Merge operation completed successfully.");
	}

	private void doMergeSyncCrossPartition(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime) {

		// Everything the forward steps commit, recorded as it commits, at its committed version. A cross-partition
		// merge cannot be made atomic, and this path does not attempt to revert what committed — so this list is
		// purely what the failure report hands the operator to revert manually.
		List<IIdType> committedResourceIds = new ArrayList<>();

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
							committedResourceIds));
		} catch (Exception theException) {
			if (!myHapiTransactionService.isRequiresNewTransactionWhenChangingPartitions()) {
				// Partition changes joined the outer transaction, which has already rolled the whole merge back, so
				// nothing is committed. This is a clean failure — propagate it like a same-partition merge failure
				// and let merge() turn it into the outcome.
				throw theException;
			}
			ourLog.error("Cross-partition merge failed", theException);
			Throwable failureCause = theException;
			if (theException instanceof PartitionedTransactionPartialFailureException partialFailure) {
				// A cross-partition transaction in the forward steps committed some sub-bundles before a later one
				// failed. Those committed entries are carried on the exception rather than recorded above (the step
				// records what it committed only after it returns), so harvest them here.
				committedResourceIds.addAll(extractCommittedResourceIds(partialFailure));
				// Report the underlying cause (e.g. the failing write's exception), not the partial-failure wrapper,
				// so the merge outcome reflects the real error and its HTTP status.
				if (partialFailure.getCause() != null) {
					failureCause = partialFailure.getCause();
				}
			}
			reportFailedCrossPartitionMerge(committedResourceIds, failureCause, theMergeOutcome);
			return;
		}

		addMergeCompletedSuccessfully(theMergeOutcome);
	}

	/**
	 * Runs the cross-partition merge as a sequence of separate steps (NOT one atomic transaction, since per-partition
	 * writes commit independently on MegaScale): copy the source compartment to the target partition and replace
	 * references (its own cross-partition transaction), update source/target, create the merge Provenances, and
	 * delete the originals (+ source when deleteSource). The Provenances are created before the delete so they can
	 * reference the to-be-deleted originals at their anticipated tombstone version (current + 1).
	 * <p>
	 * One Provenance is created per involved partition, each listing only that partition's changed resources
	 * (plus the post-merge target and source as its first two targets), and carrying the shared group id extension
	 * with a {@code ;partition=} suffix naming its partition. A main Provenance (bare group id, the merge input
	 * {@link org.hl7.fhir.r4.model.Parameters} and target-update OperationOutcome contained) is created last as the
	 * "merge succeeded" signal. The undo operation restores the merge provenance-by-provenance, pinned to the
	 * partition each Provenance's extension names — so it never needs cross-shard fan-out reads, which would
	 * resolve through MegaScale ESR surrogate rows and fail on tombstoned originals.
	 * <p>
	 * Each committed step appends what it committed to {@code theCommittedResourceIds}. None of it is reverted if a
	 * later step fails — the outer transaction cannot roll it back (the partition changes committed in their own
	 * transactions) and this path does not compensate — so the list exists solely to report what a failed merge left
	 * behind for manual reverting.
	 */
	private void doCrossPartitionForwardSteps(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime,
			List<IIdType> theCommittedResourceIds) {

		boolean deleteSource = theMergeOperationParameters.getDeleteSource();
		RequestPartitionId sourcePartition = getRequiredPartition(theSourceResource);
		RequestPartitionId targetPartition = getRequiredPartition(theTargetResource);

		// 1) Copy the source compartment resources to the target partition and replace references, as its own
		// cross-partition transaction. The originals are NOT deleted here — they are deleted in step 4, after the
		// Provenances are created, so the Provenances can reference them.
		CrossPartitionReplaceReferencesResult copyResult =
				myCrossPartitionReplaceReferencesSvc.copyCompartmentResourcesAndReplaceReferences(
						theSourceResource, theTargetResource, theRequestDetails);
		List<IIdType> copiedResourceOriginalIds = copyResult.getCopiedResourceOriginalIds();

		// Record the just-committed copies and referrer updates.
		theCommittedResourceIds.addAll(copyResult.getChangedResourceIds());

		// 2) Update source/target. The target is always updated; the source is updated when kept, or deleted below.
		DaoMethodOutcome outcome = myMergeResourceHelper.updateMergedResourcesAfterReferencesReplaced(
				theSourceResource,
				theTargetResource,
				theMergeOperationParameters.getResultResource(),
				deleteSource,
				theRequestDetails);
		theMergeOutcome.setUpdatedTargetResource(outcome.getResource());

		// The source's post-merge versioned ref: its post-update version when kept, or its post-delete version
		// (bumped by one) when deleteSource, since the delete happens below.
		IIdType sourcePostMergeId = theSourceResource.getIdElement();
		if (deleteSource) {
			sourcePostMergeId = withVersionIncremented(sourcePostMergeId);
		}
		IIdType targetPostMergeId = outcome.getResource().getIdElement();

		// Record the post-merge target (always updated), and the post-merge source when it is kept. When the source
		// is deleted, its tombstone is recorded below instead, as its delete commits.
		theCommittedResourceIds.add(targetPostMergeId);
		if (!deleteSource) {
			theCommittedResourceIds.add(sourcePostMergeId);
		}

		// 3) Create the merge Provenances: one per involved partition plus the main one.
		if (theMergeOperationParameters.getCreateProvenance()) {
			createCrossPartitionProvenances(
					theMergeOperationParameters,
					copyResult,
					sourcePostMergeId,
					sourcePartition,
					targetPostMergeId,
					targetPartition,
					outcome,
					theStartTime,
					theRequestDetails,
					theCommittedResourceIds);
		}

		// 4) Delete the source-side compartment originals copied across partitions, plus the source itself when
		// deleteSource. They all live in the source partition, so the deletes run in one transaction pinned to it —
		// this lets MegaScale route them to the correct shard even when the ids are not partition-decodable
		// (client-assigned / UUID ids), and makes this step atomic: a failure rolls back every delete, so either
		// all tombstones are recorded or none are. This happens after the Provenances are created so they can
		// reference the originals.
		List<IIdType> resourceIdsToDelete = new ArrayList<>(copiedResourceOriginalIds);
		if (deleteSource) {
			resourceIdsToDelete.add(theSourceResource.getIdElement());
		}
		if (!resourceIdsToDelete.isEmpty()) {
			theCommittedResourceIds.addAll(MergeResourceHelper.deleteResourcesInPartitionTransaction(
					resourceIdsToDelete, sourcePartition, myDaoRegistry, myHapiTransactionService));
		}
	}

	/**
	 * Creates the Provenances recording a cross-partition merge: one sub-Provenance per partition that has changed
	 * resources, plus the main Provenance. Every Provenance's first two targets are the post-merge target and
	 * source; a sub-Provenance's remaining targets are the resources changed in its partition — the copies and
	 * referrer updates committed there, the originals at their anticipated tombstone version (current + 1, bumped
	 * by the deletes that follow), the post-merge source in the source partition's sub, and the post-merge target
	 * in the target partition's sub (unless its update was a no-op). The main Provenance lists no changed
	 * resources; it carries the merge input Parameters and the target-update OperationOutcome as contained
	 * resources, and is created last as the "merge succeeded" signal.
	 */
	private void createCrossPartitionProvenances(
			MergeOperationInputParameters theMergeOperationParameters,
			CrossPartitionReplaceReferencesResult theCopyResult,
			IIdType theSourcePostMergeId,
			RequestPartitionId theSourcePartition,
			IIdType theTargetPostMergeId,
			RequestPartitionId theTargetPartition,
			DaoMethodOutcome theTargetUpdateOutcome,
			Date theStartTime,
			RequestDetails theRequestDetails,
			List<IIdType> theCommittedResourceIds) {

		String groupIdPrefix =
				MergeProvenanceGroupIdUtil.generateGroupIdPrefix(theSourcePostMergeId, theTargetPostMergeId);

		// Collect each partition's changed resources: the committed copies and referrer updates, the originals at
		// their anticipated tombstone version, and the post-merge source/target in their own partitions.
		Map<RequestPartitionId, List<IIdType>> changedResourcesByPartition = new LinkedHashMap<>();
		theCopyResult.getChangedResourceIdsByPartition().forEach((partition, ids) -> changedResourcesByPartition
				.computeIfAbsent(partition, k -> new ArrayList<>())
				.addAll(ids));
		theCopyResult
				.getCopiedResourceOriginalIdsByPartition()
				.forEach((partition, ids) -> ids.forEach(id -> changedResourcesByPartition
						.computeIfAbsent(partition, k -> new ArrayList<>())
						.add(withVersionIncremented(id))));
		changedResourcesByPartition
				.computeIfAbsent(theSourcePartition, k -> new ArrayList<>())
				.add(theSourcePostMergeId);
		if (!theTargetUpdateOutcome.isNop()) {
			changedResourcesByPartition
					.computeIfAbsent(theTargetPartition, k -> new ArrayList<>())
					.add(theTargetPostMergeId);
		}

		for (Map.Entry<RequestPartitionId, List<IIdType>> entry : changedResourcesByPartition.entrySet()) {
			String groupId = MergeProvenanceGroupIdUtil.buildGroupId(groupIdPrefix, entry.getKey());
			IIdType subProvenanceId = myMergeResourceHelper.createProvenance(
					theSourcePostMergeId,
					theTargetPostMergeId,
					entry.getValue(),
					groupId,
					theRequestDetails,
					theStartTime,
					theMergeOperationParameters.getProvenanceAgents(),
					List.of());
			if (subProvenanceId != null) {
				theCommittedResourceIds.add(subProvenanceId);
			}
		}

		// The main Provenance carries the bare group id prefix (no partition suffix) — it records no
		// partition-specific changes itself.
		List<IBaseResource> containedResources = List.of(
				theMergeOperationParameters.getOriginalInputParameters(), theTargetUpdateOutcome.getOperationOutcome());
		IIdType mainProvenanceId = myMergeResourceHelper.createProvenance(
				theSourcePostMergeId,
				theTargetPostMergeId,
				List.of(),
				groupIdPrefix,
				theRequestDetails,
				theStartTime,
				theMergeOperationParameters.getProvenanceAgents(),
				containedResources);
		if (mainProvenanceId != null) {
			theCommittedResourceIds.add(mainProvenanceId);
		}
	}

	/**
	 * Populates the outcome for a cross-partition merge that failed after one or more of its steps had already
	 * committed. A cross-partition merge is not atomic — partition changes commit in their own transactions — and
	 * nothing here is reverted, so whatever committed is left in place and reported for manual reverting.
	 *
	 * <p>Note that the merge Provenances are reported alongside the data when they had already been created: a
	 * failure after step 3 leaves the whole Provenance group behind, including the main "merge succeeded" Provenance,
	 * so until an operator removes them the failed merge is indistinguishable from a completed one and
	 * {@code $hapi.fhir.undo-merge} will act on it. This is a deliberate limitation of not rolling back.
	 *
	 * @param theCommittedResourceIds everything the forward steps committed, at its committed version
	 * @param theFailureCause the exception that ended the merge
	 * @param theOutcome the outcome to populate
	 */
	private void reportFailedCrossPartitionMerge(
			List<IIdType> theCommittedResourceIds,
			@Nullable Throwable theFailureCause,
			OperationOutcomeWithStatusCode theOutcome) {

		String msg;
		int statusCode;
		if (theCommittedResourceIds.isEmpty()) {
			// Nothing committed, so the merge failed cleanly and no resource is in a merged state. Report the
			// triggering exception's own status, just as the outer-transaction (nothing-committed) path does.
			statusCode = resolveHttpStatusCode(theFailureCause);
			msg = format(
					"Cross-partition merge failed; no resources were committed. Merge failure cause: %s",
					describeFailureCause(theFailureCause));
		} else {
			// Resources are left in their merged state — a server-side inconsistency regardless of what triggered
			// the merge failure, hence always 500. Each is reported by its versioned id: this is the post-merge
			// version the resource is left at, which points an operator straight at the state to revert.
			statusCode = STATUS_HTTP_500_INTERNAL_ERROR;
			String committedIds = theCommittedResourceIds.stream()
					.map(id -> id.toUnqualified().getValue())
					.collect(joining(", "));
			msg = format(
					"Cross-partition merge failed partway through and was not rolled back. The following resources "
							+ "were committed and remain in their merged state, and must be reverted manually: %s. "
							+ "Merge failure cause: %s",
					committedIds, describeFailureCause(theFailureCause));
		}
		theOutcome.setHttpStatusCode(statusCode);
		addErrorToOperationOutcome(myFhirContext, theOutcome.getOperationOutcome(), msg, ISSUE_TYPE_EXCEPTION);
	}

	private static int resolveHttpStatusCode(@Nullable Throwable theFailureCause) {
		if (theFailureCause instanceof BaseServerResponseException serverException) {
			return serverException.getStatusCode();
		}
		return STATUS_HTTP_500_INTERNAL_ERROR;
	}

	private static String describeFailureCause(@Nullable Throwable theFailureCause) {
		if (theFailureCause == null) {
			return "unknown";
		}
		String type = theFailureCause.getClass().getSimpleName();
		String message = theFailureCause.getMessage();
		return message != null ? type + ": " + message : type;
	}

	private static IIdType withVersionIncremented(IIdType theId) {
		return theId.withVersion(Long.toString(theId.getVersionIdPartAsLong() + 1));
	}

	private static RequestPartitionId getRequiredPartition(IBaseResource theResource) {
		return RequestPartitionId.getPartitionFromUserDataIfPresent(theResource)
				.orElseThrow(() -> new IllegalStateException(
						"Resource " + theResource.getIdElement().getValue() + " has no partition info"));
	}

	/**
	 * Recovers the versioned ids of the resources that committed before a partitioned transaction failed partway, from
	 * the {@link PartitionedTransactionPartialFailureException} that carries the committed response entries. The
	 * exception carries the committed entries grouped per committed sub-bundle; no-op responses (no change) are skipped
	 * — nothing committed for those.
	 */
	private List<IIdType> extractCommittedResourceIds(PartitionedTransactionPartialFailureException theFailure) {
		List<IIdType> committed = new ArrayList<>();
		for (List<IBase> subBundleEntries : theFailure.getCommittedResponseEntriesPerSubBundle()) {
			for (IBase entry : subBundleEntries) {
				ReplaceReferencesProvenanceSvc.extractChangedResourceId((Bundle.BundleEntryComponent) entry)
						.ifPresent(committed::add);
			}
		}
		return committed;
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
