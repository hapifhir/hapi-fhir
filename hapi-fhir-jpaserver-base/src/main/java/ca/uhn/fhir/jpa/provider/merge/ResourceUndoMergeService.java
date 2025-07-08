/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.merge.MergeOperationInputParameterNames;
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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.batch2.jobs.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.batch2.jobs.merge.MergeResourceHelper.addInfoToOperationOutcome;
import static ca.uhn.fhir.model.api.StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CHANGE;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_MERGE;
import static java.lang.String.format;

/**
 * This service implements the $hapi.fhir.undo-merge operation.
 * It reverts the changes made by a previous $merge operation based on the Provenance resource
 * that was created as part of the $merge operation.
 *
 * Current limitations:
 * - It fails if any resources to be restored have been subsequently changed since the `$merge` operation was performed.
 * - It can only run synchronously.
 * - It fails if the number of resources to restore exceeds a specified resource limit
 * (currently set to same size as getInternalSynchronousSearchSize in JPAStorageSettings by the operation provider).
 */
public class ResourceUndoMergeService {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceUndoMergeService.class);

	private final MergeProvenanceSvc myMergeProvenanceSvc;
	private final PreviousResourceVersionRestorer myResourceVersionRestorer;
	private final MergeValidationService myMergeValidationService;
	private final FhirContext myFhirContext;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final MergeOperationInputParameterNames myInputParamNames;

	public ResourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myMergeProvenanceSvc = theMergeProvenanceSvc;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
		myMergeValidationService = theMergeValidationService;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myInputParamNames = new MergeOperationInputParameterNames();
	}

	public OperationOutcomeWithStatusCode undoMerge(
			UndoMergeOperationInputParameters inputParameters, RequestDetails theRequestDetails) {

		OperationOutcomeWithStatusCode undoMergeOutcome = new OperationOutcomeWithStatusCode();
		IBaseOperationOutcome opOutcome = OperationOutcomeUtil.newInstance(myFhirContext);
		undoMergeOutcome.setOperationOutcome(opOutcome);
		try {
			return undoMergeInternal(inputParameters, theRequestDetails, undoMergeOutcome);
		} catch (Exception e) {
			ourLog.error("Undo resource merge failed with an exception", e);
			if (e instanceof BaseServerResponseException) {
				undoMergeOutcome.setHttpStatusCode(((BaseServerResponseException) e).getStatusCode());
			} else {
				undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);
			}
			addErrorToOperationOutcome(myFhirContext, opOutcome, e.getMessage(), "exception");
		}
		return undoMergeOutcome;
	}

	private OperationOutcomeWithStatusCode undoMergeInternal(
			UndoMergeOperationInputParameters inputParameters,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode undoMergeOutcome) {

		IBaseOperationOutcome opOutcome = undoMergeOutcome.getOperationOutcome();

		if (!myMergeValidationService.validateCommonMergeOperationParameters(inputParameters, opOutcome)) {
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return undoMergeOutcome;
		}

		Patient targetPatient =
				(Patient) myMergeValidationService.resolveTargetResource(inputParameters, theRequestDetails, opOutcome);
		IIdType targetId = targetPatient.getIdElement();

		Provenance provenance = null;

		if (inputParameters.getSourceResource() != null) {
			// the client provided a source id, use it to find the provenance together with the target id
			IIdType sourceId = inputParameters.getSourceResource().getReferenceElement();
			provenance =
					myMergeProvenanceSvc.findProvenance(targetId, sourceId, theRequestDetails, OPERATION_UNDO_MERGE);
		} else {
			// the client provided source identifiers, find a provenance using those identifiers and the target id
			provenance = myMergeProvenanceSvc.findProvenanceByTargetIdAndSourceIdentifiers(
					targetId, inputParameters.getSourceIdentifiers(), theRequestDetails);
		}

		if (provenance == null) {
			String msg =
					"Unable to find a Provenance created by a $merge operation for the provided source and target resources."
							+ " Ensure that the provided resource references or identifiers were previously used as parameters in a successful $merge operation";
			throw new ResourceNotFoundException(Msg.code(2747) + msg);
		}

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-merge operation",
				provenance.getIdElement().asStringValue());

		List<Reference> references = provenance.getTarget();
		if (references.size() > inputParameters.getResourceLimit()) {
			String msg = format(
					"Number of references to update (%d) exceeds the limit (%d)",
					references.size(), inputParameters.getResourceLimit());
			throw new InvalidRequestException(Msg.code(2748) + msg);
		}

		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(targetPatient.getIdElement()));

		Set<Reference> allowedToUndelete = new HashSet<>();
		Reference sourceReference = provenance.getTarget().get(1);
		if (wasSourceResourceDeletedByMergeOperation(provenance)) {
			// If the source resource was deleted by the merge operation,
			// let the version restorer know it can be undeleted.
			allowedToUndelete.add(sourceReference);
		}

		List<Reference> referencesToRestore = references;
		if (wasTargetUpdateANoop(provenance)) {
			// skip restoring the target resource if it was not updated by the merge operation.
			// This happens when the merge operation deletes the source resource (so the target doesn't have the
			// replaces link added) and either the source resource didn't have any identifiers that were copied over to
			// the target resource,
			// or a resultPatient that didn't change anything in the target was provided.
			referencesToRestore = references.subList(1, references.size());
		}

		myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
				referencesToRestore, allowedToUndelete, theRequestDetails, partitionId);

		String msg = format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				referencesToRestore.size(), provenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, opOutcome, null, msg);
		undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);

		return undoMergeOutcome;
	}

	private boolean wasSourceResourceDeletedByMergeOperation(Provenance provenance) {
		if (provenance.hasContained()) {
			List<Resource> containedResources = provenance.getContained();
			if (!containedResources.isEmpty() && containedResources.get(0) instanceof Parameters parameters) {
				if (parameters.hasParameter(myInputParamNames.getDeleteSourceParameterName())) {
					return parameters.getParameterBool(myInputParamNames.getDeleteSourceParameterName());
				}
				// by default the source resource is not deleted by the merge operation
				return false;
			}
		}

		throw new InternalErrorException(Msg.code(2749)
				+ format(
						"The provenance resource '%s' does not contain the expected contained resource for the input parameters of the merge operation.",
						provenance.getIdElement().asStringValue()));
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
