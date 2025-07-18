/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;

/**
 * This service is implements the $hapi.fhir.replace-references operation.
 * It reverts the changes made by $hapi.fhir.replace-references operation based on the Provenance resource
 * that was created as part of the $hapi.fhir.replace-references operation.
 *
 *  Current limitations:
 * - It fails if any resources to be restored have been subsequently changed since the `$hapi.fhir.replace-references` operation was performed.
 * - It can only run synchronously.
 * - It fails if the number of resources to restore exceeds a specified resource limit
 * (currently set to same size as getInternalSynchronousSearchSize in JPAStorageSettings by the operation provider).
 */
public class UndoReplaceReferencesSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(UndoReplaceReferencesSvc.class);

	private final ReplaceReferencesProvenanceSvc myReplaceReferencesProvenanceSvc;
	private final PreviousResourceVersionRestorer myResourceVersionRestorer;
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	public UndoReplaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			ReplaceReferencesProvenanceSvc theReplaceReferencesProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer) {
		myDaoRegistry = theDaoRegistry;
		myReplaceReferencesProvenanceSvc = theReplaceReferencesProvenanceSvc;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	public IBaseParameters undoReplaceReferences(
			UndoReplaceReferencesRequest theUndoReplaceReferencesRequest, RequestDetails theRequestDetails) {

		// read source and target to ensure they still exist
		readResource(theUndoReplaceReferencesRequest.sourceId, theRequestDetails);
		readResource(theUndoReplaceReferencesRequest.targetId, theRequestDetails);

		Provenance provenance = myReplaceReferencesProvenanceSvc.findProvenance(
				theUndoReplaceReferencesRequest.targetId,
				theUndoReplaceReferencesRequest.sourceId,
				theRequestDetails,
				ProviderConstants.OPERATION_UNDO_REPLACE_REFERENCES);

		if (provenance == null) {
			String msg =
					"Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs."
							+ " Ensure that IDs are correct and were previously used as parameters in a successful $hapi.fhir.replace-references operation";
			throw new ResourceNotFoundException(Msg.code(2728) + msg);
		}

		ourLog.info(
				"Found Provenance resource with id: {} to be used for $undo-replace-references operation",
				provenance.getIdElement().getValue());

		List<Reference> references = provenance.getTarget();
		// in replace-references operation provenance, the first two references are to the target and the source,
		// and they are not updated as part of the operation so we should not restore their previous versions.
		List<Reference> toRestore = references.subList(2, references.size());

		if (toRestore.size() > theUndoReplaceReferencesRequest.resourceLimit) {
			String msg = String.format(
					"Number of references to update (%d) exceeds the limit (%d)",
					toRestore.size(), theUndoReplaceReferencesRequest.resourceLimit);
			throw new InvalidRequestException(Msg.code(2729) + msg);
		}

		myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
				toRestore, Collections.emptySet(), theRequestDetails, theUndoReplaceReferencesRequest.partitionId);

		IBaseOperationOutcome opOutcome = OperationOutcomeUtil.newInstance(myFhirContext);
		String msg = String.format(
				"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
				toRestore.size(), provenance.getIdElement().getValue());
		OperationOutcomeUtil.addIssue(myFhirContext, opOutcome, "information", msg, null, null);

		IBaseParameters outputParameters = ParametersUtil.newInstance(myFhirContext);

		ParametersUtil.addParameterToParameters(
				myFhirContext, outputParameters, OPERATION_UNDO_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME, opOutcome);

		return outputParameters;
	}

	private IBaseResource readResource(IIdType theId, RequestDetails theRequestDetails) {
		String resourceType = theId.getResourceType();
		IFhirResourceDao<IBaseResource> resourceDao = myDaoRegistry.getResourceDao(resourceType);
		return resourceDao.read(theId, theRequestDetails);
	}
}
