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
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.replacereferences.UndoReplaceReferencesSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.batch2.jobs.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.batch2.jobs.merge.MergeResourceHelper.addInfoToOperationOutcome;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_404_NOT_FOUND;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_MERGE;

public class ResourceUndoMergeService {

	private static final Logger ourLog = LoggerFactory.getLogger(UndoReplaceReferencesSvc.class);

	private final MergeProvenanceSvc myMergeProvenanceSvc;
	private final PreviousResourceVersionRestorer myResourceVersionRestorer;
	private final MergeValidationService myMergeValidationService;
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final MergeOperationInputParameterNames myInputParamNames;

	public ResourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myDaoRegistry = theDaoRegistry;
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
		// Use validator functionality to resolve src and target resources
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
			provenance = myMergeProvenanceSvc.findProvenanceByTargetIdAndSourceIdentifiers(targetId, inputParameters.getSourceIdentifiers(), theRequestDetails);
		}

		if (provenance == null) {
			String msg =
					"Unable to find a Provenance created by a $merge operation for the provided source and target resources."
							+ " Ensure that the provided resource references or identifiers were previously used as parameters in a successful $merge operation";
			addErrorToOperationOutcome(myFhirContext, opOutcome, msg, "not-found");
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_404_NOT_FOUND);
		}

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-merge operation",
				provenance.getIdElement().getValue());

		List<Reference> references = provenance.getTarget();
		if (references.size() > inputParameters.getResourceLimit()) {
			String msg = String.format(
				"Number of references to update (%d) exceeds the limit (%d)",
				references.size(), inputParameters.getResourceLimit());
			//FIXME EMRE: update msg.code
			throw new InvalidRequestException(Msg.code(1234) + msg);
		}
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(targetPatient.getIdElement()));

		Set<Reference> allowedToUndelete = new HashSet<>();
		Reference sourceReference = provenance.getTarget().get(1);
		if (wasSourceResourceDeletedByMergeOperation(provenance)) {
			// If the source resource was deleted by the merge operation, let the version restorer know it can be undeleted.
			// There could be a case that the source resource wasn't deleted by the merge operation, but was deleted later by the client.
			// Current behavior in that case is to fail the undo-merge as the source resource might have been updated before it was deleted.
			// We could check if the latest version of the source resource before it was deleted was the version in the Provenance resource + 1,
			// so to confirm that it deleted right after the merge, but that would require additional logic, and not implementing that for now.
			allowedToUndelete.add(sourceReference);
		}

		myResourceVersionRestorer.restoreToPreviousVersionsInTrx(references, allowedToUndelete, theRequestDetails, partitionId);

		String msg = String.format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				references.size(), provenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, opOutcome, null, msg);
		undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);

		return undoMergeOutcome;
	}

	private boolean wasTargetResourceUpdatedByMergeOperation(Patient target) {
		// check here if the target has a replaces link to the source resource and any identifiers marked old, if that is the case return true, otherwise return false


		return true;
	}

	private boolean wasSourceResourceDeletedByMergeOperation(Provenance provenance) {
		if (provenance.hasContained()) {
			List<Resource> containedResources =  provenance.getContained();
			if(!containedResources.isEmpty() && containedResources.get(0) instanceof Parameters parameters) {
				if (parameters.hasParameter(myInputParamNames.getDeleteSourceParameterName())) {
					return parameters.getParameterBool(myInputParamNames.getDeleteSourceParameterName());
				}
				//by default the source resource is not deleted
				//TODO EMRE : maybe move this to a constant
				return false;
			}
		}

		//TODO EMRE: add msg.code and fix string
		throw new InternalErrorException("The provenance resource does not contain inp");

	}
}
