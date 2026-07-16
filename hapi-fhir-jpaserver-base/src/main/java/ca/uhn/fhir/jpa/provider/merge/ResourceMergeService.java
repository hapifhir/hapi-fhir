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
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.PartitionedTransactionPartialFailureException;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictUtil;
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
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.OperationOutcomeUtil;
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
import java.util.List;
import java.util.Optional;
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
			// transaction under REQUIRES_NEW), so the outer transaction cannot guarantee
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

		MergeRollbackContext rollbackContext = new MergeRollbackContext();

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
		} catch (Exception theException) {
			if (!myHapiTransactionService.isRequiresNewTransactionWhenChangingPartitions()) {
				// Partition changes joined the outer transaction, which has already rolled the whole merge back, so
				// there is nothing committed to revert. This is a clean failure — propagate it like a same-partition
				// merge failure and let merge() turn it into the outcome.
				throw theException;
			}
			ourLog.error("Cross-partition merge failed; attempting rollback", theException);
			Throwable failureCause = theException;
			if (theException instanceof PartitionedTransactionPartialFailureException partialFailure) {
				// A PartitionedTransactionPartialFailureException means a cross-partition transaction (the copy/
				// replace-references step, or the delete step) committed some per-shard sub-bundles before a later one
				// failed. Those committed entries are carried on the exception rather than recorded in the rollback
				// context (the step records into the context only after it returns), so harvest them here.
				rollbackContext.addResourcesToRevert(extractCommittedResourceIds(partialFailure));
				// Report the underlying cause (e.g. the failing write's exception), not the partial-failure wrapper,
				// so the merge outcome reflects the real error and its HTTP status.
				if (partialFailure.getCause() != null) {
					failureCause = partialFailure.getCause();
				}
			}
			rollbackContext.setFailureCause(failureCause);
			myCrossPartitionMergeRollbackService.rollbackPartialCrossPartitionMerge(
					rollbackContext, theRequestDetails, theMergeOutcome);
			return;
		}

		addMergeCompletedSuccessfully(theMergeOutcome);
	}

	/**
	 * Runs the cross-partition merge as a sequence of separate steps (NOT one atomic transaction, since per-partition
	 * writes commit independently on MegaScale): copy the source compartment to the target partition and replace
	 * references (its own cross-partition transaction), update source/target, create the single merge Provenance, and
	 * delete the originals (+ source when deleteSource). The Provenance is created before the delete so it can
	 * reference the to-be-deleted originals at their anticipated tombstone version (current + 1); the cross-shard
	 * targets it references are still live at that point and resolve via the shard fan-out.
	 * <p>
	 * Each committed step is recorded in {@code theRollbackContext} so that a failure in a later step can revert the
	 * earlier ones (the outer transaction cannot, because the partition changes committed in their own transactions).
	 */
	private void doCrossPartitionForwardSteps(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime,
			MergeRollbackContext theRollbackContext) {

		boolean deleteSource = theMergeOperationParameters.getDeleteSource();

		// 1) Copy the source compartment resources to the target partition and replace references, as its own
		// cross-partition transaction. The originals are NOT deleted here — they are deleted in step 4, after the
		// Provenance is created, so the Provenance can reference them.
		CrossPartitionReplaceReferencesResult copyResult =
				myCrossPartitionReplaceReferencesSvc.copyCompartmentResourcesAndReplaceReferences(
						theSourceResource, theTargetResource, theRequestDetails);
		List<IIdType> changedResourceIds = new ArrayList<>(copyResult.getChangedResourceIds());
		List<IIdType> copiedResourceOriginalIds = copyResult.getCopiedResourceOriginalIds();

		// Record the just-committed copies and referrer updates as resources to revert on rollback.
		theRollbackContext.addResourcesToRevert(changedResourceIds);

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

		// Record the post-merge target (always updated), and the post-merge source when it is kept, as resources to
		// revert. When the source is deleted it is recorded as an undelete instead (below, after the delete commits).
		theRollbackContext.addResourceToRevert(targetPostMergeId);
		if (!deleteSource) {
			theRollbackContext.addResourceToRevert(sourcePostMergeId);
		}

		// 3) Create the single merge Provenance (the "merge succeeded" signal), referencing the post-merge source and
		// target. Its changed-resource references are the copies and referrer updates, plus the to-be-deleted
		// originals at their tombstone version (current + 1, since the delete below bumps each by one).
		if (theMergeOperationParameters.getCreateProvenance()) {
			for (IIdType id : copiedResourceOriginalIds) {
				changedResourceIds.add(withVersionIncremented(id));
			}
			List<IBaseResource> containedResources =
					List.of(theMergeOperationParameters.getOriginalInputParameters(), outcome.getOperationOutcome());
			IIdType provenanceId = myMergeResourceHelper.createProvenance(
					sourcePostMergeId,
					targetPostMergeId,
					changedResourceIds,
					// no correlation id: a merge records a single Provenance, nothing to correlate
					null,
					theRequestDetails,
					theStartTime,
					theMergeOperationParameters.getProvenanceAgents(),
					containedResources);
			theRollbackContext.setProvenanceId(provenanceId);
		}

		// 4) Delete the source-side compartment originals copied across partitions, plus the source itself when
		// deleteSource. They all live in the source partition, so the delete is pinned to it — this lets MegaScale
		// route the delete to the correct shard even when the ids are not partition-decodable (client-assigned / UUID
		// ids). This happens after the Provenance is created so the Provenance can reference the originals.
		List<IIdType> resourceIdsToDelete = new ArrayList<>(copiedResourceOriginalIds);
		if (deleteSource) {
			resourceIdsToDelete.add(theSourceResource.getIdElement());
		}
		if (!resourceIdsToDelete.isEmpty()) {
			// SHORTCUT (debugging): delete the originals one-by-one with the partition hardcoded to the source
			// resource's partition, instead of a DELETE transaction bundle. This bypasses the MegaScale
			// transaction splitter, which cannot determine a partition for a DELETE entry whose id is
			// client-assigned (non-partition-decodable). Safe here because every id being deleted lives in the
			// source resource's partition.
			RequestPartitionId sourcePartition = getRequiredPartition(theSourceResource);
			deleteResourcesOneByOne(resourceIdsToDelete, sourcePartition, theRequestDetails);
		}

		// The delete committed, so record the now-tombstoned resources (at their tombstone version, original + 1) as
		// undeletes for rollback. The source's tombstone version is sourcePostMergeId (original + 1).
		for (IIdType id : copiedResourceOriginalIds) {
			theRollbackContext.addResourceToUndelete(withVersionIncremented(id));
		}
		if (deleteSource) {
			theRollbackContext.addResourceToUndelete(sourcePostMergeId);
		}
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

	/**
	 * Deletes each resource individually (a single-resource DAO delete per id) with the partition pinned to
	 * {@code thePartition}, instead of submitting one DELETE transaction bundle. Because these are not system
	 * transactions, they do not pass through the MegaScale transaction-splitting interceptor, which cannot
	 * determine a partition for a DELETE entry whose id is client-assigned (non-partition-decodable). In the
	 * cross-partition merge every resource being deleted (the copied compartment originals, plus the source
	 * resource when {@code deleteSource}) lives in the source resource's partition, so pinning them all to that
	 * single partition is safe.
	 */
	private void deleteResourcesOneByOne(
			List<IIdType> theResourcesToDelete, RequestPartitionId thePartition, RequestDetails theRequestDetails) {
		for (IIdType id : theResourcesToDelete) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			DaoMethodOutcome outcome = myHapiTransactionService
					.withRequest(theRequestDetails)
					.withRequestPartitionId(thePartition)
					.execute(() -> {
						// EXPERIMENT: seed the resolved-partition cache with the source partition (keyed by
						// "Type/id") so the 4-arg delete uses it instead of re-resolving a non-decodable id to
						// allPartitions. Hardcoded to the source partition for now.
						TransactionDetails transactionDetails = new TransactionDetails();
						transactionDetails.addResolvedPartition(
								id.getResourceType() + "/" + id.getIdPart(), thePartition);
						DeleteConflictList deleteConflicts = new DeleteConflictList();
						DaoMethodOutcome result =
								dao.delete(id, deleteConflicts, theRequestDetails, transactionDetails);
						DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(myFhirContext, deleteConflicts);
						return result;
					});
			ourLog.warn(
					"TRACE-INVESTIGATION: one-by-one delete id={} seededPartition={} -> nop(no-op)={} outcomeId={}",
					id.getValue(),
					thePartition,
					outcome != null && outcome.isNop(),
					(outcome != null && outcome.getId() != null)
							? outcome.getId().getValue()
							: null);
		}
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
