package ca.uhn.fhir.jpa.mdm.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.expunge.IExpungeEverythingService;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDeleteSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MdmStorageInterceptor implements IMdmStorageInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmStorageInterceptor.class);

	@Autowired
	private IExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private MdmLinkDeleteSvc myMdmLinkDeleteSvc;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private IMdmSettings myMdmSettings;


	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void blockManualResourceManipulationOnCreate(IBaseResource theBaseResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		ourLog.debug("Starting pre-storage resource created hook for {}, {}, {}", theBaseResource, theRequestDetails, theServletRequestDetails);
		if (theBaseResource == null) {
			ourLog.warn("Attempting to block golden resource manipulation on a null resource");
			return;
		}

		//If running in single EID mode, forbid multiple eids.
		if (myMdmSettings.isPreventMultipleEids()) {
			ourLog.debug("Forbidding multiple EIDs on ", theBaseResource);
			forbidIfHasMultipleEids(theBaseResource);
		}

		// TODO GGG MDM find a better way to identify internal calls?
		if (isInternalRequest(theRequestDetails)) {
			ourLog.debug("Internal request - completed processing");
			return;
		}

		forbidIfMdmManagedTagIsPresent(theBaseResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void blockManualGoldenResourceManipulationOnUpdate(IBaseResource theOldResource, IBaseResource theUpdatedResource, RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
		ourLog.debug("Starting pre-storage resource updated hook for {}, {}, {}, {}", theOldResource, theUpdatedResource, theRequestDetails, theServletRequestDetails);

		if (theUpdatedResource == null) {
			ourLog.warn("Attempting to block golden resource manipulation on a null resource");
			return;
		}

		//If running in single EID mode, forbid multiple eids.
		if (myMdmSettings.isPreventMultipleEids()) {
			ourLog.debug("Forbidding multiple EIDs on ", theUpdatedResource);
			forbidIfHasMultipleEids(theUpdatedResource);
		}

		if (MdmResourceUtil.isGoldenRecordRedirected(theUpdatedResource)) {
			ourLog.debug("Deleting MDM links to deactivated Golden resource {}", theUpdatedResource.getIdElement().toUnqualifiedVersionless());
			int deleted = myMdmLinkDeleteSvc.deleteNonRedirectWithAnyReferenceTo(theUpdatedResource);
			if (deleted > 0) {
				ourLog.debug("Deleted {} MDM links", deleted);
			}
		}

		if (isInternalRequest(theRequestDetails)) {
			ourLog.debug("Internal request - completed processing");
			return;
		}

		if (theOldResource != null) {
			forbidIfMdmManagedTagIsPresent(theOldResource);
			forbidModifyingMdmTag(theUpdatedResource, theOldResource);
		} else {
			ourLog.warn("Null theOldResource for {} {}", theUpdatedResource == null ? "null updated resource" : theUpdatedResource.getIdElement(), theRequestDetails);
		}

		if (myMdmSettings.isPreventEidUpdates()) {
			forbidIfModifyingExternalEidOnTarget(theUpdatedResource, theOldResource);
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
	public void deleteMdmLinks(RequestDetails theRequest, IBaseResource theResource) {
		if (!myMdmSettings.isSupportedMdmType(myFhirContext.getResourceType(theResource))) {
			return;
		}
		myMdmLinkDeleteSvc.deleteWithAnyReferenceTo(theResource);
	}

	private void forbidIfModifyingExternalEidOnTarget(IBaseResource theNewResource, IBaseResource theOldResource) {
		List<CanonicalEID> newExternalEids = Collections.emptyList();
		List<CanonicalEID> oldExternalEids = Collections.emptyList();
		if (theNewResource != null) {
			newExternalEids = myEIDHelper.getExternalEid(theNewResource);
		}
		if (theOldResource != null) {
			oldExternalEids = myEIDHelper.getExternalEid(theOldResource);
		}

		if (oldExternalEids.isEmpty()) {
			return;
		}

		if (!myEIDHelper.eidMatchExists(newExternalEids, oldExternalEids)) {
			throwBlockEidChange();
		}
	}

	private void throwBlockEidChange() {
		throw new ForbiddenOperationException(Msg.code(763) + "While running with EID updates disabled, EIDs may not be updated on source resources");
	}

	/*
	 * Will throw a forbidden error if a request attempts to add/remove the MDM tag on a Resource.
	 */
	private void forbidModifyingMdmTag(IBaseResource theNewResource, IBaseResource theOldResource) {
		if (MdmResourceUtil.isMdmManaged(theNewResource) != MdmResourceUtil.isMdmManaged(theOldResource)) {
			throwBlockMdmManagedTagChange();
		}
	}

	private void forbidIfHasMultipleEids(IBaseResource theResource) {
		String resourceType = extractResourceType(theResource);
		if (myMdmSettings.isSupportedMdmType(resourceType)) {
			if (myEIDHelper.getExternalEid(theResource).size() > 1) {
				throwBlockMultipleEids();
			}
		}
	}

	/*
	 * We assume that if we have RequestDetails, then this was an HTTP request and not an internal one.
	 */
	private boolean isInternalRequest(RequestDetails theRequestDetails) {
		return theRequestDetails == null || theRequestDetails instanceof SystemRequestDetails;
	}

	private void forbidIfMdmManagedTagIsPresent(IBaseResource theResource) {
		if (theResource == null) {
			ourLog.warn("Attempting to forbid MDM on a null resource");
			return;
		}

		if (MdmResourceUtil.isMdmManaged(theResource)) {
			throwModificationBlockedByMdm();
		}
		if (MdmResourceUtil.hasGoldenRecordSystemTag(theResource)) {
			throwModificationBlockedByMdm();
		}
	}

	private void throwBlockMdmManagedTagChange() {
		throw new ForbiddenOperationException(Msg.code(764) + "The " + MdmConstants.CODE_HAPI_MDM_MANAGED + " tag on a resource may not be changed once created.");
	}

	private void throwModificationBlockedByMdm() {
		throw new ForbiddenOperationException(Msg.code(765) + "Cannot create or modify Resources that are managed by MDM. This resource contains a tag with one of these systems: " + MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS + " or " + MdmConstants.SYSTEM_MDM_MANAGED);
	}

	private void throwBlockMultipleEids() {
		throw new ForbiddenOperationException(Msg.code(766) + "While running with multiple EIDs disabled, source resources may have at most one EID.");
	}

	private String extractResourceType(IBaseResource theResource) {
		return myFhirContext.getResourceType(theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllMdmLinks(AtomicInteger theCounter) {
		ourLog.debug("Expunging all MdmLink records");
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(MdmLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedMdmLinks(AtomicInteger theCounter, IBaseResource theResource) {
		ourLog.debug("Expunging MdmLink records with reference to {}", theResource.getIdElement());
		theCounter.addAndGet(myMdmLinkDeleteSvc.deleteWithAnyReferenceTo(theResource));
	}
}
