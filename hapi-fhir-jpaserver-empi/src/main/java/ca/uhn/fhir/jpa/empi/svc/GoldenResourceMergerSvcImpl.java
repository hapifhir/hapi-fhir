package ca.uhn.fhir.jpa.empi.svc;

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

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
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
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	PersonHelper myPersonHelper;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	@Override
	@Transactional
	public IAnyResource mergeGoldenResources(IAnyResource theFromGoldenResource, IAnyResource theToGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		Long fromGoldenResourcePid = myIdHelperService.getPidOrThrowException(theFromGoldenResource);
		Long toGoldenResourcePid = myIdHelperService.getPidOrThrowException(theToGoldenResource);
		String resourceType = theMdmTransactionContext.getResourceType();

		//Merge attributes, to be determined when survivorship is solved.
		myPersonHelper.mergeFields(theFromGoldenResource, theToGoldenResource);

		//Merge the links from the FROM to the TO resource. Clean up dangling links.
		mergeGoldenResourceLinks(theFromGoldenResource, theToGoldenResource, toGoldenResourcePid, theMdmTransactionContext);

		//Create the new REDIRECT link
		addMergeLink(toGoldenResourcePid, fromGoldenResourcePid, resourceType);

		//Strip the golden resource tag from the now-deprecated resource.
		myEmpiResourceDaoSvc.removeGoldenResourceTag(theFromGoldenResource, resourceType);

		//Add the REDIRECT tag to that same deprecated resource.
		myPersonHelper.deactivateResource(theFromGoldenResource);

		//Save the deprecated resource.
		myEmpiResourceDaoSvc.upsertSourceResource(theFromGoldenResource, resourceType);

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
		List<EmpiLink> allLinksWithTheFromAsTarget = myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(theFrom);
		allLinksWithTheFromAsTarget
			.stream()
			//TODO GGG NG MDM: Why are we keeping manual links? Haven't we already copied those over in the previous merge step?
			.filter(EmpiLink::isAuto) // only keep manual links
			.forEach(l -> {
				theMdmTransactionContext.addTransactionLogMessage(String.format("Deleting link %s", l));
				myEmpiLinkDaoSvc.deleteLink(l);
			});
	}

	private void addMergeLink(Long theSourceResourcePidAkaActive, Long theTargetResourcePidAkaDeactivated, String theResourceType) {
		EmpiLink empiLink = myEmpiLinkDaoSvc
			.getOrCreateEmpiLinkBySourceResourcePidAndTargetResourcePid(theSourceResourcePidAkaActive, theTargetResourcePidAkaDeactivated);

		empiLink
			.setEmpiTargetType(theResourceType)
			.setMatchResult(EmpiMatchResultEnum.REDIRECT)
			.setLinkSource(EmpiLinkSourceEnum.MANUAL);
		myEmpiLinkDaoSvc.save(empiLink);
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
		List<EmpiLink> fromLinks = myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(theFromResource); // fromLinks - links going to theFromResource
		List<EmpiLink> toLinks = myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(theToResource); // toLinks - links going to theToResource
		List<EmpiLink> toDelete = new ArrayList<>();

		for (EmpiLink fromLink : fromLinks) {
			Optional<EmpiLink> optionalToLink = findFirstLinkWithMatchingTarget(toLinks, fromLink);
			if (optionalToLink.isPresent()) {

				// The original links already contain this target, so move it over to the toResource
				EmpiLink toLink = optionalToLink.get();
				if (fromLink.isManual()) {
					switch (toLink.getLinkSource()) {
						case AUTO:
							//3
							log(theMdmTransactionContext, String.format("MANUAL overrides AUT0.  Deleting link %s", toLink.toString()));
							myEmpiLinkDaoSvc.deleteLink(toLink);
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
			myEmpiLinkDaoSvc.save(fromLink);
		}
		//1 Delete dangling links
		toDelete.forEach(link -> myEmpiLinkDaoSvc.deleteLink(link));
	}

	private Optional<EmpiLink> findFirstLinkWithMatchingTarget(List<EmpiLink> theEmpiLinks, EmpiLink theLinkWithTargetToMatch) {
		return theEmpiLinks.stream()
			.filter(empiLink -> empiLink.getTargetPid().equals(theLinkWithTargetToMatch.getTargetPid()))
			.findFirst();
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
