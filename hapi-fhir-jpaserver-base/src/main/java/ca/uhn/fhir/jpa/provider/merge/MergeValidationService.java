package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY;

public class MergeValidationService {
	private final FhirContext myFhirContext;
	private final IFhirResourceDao<Patient> myPatientDao;

	public MergeValidationService(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myPatientDao = theDaoRegistry.getResourceDao(Patient.class);
	}

	MergeValidationResult validate(
			MergeOperationInputParameters theMergeOperationParameters,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		IBaseOperationOutcome operationOutcome = theMergeOutcome.getOperationOutcome();

		if (!validateMergeOperationParameters(theMergeOperationParameters, operationOutcome)) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return MergeValidationResult.invalidResult();
		}

		// cast to Patient, since we only support merging Patient resources for now
		Patient sourceResource =
				(Patient) resolveSourceResource(theMergeOperationParameters, theRequestDetails, operationOutcome);

		if (sourceResource == null) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return MergeValidationResult.invalidResult();
		}

		// cast to Patient, since we only support merging Patient resources for now
		Patient targetResource =
				(Patient) resolveTargetResource(theMergeOperationParameters, theRequestDetails, operationOutcome);

		if (targetResource == null) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return MergeValidationResult.invalidResult();
		}

		if (!validateSourceAndTargetAreSuitableForMerge(sourceResource, targetResource, operationOutcome)) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return MergeValidationResult.invalidResult();
		}

		if (!validateResultResourceIfExists(
				theMergeOperationParameters, targetResource, sourceResource, operationOutcome)) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return MergeValidationResult.invalidResult();
		}
		return MergeValidationResult.validResult(sourceResource, targetResource);
	}

	private boolean validateResultResourceIfExists(
			MergeOperationInputParameters theMergeOperationParameters,
			Patient theResolvedTargetResource,
			Patient theResolvedSourceResource,
			IBaseOperationOutcome theOperationOutcome) {

		if (theMergeOperationParameters.getResultResource() == null) {
			// result resource is not provided, no further validation is needed
			return true;
		}

		boolean retval = true;

		Patient theResultResource = (Patient) theMergeOperationParameters.getResultResource();

		// validate the result resource's  id as same as the target resource
		if (!theResolvedTargetResource.getIdElement().toVersionless().equals(theResultResource.getIdElement())) {
			String msg = String.format(
					"'%s' must have the same versionless id as the actual resolved target resource. "
							+ "The actual resolved target resource's id is: '%s'",
					theMergeOperationParameters.getResultResourceParameterName(),
					theResolvedTargetResource.getIdElement().toVersionless().getValue());
			addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
			retval = false;
		}

		// validate the result resource contains the identifiers provided in the target identifiers param
		if (theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& !hasAllIdentifiers(theResultResource, theMergeOperationParameters.getTargetIdentifiers())) {
			String msg = String.format(
					"'%s' must have all the identifiers provided in %s",
					theMergeOperationParameters.getResultResourceParameterName(),
					theMergeOperationParameters.getTargetIdentifiersParameterName());
			addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
			retval = false;
		}

		// if the source resource is not being deleted, the result resource must have a replaces link to the source
		// resource
		// if the source resource is being deleted, the result resource must not have a replaces link to the source
		// resource
		if (!validateResultResourceReplacesLinkToSourceResource(
				theResultResource,
				theResolvedSourceResource,
				theMergeOperationParameters.getResultResourceParameterName(),
				theMergeOperationParameters.getDeleteSource(),
				theOperationOutcome)) {
			retval = false;
		}

		return retval;
	}

	private void addErrorToOperationOutcome(IBaseOperationOutcome theOutcome, String theDiagnosticMsg, String theCode) {
		OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "error", theDiagnosticMsg, null, theCode);
	}

	private boolean hasAllIdentifiers(Patient theResource, List<CanonicalIdentifier> theIdentifiers) {

		List<Identifier> identifiersInResource = theResource.getIdentifier();
		for (CanonicalIdentifier identifier : theIdentifiers) {
			boolean identifierFound = identifiersInResource.stream()
					.anyMatch(i -> i.getSystem()
									.equals(identifier.getSystemElement().getValueAsString())
							&& i.getValue().equals(identifier.getValueElement().getValueAsString()));

			if (!identifierFound) {
				return false;
			}
		}
		return true;
	}

	private boolean validateResultResourceReplacesLinkToSourceResource(
			Patient theResultResource,
			Patient theResolvedSourceResource,
			String theResultResourceParameterName,
			boolean theDeleteSource,
			IBaseOperationOutcome theOperationOutcome) {
		// the result resource must have the replaces link set to the source resource
		List<Reference> replacesLinkToSourceResource = getLinksToResource(
				theResultResource, Patient.LinkType.REPLACES, theResolvedSourceResource.getIdElement());

		if (theDeleteSource) {
			if (!replacesLinkToSourceResource.isEmpty()) {
				String msg = String.format(
						"'%s' must not have a 'replaces' link to the source resource "
								+ "when the source resource will be deleted, as the link may prevent deleting the source "
								+ "resource.",
						theResultResourceParameterName);
				addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
				return false;
			}
		} else {
			if (replacesLinkToSourceResource.isEmpty()) {
				String msg = String.format(
						"'%s' must have a 'replaces' link to the source resource.", theResultResourceParameterName);
				addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
				return false;
			}

			if (replacesLinkToSourceResource.size() > 1) {
				String msg = String.format(
						"'%s' has multiple 'replaces' links to the source resource. There should be only one.",
						theResultResourceParameterName);
				addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
				return false;
			}
		}
		return true;
	}

	private List<Reference> getLinksToResource(
			Patient theResource, Patient.LinkType theLinkType, IIdType theResourceId) {
		List<Reference> links = getLinksOfTypeWithNonNullReference(theResource, theLinkType);
		return links.stream()
				.filter(r -> theResourceId.toVersionless().getValue().equals(r.getReference()))
				.collect(Collectors.toList());
	}

	private List<Reference> getLinksOfTypeWithNonNullReference(Patient theResource, Patient.LinkType theLinkType) {
		List<Reference> links = new ArrayList<>();
		if (theResource.hasLink()) {
			for (Patient.PatientLinkComponent link : theResource.getLink()) {
				if (theLinkType.equals(link.getType()) && link.hasOther()) {
					links.add(link.getOther());
				}
			}
		}
		return links;
	}

	private boolean validateSourceAndTargetAreSuitableForMerge(
			Patient theSourceResource, Patient theTargetResource, IBaseOperationOutcome outcome) {

		if (theSourceResource.getId().equalsIgnoreCase(theTargetResource.getId())) {
			String msg = "Source and target resources are the same resource.";
			// What is the right code to use in these cases?
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		if (theTargetResource.hasActive() && !theTargetResource.getActive()) {
			String msg = "Target resource is not active, it must be active to be the target of a merge operation.";
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		List<Reference> replacedByLinksInTarget =
				getLinksOfTypeWithNonNullReference(theTargetResource, Patient.LinkType.REPLACEDBY);
		if (!replacedByLinksInTarget.isEmpty()) {
			String ref = replacedByLinksInTarget.get(0).getReference();
			String msg = String.format(
					"Target resource was previously replaced by a resource with reference '%s', it "
							+ "is not a suitable target for merging.",
					ref);
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		List<Reference> replacedByLinksInSource =
				getLinksOfTypeWithNonNullReference(theSourceResource, Patient.LinkType.REPLACEDBY);
		if (!replacedByLinksInSource.isEmpty()) {
			String ref = replacedByLinksInSource.get(0).getReference();
			String msg = String.format(
					"Source resource was previously replaced by a resource with reference '%s', it "
							+ "is not a suitable source for merging.",
					ref);
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		return true;
	}

	/**
	 * Validates the merge operation parameters and adds validation errors to the outcome
	 *
	 * @param theMergeOperationParameters the merge operation parameters
	 * @param theOutcome                  the outcome to add validation errors to
	 * @return true if the parameters are valid, false otherwise
	 */
	private boolean validateMergeOperationParameters(
			MergeOperationInputParameters theMergeOperationParameters, IBaseOperationOutcome theOutcome) {
		List<String> errorMessages = new ArrayList<>();
		if (!theMergeOperationParameters.hasAtLeastOneSourceIdentifier()
				&& theMergeOperationParameters.getSourceResource() == null) {
			String msg = String.format(
					"There are no source resource parameters provided, include either a '%s', or a '%s' parameter.",
					theMergeOperationParameters.getSourceResourceParameterName(),
					theMergeOperationParameters.getSourceIdentifiersParameterName());
			errorMessages.add(msg);
		}

		// Spec has conflicting information about this case
		if (theMergeOperationParameters.hasAtLeastOneSourceIdentifier()
				&& theMergeOperationParameters.getSourceResource() != null) {
			String msg = String.format(
					"Source resource must be provided either by '%s' or by '%s', not both.",
					theMergeOperationParameters.getSourceResourceParameterName(),
					theMergeOperationParameters.getSourceIdentifiersParameterName());
			errorMessages.add(msg);
		}

		if (!theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& theMergeOperationParameters.getTargetResource() == null) {
			String msg = String.format(
					"There are no target resource parameters provided, include either a '%s', or a '%s' parameter.",
					theMergeOperationParameters.getTargetResourceParameterName(),
					theMergeOperationParameters.getTargetIdentifiersParameterName());
			errorMessages.add(msg);
		}

		// Spec has conflicting information about this case
		if (theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& theMergeOperationParameters.getTargetResource() != null) {
			String msg = String.format(
					"Target resource must be provided either by '%s' or by '%s', not both.",
					theMergeOperationParameters.getTargetResourceParameterName(),
					theMergeOperationParameters.getTargetIdentifiersParameterName());
			errorMessages.add(msg);
		}

		Reference sourceRef = (Reference) theMergeOperationParameters.getSourceResource();
		if (sourceRef != null && !sourceRef.hasReference()) {
			String msg = String.format(
					"Reference specified in '%s' parameter does not have a reference element.",
					theMergeOperationParameters.getSourceResourceParameterName());
			errorMessages.add(msg);
		}

		Reference targetRef = (Reference) theMergeOperationParameters.getTargetResource();
		if (targetRef != null && !targetRef.hasReference()) {
			String msg = String.format(
					"Reference specified in '%s' parameter does not have a reference element.",
					theMergeOperationParameters.getTargetResourceParameterName());
			errorMessages.add(msg);
		}

		if (!errorMessages.isEmpty()) {
			for (String validationError : errorMessages) {
				addErrorToOperationOutcome(theOutcome, validationError, "required");
			}
			// there are validation errors
			return false;
		}

		// no validation errors
		return true;
	}

	private IBaseResource resolveSourceResource(
			MergeOperationInputParameters theOperationParameters,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome) {
		return resolveResource(
				theOperationParameters.getSourceResource(),
				theOperationParameters.getSourceIdentifiers(),
				theRequestDetails,
				theOutcome,
				theOperationParameters.getSourceResourceParameterName(),
				theOperationParameters.getSourceIdentifiersParameterName());
	}

	private IBaseResource resolveTargetResource(
			MergeOperationInputParameters theOperationParameters,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome) {
		return resolveResource(
				theOperationParameters.getTargetResource(),
				theOperationParameters.getTargetIdentifiers(),
				theRequestDetails,
				theOutcome,
				theOperationParameters.getTargetResourceParameterName(),
				theOperationParameters.getTargetIdentifiersParameterName());
	}

	private IBaseResource resolveResource(
			IBaseReference theReference,
			List<CanonicalIdentifier> theIdentifiers,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome,
			String theOperationReferenceParameterName,
			String theOperationIdentifiersParameterName) {
		if (theReference != null) {
			return resolveResourceByReference(
					theReference, theRequestDetails, theOutcome, theOperationReferenceParameterName);
		}

		return resolveResourceByIdentifiers(
				theIdentifiers, theRequestDetails, theOutcome, theOperationIdentifiersParameterName);
	}

	private IBaseResource resolveResourceByIdentifiers(
			List<CanonicalIdentifier> theIdentifiers,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome,
			String theOperationParameterName) {

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		TokenAndListParam tokenAndListParam = new TokenAndListParam();
		for (CanonicalIdentifier identifier : theIdentifiers) {
			TokenParam tokenParam = new TokenParam(
					identifier.getSystemElement().getValueAsString(),
					identifier.getValueElement().getValueAsString());
			tokenAndListParam.addAnd(tokenParam);
		}
		searchParameterMap.add("identifier", tokenAndListParam);
		searchParameterMap.setCount(2);

		IBundleProvider bundle = myPatientDao.search(searchParameterMap, theRequestDetails);
		List<IBaseResource> resources = bundle.getAllResources();
		if (resources.isEmpty()) {
			String msg = String.format(
					"No resources found matching the identifier(s) specified in '%s'", theOperationParameterName);
			addErrorToOperationOutcome(theOutcome, msg, "not-found");
			return null;
		}
		if (resources.size() > 1) {
			String msg = String.format(
					"Multiple resources found matching the identifier(s) specified in '%s'", theOperationParameterName);
			addErrorToOperationOutcome(theOutcome, msg, "multiple-matches");
			return null;
		}

		return resources.get(0);
	}

	private IBaseResource resolveResourceByReference(
			IBaseReference theReference,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome,
			String theOperationParameterName) {
		// TODO Emre: why does IBaseReference not have getIdentifier or hasReference methods?
		// casting it to r4.Reference for now
		Reference r4ref = (Reference) theReference;

		IIdType theResourceId = new IdType(r4ref.getReferenceElement().getValue());
		IBaseResource resource;
		try {
			resource = myPatientDao.read(theResourceId.toVersionless(), theRequestDetails);
		} catch (ResourceNotFoundException e) {
			String msg = String.format(
					"Resource not found for the reference specified in '%s' parameter", theOperationParameterName);
			addErrorToOperationOutcome(theOutcome, msg, "not-found");
			return null;
		}

		if (theResourceId.hasVersionIdPart()
				&& !theResourceId
						.getVersionIdPart()
						.equals(resource.getIdElement().getVersionIdPart())) {
			String msg = String.format(
					"The reference in '%s' parameter has a version specified, "
							+ "but it is not the latest version of the resource",
					theOperationParameterName);
			addErrorToOperationOutcome(theOutcome, msg, "conflict");
			return null;
		}

		return resource;
	}
}
