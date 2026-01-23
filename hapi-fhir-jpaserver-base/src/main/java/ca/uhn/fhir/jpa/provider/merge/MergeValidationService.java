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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.IResourceLinkService;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY;

/**
 * Supporting class that validates input parameters to {@link ResourceMergeService}.
 */
public class MergeValidationService {
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final ResourceLinkServiceFactory myResourceLinkServiceFactory;
	private final FhirTerser myFhirTerser;

	public MergeValidationService(
			FhirContext theFhirContext,
			DaoRegistry theDaoRegistry,
			ResourceLinkServiceFactory theResourceLinkServiceFactory) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myResourceLinkServiceFactory = theResourceLinkServiceFactory;
		myFhirTerser = myFhirContext.newTerser();
	}

	MergeValidationResult validate(
			MergeOperationInputParameters theMergeOperationParameters,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		// Initialize parameter names based on operation type
		AbstractMergeOperationInputParameterNames parameterNames =
				AbstractMergeOperationInputParameterNames.forOperation(theRequestDetails.getOperation());

		IBaseOperationOutcome operationOutcome = theMergeOutcome.getOperationOutcome();

		// Validate that the resource type supports identifier element
		String resourceType = theRequestDetails.getResourceName();
		if (!validateResourceTypeSupportsIdentifier(resourceType, operationOutcome)) {
			return MergeValidationResult.invalidResult(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
		}

		if (!validateCommonMergeOperationParameters(
				theMergeOperationParameters, operationOutcome, parameterNames, theRequestDetails)) {
			return MergeValidationResult.invalidResult(STATUS_HTTP_400_BAD_REQUEST);
		}

		IBaseResource sourceResource =
				resolveSourceResource(theMergeOperationParameters, theRequestDetails, operationOutcome, parameterNames);

		if (sourceResource == null) {
			return MergeValidationResult.invalidResult(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
		}

		IBaseResource targetResource =
				resolveTargetResource(theMergeOperationParameters, theRequestDetails, operationOutcome, parameterNames);

		if (targetResource == null) {
			return MergeValidationResult.invalidResult(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
		}

		if (!validateSourceAndTargetAreSuitableForMerge(sourceResource, targetResource, operationOutcome)) {
			return MergeValidationResult.invalidResult(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
		}

		if (!validateResultResourceIfExists(
				theMergeOperationParameters, targetResource, sourceResource, operationOutcome, parameterNames)) {
			return MergeValidationResult.invalidResult(STATUS_HTTP_400_BAD_REQUEST);
		}
		return MergeValidationResult.validResult(sourceResource, targetResource);
	}

	private boolean validateResultResourceIfExists(
			MergeOperationInputParameters theMergeOperationParameters,
			IBaseResource theResolvedTargetResource,
			IBaseResource theResolvedSourceResource,
			IBaseOperationOutcome theOperationOutcome,
			AbstractMergeOperationInputParameterNames theParameterNames) {

		if (theMergeOperationParameters.getResultResource() == null) {
			// result resource is not provided, no further validation is needed
			return true;
		}

		boolean retval = true;

		IBaseResource theResultResource = theMergeOperationParameters.getResultResource();

		// validate the result resource's  id as same as the target resource
		if (!theResolvedTargetResource.getIdElement().toVersionless().equals(theResultResource.getIdElement())) {
			String msg = String.format(
					"'%s' must have the same versionless id as the actual resolved target resource '%s'. "
							+ "The actual resolved target resource's id is: '%s'",
					theParameterNames.getResultResourceParameterName(),
					theResultResource.getIdElement(),
					theResolvedTargetResource.getIdElement().toVersionless().getValue());
			addErrorToOperationOutcome(myFhirContext, theOperationOutcome, msg, "invalid");
			retval = false;
		}

		// validate the result resource contains the identifiers provided in the target identifiers param
		if (theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& !hasAllIdentifiers(theResultResource, theMergeOperationParameters.getTargetIdentifiers())) {
			String msg = String.format(
					"'%s' must have all the identifiers provided in %s",
					theParameterNames.getResultResourceParameterName(),
					theParameterNames.getTargetIdentifiersParameterName());
			addErrorToOperationOutcome(myFhirContext, theOperationOutcome, msg, "invalid");
			retval = false;
		}

		// if the source resource is not being deleted, the result resource must have a replaces link to the source
		// resource
		// if the source resource is being deleted, the result resource must not have a replaces link to the source
		// resource
		if (!validateResultResourceReplacesLinkToSourceResource(
				theResultResource,
				theResolvedSourceResource,
				theParameterNames.getResultResourceParameterName(),
				theMergeOperationParameters.getDeleteSource(),
				theOperationOutcome)) {
			retval = false;
		}

		return retval;
	}

	private boolean hasAllIdentifiers(IBaseResource theResource, List<CanonicalIdentifier> theIdentifiers) {

		// Get identifiers using FhirTerser (works for any FHIR resource and version)
		List<IBase> identifiersInResource = myFhirTerser.getValues(theResource, "identifier");

		// Convert to CanonicalIdentifier for comparison
		Set<CanonicalIdentifier> resourceIdentifiers = identifiersInResource.stream()
				.map(CanonicalIdentifier::fromIdentifier)
				.collect(Collectors.toSet());

		// Check if resource contains all required identifiers
		return resourceIdentifiers.containsAll(theIdentifiers);
	}

	private boolean validateResultResourceReplacesLinkToSourceResource(
			IBaseResource theResultResource,
			IBaseResource theResolvedSourceResource,
			String theResultResourceParameterName,
			boolean theDeleteSource,
			IBaseOperationOutcome theOperationOutcome) {

		List<IBaseReference> replacesLinkToSourceResource =
				getReplacesLinksTo(theResultResource, theResolvedSourceResource.getIdElement());

		if (theDeleteSource) {
			if (!replacesLinkToSourceResource.isEmpty()) {
				String msg = String.format(
						"'%s' must not have a 'replaces' link to the source resource "
								+ "when the source resource will be deleted, as the link may prevent deleting the source "
								+ "resource.",
						theResultResourceParameterName);
				addErrorToOperationOutcome(myFhirContext, theOperationOutcome, msg, "invalid");
				return false;
			}
		} else {
			if (replacesLinkToSourceResource.isEmpty()) {
				String msg = String.format(
						"'%s' must have a 'replaces' link to the source resource.", theResultResourceParameterName);
				addErrorToOperationOutcome(myFhirContext, theOperationOutcome, msg, "invalid");
				return false;
			}

			if (replacesLinkToSourceResource.size() > 1) {
				String msg = String.format(
						"'%s' has multiple 'replaces' links to the source resource. There should be only one.",
						theResultResourceParameterName);
				addErrorToOperationOutcome(myFhirContext, theOperationOutcome, msg, "invalid");
				return false;
			}
		}
		return true;
	}

	/**
	 * Helper method to get all "replaces" links from a resource that point to a specific target resource.
	 *
	 * @param theResource the resource to check
	 * @param theTargetId the target resource ID to look for
	 * @return list of references to the target resource (typically 0 or 1)
	 */
	private List<IBaseReference> getReplacesLinksTo(IBaseResource theResource, IIdType theTargetId) {
		IResourceLinkService linkService = myResourceLinkServiceFactory.getServiceForResource(theResource);
		List<IBaseReference> replacesLinks = linkService.getReplacesLinks(theResource);
		List<IBaseReference> matchingLinks = new ArrayList<>();

		String targetIdValue = theTargetId.toUnqualifiedVersionless().getValue();

		for (IBaseReference link : replacesLinks) {
			IIdType linkRefElement = link.getReferenceElement();
			if (linkRefElement == null) {
				continue;
			}

			String linkIdValue = linkRefElement.toUnqualifiedVersionless().getValue();

			if (targetIdValue.equals(linkIdValue)) {
				matchingLinks.add(link);
			}
		}

		return matchingLinks;
	}

	private boolean validateSourceAndTargetAreSuitableForMerge(
			IBaseResource theSourceResource, IBaseResource theTargetResource, IBaseOperationOutcome outcome) {

		String versionlessSourceId =
				theSourceResource.getIdElement().toUnqualifiedVersionless().getValue();
		String versionlessTargetId =
				theTargetResource.getIdElement().toUnqualifiedVersionless().getValue();

		if (versionlessSourceId.equalsIgnoreCase(versionlessTargetId)) {
			String msg = "Source and target resources are the same resource.";
			// What is the right code to use in these cases?
			addErrorToOperationOutcome(myFhirContext, outcome, msg, "invalid");
			return false;
		}

		// Check if target resource has an active field and if it's set to false
		// Note: Not all resource types have an 'active' field (e.g., Observation doesn't)
		if (myFhirTerser.fieldExists("active", theTargetResource)) {
			IPrimitiveType<?> activePrimitive =
					myFhirTerser.getSingleValueOrNull(theTargetResource, "active", IPrimitiveType.class);
			if (activePrimitive != null && Boolean.FALSE.equals(activePrimitive.getValue())) {
				String msg = "Target resource is not active, it must be active to be the target of a merge operation.";
				addErrorToOperationOutcome(myFhirContext, outcome, msg, "invalid");
				return false;
			}
		}

		// Use IResourceLinkService to check for replaced-by links
		IResourceLinkService linkService = myResourceLinkServiceFactory.getServiceForResource(theTargetResource);
		List<IBaseReference> replacedByLinksInTarget = linkService.getReplacedByLinks(theTargetResource);
		if (!replacedByLinksInTarget.isEmpty()) {
			IIdType refEl = replacedByLinksInTarget.get(0).getReferenceElement();
			String ref = refEl == null ? "null" : refEl.getValue();
			String msg = String.format(
					"Target resource was previously replaced by a resource with reference '%s', it "
							+ "is not a suitable target for merging.",
					ref);
			addErrorToOperationOutcome(myFhirContext, outcome, msg, "invalid");
			return false;
		}

		List<IBaseReference> replacedByLinksInSource = linkService.getReplacedByLinks(theSourceResource);
		if (!replacedByLinksInSource.isEmpty()) {
			String ref = replacedByLinksInSource.get(0).getReferenceElement().getValue();
			String msg = String.format(
					"Source resource was previously replaced by a resource with reference '%s', it "
							+ "is not a suitable source for merging.",
					ref);
			addErrorToOperationOutcome(myFhirContext, outcome, msg, "invalid");
			return false;
		}

		return true;
	}

	/**
	 * Validates that the resource type supports the 'identifier' element, which is required for merge operations.
	 *
	 * @param theResourceType the resource type to validate
	 * @param theOutcome the outcome to add validation errors to
	 * @return true if the resource type supports identifier, false otherwise
	 */
	private boolean validateResourceTypeSupportsIdentifier(String theResourceType, IBaseOperationOutcome theOutcome) {
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
		BaseRuntimeChildDefinition identifierChild = resourceDefinition.getChildByName("identifier");

		if (identifierChild == null) {
			String msg = String.format(
					"Merge operation cannot be performed on resource type '%s' because it does not have an 'identifier' element.",
					theResourceType);
			addErrorToOperationOutcome(myFhirContext, theOutcome, msg, "invalid");
			return false;
		}

		return true;
	}

	/**
	 * Validates the common input parameters to both merge and undo-merge operations and adds validation errors to the outcome
	 *
	 * @param theCommonInputParameters the operation input parameters
	 * @param theOutcome the outcome to add validation errors to
	 * @param theParameterNames the parameter names for the operation
	 * @param theRequestDetails the request details
	 * @return true if the parameters are valid, false otherwise
	 */
	boolean validateCommonMergeOperationParameters(
			MergeOperationsCommonInputParameters theCommonInputParameters,
			IBaseOperationOutcome theOutcome,
			AbstractMergeOperationInputParameterNames theParameterNames,
			RequestDetails theRequestDetails) {
		List<String> errorMessages = new ArrayList<>();
		if (!theCommonInputParameters.hasAtLeastOneSourceIdentifier()
				&& theCommonInputParameters.getSourceResource() == null) {
			String msg = String.format(
					"There are no source resource parameters provided, include either a '%s', or a '%s' parameter.",
					theParameterNames.getSourceResourceParameterName(),
					theParameterNames.getSourceIdentifiersParameterName());
			errorMessages.add(msg);
		}

		// Spec has conflicting information about this case
		if (theCommonInputParameters.hasAtLeastOneSourceIdentifier()
				&& theCommonInputParameters.getSourceResource() != null) {
			String msg = String.format(
					"Source resource must be provided either by '%s' or by '%s', not both.",
					theParameterNames.getSourceResourceParameterName(),
					theParameterNames.getSourceIdentifiersParameterName());
			errorMessages.add(msg);
		}

		if (!theCommonInputParameters.hasAtLeastOneTargetIdentifier()
				&& theCommonInputParameters.getTargetResource() == null) {
			String msg = String.format(
					"There are no target resource parameters provided, include either a '%s', or a '%s' parameter.",
					theParameterNames.getTargetResourceParameterName(),
					theParameterNames.getTargetIdentifiersParameterName());
			errorMessages.add(msg);
		}

		// Spec has conflicting information about this case
		if (theCommonInputParameters.hasAtLeastOneTargetIdentifier()
				&& theCommonInputParameters.getTargetResource() != null) {
			String msg = String.format(
					"Target resource must be provided either by '%s' or by '%s', not both.",
					theParameterNames.getTargetResourceParameterName(),
					theParameterNames.getTargetIdentifiersParameterName());
			errorMessages.add(msg);
		}

		Reference sourceRef = (Reference) theCommonInputParameters.getSourceResource();
		if (sourceRef != null && !sourceRef.hasReference()) {
			String msg = String.format(
					"Reference specified in '%s' parameter does not have a reference element.",
					theParameterNames.getSourceResourceParameterName());
			errorMessages.add(msg);
		}

		Reference targetRef = (Reference) theCommonInputParameters.getTargetResource();
		if (targetRef != null && !targetRef.hasReference()) {
			String msg = String.format(
					"Reference specified in '%s' parameter does not have a reference element.",
					theParameterNames.getTargetResourceParameterName());
			errorMessages.add(msg);
		}

		// Validate if source reference exists its resource type matches API path resource type
		String apiResourceType = theRequestDetails.getResourceName();
		validateReferenceResourceTypeMatchesRequest(
				sourceRef, apiResourceType, theParameterNames.getSourceResourceParameterName(), errorMessages);

		// Validate if target reference exists its resource type matches API path resource type
		validateReferenceResourceTypeMatchesRequest(
				targetRef, apiResourceType, theParameterNames.getTargetResourceParameterName(), errorMessages);

		if (!errorMessages.isEmpty()) {
			for (String validationError : errorMessages) {
				addErrorToOperationOutcome(myFhirContext, theOutcome, validationError, "required");
			}
			// there are validation errors
			return false;
		}

		// no validation errors
		return true;
	}

	/**
	 * Validates that the resource type in a reference matches the API path resource type.
	 *
	 * @param theReference the reference to validate
	 * @param theApiResourceType the expected resource type from the API path
	 * @param theParameterName the parameter name for error messages
	 * @param theErrorMessages the list to add error messages to
	 */
	private void validateReferenceResourceTypeMatchesRequest(
			Reference theReference, String theApiResourceType, String theParameterName, List<String> theErrorMessages) {
		if (theReference != null && theReference.hasReference()) {
			IdType id = new IdType(theReference.getReference());
			String referenceResourceType = id.getResourceType();
			if (!Objects.equals(referenceResourceType, theApiResourceType)) {
				String msg = String.format(
						"The request was for \"%s\" type but %s contains a reference for \"%s\" type",
						theApiResourceType, theParameterName, referenceResourceType);
				theErrorMessages.add(msg);
			}
		}
	}

	private IBaseResource resolveSourceResource(
			MergeOperationsCommonInputParameters theOperationParameters,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome,
			AbstractMergeOperationInputParameterNames theParameterNames) {
		return resolveResource(
				theOperationParameters.getSourceResource(),
				theOperationParameters.getSourceIdentifiers(),
				theRequestDetails,
				theOutcome,
				theParameterNames.getSourceResourceParameterName(),
				theParameterNames.getSourceIdentifiersParameterName());
	}

	protected IBaseResource resolveTargetResource(
			MergeOperationsCommonInputParameters theOperationParameters,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome,
			AbstractMergeOperationInputParameterNames theParameterNames) {
		return resolveResource(
				theOperationParameters.getTargetResource(),
				theOperationParameters.getTargetIdentifiers(),
				theRequestDetails,
				theOutcome,
				theParameterNames.getTargetResourceParameterName(),
				theParameterNames.getTargetIdentifiersParameterName());
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

		// Get the resource type from the request and dynamically fetch the appropriate DAO
		String resourceType = theRequestDetails.getResourceName();
		IFhirResourceDao<IBaseResource> resourceDao = myDaoRegistry.getResourceDao(resourceType);

		IBundleProvider bundle = resourceDao.search(searchParameterMap, theRequestDetails);
		List<IBaseResource> resources = bundle.getAllResources();
		if (resources.isEmpty()) {
			String msg = String.format(
					"No resources found matching the identifier(s) specified in '%s'", theOperationParameterName);
			addErrorToOperationOutcome(myFhirContext, theOutcome, msg, "not-found");
			return null;
		}
		if (resources.size() > 1) {
			String msg = String.format(
					"Multiple resources found matching the identifier(s) specified in '%s'", theOperationParameterName);
			addErrorToOperationOutcome(myFhirContext, theOutcome, msg, "multiple-matches");
			return null;
		}

		return resources.get(0);
	}

	private IBaseResource resolveResourceByReference(
			IBaseReference theReference,
			RequestDetails theRequestDetails,
			IBaseOperationOutcome theOutcome,
			String theOperationParameterName) {

		IIdType theResourceId = theReference.getReferenceElement();

		// Get the resource type from request details and dynamically fetch the appropriate DAO
		String resourceType = theRequestDetails.getResourceName();
		IFhirResourceDao<IBaseResource> resourceDao = myDaoRegistry.getResourceDao(resourceType);

		IBaseResource resource;
		try {
			resource = resourceDao.read(theResourceId.toVersionless(), theRequestDetails);
		} catch (ResourceNotFoundException e) {
			String msg = String.format(
					"Resource not found for the reference specified in '%s' parameter", theOperationParameterName);
			addErrorToOperationOutcome(myFhirContext, theOutcome, msg, "not-found");
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
			addErrorToOperationOutcome(myFhirContext, theOutcome, msg, "conflict");
			return null;
		}

		return resource;
	}
}
