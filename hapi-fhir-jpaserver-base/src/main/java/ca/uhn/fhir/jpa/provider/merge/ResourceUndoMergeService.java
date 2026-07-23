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

		try {
			myResourceVersionRestorer.restoreToPreviousVersionsInTrx(referencesToRestore, theRequestDetails);
			populateSuccessOutcome(referencesToRestore.size(), theProvenance, theUndoMergeOutcome);
		} catch (PartitionedTransactionPartialFailureException thePartialFailure) {
			ourLog.error(
					"Undo-merge partially failed; some partitions committed before a later one failed",
					thePartialFailure);
			buildPartialUndoFailureOutcome(
					referencesToRestore, thePartialFailure, theRequestDetails, theUndoMergeOutcome);
		}
	}

	private record SubProvenanceRestore(
			Provenance provenance, RequestPartitionId partition, List<Reference> dataRefs) {}

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

	private void populateSuccessOutcome(
			int theRestoredCount, Provenance theMainProvenance, OperationOutcomeWithStatusCode theUndoMergeOutcome) {
		String msg = format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				theRestoredCount, theMainProvenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, theUndoMergeOutcome.getOperationOutcome(), null, msg);
		theUndoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);
	}

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
			String msg = format(
					"Undo-merge failed. No resources could be restored; the merge remains fully in effect. Cause: %s",
					theFailure.getMessage());
			addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
			return;
		}

		String msg = format(
				"Undo-merge partially failed; changes that were already committed cannot be automatically "
						+ "rolled back and require manual reconciliation. Restored provenances: %s. Not restored "
						+ "provenances: %s. Cause: %s",
				revertedProvenances, notRevertedProvenances, theFailure.getMessage());
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
	}

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
