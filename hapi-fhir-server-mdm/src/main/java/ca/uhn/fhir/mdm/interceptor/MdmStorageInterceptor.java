/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.dao.expunge.IExpungeEverythingService;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;

@SuppressWarnings("rawtypes")
@Service
public class MdmStorageInterceptor implements IMdmStorageInterceptor {

	private static final String GOLDEN_RESOURCES_TO_DELETE = "GR_TO_DELETE";

	private static final Logger ourLog = LoggerFactory.getLogger(MdmStorageInterceptor.class);

	// Used to bypass trying to remove mdm links associated to a resource when running mdm-clear batch job, which
	// deletes all links beforehand, and impacts performance for no action
	private static final ThreadLocal<Boolean> ourLinksDeletedBeforehand = ThreadLocal.withInitial(() -> Boolean.FALSE);

	@Autowired
	private IMdmClearHelperSvc<? extends IResourcePersistentId<?>> myIMdmClearHelperSvc;

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
	private IMdmLinkUpdaterSvc mdmLinkUpdaterSvc;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void blockManualResourceManipulationOnCreate(
			IBaseResource theBaseResource,
			RequestDetails theRequestDetails,
			ServletRequestDetails theServletRequestDetails) {
		ourLog.debug(
				"Starting pre-storage resource created hook for {}, {}, {}",
				theBaseResource,
				theRequestDetails,
				theServletRequestDetails);
		if (theBaseResource == null) {
			ourLog.warn("Attempting to block golden resource manipulation on a null resource");
			return;
		}

		// If running in single EID mode, forbid multiple eids.
		if (myMdmSettings.isPreventMultipleEids()) {
			ourLog.debug("Forbidding multiple EIDs on {}", theBaseResource);
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
	public void blockManualGoldenResourceManipulationOnUpdate(
			IBaseResource theOldResource,
			IBaseResource theUpdatedResource,
			RequestDetails theRequestDetails,
			ServletRequestDetails theServletRequestDetails) {
		ourLog.debug(
				"Starting pre-storage resource updated hook for {}, {}, {}, {}",
				theOldResource,
				theUpdatedResource,
				theRequestDetails,
				theServletRequestDetails);

		if (theUpdatedResource == null) {
			ourLog.warn("Attempting to block golden resource manipulation on a null resource");
			return;
		}

		// If running in single EID mode, forbid multiple eids.
		if (myMdmSettings.isPreventMultipleEids()) {
			ourLog.debug("Forbidding multiple EIDs on {}", theUpdatedResource);
			forbidIfHasMultipleEids(theUpdatedResource);
		}

		if (MdmResourceUtil.isGoldenRecordRedirected(theUpdatedResource)) {
			ourLog.debug(
					"Deleting MDM links to deactivated Golden resource {}",
					theUpdatedResource.getIdElement().toUnqualifiedVersionless());
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

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void deletePostCommit(
			RequestDetails theRequest, IBaseResource theResource, TransactionDetails theTransactionDetails) {
		if (myMdmSettings.isSupportedMdmType(myFhirContext.getResourceType(theResource))) {
			Map<IResourcePersistentId, Set<IResourcePersistentId>> goldenIdToSourceIdsMap =
					theTransactionDetails.getUserData(GOLDEN_RESOURCES_TO_DELETE);
			if (goldenIdToSourceIdsMap != null) {
				IResourcePersistentId sourcePid =
						myIdHelperSvc.getPidOrNull(RequestPartitionId.allPartitions(), theResource);
				if (sourcePid != null) {
					for (IResourcePersistentId goldenPid : goldenIdToSourceIdsMap.keySet()) {
						if (goldenIdToSourceIdsMap.get(goldenPid).contains(sourcePid)) {
							// we only delete the golden resource if it's matched to a source id;
							// there could be multiple of these, so we only delete the first
							if (!theTransactionDetails.getDeletedResourceIds().contains(goldenPid)) {
								IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResource);
								deleteGoldenResource(goldenPid, dao, theRequest);
								/*
								 * We will add the removed id to the deleted list so that
								 * the deletedResourceId list is accurate for what has been
								 * deleted.
								 *
								 * This benefits other interceptor writers who might want
								 * to do their own resource deletion on this same pre-commit
								 * hook (and wouldn't be aware if we did this deletion already).
								 */
								theTransactionDetails.addDeletedResourceId(goldenPid);
								// remove the golden resource id so it won't be 're-deleted'
								// if a second id related to this golden id (linked in some way)
								// is processed
								goldenIdToSourceIdsMap.remove(goldenPid);
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
	public void deleteMdmLinks(
			RequestDetails theRequest, IBaseResource theResource, TransactionDetails theTransactionDetails) {
		if (ourLinksDeletedBeforehand.get()) {
			return;
		}

		if (myMdmSettings.isSupportedMdmType(myFhirContext.getResourceType(theResource))) {
			IIdType sourceId = theResource.getIdElement().toVersionless();
			IResourcePersistentId sourcePid =
					myIdHelperSvc.getPidOrThrowException(RequestPartitionId.allPartitions(), sourceId);
			List<IMdmLink> allLinks =
					myMdmLinkDao.findLinksAssociatedWithGoldenResourceOfSourceResourceExcludingNoMatch(sourcePid);

			Map<MdmMatchResultEnum, List<IMdmLink>> linksByMatchResult =
					allLinks.stream().collect(Collectors.groupingBy(IMdmLink::getMatchResult));
			List<IMdmLink> matches =
					linksByMatchResult.containsKey(MATCH) ? linksByMatchResult.get(MATCH) : new ArrayList<>();
			List<IMdmLink> possibleMatches = linksByMatchResult.containsKey(POSSIBLE_MATCH)
					? linksByMatchResult.get(POSSIBLE_MATCH)
					: new ArrayList<>();

			if (isDeletingLastMatchedSourceResource(sourcePid, matches)) {
				/*
				 * We are attempting to delete the only source resource left linked to the golden resource.
				 * In this case, we'll clean up remaining links and mark the orphaned
				 * golden resource for deletion, which we'll do in STORAGE_PRECOMMIT_RESOURCE_DELETED
				 */
				IResourcePersistentId goldenPid = extractGoldenPid(theResource, matches.get(0));
				if (!theTransactionDetails.getDeletedResourceIds().contains(goldenPid)) {
					IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResource);
					cleanUpPossibleMatches(possibleMatches, dao, goldenPid, theRequest);
					IAnyResource goldenResource = (IAnyResource) dao.readByPid(goldenPid);
					myMdmLinkDeleteSvc.deleteWithAnyReferenceTo(goldenResource);
					/*
					 * Mark the golden resource for deletion.
					 * We won't do it yet, because there might be additional deletes coming
					 * that include this exact golden resource
					 * (eg, if delete is done by a filter and multiple delete is enabled)
					 */
					Map<IResourcePersistentId, Set<IResourcePersistentId>> goldenIdsToDelete2linkedSrcIds =
							theTransactionDetails.getUserData(GOLDEN_RESOURCES_TO_DELETE);
					if (goldenIdsToDelete2linkedSrcIds == null) {
						goldenIdsToDelete2linkedSrcIds = new ConcurrentHashMap<>();
					}
					if (!goldenIdsToDelete2linkedSrcIds.containsKey(goldenPid)) {
						goldenIdsToDelete2linkedSrcIds.put(goldenPid, new HashSet<>());
					}
					goldenIdsToDelete2linkedSrcIds.get(goldenPid).add(sourcePid);
					theTransactionDetails.putUserData(GOLDEN_RESOURCES_TO_DELETE, goldenIdsToDelete2linkedSrcIds);
				}
			}
			myMdmLinkDeleteSvc.deleteWithAnyReferenceTo(theResource);
		}
	}

	@SuppressWarnings("rawtypes")
	private void deleteGoldenResource(
			IResourcePersistentId goldenPid, IFhirResourceDao<?> theDao, RequestDetails theRequest) {
		setLinksDeletedBeforehand();

		if (myMdmSettings.isAutoExpungeGoldenResources()) {
			int numDeleted = deleteExpungeGoldenResource(goldenPid);
			if (numDeleted > 0) {
				ourLog.info("Removed {} golden resource(s).", numDeleted);
			}
		} else {
			String url = theRequest == null ? "" : theRequest.getCompleteUrl();
			theDao.deletePidList(
					url,
					Collections.singleton(goldenPid),
					new DeleteConflictList(),
					theRequest,
					new TransactionDetails());
		}
		resetLinksDeletedBeforehand();
	}

	/**
	 *  Clean up possible matches associated with a GR if they are the only link left
	 *  since they are no longer "real matches"
	 *  Possible match resources are resubmitted for matching
	 */
	@SuppressWarnings("unchecked")
	private void cleanUpPossibleMatches(
			List<IMdmLink> possibleMatches,
			IFhirResourceDao<?> theDao,
			IResourcePersistentId theGoldenPid,
			RequestDetails theRequestDetails) {
		String resourceType = theDao.getResourceType().getSimpleName();
		IIdType goldenId = myIdHelperSvc.resourceIdFromPidOrThrowException(theGoldenPid, resourceType);
		IAnyResource goldenResource = (IAnyResource) theDao.read(goldenId, theRequestDetails);
		for (IMdmLink possibleMatch : possibleMatches) {
			if (possibleMatch.getGoldenResourcePersistenceId().equals(theGoldenPid)) {
				IIdType sourceId = myIdHelperSvc.resourceIdFromPidOrThrowException(
						possibleMatch.getSourcePersistenceId(), resourceType);
				IBaseResource sourceResource = theDao.read(sourceId, theRequestDetails);
				MdmCreateOrUpdateParams params = new MdmCreateOrUpdateParams();
				params.setGoldenResource(goldenResource);
				params.setSourceResource((IAnyResource) sourceResource);
				params.setMatchResult(NO_MATCH);
				MdmTransactionContext mdmContext =
						createMdmContext(MdmTransactionContext.OperationType.UPDATE_LINK, sourceResource.fhirType());
				params.setMdmContext(mdmContext);
				params.setRequestDetails(theRequestDetails);

				mdmLinkUpdaterSvc.updateLink(params);
			}
		}
	}

	private IResourcePersistentId extractGoldenPid(IBaseResource theResource, IMdmLink theMdmLink) {
		IResourcePersistentId goldenPid = theMdmLink.getGoldenResourcePersistenceId();
		goldenPid = myIdHelperSvc.newPidFromStringIdAndResourceName(
				goldenPid.getPartitionId(), goldenPid.getId().toString(), theResource.fhirType());
		return goldenPid;
	}

	private boolean isDeletingLastMatchedSourceResource(IResourcePersistentId theSourcePid, List<IMdmLink> theMatches) {
		if (theMatches.size() != 1) {
			// if there's not 1 match, it can't be the last match
			return false;
		}

		IMdmLink match = theMatches.get(0);

		if (theSourcePid.getPartitionId() != null) {
			// if they are in different partitions, it's not the matching resource
			if (!theSourcePid.getPartitionId().equals(match.getPartitionId().getPartitionId())) {
				return false;
			}
		}

		return match.getSourcePersistenceId().getId().equals(theSourcePid.getId());
	}

	private MdmTransactionContext createMdmContext(
			MdmTransactionContext.OperationType theOperation, String theResourceType) {
		TransactionLogMessages transactionLogMessages = TransactionLogMessages.createNew();
		MdmTransactionContext retVal = new MdmTransactionContext(transactionLogMessages, theOperation);
		retVal.setResourceType(theResourceType);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private int deleteExpungeGoldenResource(IResourcePersistentId theGoldenPid) {
		IDeleteExpungeSvc deleteExpungeSvc = myIMdmClearHelperSvc.getDeleteExpungeSvc();
		return deleteExpungeSvc.deleteExpunge(new ArrayList<>(Collections.singleton(theGoldenPid)), false, null);
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
		throw new ForbiddenOperationException(
				Msg.code(763) + "While running with EID updates disabled, EIDs may not be updated on source resources");
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
		throw new ForbiddenOperationException(Msg.code(764) + "The " + MdmConstants.CODE_HAPI_MDM_MANAGED
				+ " tag on a resource may not be changed once created.");
	}

	private void throwModificationBlockedByMdm() {
		throw new ForbiddenOperationException(Msg.code(765)
				+ "Cannot create or modify Resources that are managed by MDM. This resource contains a tag with one of these systems: "
				+ MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS + " or " + MdmConstants.SYSTEM_MDM_MANAGED);
	}

	private void throwBlockMultipleEids() {
		throw new ForbiddenOperationException(Msg.code(766)
				+ "While running with multiple EIDs disabled, source resources may have at most one EID.");
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
