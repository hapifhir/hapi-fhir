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
import ca.uhn.fhir.jpa.provider.CrossPartitionReplaceReferencesPrepareResult;
import ca.uhn.fhir.jpa.provider.CrossPartitionReplaceReferencesSvc;
import ca.uhn.fhir.merge.MergeResourceHelper;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
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
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
				// PartitionedTransactionPartialFailureException indicates some changes are committed
				// when running cross-partition replace references.
				// The committed entries are carried on the exception rather than recorded in the rollback context,
				// so harvest them here.
				extractCommittedResourceIdsByPartition(partialFailure, theRequestDetails)
						.values()
						.forEach(rollbackContext::addResourcesToRevert);
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
	 * Runs the cross-partition merge as a SINGLE FHIR transaction bundle: POST the compartment-resource copies, PUT
	 * the referrer updates and the source/target, POST the merge Provenance, and DELETE the originals (+ source when
	 * deleteSource). On MegaScale the bundle is sliced per shard by the splitter and committed sub-bundle by
	 * sub-bundle. A post-commit pass reads back the updated target for the outcome and finalizes the Provenance
	 * (versions the copy references and adds the target-update OperationOutcome) — both depend on the transaction
	 * response and so cannot be known when the bundle is built.
	 * <p>
	 * Rollback bookkeeping is no longer recorded step by step here: the whole forward flow is one atomic transaction,
	 * so the success path has nothing committed-then-failed to revert. A partial failure (some sub-bundles committed)
	 * surfaces as {@link PartitionedTransactionPartialFailureException}, which the caller harvests for rollback.
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

		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// 1) Append the copy (POST) and referrer-update (PUT) entries (no execution).
		CrossPartitionReplaceReferencesPrepareResult prepareResult =
				myCrossPartitionReplaceReferencesSvc.prepareCopyAndUpdateEntries(
						theSourceResource, theTargetResource, theRequestDetails, bundleBuilder);

		Map<RequestPartitionId, List<IIdType>> copiedOriginalsByPartition =
				prepareResult.getCopiedResourceOriginalIdsByPartition();

		// 2) Prepare and append the source/target PUT entries. The target PUT is the first entry after the
		// copy/update entries; the source PUT (when the source is kept) immediately follows it.
		MergeResourceHelper.PreparedMergeResources prepared = myMergeResourceHelper.prepareMergedResourcesForUpdate(
				theSourceResource, theTargetResource, theMergeOperationParameters.getResultResource(), deleteSource);

		int targetEntryIndex = prepareResult.getCopyAndUpdateEntryPartitions().size();
		addStampedUpdateEntry(bundleBuilder, prepared.getTargetToUpdate(), theRequestDetails);
		if (prepared.getSourceToUpdate() != null) {
			addStampedUpdateEntry(bundleBuilder, prepared.getSourceToUpdate(), theRequestDetails);
		}

		// The single transaction is the sole writer, so each post-merge version is current + 1: the target is always
		// updated; the source is updated when kept or deleted (its tombstone) when not.
		IIdType sourcePostMergeId = withVersionIncremented(theSourceResource.getIdElement());
		IIdType targetPostMergeId = withVersionIncremented(theTargetResource.getIdElement());

		// 3) Build and append the merge Provenance (POST). Its changed-resource references are: copies (by urn:uuid
		// placeholder, resolved to the assigned id by the transaction and versioned in the post-commit pass) and
		// deleted originals (at their tombstone version, current + 1). Updated referrers and the contained
		// target-update OperationOutcome are added in the post-commit pass (they aren't known until the tx runs).
		Provenance provenance = null;
		int provenanceEntryIndex = -1;
		if (theMergeOperationParameters.getCreateProvenance()) {
			// Copies (placeholders, resolved + versioned post-commit) and deleted originals (tombstone = current + 1)
			// are known now. Updated referrers are added post-commit from the response, skipping no-op PUTs (a referrer
			// whose only reference to the source was a versioned one is not rewritten, so its PUT is a no-op and must
			// not appear as a changed resource).
			List<IIdType> changedResourceIds = new ArrayList<>();
			prepareResult.getCopyPlaceholdersInEntryOrder().forEach(ph -> changedResourceIds.add(new IdDt(ph)));
			newMapWithVersionIncremented(copiedOriginalsByPartition).values().forEach(changedResourceIds::addAll);

			provenance = myMergeResourceHelper.buildProvenance(
					sourcePostMergeId,
					targetPostMergeId,
					changedResourceIds,
					// no correlation id: a merge records a single Provenance, nothing to correlate
					null,
					theStartTime,
					theMergeOperationParameters.getProvenanceAgents(),
					// only the input Parameters at build time; the target-update OperationOutcome is added post-commit
					List.of(theMergeOperationParameters.getOriginalInputParameters()));

			provenanceEntryIndex = currentEntryCount(bundleBuilder);
			bundleBuilder.addTransactionCreateEntry(provenance);
			stampEntryPartition(
					bundleBuilder, provenanceEntryIndex, resolveWritePartition(provenance, theRequestDetails));
		}

		// 4) DELETE the source-side compartment originals (+ the source itself when deleteSource). Each id-only DELETE
		// entry is stamped with its partition so the transaction processor routes it to the correct shard (the id may
		// be non-decodable, e.g. a client-assigned/UUID id in MegaScale Patient ID mode).
		copiedOriginalsByPartition.forEach((partition, ids) ->
				ids.forEach(id -> addStampedDeleteEntry(bundleBuilder, id.toVersionless(), partition)));
		if (deleteSource) {
			addStampedDeleteEntry(
					bundleBuilder,
					theSourceResource.getIdElement().toVersionless(),
					getRequiredPartition(theSourceResource));
		}

		// 5) Execute the entire forward merge as one transaction.
		Bundle response =
				(Bundle) myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, bundleBuilder.getBundle());
		List<Bundle.BundleEntryComponent> responseEntries = response.getEntry();

		// 6) Populate the outcome's updated target from a fresh read (full server meta), pinned to its partition.
		RequestPartitionId targetPartitionId = getRequiredPartition(theTargetResource);
		IBaseResource updatedTarget = myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(targetPartitionId)
				.execute(() -> myDaoRegistry
						.getResourceDao(theTargetResource.fhirType())
						.read(targetPostMergeId.toVersionless(), theRequestDetails));
		theMergeOutcome.setUpdatedTargetResource(updatedTarget);

		// 7) Post-commit Provenance fixup: version the copy references and add the target-update OperationOutcome.
		if (provenance != null) {
			finalizeProvenance(
					provenance,
					prepareResult,
					responseEntries,
					targetEntryIndex,
					provenanceEntryIndex,
					theRequestDetails);
		}
	}

	/**
	 * Finalizes the merge Provenance after the single transaction has committed: replaces each copy reference (held
	 * as a {@code urn:uuid} placeholder on the in-memory Provenance) with the committed versioned id, appends the
	 * target-update {@link org.hl7.fhir.r4.model.OperationOutcome} (from the target response entry) as the second
	 * contained resource so undo can detect a no-op target update, and persists the updated Provenance.
	 */
	private void finalizeProvenance(
			Provenance theProvenance,
			CrossPartitionReplaceReferencesPrepareResult thePrepareResult,
			List<Bundle.BundleEntryComponent> theResponseEntries,
			int theTargetEntryIndex,
			int theProvenanceEntryIndex,
			RequestDetails theRequestDetails) {

		// Copies are the first N response entries (POSTs). The transaction already resolved each copy's urn:uuid
		// placeholder to the assigned VERSIONLESS id in-place on this Provenance object, so map versionless ->
		// versioned
		// and version any target that matches a committed copy.
		Map<String, String> versionlessToVersioned = new HashMap<>();
		for (int i = 0; i < thePrepareResult.getCopyEntryCount(); i++) {
			IdDt committed = new IdDt(theResponseEntries.get(i).getResponse().getLocation());
			versionlessToVersioned.put(committed.toUnqualifiedVersionless().getValue(), committed.getValue());
		}
		theProvenance.getTarget().forEach(target -> {
			if (target.getReference() != null) {
				String versionless = new IdDt(target.getReference())
						.toUnqualifiedVersionless()
						.getValue();
				String versioned = versionlessToVersioned.get(versionless);
				if (versioned != null) {
					target.setReference(versioned);
				}
			}
		});

		// Add the referrer updates that actually changed as changed-resource targets, at their committed versions. The
		// update (PUT) entries occupy the bundle positions between the copies and the target entry. A no-op PUT (e.g. a
		// referrer whose only source reference was versioned and so not rewritten) is skipped.
		for (int i = thePrepareResult.getCopyEntryCount(); i < theTargetEntryIndex; i++) {
			Bundle.BundleEntryResponseComponent entryResponse =
					theResponseEntries.get(i).getResponse();
			if (!ReplaceReferencesProvenanceSvc.isNoChangeResponse(entryResponse)) {
				theProvenance.addTarget(new Reference(new IdDt(entryResponse.getLocation())));
			}
		}

		// Add the target-update OperationOutcome as contained[1] (input Parameters is contained[0]).
		Resource targetOutcome =
				theResponseEntries.get(theTargetEntryIndex).getResponse().getOutcome();
		if (targetOutcome != null) {
			theProvenance.addContained(targetOutcome);
		}

		IdDt provenanceId = new IdDt(
				theResponseEntries.get(theProvenanceEntryIndex).getResponse().getLocation());
		theProvenance.setId(provenanceId.toUnqualifiedVersionless());

		// The Provenance routes by its patient-compartment extension (set at build time), so no partition pinning is
		// needed here — same routing the in-transaction POST used.
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.execute(() -> myDaoRegistry.getResourceDao("Provenance").update(theProvenance, theRequestDetails));
	}

	private void addStampedDeleteEntry(BundleBuilder theBundleBuilder, IIdType theId, RequestPartitionId thePartition) {
		theBundleBuilder.addTransactionDeleteEntry(theId);
		stampEntryPartition(theBundleBuilder, currentEntryCount(theBundleBuilder) - 1, thePartition);
	}

	/**
	 * Appends a PUT entry and stamps it with the resource's write partition. Stamping ensures every entry in a
	 * per-shard sub-bundle resolves to the SAME {@link RequestPartitionId} object (id AND name): the copies resolve
	 * their partition via {@code determineCreatePartitionForRequest}, so the source/target PUTs must resolve it the
	 * same way rather than via the {@code TransactionDetails} cache, which can yield an id-only (unnamed) partition
	 * that compares unequal in the strict per-shard compatibility check.
	 */
	private void addStampedUpdateEntry(
			BundleBuilder theBundleBuilder, IBaseResource theResource, RequestDetails theRequestDetails) {
		theBundleBuilder.addTransactionUpdateEntry(theResource);
		stampEntryPartition(
				theBundleBuilder,
				currentEntryCount(theBundleBuilder) - 1,
				resolveWritePartition(theResource, theRequestDetails));
	}

	private RequestPartitionId resolveWritePartition(IBaseResource theResource, RequestDetails theRequestDetails) {
		// Temporarily clear any cached RESOURCE_PARTITION_ID so the partition is resolved fresh via the
		// compartment interceptor (which yields a named partition), rather than short-circuiting to the resource's
		// existing userData partition (set unnamed during the merge read). This keeps every entry in a per-shard
		// sub-bundle resolving to the SAME (named) RequestPartitionId as the copies, which the strict per-shard
		// compatibility check requires.
		Object savedPartition = theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		try {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			return myRequestPartitionHelperSvc.determineCreatePartitionForRequest(
					theRequestDetails, theResource, myFhirContext.getResourceType(theResource));
		} finally {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, savedPartition);
		}
	}

	private void stampEntryPartition(
			BundleBuilder theBundleBuilder, int theEntryIndex, RequestPartitionId thePartition) {
		Bundle bundle = (Bundle) theBundleBuilder.getBundle();
		bundle.getEntry().get(theEntryIndex).setUserData(Constants.RESOURCE_PARTITION_ID, thePartition);
	}

	private int currentEntryCount(BundleBuilder theBundleBuilder) {
		return ((Bundle) theBundleBuilder.getBundle()).getEntry().size();
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
	 * Recovers the versioned ids of the resources that committed before a partitioned data bundle failed partway,
	 * from the {@link PartitionedTransactionPartialFailureException} that carries the committed response entries,
	 * keyed by partition for the rollback. The exception carries the committed entries grouped per committed
	 * sub-bundle (one inner list each); each sub-bundle is keyed by a partition derived from its first ref. No-op
	 * responses (no change) are skipped — nothing committed for those.
	 */
	private Map<RequestPartitionId, List<IIdType>> extractCommittedResourceIdsByPartition(
			PartitionedTransactionPartialFailureException theFailure, RequestDetails theRequestDetails) {
		Map<RequestPartitionId, List<IIdType>> committedByPartition = new LinkedHashMap<>();
		for (List<IBase> subBundleEntries : theFailure.getCommittedResponseEntriesPerSubBundle()) {
			// Collect the versioned id of every resource this sub-bundle actually changed, skipping entries
			// with no location or a no-change outcome (nothing was committed for those).
			List<IIdType> committedRefsInSubBundle = new ArrayList<>();
			for (IBase entry : subBundleEntries) {
				ReplaceReferencesProvenanceSvc.extractChangedResourceId((Bundle.BundleEntryComponent) entry)
						.ifPresent(committedRefsInSubBundle::add);
			}
			if (!committedRefsInSubBundle.isEmpty()) {
				// Key this sub-bundle by a partition derived from its first ref; the rollback uses it to pin the
				// restore transaction.
				RequestPartitionId partition = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
						theRequestDetails, committedRefsInSubBundle.get(0).toUnqualifiedVersionless());
				committedByPartition.put(partition, committedRefsInSubBundle);
			}
		}
		return committedByPartition;
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
