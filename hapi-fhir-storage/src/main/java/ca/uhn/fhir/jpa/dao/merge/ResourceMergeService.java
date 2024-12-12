/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.IReplaceReferencesSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.replacereferences.ReplaceReferenceRequest;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;

public class ResourceMergeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceMergeService.class);

	private final IFhirResourceDaoPatient<Patient> myDao;
	private final IReplaceReferencesSvc myReplaceReferencesSvc;
	private final IHapiTransactionService myHapiTransactionService;
	private final FhirContext myFhirContext;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public ResourceMergeService(
			IFhirResourceDaoPatient<Patient> thePatientDao,
			IReplaceReferencesSvc theReplaceReferencesSvc,
			IHapiTransactionService theHapiTransactionService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myDao = thePatientDao;
		myReplaceReferencesSvc = theReplaceReferencesSvc;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myFhirContext = myDao.getContext();
		myHapiTransactionService = theHapiTransactionService;
	}

	/**
	 * Implementation of the $merge operation for resources
	 *
	 * @param theMergeOperationParameters the merge operation parameters
	 * @param theRequestDetails           the request details
	 * @return the merge outcome containing OperationOutcome and HTTP status code
	 */
	public MergeOperationOutcome merge(
			MergeOperationInputParameters theMergeOperationParameters, RequestDetails theRequestDetails) {

		// FIXME ED update to work like ReplaceReferencesSvcImpl.replaceReferences()

		if (theRequestDetails.isPreferAsync()) {
			return mergePreferAsync(theMergeOperationParameters, theRequestDetails);
		} else {
			return mergePreferSync(theMergeOperationParameters, theRequestDetails);
		}
	}

	private MergeOperationOutcome mergePreferAsync(MergeOperationInputParameters theMergeOperationParameters, RequestDetails theRequestDetails) {
// FIXME ED lots of copy/paste from replaceReferencesPreferAsync, but maybe some shared code in a Util class?
		return null;
	}

	private MergeOperationOutcome mergePreferSync(MergeOperationInputParameters theMergeOperationParameters, RequestDetails theRequestDetails) {

	// in replaceReferencesPreferSync, still need to fallback to async if count exceeds batchSize,
		// but don't need to stream resources, can just use count method for that.

		MergeOperationOutcome mergeOutcome = new MergeOperationOutcome();
		IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.newInstance(myFhirContext);
		mergeOutcome.setOperationOutcome(operationOutcome);
		// default to 200 OK, would be changed to another code during processing as required
		mergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);

		try {
			// FIXME ED call count method.
			if (count > theMergeOperationParameters.getBatchSize()) {
				ourLog.warn("Too many results. Switching to asynchronous merge.");
				return mergePreferAsync(theMergeOperationParameters, theRequestDetails);
			}

			validateAndMerge(theMergeOperationParameters, theRequestDetails, mergeOutcome);
		} catch (Exception e) {
			ourLog.error("Resource merge failed", e);
			if (e instanceof BaseServerResponseException) {
				mergeOutcome.setHttpStatusCode(((BaseServerResponseException) e).getStatusCode());
			} else {
				mergeOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);
			}
			OperationOutcomeUtil.addIssue(myFhirContext, operationOutcome, "error", e.getMessage(), null, "exception");
		}
		return mergeOutcome;
	}

	private void validateAndMerge(
			MergeOperationInputParameters theMergeOperationParameters,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		IBaseOperationOutcome operationOutcome = theMergeOutcome.getOperationOutcome();

		if (!validateMergeOperationParameters(theMergeOperationParameters, operationOutcome)) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return;
		}

		// cast to Patient, since we only support merging Patient resources for now
		Patient sourceResource =
				(Patient) resolveSourceResource(theMergeOperationParameters, theRequestDetails, operationOutcome);

		if (sourceResource == null) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return;
		}

		// cast to Patient, since we only support merging Patient resources for now
		Patient targetResource =
				(Patient) resolveTargetResource(theMergeOperationParameters, theRequestDetails, operationOutcome);

		if (targetResource == null) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return;
		}

		if (!validateSourceAndTargetAreSuitableForMerge(sourceResource, targetResource, operationOutcome)) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return;
		}

		if (!validateResultResourceIfExists(
				theMergeOperationParameters, targetResource, sourceResource, operationOutcome)) {
			theMergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return;
		}

		if (theMergeOperationParameters.getPreview()) {
			Integer referencingResourceCount = myReplaceReferencesSvc.countResourcesReferencingResource(
					sourceResource.getIdElement(), theRequestDetails);

			// in preview mode, we should also return how the target would look like
			Patient theResultResource = (Patient) theMergeOperationParameters.getResultResource();
			Patient targetPatientAsIfUpdated = prepareTargetPatientForUpdate(
					targetResource, sourceResource, theResultResource, theMergeOperationParameters.getDeleteSource());
			theMergeOutcome.setUpdatedTargetResource(targetPatientAsIfUpdated);

			// adding +2 because the source and the target resources themselved would be updated as well
			String diagnosticsMsg = String.format("Merge would update %d resources", referencingResourceCount + 2);
			String detailsText = "Preview only merge operation - no issues detected";
			addInfoToOperationOutcome(operationOutcome, diagnosticsMsg, detailsText);
			return;
		}

		mergeInTransaction(
				theMergeOperationParameters, sourceResource, targetResource, theRequestDetails, theMergeOutcome);

		String detailsText = "Merge operation completed successfully.";
		addInfoToOperationOutcome(operationOutcome, null, detailsText);
	}

	private void mergeInTransaction(
			MergeOperationInputParameters theMergeOperationParameters,
			Patient theSourceResource,
			Patient theTargetResource,
			RequestDetails theRequestDetails,
			MergeOperationOutcome theMergeOutcome) {

		// TODO: cannot do this in transaction yet, because systemDAO.transaction called by replaceReferences complains
		// that  there is  an active transaction already.
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(theTargetResource.getIdElement()));

		ReplaceReferenceRequest replaceReferenceRequest = new ReplaceReferenceRequest(
				theSourceResource.getIdElement(),
				theTargetResource.getIdElement(),
				theMergeOperationParameters.getBatchSize(),
				partitionId);

		// FIXME ED if we're in the synchronous case, force sync
		replaceReferenceRequest.forceSyncMode();

		// FIXME ED this will need to change because this calls JOB_REPLACE_REFERENCES when you want to call
		// JOB_MERGEmdm
		Parameters replaceRefsOutParams =
				(Parameters) myReplaceReferencesSvc.replaceReferences(replaceReferenceRequest, theRequestDetails);

		Parameters.ParametersParameterComponent taskOutParam =
				replaceRefsOutParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK);
		if (taskOutParam != null) {
			theMergeOutcome.setTask(taskOutParam.getResource());
		}

		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			Patient theResultResource = (Patient) theMergeOperationParameters.getResultResource();
			Patient patientToUpdate = prepareTargetPatientForUpdate(
					theTargetResource,
					theSourceResource,
					theResultResource,
					theMergeOperationParameters.getDeleteSource());
			// update the target patient resource after the references are updated
			Patient targetPatientAfterUpdate = updateResource(patientToUpdate, theRequestDetails);
			theMergeOutcome.setUpdatedTargetResource(targetPatientAfterUpdate);

			if (theMergeOperationParameters.getDeleteSource()) {
				deleteResource(theSourceResource, theRequestDetails);
			} else {
				prepareSourceResourceForUpdate(theSourceResource, theTargetResource);
				updateResource(theSourceResource, theRequestDetails);
			}
		});
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

		boolean isValid = true;

		Patient theResultResource = (Patient) theMergeOperationParameters.getResultResource();

		// validate the result resource's  id as same as the target resource
		if (!theResolvedTargetResource.getIdElement().toVersionless().equals(theResultResource.getIdElement())) {
			String msg = String.format(
					"'%s' must have the same versionless id as the actual resolved target resource. "
							+ "The actual resolved target resource's id is: '%s'",
					theMergeOperationParameters.getResultResourceParameterName(),
					theResolvedTargetResource.getIdElement().toVersionless().getValue());
			addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
			isValid = false;
		}

		// validate the result resource contains the identifiers provided in the target identifiers param
		if (theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& !hasAllIdentifiers(theResultResource, theMergeOperationParameters.getTargetIdentifiers())) {
			String msg = String.format(
					"'%s' must have all the identifiers provided in %s",
					theMergeOperationParameters.getResultResourceParameterName(),
					theMergeOperationParameters.getTargetIdentifiersParameterName());
			addErrorToOperationOutcome(theOperationOutcome, msg, "invalid");
			isValid = false;
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
			isValid = false;
		}

		return isValid;
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

	private List<Reference> getLinksToResource(
			Patient theResource, Patient.LinkType theLinkType, IIdType theResourceId) {
		List<Reference> links = getLinksOfType(theResource, theLinkType);
		return links.stream()
				.filter(r -> theResourceId.toVersionless().getValue().equals(r.getReference()))
				.collect(Collectors.toList());
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

	protected List<Reference> getLinksOfType(Patient theResource, Patient.LinkType theLinkType) {
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

		List<Reference> replacedByLinksInTarget = getLinksOfType(theTargetResource, Patient.LinkType.REPLACEDBY);
		if (!replacedByLinksInTarget.isEmpty()) {
			String ref = replacedByLinksInTarget.get(0).getReference();
			String msg = String.format(
					"Target resource was previously replaced by a resource with reference '%s', it "
							+ "is not a suitable target for merging.",
					ref);
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		List<Reference> replacedByLinksInSource = getLinksOfType(theSourceResource, Patient.LinkType.REPLACEDBY);
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

	private void prepareSourceResourceForUpdate(Patient theSourceResource, Patient theTargetResource) {
		theSourceResource.setActive(false);
		theSourceResource
				.addLink()
				.setType(Patient.LinkType.REPLACEDBY)
				.setOther(new Reference(theTargetResource.getIdElement().toVersionless()));
	}

	private Patient prepareTargetPatientForUpdate(
			Patient theTargetResource,
			Patient theSourceResource,
			@Nullable Patient theResultResource,
			boolean theDeleteSource) {

		// if the client provided a result resource as input then use it to update the target resource
		if (theResultResource != null) {
			return theResultResource;
		}

		// client did not provide a result resource, we should update the target resource,
		// add the replaces link to the target resource, if the source resource is not to be deleted
		if (!theDeleteSource) {
			theTargetResource
					.addLink()
					.setType(Patient.LinkType.REPLACES)
					.setOther(new Reference(theSourceResource.getIdElement().toVersionless()));
		}

		// copy all identifiers from the source to the target
		copyIdentifiersAndMarkOld(theSourceResource, theTargetResource);

		return theTargetResource;
	}

	/**
	 * Checks if theIdentifiers contains theIdentifier using equalsDeep
	 *
	 * @param theIdentifiers the list of identifiers
	 * @param theIdentifier  the identifier to check
	 * @return true if theIdentifiers contains theIdentifier, false otherwise
	 */
	private boolean containsIdentifier(List<Identifier> theIdentifiers, Identifier theIdentifier) {
		for (Identifier identifier : theIdentifiers) {
			if (identifier.equalsDeep(theIdentifier)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Copies each identifier from theSourceResource to theTargetResource, after checking that theTargetResource does
	 * not already contain the source identifier. Marks the copied identifiers marked as old.
	 *
	 * @param theSourceResource the source resource to copy identifiers from
	 * @param theTargetResource the target resource to copy identifiers to
	 */
	private void copyIdentifiersAndMarkOld(Patient theSourceResource, Patient theTargetResource) {
		if (theSourceResource.hasIdentifier()) {
			List<Identifier> sourceIdentifiers = theSourceResource.getIdentifier();
			List<Identifier> targetIdentifiers = theTargetResource.getIdentifier();
			for (Identifier sourceIdentifier : sourceIdentifiers) {
				if (!containsIdentifier(targetIdentifiers, sourceIdentifier)) {
					Identifier copyOfSrcIdentifier = sourceIdentifier.copy();
					copyOfSrcIdentifier.setUse(Identifier.IdentifierUse.OLD);
					theTargetResource.addIdentifier(copyOfSrcIdentifier);
				}
			}
		}
	}

	private Patient updateResource(Patient theResource, RequestDetails theRequestDetails) {
		DaoMethodOutcome outcome = myDao.update(theResource, theRequestDetails);
		return (Patient) outcome.getResource();
	}

	private void deleteResource(Patient theResource, RequestDetails theRequestDetails) {
		myDao.delete(theResource.getIdElement(), theRequestDetails);
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

		IBundleProvider bundle = myDao.search(searchParameterMap, theRequestDetails);
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
			resource = myDao.read(theResourceId.toVersionless(), theRequestDetails);
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

	private void addInfoToOperationOutcome(
			IBaseOperationOutcome theOutcome, String theDiagnosticMsg, String theDetailsText) {
		IBase issue =
				OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "information", theDiagnosticMsg, null, null);
		OperationOutcomeUtil.addDetailsToIssue(myFhirContext, issue, null, null, theDetailsText);
	}

	private void addErrorToOperationOutcome(IBaseOperationOutcome theOutcome, String theDiagnosticMsg, String theCode) {
		OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "error", theDiagnosticMsg, null, theCode);
	}
}
