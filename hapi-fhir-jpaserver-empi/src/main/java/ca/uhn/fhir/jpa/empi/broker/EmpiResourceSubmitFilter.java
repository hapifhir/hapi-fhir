package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.apache.commons.collections4.SetUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static ca.uhn.fhir.rest.api.Constants.CODE_HAPI_EMPI_MANAGED;
import static ca.uhn.fhir.rest.api.Constants.SYSTEM_EMPI_MANAGED;

@Service
public class EmpiResourceSubmitFilter  {

	@Autowired
	FhirContext myFhirContext;

	static final Set<String> ourSupportedResourceTypes = SetUtils.hashSet("Patient", "Practitioner");

	// FIXME KHS move back to interceptor
	public boolean canSubmitResource(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails) {
		String resourcetype = extractResourceType(theNewResource);
		if (isInternalRequest(theRequestDetails)) {
			return false;
		}
		if (theOldResource == null) {
			forbidIfEmpiManagedTagIsPresent(resourcetype, theNewResource);
		} else {
			forbidIfEmpiManagedTagIsPresent(resourcetype, theOldResource);
			forbidModifyingEmpiTag(theNewResource, theOldResource);
		}
		if (!ourSupportedResourceTypes.contains(resourcetype)) {
			return false;
		}
		return true;
 	}

	/*
	 * Will throw a forbidden error if a request attempts to add/remove the EMPI tag on a Person.
	 */
	private void forbidModifyingEmpiTag(IBaseResource theNewResource, IBaseResource theOldResource) {
		if (extractResourceType(theNewResource).equalsIgnoreCase("Person")) {
			if (isEmpiManaged(theNewResource) != isEmpiManaged(theOldResource)) {
				throwBlockEmpiStatusChange();
			}
		}
	}

	private void throwBlockEmpiStatusChange(){
		throw new ForbiddenOperationException("The EMPI status of a Person may not be changed once created.");
	}

	/**
	 * Checks for the presence of the EMPI-managed tag, indicating the EMPI system has ownership
	 * of this Person's links.
	 *
	 * @param theBaseResource the Person to check .
	 * @return a boolean indicating whether or not EMPI manages this Person.
	 */
	private boolean isEmpiManaged(IBaseResource theBaseResource) {
		return theBaseResource.getMeta().getTag(SYSTEM_EMPI_MANAGED, CODE_HAPI_EMPI_MANAGED) != null;
	}

	private void forbidIfEmpiManagedTagIsPresent(String theResourceType, IBaseResource theNewResource) {
		if ("Person".equalsIgnoreCase(theResourceType)) {
			if (theNewResource.getMeta().getTag(SYSTEM_EMPI_MANAGED, CODE_HAPI_EMPI_MANAGED) != null) {
				throwModificationBlockedByEmpi();
			}
		}
	}

	private void throwModificationBlockedByEmpi(){
		throw new ForbiddenOperationException("Cannot create or modify Persons who are managed by EMPI.");
	}

	private String extractResourceType(IBaseResource theResource) {
		return myFhirContext.getResourceDefinition(theResource).getName();
	}

	/*
	 * We assume that if we have RequestDetails, then this was an HTTP request and not an internal one.
	 */
	private boolean isInternalRequest(RequestDetails theRequestDetails) {
		return theRequestDetails == null;
	}
}
