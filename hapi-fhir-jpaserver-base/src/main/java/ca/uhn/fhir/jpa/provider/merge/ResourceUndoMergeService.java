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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.PartitionedTransactionPartialFailureException;
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
import java.util.List;

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
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	public ResourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService) {
		myDaoRegistry = theDaoRegistry;
		myMergeProvenanceSvc = theMergeProvenanceSvc;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
		myMergeValidationService = theMergeValidationService;
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

		// A merge records a single Provenance listing every changed resource across all partitions.
		List<Provenance> provenances = findMergeProvenances(inputParameters, targetId, theRequestDetails);
		Provenance provenance = provenances.get(0);

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-merge operation",
				provenance.getIdElement().asStringValue());

		undoFromProvenance(provenance, inputParameters, theRequestDetails, undoMergeOutcome);

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

	private void undoFromProvenance(
			Provenance theProvenance,
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		ourLog.info(
				"Undoing merge from Provenance: {}",
				theProvenance.getIdElement().toUnqualifiedVersionless().getValue());

		// target.get(0) is the target, target.get(1) is the source, and everything from index 2 on is the changed
		// data across all partitions. The restore submits them as a single cross-partition FHIR transaction; when a
		// transaction-partitioning interceptor is present it splits and dependency-orders the writes per partition
		// — so the resources a referrer points at are committed before the referrer is restored, with no manual
		// source-first/target-last ordering needed here. When the target update was a no-op there is nothing to
		// revert for it, so it is sliced off.
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
	 * Populates the {@link OperationOutcomeWithStatusCode} for a successful undo-merge with an informational
	 * message and a 200 status code.
	 */
	private void populateSuccessOutcome(
			int theRestoredCount, Provenance theProvenance, OperationOutcomeWithStatusCode theUndoMergeOutcome) {
		String msg = format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				theRestoredCount, theProvenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, theUndoMergeOutcome.getOperationOutcome(), null, msg);
		theUndoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);
	}

	/**
	 * Builds the {@link OperationOutcomeWithStatusCode} for a partially-failed undo-merge (only reached for a
	 * {@link PartitionedTransactionPartialFailureException}, i.e. when some partitions committed before a later
	 * one failed). We determine which resources actually persisted by probing each reference's current version
	 * (see {@link #wasReferenceRestored(Reference, RequestDetails)}): a committed restore advances the version,
	 * while a rolled-back one does not. The committed restores cannot be rolled back automatically, so the
	 * outcome reports both sets at 500 for manual reconciliation.
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
			if (wasReferenceRestored(ref, theRequestDetails)) {
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
