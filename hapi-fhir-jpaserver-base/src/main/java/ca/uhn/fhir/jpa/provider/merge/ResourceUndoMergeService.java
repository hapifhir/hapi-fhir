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
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.replacereferences.UndoReplaceReferencesSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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

		Patient sourcePatient = null;
		try {
			sourcePatient = (Patient)
					myMergeValidationService.resolveSourceResource(inputParameters, theRequestDetails, opOutcome);
		} catch (ResourceGoneException goneException) {
			// source patient might have been deleted as a result of the merge operation,
			// we will verify if that is the case from the provenance resource
		}

		Provenance provenance = null;

		IIdType targetId = targetPatient.getIdElement();

		if (sourcePatient != null || inputParameters.getSourceResource() != null) {
			// the source resource wasn't deleted or the client provided a source reference,
			// in each case we know the resource id of the source resource, and we can use it to find the provenance
			IIdType sourceId = inputParameters.getSourceResource().getReferenceElement();
			provenance =
					myMergeProvenanceSvc.findProvenance(targetId, sourceId, theRequestDetails, OPERATION_UNDO_MERGE);
		} else {
			// the client provided source identifiers, find a provenance using those identifiers and the target
			// reference
		}

		if (provenance == null) {
			String msg =
					"Unable to find a Provenance created by a $merge operation for the provided source and target resources."
							+ " Ensure that the provided resource references or identifiers were previously used as parameters in a successful $merge operation";
			addErrorToOperationOutcome(myFhirContext, opOutcome, msg, "not-found");
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_404_NOT_FOUND);
		}

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-replace-references operation",
				provenance.getIdElement().getValue());

		List<Reference> references = provenance.getTarget();
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(targetPatient.getIdElement()));

		if (sourcePatient == null) {
			// here we should check if the source resource was deleted after the merge operation,
			// if it wasn't but gone then fail the operation (we could check if the last version of it is what is in the
			// provenance + 1 and
			// maybe allow it in that case but not implementing that for now)

		}

		Reference sourceReference = provenance.getTarget().get(1);
		Set<Reference> allowedToUndelete = sourcePatient == null ? Set.of(sourceReference) : Collections.emptySet();
		myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
				references, allowedToUndelete, theRequestDetails, partitionId);

		String msg = String.format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				references.size(), provenance.getIdElement().getValue());
		addInfoToOperationOutcome(myFhirContext, opOutcome, null, msg);
		undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);

		return undoMergeOutcome;
	}
}
