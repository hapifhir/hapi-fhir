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
import ca.uhn.fhir.jpa.dao.PartitionedTransactionPartialFailureException;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.MergeProvenanceGroupIdUtil;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import jakarta.annotation.Nullable;
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
 * It reverts the changes made by a previous merge (Patient/$merge or {resourceType}/$hapi.fhir.merge) operation based on the Provenance resources
 * that were created as part of the merge operation.
 *
 * Supports both Patient-specific ($hapi.fhir.undo-merge on Patient) and generic
 * ($hapi.fhir.undo-merge on any resource type with 'identifier' element) operations.
 *
 * A same-partition merge records a single Provenance listing every changed resource; a cross-partition merge
 * records one sub-Provenance per involved partition (grouped with the main Provenance via the provenance-group
 * extension), and the undo restores it provenance-by-provenance, pinned to the partition each sub-Provenance's
 * extension names.
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

	public ResourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService,
			IHapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myMergeProvenanceSvc = theMergeProvenanceSvc;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
		myMergeValidationService = theMergeValidationService;
		myHapiTransactionService = theHapiTransactionService;
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

		// The first element is always the main Provenance; the rest (if any) are the per-partition sub-Provenances
		// of a cross-partition merge.
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

	/**
	 * Undoes a same-partition merge from its single Provenance, which lists every changed resource. The restore
	 * is submitted as one cross-partition FHIR transaction (see the comment inside).
	 */
	private void undoSingleProvenance(
			Provenance theProvenance,
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		ourLog.info(
				"Undoing merge from a single Provenance: {}",
				theProvenance.getIdElement().toUnqualifiedVersionless().getValue());

		// target.get(0) is the target, target.get(1) is the source, and everything from index 2 on is the changed
		// data. The restore submits them as a single FHIR transaction; when a transaction-partitioning interceptor
		// is present it splits and dependency-orders the writes per partition — so the resources a referrer points
		// at are committed before the referrer is restored, with no manual source-first/target-last ordering needed
		// here. When the target update was a no-op there is nothing to revert for it, so it is sliced off.
		List<Reference> references = theProvenance.getTarget();
		validateResourceLimit(references.size(), inputParameters.getResourceLimit());

		List<Reference> referencesToRestore = references;
		if (wasTargetUpdateANoop(theProvenance)) {
			referencesToRestore = references.subList(1, references.size());
		}

		try {
			myResourceVersionRestorer.restoreToPreviousVersionsInTrx(referencesToRestore, theRequestDetails);
			populateSuccessOutcome(referencesToRestore.size(), theProvenance, theUndoMergeOutcome);
		} catch (PartitionedTransactionPartialFailureException thePartialFailure) {
			// Under MegaScale the restore is split per partition and partitions commit independently, so some
			// may have committed before a later one failed. Those committed restores cannot be rolled back
			// automatically; report them for manual reconciliation. Any other failure — a version conflict, a
			// deleted resource, or a single-database atomic rollback where nothing committed — propagates to
			// undoMerge's handler, which surfaces its real status (e.g. 409/410) and leaves the merge intact.
			ourLog.error(
					"Undo-merge partially failed; some partitions committed before a later one failed",
					thePartialFailure);
			buildPartialUndoFailureOutcome(
					referencesToRestore, thePartialFailure, theRequestDetails, theUndoMergeOutcome);
		}
	}

	/**
	 * The restore work of one sub-Provenance: the partition its group extension names, and its data references
	 * (targets from index 2 on — index 0 and 1 are the merge target and source, present in every Provenance of the
	 * group for discovery).
	 */
	private record SubProvenanceRestore(
			Provenance provenance, RequestPartitionId partition, List<Reference> dataRefs) {}

	/**
	 * Undoes a cross-partition merge from its per-partition sub-Provenances, restoring provenance-by-provenance,
	 * each pinned to the partition its group extension names — no cross-shard fan-out reads (which would resolve
	 * through MegaScale ESR surrogate rows and fail on tombstoned originals).
	 *
	 * <p>Ordering: the source partition's sub-Provenance is restored first — it undeletes the tombstoned originals
	 * (and the source itself) that the other partitions' restored referrers point at. The target partition's
	 * sub-Provenance is restored last — it deletes the merge-created copies, which must wait until every referrer
	 * elsewhere has been repointed away from them. The source/target subs are recognized by containing the merge
	 * source/target (the main Provenance's first two targets) among their data references.
	 *
	 * <p>The restores run inside one outer transaction: on a single database that makes the whole undo atomic; when
	 * partition changes commit independently (MegaScale), each per-partition restore commits on its own, the undo
	 * stops at the first failure, and the outcome reports which partitions had already been restored.
	 */
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

		List<SubProvenanceRestore> orderedRestores = orderSubProvenanceRestores(theMainProvenance, theSubProvenances);

		try {
			int restoredCount = myHapiTransactionService
					.withRequest(theRequestDetails)
					.execute(() -> {
						int totalRestored = 0;
						for (SubProvenanceRestore restore : orderedRestores) {
							ourLog.info(
									"Restoring {} resource(s) from sub-Provenance {} pinned to partition {}",
									restore.dataRefs().size(),
									restore.provenance()
											.getIdElement()
											.toUnqualifiedVersionless()
											.getValue(),
									restore.partition());
							myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
									restore.dataRefs(), restore.partition(), theRequestDetails);
							totalRestored += restore.dataRefs().size();
						}
						return totalRestored;
					});
			populateSuccessOutcome(restoredCount, theMainProvenance, theUndoMergeOutcome);
		} catch (Exception theException) {
			ourLog.error(
					"Grouped undo-merge failed; determining which partitions were already committed", theException);
			buildGroupedUndoFailureOutcome(orderedRestores, theException, theUndoMergeOutcome);
		}
	}

	/**
	 * Builds the per-sub-Provenance restore work and orders it: the sub containing the merge source among its data
	 * references first, the sub containing the merge target last, the rest in between (see
	 * {@link #undoGroupedMerge}).
	 */
	private List<SubProvenanceRestore> orderSubProvenanceRestores(
			Provenance theMainProvenance, List<Provenance> theSubProvenances) {

		String targetId = versionlessRefValue(theMainProvenance.getTarget().get(0));
		String sourceId = versionlessRefValue(theMainProvenance.getTarget().get(1));

		SubProvenanceRestore sourceSub = null;
		SubProvenanceRestore targetSub = null;
		List<SubProvenanceRestore> middleSubs = new ArrayList<>();
		for (Provenance sub : theSubProvenances) {
			List<Reference> dataRefs =
					sub.getTarget().subList(2, sub.getTarget().size());
			SubProvenanceRestore restore = new SubProvenanceRestore(sub, extractRequiredPartition(sub), dataRefs);
			if (containsVersionlessRef(dataRefs, sourceId)) {
				sourceSub = restore;
			} else if (containsVersionlessRef(dataRefs, targetId)) {
				targetSub = restore;
			} else {
				middleSubs.add(restore);
			}
		}

		List<SubProvenanceRestore> ordered = new ArrayList<>();
		if (sourceSub != null) {
			ordered.add(sourceSub);
		}
		ordered.addAll(middleSubs);
		if (targetSub != null) {
			ordered.add(targetSub);
		}
		return ordered;
	}

	/**
	 * Extracts the partition a sub-Provenance records changes for, from its group extension's
	 * {@code ;partition=} suffix. Every sub-Provenance is written with one; its absence means the Provenance
	 * group is malformed and the undo cannot restore partition-pinned, so it fails rather than guessing.
	 */
	private RequestPartitionId extractRequiredPartition(Provenance theSubProvenance) {
		String groupId = MergeProvenanceSvc.getProvenanceGroupId(theSubProvenance);
		RequestPartitionId partition = groupId != null ? MergeProvenanceGroupIdUtil.extractPartition(groupId) : null;
		if (partition == null) {
			throw new InternalErrorException(Msg.code(2996)
					+ String.format(
							"The sub-Provenance '%s' does not name the partition it records changes for in its group extension.",
							theSubProvenance.getIdElement().asStringValue()));
		}
		return partition;
	}

	private static String versionlessRefValue(Reference theReference) {
		return theReference.getReferenceElement().toUnqualifiedVersionless().getValue();
	}

	private static boolean containsVersionlessRef(List<Reference> theReferences, String theVersionlessId) {
		return theReferences.stream().anyMatch(ref -> versionlessRefValue(ref).equals(theVersionlessId));
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
	 * Builds the {@link OperationOutcomeWithStatusCode} for a partially-failed undo-merge (only reached for a
	 * {@link PartitionedTransactionPartialFailureException}, i.e. when some partitions committed before a later
	 * one failed). We determine which resources actually persisted by probing each reference's current version
	 * (see {@link #wasReferenceRestored(Reference, RequestPartitionId, RequestDetails)}): a committed restore
	 * advances the version, while a rolled-back one does not. The committed restores cannot be rolled back
	 * automatically, so the outcome reports both sets at 500 for manual reconciliation.
	 */
	private void buildPartialUndoFailureOutcome(
			List<Reference> theRestoredReferences,
			Exception theFailure,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		IBaseOperationOutcome opOutcome = theUndoMergeOutcome.getOperationOutcome();
		theUndoMergeOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);

		List<String> restored = new ArrayList<>();
		List<String> notRestored = new ArrayList<>();
		for (Reference ref : theRestoredReferences) {
			String id = ref.getReferenceElement().toUnqualifiedVersionless().getValue();
			if (wasReferenceRestored(ref, null, theRequestDetails)) {
				restored.add(id);
			} else {
				notRestored.add(id);
			}
		}

		String msg = format(
				"Undo-merge partially failed; changes that were already committed cannot be automatically "
						+ "rolled back and require manual reconciliation. Restored resources: %s. Not restored "
						+ "resources: %s. Cause: %s",
				restored, notRestored, theFailure.getMessage());
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
	}

	/**
	 * Builds the {@link OperationOutcomeWithStatusCode} for a failed grouped undo-merge.
	 *
	 * <p>Per-partition restores commit independently when partitions span separate databases, so a failure partway
	 * through can leave some partitions reverted to their pre-merge state while others remain merged. We determine
	 * which actually persisted by probing the current version of one data reference per sub-Provenance, pinned to
	 * its partition (each per-partition restore is atomic, so one reference tells the whole sub-Provenance's fate).
	 * When everything lives in a single database the failed outer transaction rolls all of it back, so nothing
	 * probes as persisted and the merge is reported as fully intact.
	 */
	private void buildGroupedUndoFailureOutcome(
			List<SubProvenanceRestore> theRestores, Exception theFailure, OperationOutcomeWithStatusCode theOutcome) {

		IBaseOperationOutcome opOutcome = theOutcome.getOperationOutcome();
		theOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);

		List<String> revertedProvenances = new ArrayList<>();
		List<String> notRevertedProvenances = new ArrayList<>();
		for (SubProvenanceRestore restore : theRestores) {
			if (restore.dataRefs().isEmpty()) {
				continue;
			}
			String provenanceId = restore.provenance()
					.getIdElement()
					.toUnqualifiedVersionless()
					.getValue();
			if (wasReferenceRestored(restore.dataRefs().get(0), restore.partition(), null)) {
				revertedProvenances.add(provenanceId);
			} else {
				notRevertedProvenances.add(provenanceId);
			}
		}

		if (revertedProvenances.isEmpty()) {
			// Single-database (atomic) case, or the failure happened before anything was restored: the
			// outer transaction rolled everything back, so the merge is fully intact.
			String msg = format(
					"Undo-merge failed. No resources could be restored; the merge remains fully in effect. Cause: %s",
					theFailure.getMessage());
			addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
			return;
		}

		// Some restores committed independently before the failure and cannot be automatically rolled back.
		// Report at Provenance granularity (each per-partition restore is all-or-nothing).
		String msg = format(
				"Undo-merge partially failed; changes that were already committed cannot be automatically "
						+ "rolled back and require manual reconciliation. Restored provenances: %s. Not restored "
						+ "provenances: %s. Cause: %s",
				revertedProvenances, notRevertedProvenances, theFailure.getMessage());
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
	}

	/**
	 * Returns {@code true} if the resource referenced by the given versioned Provenance reference now has a
	 * current version greater than the version recorded in the reference — i.e. a restore actually committed
	 * for it. A resource that was created by the merge and restored by deleting it is detected via its
	 * tombstone version. When {@code thePartition} is supplied the probe read is pinned to it (a fanned-out
	 * read could resolve through an ESR surrogate row and misreport); otherwise it reads with the caller's
	 * request details. Probe failures are treated as "not restored" so the resource is reported as needing
	 * manual cleanup rather than being silently assumed reverted.
	 */
	private boolean wasReferenceRestored(
			Reference theProvenanceRef,
			@Nullable RequestPartitionId thePartition,
			@Nullable RequestDetails theRequestDetails) {
		IIdType versionedId = theProvenanceRef.getReferenceElement();
		if (!versionedId.hasVersionIdPart()) {
			return false;
		}
		long provenanceVersion = versionedId.getVersionIdPartAsLong();
		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(versionedId.getResourceType());
		RequestDetails probeRequestDetails =
				thePartition != null ? SystemRequestDetails.forRequestPartitionId(thePartition) : theRequestDetails;
		try {
			IBaseResource current = dao.read(versionedId.toUnqualifiedVersionless(), probeRequestDetails);
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
			referencedResources.add(versionlessRefValue(ref));
		}
		for (Provenance sub : theSubProvenances) {
			for (Reference ref : sub.getTarget()) {
				referencedResources.add(versionlessRefValue(ref));
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
