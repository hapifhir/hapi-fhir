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
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.MergeProvenanceGroup;
import ca.uhn.fhir.merge.MergeProvenanceGroupUtil;
import ca.uhn.fhir.merge.MergeProvenanceOperation;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ca.uhn.fhir.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.merge.MergeResourceHelper.addInfoToOperationOutcome;
import static ca.uhn.fhir.model.api.StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

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

	public ResourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService,
			IHapiTransactionService theHapiTransactionService) {
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
		if (targetResource == null) {
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return undoMergeOutcome;
		}
		IIdType targetId = targetResource.getIdElement();

		MergeProvenanceGroup provenanceGroup =
				findMergeProvenancesOrThrow(inputParameters, targetId, theRequestDetails);
		Provenance mainProvenance = provenanceGroup.mainProvenance();

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-merge operation",
				mainProvenance.getIdElement().asStringValue());

		if (provenanceGroup.memberProvenances().isEmpty()) {
			undoSingleProvenance(mainProvenance, inputParameters, theRequestDetails, undoMergeOutcome);
		} else {
			undoGroupedMerge(
					mainProvenance,
					provenanceGroup.memberProvenances(),
					inputParameters,
					theRequestDetails,
					undoMergeOutcome);
		}

		return undoMergeOutcome;
	}

	private MergeProvenanceGroup findMergeProvenancesOrThrow(
			UndoMergeOperationInputParameters inputParameters, IIdType targetId, RequestDetails theRequestDetails) {

		Optional<MergeProvenanceGroup> provenanceGroup;
		if (inputParameters.getSourceResource() != null) {
			// the client provided a source id, use it to find the provenance together with the target id
			IIdType sourceId = inputParameters.getSourceResource().getReferenceElement();
			provenanceGroup = myMergeProvenanceSvc.findMergeProvenances(targetId, sourceId, theRequestDetails);
		} else {
			// the client provided source identifiers, find a provenance using those identifiers and the target id
			provenanceGroup = myMergeProvenanceSvc.findMergeProvenancesBySourceIdentifiers(
					targetId, inputParameters.getSourceIdentifiers(), theRequestDetails);
		}

		return provenanceGroup.orElseThrow(() -> {
			String msg =
					"Unable to find a Provenance created by a $merge operation for the provided source and target resources."
							+ " Ensure that the provided resource references or identifiers were previously used as parameters in a successful $merge operation";
			return new ResourceNotFoundException(Msg.code(2747) + msg);
		});
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
			// skip restoring the target resource if it was not updated by the merge operation.
			// This happens when the merge operation deletes the source resource (so the target doesn't have the
			// replaces link added) and either the source resource didn't have any identifiers that were copied over to
			// the target resource,
			// or a resultPatient that didn't change anything in the target was provided.
			referencesToRestore = references.subList(1, references.size());
		}

		myResourceVersionRestorer.restoreToPreviousVersionsInTrx(referencesToRestore, theRequestDetails);
		populateSuccessOutcome(referencesToRestore.size(), theProvenance, theUndoMergeOutcome);
	}

	private record PartitionRestore(
			Provenance provenance,
			RequestPartitionId partition,
			MergeProvenanceOperation operation,
			List<Reference> dataRefs) {}

	private void undoGroupedMerge(
			Provenance theMainProvenance,
			List<Provenance> theChangeProvenances,
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theUndoMergeOutcome) {

		validateGroupedMergeResourceLimit(theMainProvenance, theChangeProvenances, inputParameters);

		ourLog.info(
				"Undoing grouped merge from main Provenance: {} with {} member Provenance(s)",
				theMainProvenance.getIdElement().toUnqualifiedVersionless().getValue(),
				theChangeProvenances.size());

		List<PartitionRestore> orderedRestores = orderPartitionRestores(theMainProvenance, theChangeProvenances);
		List<PartitionRestore> completedRestores = new ArrayList<>();

		try {
			int restoredCount = myHapiTransactionService
					.withRequest(theRequestDetails)
					.execute(() -> {
						int totalRestored = 0;
						for (PartitionRestore restore : orderedRestores) {
							ourLog.info(
									"Restoring {} resource(s) the merge did {} to, from member Provenance {} for partition {}",
									restore.dataRefs().size(),
									restore.operation().getCode(),
									restore.provenance()
											.getIdElement()
											.toUnqualifiedVersionless()
											.getValue(),
									restore.partition());
							myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
									restore.dataRefs(), restore.partition(), theRequestDetails);
							completedRestores.add(restore);
							totalRestored += restore.dataRefs().size();
						}
						return totalRestored;
					});
			populateSuccessOutcome(restoredCount, theMainProvenance, theUndoMergeOutcome);
		} catch (Exception theException) {
			if (!myHapiTransactionService.isRequiresNewTransactionWhenChangingPartitions()) {
				throw theException;
			}
			buildNonAtomicUndoFailureOutcome(orderedRestores, completedRestores, theException, theUndoMergeOutcome);
		}
	}

	/**
	 * Orders the restores to preserve referential integrity: resources the merge deleted are undeleted first, so that
	 * the referrers restored afterwards point at live resources; resources the merge created are deleted last, once
	 * every referrer has been repointed away from them.
	 */
	private List<PartitionRestore> orderPartitionRestores(
			Provenance theMainProvenance, List<Provenance> theChangeProvenances) {

		String versionlessTargetId =
				versionlessRefValue(theMainProvenance.getTarget().get(0));
		String versionlessSourceId =
				versionlessRefValue(theMainProvenance.getTarget().get(1));

		List<PartitionRestore> restores = new ArrayList<>();
		for (Provenance memberProvenance : theChangeProvenances) {
			// first two targets are the merge target and source, used to locate this Provenance
			// the rest are the refs to restore
			validateFirstTwoTargetsAreTargetAndSource(memberProvenance, versionlessTargetId, versionlessSourceId);
			List<Reference> dataRefs = memberProvenance
					.getTarget()
					.subList(2, memberProvenance.getTarget().size());

			restores.add(new PartitionRestore(
					memberProvenance,
					extractRequiredPartition(memberProvenance),
					extractRequiredOperation(memberProvenance),
					dataRefs));
		}

		restores.sort(Comparator.comparingInt(restore -> restore.operation().getUndoOrder()));
		return restores;
	}

	private void validateFirstTwoTargetsAreTargetAndSource(
			Provenance theChangeProvenance, String theVersionlessTargetId, String theVersionlessSourceId) {

		List<Reference> targets = theChangeProvenance.getTarget();
		if (targets.size() < 2
				|| !versionlessRefValue(targets.get(0)).equals(theVersionlessTargetId)
				|| !versionlessRefValue(targets.get(1)).equals(theVersionlessSourceId)) {
			throw new InternalErrorException(Msg.code(2998)
					+ String.format(
							"The member Provenance '%s' does not start with the merge target '%s' and source '%s' references.",
							theChangeProvenance.getIdElement().asStringValue(),
							theVersionlessTargetId,
							theVersionlessSourceId));
		}
	}

	private RequestPartitionId extractRequiredPartition(Provenance theChangeProvenance) {
		return MergeProvenanceGroupUtil.getProvenanceGroupValue(theChangeProvenance)
				.flatMap(MergeProvenanceGroupUtil::extractPartition)
				.orElseThrow(() -> new InternalErrorException(Msg.code(2996)
						+ String.format(
								"The member Provenance '%s' does not name the partition it records changes for in its group extension.",
								theChangeProvenance.getIdElement().asStringValue())));
	}

	private MergeProvenanceOperation extractRequiredOperation(Provenance theChangeProvenance) {
		return MergeProvenanceGroupUtil.getProvenanceGroupValue(theChangeProvenance)
				.flatMap(MergeProvenanceGroupUtil::extractOperation)
				.orElseThrow(() -> new InternalErrorException(Msg.code(3000)
						+ String.format(
								"The member Provenance '%s' does not name the operation it records changes for in its group extension.",
								theChangeProvenance.getIdElement().asStringValue())));
	}

	private static String versionlessRefValue(Reference theReference) {
		return theReference.getReferenceElement().toUnqualifiedVersionless().getValue();
	}

	private void populateSuccessOutcome(
			int theRestoredCount, Provenance theMainProvenance, OperationOutcomeWithStatusCode theUndoMergeOutcome) {
		String msg = format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				theRestoredCount, theMainProvenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, theUndoMergeOutcome.getOperationOutcome(), null, msg);
		theUndoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);
	}

	private void buildNonAtomicUndoFailureOutcome(
			List<PartitionRestore> theOrderedRestores,
			List<PartitionRestore> theCompletedRestores,
			Exception theFailure,
			OperationOutcomeWithStatusCode theOutcome) {

		IBaseOperationOutcome opOutcome = theOutcome.getOperationOutcome();
		theOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);

		if (theCompletedRestores.isEmpty()) {
			String msg = format(
					"Undo-merge failed. No resources could be restored; the merge remains fully in effect. "
							+ "Undo failure cause: %s",
					theFailure.getMessage());
			addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
			return;
		}

		List<PartitionRestore> remainingRestores = new ArrayList<>(theOrderedRestores);
		remainingRestores.removeAll(theCompletedRestores);

		String msg = format(
				"Undo-merge failed partway through and could not be rolled back automatically. The changes recorded "
						+ "by the following Provenance resources may have been restored, but may also have been left "
						+ "in their merged state; they should be checked and, if necessary, reconciled manually: %s. "
						+ "The changes recorded by these Provenance resources were not restored: %s. "
						+ "Undo failure cause: %s",
				describeProvenances(theCompletedRestores),
				describeProvenances(remainingRestores),
				theFailure.getMessage());
		ourLog.error("Reporting undo-merge failure to caller: {}", msg);
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
	}

	private static String describeProvenances(List<PartitionRestore> theRestores) {
		return theRestores.stream()
				.map(restore -> restore.provenance()
						.getIdElement()
						.toUnqualifiedVersionless()
						.getValue())
				.collect(joining(", "));
	}

	private void validateGroupedMergeResourceLimit(
			Provenance theMainProvenance,
			List<Provenance> theChangeProvenances,
			UndoMergeOperationInputParameters inputParameters) {
		Set<String> referencedResources = new HashSet<>();
		for (Reference ref : theMainProvenance.getTarget()) {
			referencedResources.add(versionlessRefValue(ref));
		}
		for (Provenance memberProvenance : theChangeProvenances) {
			for (Reference ref : memberProvenance.getTarget()) {
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
