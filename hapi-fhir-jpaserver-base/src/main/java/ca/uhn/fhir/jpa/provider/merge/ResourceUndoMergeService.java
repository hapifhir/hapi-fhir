package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.replacereferences.UndoReplaceReferencesSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_404_NOT_FOUND;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_MERGE_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_UNDO_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;

public class ResourceUndoMergeService {

	private static final Logger ourLog = LoggerFactory.getLogger(UndoReplaceReferencesSvc.class);

	private final MergeProvenanceSvc myMergeProvenance;
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
		myMergeProvenance = theMergeProvenanceSvc;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
		myMergeValidationService = theMergeValidationService;
	}


	public OperationOutcomeWithStatusCode undoMerge(PatientUndoMergeOperationInputParameters inputParameters, RequestDetails theRequestDetails) {

		OperationOutcomeWithStatusCode undoMergeOutcome = new OperationOutcomeWithStatusCode();
		IBaseOperationOutcome opOutcome = OperationOutcomeUtil.newInstance(myFhirContext);
		undoMergeOutcome.setOperationOutcome(opOutcome);

		// Use validator functionality to resolve src and target resources
		if(myMergeValidationService.validateCommonMergeOperationParameters(inputParameters, opOutcome)) {
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return undoMergeOutcome;
		}

		Patient targetPatient = (Patient) myMergeValidationService.resolveTargetResource(inputParameters, theRequestDetails, opOutcome);

		Patient sourcePatient = null;
		try {
			sourcePatient = (Patient) myMergeValidationService.resolveSourceResource(inputParameters, theRequestDetails, opOutcome);
		} catch (ResourceGoneException goneException) {
			//source patient might have been deleted as a result of the merge operation,
			//we will verify if that is the case from the provenance resource
		}


		Provenance provenance = null;

		IIdType targetId = targetPatient.getIdElement();

		if (sourcePatient != null || inputParameters.getSourceResource() != null) {
			// the source resource wasn't deleted or the client provided a source reference,
			// in each case we know the resource id of the source resource, and we can use it to find the provenance
			IIdType sourceId = inputParameters.getSourceResource().getReferenceElement();
			Provenance provenance = myMergeProvenance.findProvenance(targetId, sourceId, theRequestDetails, OPERATION_UNDO_MERGE);

		}
		else {
			// the client provided source identifiers, find a provenance using those identifiers and the target reference
		}

		if (provenance == null) {
			String msg =
				"Unable to find a Provenance created by a $merge operation for the provided source and target resources."
					+ " Ensure that the provided resource references or identifiers were previously used as parameters in a successful $merge operation";
			addErrorToOperationOutcome(opOutcome, msg, "not-found");
			undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_404_NOT_FOUND);
		}

		//FIXME ED: the cound in the message
		String msg = String.format(
			"Successfully restored %d resources to their previous versions based on the Provenance resource: %s",
			10, provenance.getIdElement().getValue());
		OperationOutcomeUtil.addIssue(myFhirContext, opOutcome, "information", msg, null, null);
		undoMergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);

		return undoMergeOutcome;
	}



	private void addErrorToOperationOutcome(IBaseOperationOutcome theOutcome, String theDiagnosticMsg, String theCode) {
		OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "error", theDiagnosticMsg, null, theCode);
	}

}
