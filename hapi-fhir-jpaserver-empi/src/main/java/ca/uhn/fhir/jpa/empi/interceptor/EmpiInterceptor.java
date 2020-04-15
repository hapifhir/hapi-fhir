package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.Constants;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmpiInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInterceptor.class);
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private FhirContext myFhirContext;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void blockManualPersonManipulationOnCreate(IBaseResource theBaseResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		if (isInternalRequest(theRequestDetails)) {
			return;
		}
		forbidIfEmpiManagedTagIsPresent(theBaseResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void blockManualPersonManipulationOnUpdate(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		if (isInternalRequest(theRequestDetails)) {
			return;
		}
		forbidIfEmpiManagedTagIsPresent(theOldResource);
		forbidModifyingEmpiTag(theNewResource, theOldResource);
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

	/**
	 * Checks for the presence of the EMPI-managed tag, indicating the EMPI system has ownership
	 * of this Person's links.
	 *
	 * @param theBaseResource the Person to check .
	 * @return a boolean indicating whether or not EMPI manages this Person.
	 */
	private boolean isEmpiManaged(IBaseResource theBaseResource) {
		return theBaseResource.getMeta().getTag(ca.uhn.fhir.empi.api.Constants.SYSTEM_EMPI_MANAGED, ca.uhn.fhir.empi.api.Constants.CODE_HAPI_EMPI_MANAGED) != null;
	}

	/*
	 * We assume that if we have RequestDetails, then this was an HTTP request and not an internal one.
	 */
	private boolean isInternalRequest(RequestDetails theRequestDetails) {
		return theRequestDetails == null;
	}


	private void forbidIfEmpiManagedTagIsPresent(IBaseResource theResource) {
		if (extractResourceType(theResource).equalsIgnoreCase("Person")) {
			if (theResource.getMeta().getTag(ca.uhn.fhir.empi.api.Constants.SYSTEM_EMPI_MANAGED, Constants.CODE_HAPI_EMPI_MANAGED) != null) {
				throwModificationBlockedByEmpi();
			}
		}
	}

	private void throwBlockEmpiStatusChange(){
		throw new ForbiddenOperationException("The EMPI status of a Person may not be changed once created.");
	}
	private void throwModificationBlockedByEmpi(){
		throw new ForbiddenOperationException("Cannot create or modify Persons who are managed by EMPI.");
	}

	private String extractResourceType(IBaseResource theResource) {
		return myFhirContext.getResourceDefinition(theResource).getName();
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllEmpiLinks(AtomicInteger theCounter) {
		ourLog.debug("Expunging all EmpiLink records");
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedEmpiLinks(AtomicInteger theCounter, IBaseResource theResource) {
		// FIXME EMPI
	}
}
