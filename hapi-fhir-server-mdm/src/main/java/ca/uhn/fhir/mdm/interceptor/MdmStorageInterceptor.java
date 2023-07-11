/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.dao.expunge.IExpungeEverythingService;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.svc.MdmLinkDeleteSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MdmStorageInterceptor implements IMdmStorageInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmStorageInterceptor.class);

	// Used to bypass trying to remove mdm links associated to a resource when running mdm-clear batch job, which
	// deletes all links beforehand, and impacts performance for no action
	private static final ThreadLocal<Boolean> ourLinksDeletedBeforehand = ThreadLocal.withInitial(() -> Boolean.FALSE);

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
	@Autowired
	private IIdHelperService myIdHelperSvc;
	@Autowired
	private IMdmLinkDao myMdmLinkDao;
	@Autowired
	private IMdmSubmitSvc myMdmSubmitSvc;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IMdmChannelSubmitterSvc myMdmChannelSubmitterSvc;
//	@Autowired
//	private MdmMatchLinkSvc
	@Autowired
	private IMdmLinkUpdaterSvc mdmLinkUpdaterSvc;
//	@Autowired
//	private MdmLinkFactory<IMdmLink<IResourcePersistentId>> k;

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
		}

		if (myMdmSettings.isPreventEidUpdates()) {
			forbidIfModifyingExternalEidOnTarget(theUpdatedResource, theOldResource);
		}
	}

	@Autowired
	private IMdmClearHelperSvc<? extends IResourcePersistentId<?>> myIMdmClearHelperSvc;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
	public void deleteMdmLinks(RequestDetails theRequest, IBaseResource theResource) {
		if (ourLinksDeletedBeforehand.get()) {
			return;
		}

		if (myMdmSettings.isSupportedMdmType(myFhirContext.getResourceType(theResource))) {
			System.out.println("JDJD " + myMdmSettings.isAutoExpungeGoldenResources());
			System.out.println("JDJD " + myMdmSettings.isPreventMultipleEids());
			System.out.println("JDJD " + theResource.getIdElement());
			System.out.println("JDJD " + myMdmSettings.getSearchAllPartitionForMatch());


			IIdType sourceId = theResource.getIdElement().toVersionless();
			IResourcePersistentId sourcePid = myIdHelperSvc.getPidOrThrowException(RequestPartitionId.allPartitions(), sourceId);
			List<? extends IMdmLink> allLinks = myMdmLinkDao.expandPidsAndMatchResultBySourcePid(sourcePid);

			// this invalid bc it only finds links the source is in. eg: 1 --> gr <-- 2 it will not find 2 --> gr
//			IMdmLink<IResourcePersistentId> exampleLink = k.newMdmLinkVersionless();
//			exampleLink.setGoldenResourcePersistenceId(sourcePid);
//			Example<IMdmLink<IResourcePersistentId>> example = Example.of(exampleLink);
//			List<IMdmLink> allLinks = myMdmLinkDao.findAll(example);

			Map<MdmMatchResultEnum, List<IMdmLink>> linksByMatchResult = allLinks.stream()
				.collect(Collectors.groupingBy(IMdmLink::getMatchResult));
			List<IMdmLink> matches = linksByMatchResult.containsKey(MdmMatchResultEnum.MATCH) ? linksByMatchResult.get(MdmMatchResultEnum.MATCH) : new ArrayList<>();
			List<IMdmLink> possibleMatches = linksByMatchResult.containsKey(MdmMatchResultEnum.POSSIBLE_MATCH) ? linksByMatchResult.get(MdmMatchResultEnum.POSSIBLE_MATCH) : new ArrayList<>();

			// case to handle: we are deleting a possible match source resource

			if (matches.size() == 1 && matches.get(0).getSourcePersistenceId().equals(sourcePid)) {
				// We are attempting to delete the only source resource left linked to the golden resource
				// In this case, we should automatically delete the golden resource to prevent orphaning
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResource);
				IResourcePersistentId grPid = matches.get(0).getGoldenResourcePersistenceId();
				IBaseResource gr = dao.readByPid(grPid); //todo jdjd do i need to call this in the loop?

				// Clean up possible matches, since they are no longer "real matches"
				for (IMdmLink possibleMatch : possibleMatches) {
					IBaseResource resource = dao.readByPid(possibleMatch.getSourcePersistenceId());

					mdmLinkUpdaterSvc.updateLink((IAnyResource) gr, (IAnyResource) resource, MdmMatchResultEnum.NO_MATCH,
						createMdmContext(MdmTransactionContext.OperationType.UPDATE_LINK, theResource.fhirType()));
				}

				int numDeleted;
				if (myMdmSettings.isAutoExpungeGoldenResources()) {
					System.out.println("JDJD exp");
					numDeleted = deleteLinkedGoldenResource(grPid);
				} else {
					System.out.println("JDJD del");
					setLinksDeletedBeforehand();
					DeleteMethodOutcome deleteOutcome = dao.deletePidList(theRequest.getCompleteUrl(), Collections.singleton(grPid), new DeleteConflictList(), theRequest, new TransactionDetails());
					numDeleted = deleteOutcome.getDeletedEntities().size();
					resetLinksDeletedBeforehand();
				}

				if (numDeleted > 0) {
					ourLog.info("Removed {} golden resource(s) with references to {}", numDeleted, sourceId);
				}
			} else if (possibleMatches.size() >= 1) {
				//todo jdjd do i need this part
				Set<IResourcePersistentId> gridset = possibleMatches.stream().map(IMdmLink::getGoldenResourcePersistenceId).collect(Collectors.toSet());
				for (IResourcePersistentId grid : gridset) {
					long count = allLinks.stream().filter(IMdmLink::isMatch).filter(t->t.getGoldenResourcePersistenceId().equals(grid)).count();
					if (count == 0) {
						 deleteLinkedGoldenResource(grid);
					}
				}
			}

			myMdmLinkDeleteSvc.deleteWithAnyReferenceTo(theResource);
		}
	}

	private MdmTransactionContext createMdmContext(MdmTransactionContext.OperationType theOperation, String theResourceType) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createNew();
		MdmTransactionContext retVal = new MdmTransactionContext(transactionLogMessages, theOperation);
		retVal.setResourceType(theResourceType);
		return retVal;
	}

	private int deleteLinkedGoldenResource(IResourcePersistentId theGoldenPid) {
		setLinksDeletedBeforehand();

		IDeleteExpungeSvc deleteExpungeSvc = myIMdmClearHelperSvc.getDeleteExpungeSvc();
		int numDeleted = deleteExpungeSvc.deleteExpunge(new ArrayList<>(Collections.singleton(theGoldenPid)),
			false,null);

		resetLinksDeletedBeforehand();
		return numDeleted;
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
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingMdmLinks());
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedMdmLinks(AtomicInteger theCounter, IBaseResource theResource) {
		ourLog.debug("Expunging MdmLink records with reference to {}", theResource.getIdElement());
		theCounter.addAndGet(myMdmLinkDeleteSvc.deleteWithAnyReferenceTo(theResource));
	}

	public static void setLinksDeletedBeforehand() {
		ourLinksDeletedBeforehand.set(Boolean.TRUE);
	}

	public static void resetLinksDeletedBeforehand() {
		ourLinksDeletedBeforehand.remove();
	}

}
