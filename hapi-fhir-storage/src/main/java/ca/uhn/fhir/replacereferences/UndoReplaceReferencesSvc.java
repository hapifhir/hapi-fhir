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

import java.util.List;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;

/**
 * This service is implements the $hapi.fhir.replace-references operation.
 * It reverts the changes made by $hapi.fhir.replace-references operation based on the Provenance resource
 * that was created as part of the $hapi.fhir.replace-references operation.
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
				toRestore, theRequestDetails, theUndoReplaceReferencesRequest.partitionId);

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
