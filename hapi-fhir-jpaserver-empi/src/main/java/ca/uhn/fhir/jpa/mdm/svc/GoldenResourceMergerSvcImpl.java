package ca.uhn.fhir.jpa.mdm.svc;

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

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
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
	IMdmLinkSvc myEmpiLinkSvc;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	MdmResourceDaoSvc myMdmResourceDaoSvc;

	@Override
	@Transactional
	public IAnyResource mergeGoldenResources(IAnyResource theFromGoldenResource, IAnyResource theToGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		Long fromGoldenResourcePid = myIdHelperService.getPidOrThrowException(theFromGoldenResource);
		Long toGoldenResourcePid = myIdHelperService.getPidOrThrowException(theToGoldenResource);
		String resourceType = theMdmTransactionContext.getResourceType();

		//Merge attributes, to be determined when survivorship is solved.
		myGoldenResourceHelper.mergeFields(theFromGoldenResource, theToGoldenResource);

		//Merge the links from the FROM to the TO resource. Clean up dangling links.
		mergeGoldenResourceLinks(theFromGoldenResource, theToGoldenResource, toGoldenResourcePid, theMdmTransactionContext);

		//Create the new REDIRECT link
		addMergeLink(toGoldenResourcePid, fromGoldenResourcePid, resourceType);

		//Strip the golden resource tag from the now-deprecated resource.
		myMdmResourceDaoSvc.removeGoldenResourceTag(theFromGoldenResource, resourceType);

		//Add the REDIRECT tag to that same deprecated resource.
		myGoldenResourceHelper.deactivateResource(theFromGoldenResource);

		//Save the deprecated resource.
		myMdmResourceDaoSvc.upsertSourceResource(theFromGoldenResource, resourceType);

		log(theMdmTransactionContext, "Merged " + theFromGoldenResource.getIdElement().toVersionless() + " into " + theToGoldenResource.getIdElement().toVersionless());
		return theToGoldenResource;
	}

	/**
	 * Removes non-manual links from source to target
	 *
	 * @param theFrom                   Target of the link
	 * @param theTo                     Source resource of the link
	 * @param theMdmTransactionContext Context to keep track of the deletions
	 */
	private void removeTargetLinks(IAnyResource theFrom, IAnyResource theTo, MdmTransactionContext theMdmTransactionContext) {
		List<MdmLink> allLinksWithTheFromAsTarget = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theFrom);
		allLinksWithTheFromAsTarget
			.stream()
			//TODO GGG NG MDM: Why are we keeping manual links? Haven't we already copied those over in the previous merge step?
			.filter(MdmLink::isAuto) // only keep manual links
			.forEach(l -> {
				theMdmTransactionContext.addTransactionLogMessage(String.format("Deleting link %s", l));
				myMdmLinkDaoSvc.deleteLink(l);
			});
	}

	private void addMergeLink(Long theSourceResourcePidAkaActive, Long theTargetResourcePidAkaDeactivated, String theResourceType) {
		MdmLink mdmLink = myMdmLinkDaoSvc
			.getOrCreateMdmLinkBySourceResourcePidAndTargetResourcePid(theSourceResourcePidAkaActive, theTargetResourcePidAkaDeactivated);

		mdmLink
			.setMdmTargetType(theResourceType)
			.setMatchResult(MdmMatchResultEnum.REDIRECT)
			.setLinkSource(MdmLinkSourceEnum.MANUAL);
		myMdmLinkDaoSvc.save(mdmLink);
	}


	/**
	 * Helper method which performs merger of links between resources, and cleans up dangling links afterwards.
	 *
	 * For each incomingLink, either ignore it, move it, or replace the original one
	 * 1. If the link already exists on the TO resource, and it is an automatic link, ignore the link, and subsequently delete it.
	 * 2. If the link does not exist on the TO resource, redirect the link from the FROM resource to the TO resource
	 * 3. If an incoming link is MANUAL, and theres a matching link on the FROM resource which is AUTOMATIC, the manual link supercedes the automatic one.
	 * 4. Manual link collisions cause invalid request exception.
	 *
	 * @param theFromResource
	 * @param theToResource
	 * @param theToResourcePid
	 * @param theMdmTransactionContext
	 */
	private void mergeGoldenResourceLinks(IAnyResource theFromResource, IAnyResource theToResource, Long theToResourcePid, MdmTransactionContext theMdmTransactionContext) {
		List<MdmLink> fromLinks = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theFromResource); // fromLinks - links going to theFromResource
		List<MdmLink> toLinks = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theToResource); // toLinks - links going to theToResource
		List<MdmLink> toDelete = new ArrayList<>();

		for (MdmLink fromLink : fromLinks) {
			Optional<MdmLink> optionalToLink = findFirstLinkWithMatchingTarget(toLinks, fromLink);
			if (optionalToLink.isPresent()) {

				// The original links already contain this target, so move it over to the toResource
				MdmLink toLink = optionalToLink.get();
				if (fromLink.isManual()) {
					switch (toLink.getLinkSource()) {
						case AUTO:
							//3
							log(theMdmTransactionContext, String.format("MANUAL overrides AUT0.  Deleting link %s", toLink.toString()));
							myMdmLinkDaoSvc.deleteLink(toLink);
							break;
						case MANUAL:
							if (fromLink.getMatchResult() != toLink.getMatchResult()) {
								throw new InvalidRequestException("A MANUAL " + fromLink.getMatchResult() + " link may not be merged into a MANUAL " + toLink.getMatchResult() + " link for the same target");
							}
					}
				} else {
					//1
					toDelete.add(fromLink);
					continue;
				}
			}
			//2 The original TO links didn't contain this target, so move it over to the toGoldenResource
			fromLink.setGoldenResourcePid(theToResourcePid);
			ourLog.trace("Saving link {}", fromLink);
			myMdmLinkDaoSvc.save(fromLink);
		}
		//1 Delete dangling links
		toDelete.forEach(link -> myMdmLinkDaoSvc.deleteLink(link));
	}

	private Optional<MdmLink> findFirstLinkWithMatchingTarget(List<MdmLink> theMdmLinks, MdmLink theLinkWithTargetToMatch) {
		return theMdmLinks.stream()
			.filter(empiLink -> empiLink.getTargetPid().equals(theLinkWithTargetToMatch.getTargetPid()))
			.findFirst();
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
