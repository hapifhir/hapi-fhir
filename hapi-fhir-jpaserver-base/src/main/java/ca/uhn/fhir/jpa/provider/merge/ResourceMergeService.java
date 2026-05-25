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
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

		// Outer tx is intentionally not pinned to a partition: per-operation partition resolution
		// handles writes (per-bundle-entry resolution in transactionNested) and link discovery
		// fans out to all shards explicitly inside CrossPartitionReplaceReferencesSvc.
		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			if (crossPartition) {
				doMergeSyncCrossPartition(
						theMergeOperationParameters,
						theSourceResource,
						theTargetResource,
						theRequestDetails,
						theMergeOutcome,
						startTime);
			} else {
				doMergeSyncSamePartition(
						theMergeOperationParameters,
						theSourceResource,
						theTargetResource,
						partitionId,
						theRequestDetails,
						theMergeOutcome,
						startTime);
			}
		});

		String detailsText = "Merge operation completed successfully.";
		addInfoToOperationOutcome(myFhirContext, theMergeOutcome.getOperationOutcome(), null, detailsText);
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

		List<IIdType> resourcesToDeleteIds = new ArrayList<>();
		if (theMergeOperationParameters.getDeleteSource()) {
			resourcesToDeleteIds.add(theSourceResource.getIdElement());
		}

		finalizeMerge(
				theMergeOperationParameters,
				theSourceResource,
				theTargetResource,
				changedResourceIds,
				null,
				resourcesToDeleteIds,
				theRequestDetails,
				theMergeOutcome,
				theStartTime);
	}

	private void doMergeSyncCrossPartition(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime) {

		String provenanceGroupId = generateProvenanceGroupId(theSourceResource, theTargetResource);
		boolean deleteSource = theMergeOperationParameters.getDeleteSource();

		// Step 1: Data bundle — copy compartment resources and replace references
		CrossPartitionReplaceReferencesResult copyResult =
				myCrossPartitionReplaceReferencesSvc.copyCompartmentResourcesAndReplaceReferences(
						theSourceResource, theTargetResource, theRequestDetails);

		Map<RequestPartitionId, List<IIdType>> committedByPartition = copyResult.getCommittedResourcesByPartition();
		Map<RequestPartitionId, List<IIdType>> originalsToDeleteByPartition =
				copyResult.getCopiedResourceOriginalIdsByPartition();

		// Step 2: Per-partition Provenances (sequential, continue-on-failure).
		// Each includes [tgt, src, partition-specific committed + tombstone refs].
		// Source and target are referenced at their current versions (pre-update) since
		// the src/tgt update hasn't happened yet at this point.
		if (theMergeOperationParameters.getCreateProvenance()) {
			IIdType sourceId = theSourceResource.getIdElement();
			IIdType targetId = theTargetResource.getIdElement();

			Set<RequestPartitionId> allPartitions = new LinkedHashSet<>();
			allPartitions.addAll(committedByPartition.keySet());
			allPartitions.addAll(originalsToDeleteByPartition.keySet());

			for (RequestPartitionId partition : allPartitions) {
				List<IIdType> partitionRefs = new ArrayList<>();

				List<IIdType> committedInPartition = committedByPartition.getOrDefault(partition, List.of());
				partitionRefs.addAll(committedInPartition);

				List<IIdType> originalsToDeleteInPartition =
						originalsToDeleteByPartition.getOrDefault(partition, List.of());
				for (IIdType id : originalsToDeleteInPartition) {
					partitionRefs.add(id.withVersion(String.valueOf(id.getVersionIdPartAsLong() + 1)));
				}

				myMergeResourceHelper.createProvenance(
						sourceId,
						targetId,
						partitionRefs,
						provenanceGroupId,
						theRequestDetails,
						theStartTime,
						theMergeOperationParameters.getProvenanceAgents(),
						List.of());
			}
		}

		// Step 3-5: Update source/target, create main Provenance, delete originals
		List<IIdType> resourcesToDeleteIds = new ArrayList<>(copyResult.getCopiedResourceOriginalIds());
		if (deleteSource) {
			resourcesToDeleteIds.add(theSourceResource.getIdElement());
		}

		finalizeMerge(
				theMergeOperationParameters,
				theSourceResource,
				theTargetResource,
				List.of(),
				provenanceGroupId,
				resourcesToDeleteIds,
				theRequestDetails,
				theMergeOutcome,
				theStartTime);
	}

	private void finalizeMerge(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			List<IIdType> theProvenanceTargetIds,
			@Nullable String theProvenanceGroupId,
			List<IIdType> theResourcesToDeleteIds,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome,
			Date theStartTime) {

		DaoMethodOutcome outcome = myMergeResourceHelper.updateMergedResourcesAfterReferencesReplaced(
				theSourceResource,
				theTargetResource,
				theMergeOperationParameters.getResultResource(),
				theMergeOperationParameters.getDeleteSource(),
				theRequestDetails);

		theMergeOutcome.setUpdatedTargetResource(outcome.getResource());

		if (theMergeOperationParameters.getCreateProvenance()) {
			IIdType targetVersionedId = outcome.getResource().getIdElement();
			IIdType sourceVersionedId = theSourceResource.getIdElement();
			if (theMergeOperationParameters.getDeleteSource()) {
				// Source is not updated when deleteSource — it goes straight to delete (after provenance).
				// Bump version to anticipate the tombstone.
				sourceVersionedId =
						sourceVersionedId.withVersion(Long.toString(sourceVersionedId.getVersionIdPartAsLong() + 1));
			}

			List<IBaseResource> containedResources =
					List.of(theMergeOperationParameters.getOriginalInputParameters(), outcome.getOperationOutcome());

			myMergeResourceHelper.createProvenance(
					sourceVersionedId,
					targetVersionedId,
					theProvenanceTargetIds,
					theProvenanceGroupId,
					theRequestDetails,
					theStartTime,
					theMergeOperationParameters.getProvenanceAgents(),
					containedResources);
		}

		if (!theResourcesToDeleteIds.isEmpty()) {
			deleteResources(theResourcesToDeleteIds, theRequestDetails);
		}
	}

	private static String generateProvenanceGroupId(IBaseResource theSourceResource, IBaseResource theTargetResource) {
		String src = theSourceResource.getIdElement().getIdPart();
		String tgt = theTargetResource.getIdElement().getIdPart();
		String resourceType = theSourceResource.getIdElement().getResourceType();
		String uuid = java.util.UUID.randomUUID().toString();
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
		Optional<RequestPartitionId> srcPart = RequestPartitionId.getPartitionIfAssigned(theSourceResource);
		Optional<RequestPartitionId> tgtPart = RequestPartitionId.getPartitionIfAssigned(theTargetResource);
		return srcPart.isPresent() && tgtPart.isPresent() && !srcPart.get().equals(tgtPart.get());
	}
}
