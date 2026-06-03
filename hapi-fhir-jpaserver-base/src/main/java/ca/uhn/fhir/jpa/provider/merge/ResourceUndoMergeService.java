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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.merge.MergeResourceHelper.addInfoToOperationOutcome;
import static ca.uhn.fhir.model.api.StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static java.lang.String.format;

/**
 * This service implements the $hapi.fhir.undo-merge operation.
 * It reverts the changes made by a previous merge (Patient/$merge or {resourceType}/$hapi.fhir.merge) operation based on the Provenance resource
 * that was created as part of the merge operation.
 *
 * Supports both Patient-specific ($hapi.fhir.undo-merge on Patient) and generic
 * ($hapi.fhir.undo-merge on any resource type with 'identifier' element) operations.
 *
 * Current limitations:
 * - It fails if any resources to be restored have been subsequently changed since the merge operation was performed.
 * - It can only run synchronously.
 * - It fails if the number of resources to restore exceeds a specified resource limit
 * (currently set to same size as getInternalSynchronousSearchSize in JPAStorageSettings by the operation provider).
 */
public class ResourceUndoMergeService {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceUndoMergeService.class);

	private static final String ISSUE_TYPE_EXCEPTION = "exception";

	private final MergeProvenanceSvc myMergeProvenanceSvc;
	private final PreviousResourceVersionRestorer myResourceVersionRestorer;
	private final MergeValidationService myMergeValidationService;
	private final IHapiTransactionService myHapiTransactionService;
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public ResourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService,
			IHapiTransactionService theHapiTransactionService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myDaoRegistry = theDaoRegistry;
		myMergeProvenanceSvc = theMergeProvenanceSvc;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
		myMergeValidationService = theMergeValidationService;
		myHapiTransactionService = theHapiTransactionService;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	public OperationOutcomeWithStatusCode undoMerge(
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			AbstractMergeOperationInputParameterNames theInputParamNames) {

		OperationOutcomeWithStatusCode undoMergeOutcome = new OperationOutcomeWithStatusCode();
		IBaseOperationOutcome opOutcome = OperationOutcomeUtil.newInstance(myFhirContext);
		undoMergeOutcome.setOperationOutcome(opOutcome);
		try {
			return undoMergeInternal(inputParameters, theRequestDetails, undoMergeOutcome, theInputParamNames);
		} catch (Exception e) {
			ourLog.error("Undo resource merge failed with an exception", e);
			if (e instanceof BaseServerResponseException) {
				undoMergeOutcome.setHttpStatusCode(((BaseServerResponseException) e).getStatusCode());
			} else {
				undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);
			}
			addErrorToOperationOutcome(myFhirContext, opOutcome, e.getMessage(), ISSUE_TYPE_EXCEPTION);
		}
		return undoMergeOutcome;
	}

	private OperationOutcomeWithStatusCode undoMergeInternal(
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode undoMergeOutcome,
			AbstractMergeOperationInputParameterNames theInputParamNames) {

		IBaseOperationOutcome opOutcome = undoMergeOutcome.getOperationOutcome();

		if (!myMergeValidationService.validateCommonMergeOperationParameters(
				inputParameters, opOutcome, theInputParamNames, theRequestDetails)) {
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return undoMergeOutcome;
		}

		IBaseResource targetResource = myMergeValidationService.resolveTargetResource(
				inputParameters, theRequestDetails, opOutcome, theInputParamNames);
		IIdType targetId = targetResource.getIdElement();

		// The first element is always the main Provenance; the rest (if any) are sub-Provenances.
		List<Provenance> provenances = findMergeProvenances(inputParameters, targetId, theRequestDetails);
		Provenance mainProvenance = provenances.get(0);

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-merge operation",
				mainProvenance.getIdElement().asStringValue());

		if (provenances.size() == 1) {
			undoSingleProvenance(mainProvenance, inputParameters, theRequestDetails, undoMergeOutcome);
		} else {
			List<Provenance> subProvenances = provenances.subList(1, provenances.size());
			undoGroupedMerge(mainProvenance, subProvenances, inputParameters, theRequestDetails, undoMergeOutcome);
		}

		return undoMergeOutcome;
	}

	private List<Provenance> findMergeProvenances(
			UndoMergeOperationInputParameters inputParameters, IIdType targetId, RequestDetails theRequestDetails) {

		List<Provenance> provenances;
		if (inputParameters.getSourceResource() != null) {
			IIdType sourceId = inputParameters.getSourceResource().getReferenceElement();
			provenances = myMergeProvenanceSvc.findMergeProvenances(targetId, sourceId, theRequestDetails);
		} else {
			provenances = myMergeProvenanceSvc.findMergeProvenancesBySourceIdentifiers(
					targetId, inputParameters.getSourceIdentifiers(), theRequestDetails);
		}

		if (provenances.isEmpty()) {
			String msg =
					"Unable to find a Provenance created by a $merge operation for the provided source and target resources."
							+ " Ensure that the provided resource references or identifiers were previously used as parameters in a successful $merge operation";
			throw new ResourceNotFoundException(Msg.code(2747) + msg);
		}
		return provenances;
	}

	private void undoSingleProvenance(
			Provenance theProvenance,
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		ourLog.info(
				"Undoing merge from a single Provenance: {}",
				theProvenance.getIdElement().toUnqualifiedVersionless().getValue());

		List<Reference> references = theProvenance.getTarget();
		validateResourceLimit(references.size(), inputParameters.getResourceLimit());

		List<Reference> referencesToRestore = references;
		if (wasTargetUpdateANoop(theProvenance)) {
			referencesToRestore = references.subList(1, references.size());
		}

		myResourceVersionRestorer.restoreToPreviousVersionsInTrx(referencesToRestore, theRequestDetails);
		populateSuccessOutcome(referencesToRestore.size(), theProvenance, theUndoMergeOutcome);
	}

	private void undoGroupedMerge(
			Provenance theMainProvenance,
			List<Provenance> theSubProvenances,
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		validateGroupedMergeResourceLimit(theMainProvenance, theSubProvenances, inputParameters);

		ourLog.info(
				"Undoing grouped merge from main Provenance: {} with {} sub-Provenance(s)",
				theMainProvenance.getIdElement().toUnqualifiedVersionless().getValue(),
				theSubProvenances.size());

		// In every Provenance, target.get(0) is the target and target.get(1) is the source (see
		// ReplaceReferencesProvenanceSvc.createProvenance); everything from index 2 onward is the
		// partition-specific data. Source and target are restored separately (source first, target
		// last) for referential integrity, so we slice them off each sub-Provenance.
		Reference targetRef = theMainProvenance.getTarget().get(0);
		Reference sourceRef = theMainProvenance.getTarget().get(1);

		// Restore all Provenances within a single outer transaction. When all the data lives in a single
		// database the whole undo is atomic — if any restore fails, the entire transaction rolls back and
		// nothing persists. When changing partitions requires a new transaction, each per-partition restore
		// commits independently, so a failure can leave earlier partitions reverted while later ones stay
		// merged. We stop at the first failure and then report which partitions actually persisted.
		try {
			int restoredCount = myHapiTransactionService
					.withRequest(theRequestDetails)
					.execute(() -> {
						int totalRestored = 0;

						// Restore source first (undelete if deleted) so that referential integrity checks pass when
						// restoring data resources that reference source. Each restore is pinned to the partition the
						// resources live on so the resources of one Provenance share a single transaction and are
						// restored all-or-nothing (see PreviousResourceVersionRestorer#restoreToPreviousVersionsInTrx).
						ourLog.info("Restoring source resource: {}", sourceRef.getReference());
						myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
								List.of(sourceRef), partitionFor(sourceRef, theRequestDetails), theRequestDetails);
						totalRestored++;

						// Restore each sub-Provenance's data separately,
						// each of which contains resources from the same partition
						for (Provenance sub : theSubProvenances) {
							List<Reference> dataRefs =
									sub.getTarget().subList(2, sub.getTarget().size());
							ourLog.info(
									"Restoring {} resource(s) from sub-Provenance: {}",
									dataRefs.size(),
									sub.getIdElement()
											.toUnqualifiedVersionless()
											.getValue());
							if (!dataRefs.isEmpty()) {
								myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
										dataRefs, partitionFor(dataRefs.get(0), theRequestDetails), theRequestDetails);
								totalRestored += dataRefs.size();
							}
						}

						// Restore target last.
						if (!wasTargetUpdateANoop(theMainProvenance)) {
							ourLog.info("Restoring target resource: {}", targetRef.getReference());
							myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
									List.of(targetRef), partitionFor(targetRef, theRequestDetails), theRequestDetails);
							totalRestored++;
						}

						return totalRestored;
					});
			populateSuccessOutcome(restoredCount, theMainProvenance, theUndoMergeOutcome);
		} catch (Exception theException) {
			ourLog.error(
					"Grouped undo-merge failed; determining which partitions were already committed", theException);
			buildGroupedUndoFailureOutcome(
					theMainProvenance, theSubProvenances, theException, theRequestDetails, theUndoMergeOutcome);
		}
	}

	/**
	 * Populates the {@link OperationOutcomeWithStatusCode} for a successful undo-merge with an informational
	 * message and a 200 status code.
	 */
	private void populateSuccessOutcome(
			int theRestoredCount, Provenance theMainProvenance, OperationOutcomeWithStatusCode theUndoMergeOutcome) {
		String msg = format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				theRestoredCount, theMainProvenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, theUndoMergeOutcome.getOperationOutcome(), null, msg);
		theUndoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);
	}

	/**
	 * Builds the {@link OperationOutcomeWithStatusCode} for a failed grouped undo-merge.
	 *
	 * <p>Per-partition restores can commit independently when partitions span separate databases, so a
	 * failure partway through can leave some partitions reverted to their pre-merge state while others remain
	 * merged. We determine which actually persisted by probing the current version of one reference per
	 * Provenance (see {@link #wasReferenceRestored(Reference, RequestDetails)}): a committed restore advances
	 * the version, while a rolled-back or never-attempted one does not. When everything lives in a single
	 * database the failed outer transaction rolls all of it back, so nothing probes as persisted and the
	 * merge is reported as fully intact.
	 */
	private void buildGroupedUndoFailureOutcome(
			Provenance theMainProvenance,
			List<Provenance> theSubProvenances,
			Exception theFailure,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		IBaseOperationOutcome opOutcome = theUndoMergeOutcome.getOperationOutcome();
		theUndoMergeOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);

		Reference targetRef = theMainProvenance.getTarget().get(0);
		Reference sourceRef = theMainProvenance.getTarget().get(1);

		boolean sourceReverted = wasReferenceRestored(sourceRef, theRequestDetails);
		boolean targetReverted = wasReferenceRestored(targetRef, theRequestDetails);

		// Each sub-Provenance restores atomically, so its first data reference tells us whether the whole
		// Provenance was reverted.
		List<String> revertedProvenances = new ArrayList<>();
		List<String> notRevertedProvenances = new ArrayList<>();
		for (Provenance sub : theSubProvenances) {
			List<Reference> dataRefs =
					sub.getTarget().subList(2, sub.getTarget().size());
			if (dataRefs.isEmpty()) {
				continue;
			}
			String provenanceId = sub.getIdElement().toUnqualifiedVersionless().getValue();
			if (wasReferenceRestored(dataRefs.get(0), theRequestDetails)) {
				revertedProvenances.add(provenanceId);
			} else {
				notRevertedProvenances.add(provenanceId);
			}
		}

		if (revertedProvenances.isEmpty() && !sourceReverted && !targetReverted) {
			// Single-database (atomic) case, or the failure happened before anything was restored: the
			// outer transaction rolled everything back, so the merge is fully intact.
			String msg = format(
					"Undo-merge failed. No resources could be restored; the merge remains fully in effect. Cause: %s",
					theFailure.getMessage());
			addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
			return;
		}

		// Some restores committed independently before the failure and cannot be automatically rolled back.
		// Report at Provenance granularity (each Provenance is all-or-nothing) plus the source and target.
		String msg = format(
				"Undo-merge partially failed; changes that were already committed cannot be automatically "
						+ "rolled back and require manual reconciliation. Restored provenances: %s. Not restored "
						+ "provenances: %s. Source resource restored: %s (%s). Target resource restored: %s (%s). "
						+ "Cause: %s",
				revertedProvenances,
				notRevertedProvenances,
				sourceReverted,
				sourceRef.getReferenceElement().toUnqualifiedVersionless().getValue(),
				targetReverted,
				targetRef.getReferenceElement().toUnqualifiedVersionless().getValue(),
				theFailure.getMessage());
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
	}

	/**
	 * Returns {@code true} if the resource referenced by the given versioned Provenance reference now has a
	 * current version greater than the version recorded in the reference — i.e. a restore actually committed
	 * for it. A resource that was created by the merge and restored by deleting it is detected via its
	 * tombstone version. Probe failures are treated as "not restored" so the resource is reported as needing
	 * manual cleanup rather than being silently assumed reverted.
	 */
	private boolean wasReferenceRestored(Reference theProvenanceRef, RequestDetails theRequestDetails) {
		IIdType versionedId = theProvenanceRef.getReferenceElement();
		if (!versionedId.hasVersionIdPart()) {
			return false;
		}
		long provenanceVersion = versionedId.getVersionIdPartAsLong();
		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(versionedId.getResourceType());
		try {
			IBaseResource current = dao.read(versionedId.toUnqualifiedVersionless(), theRequestDetails);
			return current.getIdElement().getVersionIdPartAsLong() > provenanceVersion;
		} catch (ResourceGoneException e) {
			// Resource was created by the merge and restored by deleting it; the tombstone is a new version.
			IIdType goneId = e.getResourceId();
			return goneId != null && goneId.hasVersionIdPart() && goneId.getVersionIdPartAsLong() > provenanceVersion;
		} catch (Exception e) {
			ourLog.warn(
					"Could not determine whether {} was restored; reporting it as still merged",
					versionedId.getValue(),
					e);
			return false;
		}
	}

	/**
	 * Resolves the partition a referenced resource lives on, so the restore can be pinned to it. All
	 * references within a single sub-Provenance live on the same partition, so resolving from one is enough.
	 */
	private RequestPartitionId partitionFor(Reference theRef, RequestDetails theRequestDetails) {
		IIdType id = theRef.getReferenceElement().toUnqualifiedVersionless();
		return myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(theRequestDetails, id);
	}

	/**
	 * Validates the resource limit against the number of distinct resources referenced across all
	 * Provenances. Source and target appear in every Provenance, so collecting into a set dedups
	 * them automatically — no special handling needed.
	 */
	private void validateGroupedMergeResourceLimit(
			Provenance theMainProvenance,
			List<Provenance> theSubProvenances,
			UndoMergeOperationInputParameters inputParameters) {
		Set<String> referencedResources = new HashSet<>();
		for (Reference ref : theMainProvenance.getTarget()) {
			referencedResources.add(
					ref.getReferenceElement().toUnqualifiedVersionless().getValue());
		}
		for (Provenance sub : theSubProvenances) {
			for (Reference ref : sub.getTarget()) {
				referencedResources.add(
						ref.getReferenceElement().toUnqualifiedVersionless().getValue());
			}
		}
		validateResourceLimit(referencedResources.size(), inputParameters.getResourceLimit());
	}

	private static void validateResourceLimit(int theCount, int theLimit) {
		if (theCount > theLimit) {
			String msg = format("Number of references to update (%d) exceeds the limit (%d)", theCount, theLimit);
			throw new InvalidRequestException(Msg.code(2748) + msg);
		}
	}

	private boolean wasTargetUpdateANoop(Provenance provenance) {
		List<Resource> containedResources = provenance.getContained();

		// currently the second contained resource is the OperationOutcome of updating the target resource in the
		// Provenance resource.
		if (containedResources.size() > 1 && containedResources.get(1) instanceof OperationOutcome operationOutcome) {

			List<OperationOutcome.OperationOutcomeIssueComponent> issues = operationOutcome.getIssue();

			return issues.stream()
					.filter(issue -> issue.hasDetails() && issue.getDetails().hasCoding())
					.map(issue -> issue.getDetails().getCoding())
					.flatMap(List::stream)
					.anyMatch(coding -> StorageResponseCodeEnum.SYSTEM.equals(coding.getSystem())
							&& SUCCESSFUL_UPDATE_NO_CHANGE.getCode().equals(coding.getCode()));
		}

		throw new InternalErrorException(Msg.code(2750)
				+ String.format(
						"The Provenance resource '%s' does not contain an OperationOutcome of the target resource.",
						provenance.getIdElement().asStringValue()));
	}
}
