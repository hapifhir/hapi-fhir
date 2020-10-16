package ca.uhn.fhir.jpa.empi.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.empi.EmpiLinkDeleteSvc;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmpiStorageInterceptor implements IEmpiStorageInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiStorageInterceptor.class);
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private EmpiLinkDeleteSvc myEmpiLinkDeleteSvc;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private IEmpiSettings myEmpiSettings;
	@Autowired
	private PersonHelper myPersonHelper;


	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void blockManualPersonManipulationOnCreate(IBaseResource theBaseResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {

		//If running in single EID mode, forbid multiple eids.
		if (myEmpiSettings.isPreventMultipleEids()) {
			forbidIfHasMultipleEids(theBaseResource);
		}

		// TODO EMPI find a better way to identify EMPI calls
		if (isInternalRequest(theRequestDetails)) {
			return;
		}

		forbidIfEmpiManagedTagIsPresent(theBaseResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void blockManualPersonManipulationOnUpdate(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {

		//If running in single EID mode, forbid multiple eids.
		if (myEmpiSettings.isPreventMultipleEids()) {
			forbidIfHasMultipleEids(theNewResource);
		}

		if (EmpiUtil.isEmpiManagedPerson(myFhirContext, theNewResource) &&
			myPersonHelper.isDeactivated(theNewResource)) {
			ourLog.debug("Deleting empi links to deactivated Person {}", theNewResource.getIdElement().toUnqualifiedVersionless());
			int deleted = myEmpiLinkDeleteSvc.deleteNonRedirectWithWithAnyReferenceTo(theNewResource);
			if (deleted > 0) {
				ourLog.debug("Deleted {} empi links", deleted);
			}
		}

		if (isInternalRequest(theRequestDetails)) {
			return;
		}
		forbidIfEmpiManagedTagIsPresent(theOldResource);
		forbidModifyingEmpiTag(theNewResource, theOldResource);

		if (myEmpiSettings.isPreventEidUpdates()) {
			forbidIfModifyingExternalEidOnTarget(theNewResource, theOldResource);
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
	public void deleteEmpiLinks(RequestDetails theRequest, IBaseResource theResource) {
		if (!EmpiUtil.isEmpiResourceType(myFhirContext, theResource)) {
			return;
		}
		myEmpiLinkDeleteSvc.deleteWithAnyReferenceTo(theResource);
	}

	private void forbidIfModifyingExternalEidOnTarget(IBaseResource theNewResource, IBaseResource theOldResource) {
		List<CanonicalEID> newExternalEids = myEIDHelper.getExternalEid(theNewResource);
		List<CanonicalEID> oldExternalEids = myEIDHelper.getExternalEid(theOldResource);
		if (oldExternalEids.isEmpty()) {
			return;
		}

		if (!myEIDHelper.eidMatchExists(newExternalEids, oldExternalEids)) {
			throwBlockEidChange();
		}
	}

	private void throwBlockEidChange() {
		throw new ForbiddenOperationException("While running with EID updates disabled, EIDs may not be updated on Patient/Practitioner resources");
	}

	/*
	 * Will throw a forbidden error if a request attempts to add/remove the EMPI tag on a Person.
	 */
	private void forbidModifyingEmpiTag(IBaseResource theNewResource, IBaseResource theOldResource) {
		if (EmpiUtil.isEmpiManaged(theNewResource) != EmpiUtil.isEmpiManaged(theOldResource)) {
			throwBlockEmpiManagedTagChange();
		}
	}

	private void forbidIfHasMultipleEids(IBaseResource theResource) {
		String resourceType = extractResourceType(theResource);
		if (resourceType.equalsIgnoreCase("Patient") || resourceType.equalsIgnoreCase("Practitioner")) {
			if (myEIDHelper.getExternalEid(theResource).size() > 1) {
				throwBlockMultipleEids();
			}
		}
	}

	/*
	 * We assume that if we have RequestDetails, then this was an HTTP request and not an internal one.
	 */
	private boolean isInternalRequest(RequestDetails theRequestDetails) {
		return theRequestDetails == null;
	}

	private void forbidIfEmpiManagedTagIsPresent(IBaseResource theResource) {
		if (EmpiUtil.isEmpiManaged(theResource)) {
			throwModificationBlockedByEmpi();
		}
	}

	private void throwBlockEmpiManagedTagChange() {
		throw new ForbiddenOperationException("The " + EmpiConstants.CODE_HAPI_EMPI_MANAGED + " tag on a resource may not be changed once created.");
	}

	private void throwModificationBlockedByEmpi() {
		throw new ForbiddenOperationException("Cannot create or modify Resources that are managed by EMPI.");
	}

	private void throwBlockMultipleEids() {
		throw new ForbiddenOperationException("While running with multiple EIDs disabled, Patient/Practitioner resources may have at most one EID.");
	}

	private String extractResourceType(IBaseResource theResource) {
		return myFhirContext.getResourceType(theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllEmpiLinks(AtomicInteger theCounter) {
		ourLog.debug("Expunging all EmpiLink records");
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedEmpiLinks(AtomicInteger theCounter, IBaseResource theResource) {
		ourLog.debug("Expunging EmpiLink records with reference to {}", theResource.getIdElement());
		theCounter.addAndGet(myEmpiLinkDeleteSvc.deleteWithAnyReferenceTo(theResource));
	}
}
