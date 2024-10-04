/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmMergeGoldenResourcesParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmEventResource;
import ca.uhn.fhir.mdm.model.mdmevents.MdmMergeEvent;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoldenResourceMergerSvcImpl implements IGoldenResourceMergerSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	GoldenResourceHelper myGoldenResourceHelper;

	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	IMdmLinkSvc myMdmLinkSvc;

	@Autowired
	IIdHelperService myIdHelperService;

	@Autowired
	IMdmResourceDaoSvc myMdmResourceDaoSvc;

	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	@Autowired
	IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	@Override
	@Transactional
	public IAnyResource mergeGoldenResources(MdmMergeGoldenResourcesParams theParams) {
		MdmTransactionContext mdmTransactionContext = theParams.getMdmTransactionContext();
		IAnyResource mergedResource = theParams.getManuallyMergedResource();
		IAnyResource fromGoldenResource = theParams.getFromGoldenResource();
		IAnyResource toGoldenResource = theParams.getToGoldenResource();

		String resourceType = mdmTransactionContext.getResourceType();

		if (mergedResource != null) {
			if (myGoldenResourceHelper.hasIdentifier(mergedResource)) {
				throw new IllegalArgumentException(
						Msg.code(751) + "Manually merged resource can not contain identifiers");
			}
			myGoldenResourceHelper.mergeIndentifierFields(fromGoldenResource, mergedResource, mdmTransactionContext);
			myGoldenResourceHelper.mergeIndentifierFields(toGoldenResource, mergedResource, mdmTransactionContext);

			mergedResource.setId(toGoldenResource.getId());
			toGoldenResource = (IAnyResource) myMdmResourceDaoSvc
					.upsertGoldenResource(mergedResource, resourceType)
					.getResource();
		} else {
			myGoldenResourceHelper.mergeIndentifierFields(fromGoldenResource, toGoldenResource, mdmTransactionContext);
			myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(
					fromGoldenResource, toGoldenResource, mdmTransactionContext);
			// Save changes to the golden resource
			myMdmResourceDaoSvc.upsertGoldenResource(toGoldenResource, resourceType);
		}

		myMdmPartitionHelper.validateMdmResourcesPartitionMatches(fromGoldenResource, toGoldenResource);

		// Merge the links from the FROM to the TO resource. Clean up dangling links.
		mergeGoldenResourceLinks(
				fromGoldenResource, toGoldenResource, fromGoldenResource.getIdElement(), mdmTransactionContext);

		// Create the new REDIRECT link
		addMergeLink(toGoldenResource, fromGoldenResource, resourceType, mdmTransactionContext);

		// Strip the golden resource tag from the now-deprecated resource.
		myMdmResourceDaoSvc.removeGoldenResourceTag(fromGoldenResource, resourceType);

		// Add the REDIRECT tag to that same deprecated resource.
		MdmResourceUtil.setGoldenResourceRedirected(fromGoldenResource);

		// Save the deprecated resource.
		myMdmResourceDaoSvc.upsertGoldenResource(fromGoldenResource, resourceType);

		log(
				mdmTransactionContext,
				"Merged " + fromGoldenResource.getIdElement().toVersionless() + " into "
						+ toGoldenResource.getIdElement().toVersionless());

		// invoke hooks
		invokeMdmMergeGoldenResourcesHook(theParams, fromGoldenResource, toGoldenResource);

		return toGoldenResource;
	}

	private void invokeMdmMergeGoldenResourcesHook(
			MdmMergeGoldenResourcesParams theParams, IAnyResource fromGoldenResource, IAnyResource toGoldenResource) {
		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_POST_MERGE_GOLDEN_RESOURCES)) {
			// pointcut for MDM_POST_MERGE_GOLDEN_RESOURCES
			MdmMergeEvent event = new MdmMergeEvent();
			MdmEventResource from = new MdmEventResource();
			from.setId(
					fromGoldenResource.getIdElement().toUnqualifiedVersionless().getValue());
			from.setResourceType(fromGoldenResource.fhirType());
			from.setIsGoldenResource(true);
			event.setFromResource(from);

			MdmEventResource to = new MdmEventResource();
			to.setId(toGoldenResource.getIdElement().toUnqualifiedVersionless().getValue());
			to.setResourceType(toGoldenResource.fhirType());
			to.setIsGoldenResource(true);
			event.setToResource(to);

			HookParams params = new HookParams();
			params.add(MdmMergeEvent.class, event);
			params.add(RequestDetails.class, theParams.getRequestDetails());
			params.add(MdmTransactionContext.class, theParams.getMdmTransactionContext());
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_POST_MERGE_GOLDEN_RESOURCES, params);
		}
	}

	/**
	 * This connects 2 golden resources (GR and TR here)
	 *
	 * 1 Deletes any current links: TR, ?, ?, GR
	 * 2 Creates a new link: GR, MANUAL, REDIRECT, TR
	 *
	 * Before:
	 * TR -> GR
	 *
	 * After:
	 * GR -> TR
	 */
	private void addMergeLink(
			IAnyResource theGoldenResource,
			IAnyResource theTargetResource,
			String theResourceType,
			MdmTransactionContext theMdmTransactionContext) {
		myMdmLinkSvc.deleteLink(theGoldenResource, theTargetResource, theMdmTransactionContext);

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(
				theTargetResource, // golden / LHS
				theGoldenResource, // source / RHS
				new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.REDIRECT),
				MdmLinkSourceEnum.MANUAL,
				theMdmTransactionContext // mdm transaction context
				);
	}

	private RequestPartitionId getPartitionIdForResource(IAnyResource theResource) {
		RequestPartitionId partitionId = (RequestPartitionId) theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		// TODO - this seems to be null on the put with
		// client id (forced id). Is this a bug?
		if (partitionId == null) {
			partitionId = RequestPartitionId.allPartitions();
		}
		return partitionId;
	}

	/**
	 * Helper method which performs merger of links between resources, and cleans up dangling links afterwards.
	 * <p>
	 * For each incomingLink, either ignore it, move it, or replace the original one
	 * 1. If the link already exists on the TO resource, and it is an automatic link, ignore the link, and subsequently delete it.
	 * 2.a If the link does not exist on the TO resource, redirect the link from the FROM resource to the TO resource
	 * 2.b If the link does not exist on the TO resource, but is actually self-referential, it will just be removed
	 * 3. If an incoming link is MANUAL, and there's a matching link on the FROM resource which is AUTOMATIC, the manual link supercedes the automatic one.
	 * 4. Manual link collisions cause invalid request exception.
	 *
	 * @param theFromResource
	 * @param theToResource
	 * @param theToResourcePid
	 * @param theMdmTransactionContext
	 */
	private void mergeGoldenResourceLinks(
			IAnyResource theFromResource,
			IAnyResource theToResource,
			IIdType theToResourcePid,
			MdmTransactionContext theMdmTransactionContext) {
		// fromLinks - links from theFromResource to any resource
		List<? extends IMdmLink> fromLinks = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theFromResource);
		// toLinks - links from theToResource to any resource
		List<? extends IMdmLink> toLinks = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theToResource);
		List<IMdmLink> toDelete = new ArrayList<>();

		IResourcePersistentId goldenResourcePid = myIdHelperService.resolveResourcePersistentIds(
				getPartitionIdForResource(theToResource),
				theToResource.getIdElement().getResourceType(),
				theToResource.getIdElement().getIdPart());

		// reassign links:
		// to <- from
		for (IMdmLink fromLink : fromLinks) {
			Optional<? extends IMdmLink> optionalToLink = findFirstLinkWithMatchingSource(toLinks, fromLink);
			if (optionalToLink.isPresent()) {

				// The original links already contain this target, so move it over to the toResource
				IMdmLink toLink = optionalToLink.get();
				if (fromLink.isManual()) {
					switch (toLink.getLinkSource()) {
						case AUTO:
							// 3
							log(
									theMdmTransactionContext,
									String.format("MANUAL overrides AUT0.  Deleting link %s", toLink.toString()));
							myMdmLinkDaoSvc.deleteLink(toLink);
							break;
						case MANUAL:
							if (fromLink.getMatchResult() != toLink.getMatchResult()) {
								throw new InvalidRequestException(Msg.code(752) + "A MANUAL "
										+ fromLink.getMatchResult() + " link may not be merged into a MANUAL "
										+ toLink.getMatchResult() + " link for the same target");
							}
					}
				} else {
					// 1
					toDelete.add(fromLink);
					continue;
				}
			}

			if (fromLink.getSourcePersistenceId().equals(goldenResourcePid)) {
				// 2.b if the link is going to be self-referential we'll just delete it
				// (ie, do not link back to itself)
				myMdmLinkDaoSvc.deleteLink(fromLink);
			} else {
				// 2.a The original TO links didn't contain this target, so move it over to the toGoldenResource.
				fromLink.setGoldenResourcePersistenceId(goldenResourcePid);
				ourLog.trace("Saving link {}", fromLink);
				myMdmLinkDaoSvc.save(fromLink);
			}
		}

		// 1 Delete dangling links
		toDelete.forEach(link -> myMdmLinkDaoSvc.deleteLink(link));
	}

	private Optional<? extends IMdmLink> findFirstLinkWithMatchingSource(
			List<? extends IMdmLink> theMdmLinks, IMdmLink theLinkWithSourceToMatch) {
		return theMdmLinks.stream()
				.filter(mdmLink ->
						mdmLink.getSourcePersistenceId().equals(theLinkWithSourceToMatch.getSourcePersistenceId()))
				.findFirst();
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
