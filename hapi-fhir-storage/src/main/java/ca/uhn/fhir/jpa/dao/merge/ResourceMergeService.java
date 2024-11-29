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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.IdentifierUtil;
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

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_200_OK;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_422_UNPROCESSABLE_ENTITY;

public class ResourceMergeService {

	IFhirResourceDaoPatient<Patient> myDao;
	FhirContext myFhirContext;

	public ResourceMergeService(IFhirResourceDaoPatient<Patient> thePatientDao) {
		myDao = thePatientDao;
		myFhirContext = myDao.getContext();
	}

	/**
	 * Implemention of the $merge operation for resources
	 * @param theMergeOperationParameters	the merge operation parameters
	 * @param theRequestDetails	the request details
	 * @return the merge outcome containing OperationOutcome and HTTP status code
	 */
	public MergeOutcome merge(MergeOperationParameters theMergeOperationParameters, RequestDetails theRequestDetails) {

		MergeOutcome mergeOutcome = new MergeOutcome();
		IBaseOperationOutcome outcome = OperationOutcomeUtil.newInstance(myFhirContext);
		mergeOutcome.setOperationOutcome(outcome);
		// default to 200 OK, would be changed to another code during processing as required
		mergeOutcome.setHttpStatusCode(STATUS_HTTP_200_OK);

		if (!validateMergeOperationParameters(theMergeOperationParameters, outcome)) {
			mergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return mergeOutcome;
		}

		// cast to Patient, since we only support merging Patient resources for now
		Patient sourceResource =
				(Patient) resolveSourceResource(theMergeOperationParameters, theRequestDetails, outcome);

		if (sourceResource == null) {
			mergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return mergeOutcome;
		}

		// cast to Patient, since we only support merging Patient resources for now
		Patient targetResource =
				(Patient) resolveTargetResource(theMergeOperationParameters, theRequestDetails, outcome);

		if (targetResource == null) {
			mergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return mergeOutcome;
		}

		if (!validateSourceAndTargetAreMergable(sourceResource, targetResource, outcome)) {
			mergeOutcome.setHttpStatusCode(STATUS_HTTP_422_UNPROCESSABLE_ENTITY);
			return mergeOutcome;
		}

		// TODO Emre: do the actual ref updates

		// update resources after the refs update is completed
		// updateTargetResourceAfterRefsUpdated();
		updateSourceResourceAfterRefsUpdated(sourceResource, targetResource, theRequestDetails);

		addInfoToOperationOutcome(outcome, "Merge operation completed successfully.");
		return mergeOutcome;
	}

	private boolean validateSourceAndTargetAreMergable(
			Patient theSourceResource, Patient theTargetResource, IBaseOperationOutcome outcome) {
		// is this the right way to check if the resources are the same?
		if (theSourceResource.getId().equalsIgnoreCase(theTargetResource.getId())) {
			String msg = "Source and target resources are the same resource.";
			// What is the right code to use in these cases?
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		if (theTargetResource.hasActive() && !theTargetResource.getActive()) {
			String msg = "Target resource is not active, it must be active to be the target of a merge operation";
			addErrorToOperationOutcome(outcome, msg, "invalid");
			return false;
		}

		if (hasSourceResourceAlreadyReplacedByTargetOrAnotherResource(theSourceResource, theTargetResource, outcome)) {
			return false;
		}

		return true;
	}

	private boolean hasSourceResourceAlreadyReplacedByTargetOrAnotherResource(
			Patient theSourceResource, Patient theTargetResource, IBaseOperationOutcome outcome) {
		if (theSourceResource.hasLink()) {
			for (Patient.PatientLinkComponent link : theSourceResource.getLink()) {
				if (Patient.LinkType.REPLACEDBY.equals(link.getType())) {
					if (link.hasOther()) {
						String otherReference = link.getOther().getReference();
						if (otherReference != null) {
							if (otherReference.equalsIgnoreCase(theTargetResource.getId())) {
								String msg = "Source resource is already replaced by the target resource";
								addErrorToOperationOutcome(outcome, msg, "invalid");
								return true;
							} else {
								// is this an error case or should we just ignore it?
								String msg = "Source resource is already replaced by another resource";
								addErrorToOperationOutcome(outcome, msg, "invalid");
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void deleteSourceResourceAfterRefsUpdated(Patient theSourceResource, RequestDetails theRequestDetails) {
		// TODO: handle errors
		myDao.delete(theSourceResource.getIdElement(), theRequestDetails);
	}

	private void updateSourceResourceAfterRefsUpdated(
			Patient theSourceResource, Patient theTargetResource, RequestDetails theRequestDetails) {
		theSourceResource.setActive(false);
		theSourceResource
				.addLink()
				.setType(Patient.LinkType.REPLACEDBY)
				.setOther(new Reference(theTargetResource.getId()));
		myDao.update(theSourceResource, theRequestDetails);
	}

	/**
	 * Validates the merge operation parameters and adds validation errors to the outcome
	 * @param theMergeOperationParameters the merge operation parameters
	 * @param theOutcome the outcome to add validation errors to
	 * @return true if the parameters are valid, false otherwise
	 */
	private boolean validateMergeOperationParameters(
			MergeOperationParameters theMergeOperationParameters, IBaseOperationOutcome theOutcome) {
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
			MergeOperationParameters theOperationParameters,
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
			MergeOperationParameters theOperationParameters,
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

		if (r4ref.hasReferenceElement()) {

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

		// reference may have a identifier
		if (r4ref.hasIdentifier()) {
			Identifier identifier = r4ref.getIdentifier();
			CanonicalIdentifier canonicalIdentifier = IdentifierUtil.identifierDtFromIdentifier(identifier);
			return resolveResourceByIdentifiers(
					List.of(canonicalIdentifier), theRequestDetails, theOutcome, theOperationParameterName);
		}
		return null;
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

	private void addInfoToOperationOutcome(IBaseOperationOutcome theOutcome, String theMsg) {
		OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "information", theMsg, null, null);
	}

	private void addErrorToOperationOutcome(IBaseOperationOutcome theOutcome, String theMsg, String theCode) {
		OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "error", theMsg, null, theCode);
	}

	public static class MergeOutcome {
		private IBaseOperationOutcome myOperationOutcome;
		private int myHttpStatusCode;

		public IBaseOperationOutcome getOperationOutcome() {
			return myOperationOutcome;
		}

		public void setOperationOutcome(IBaseOperationOutcome theOperationOutcome) {
			this.myOperationOutcome = theOperationOutcome;
		}

		public int getHttpStatusCode() {
			return myHttpStatusCode;
		}

		public void setHttpStatusCode(int theHttpStatusCode) {
			this.myHttpStatusCode = theHttpStatusCode;
		}
	}
}
