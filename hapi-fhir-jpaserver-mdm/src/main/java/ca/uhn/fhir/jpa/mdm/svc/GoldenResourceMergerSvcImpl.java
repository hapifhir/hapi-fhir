package ca.uhn.fhir.jpa.mdm.svc;

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
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
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
	IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	IJpaIdHelperService myIdHelperService;
	@Autowired
	MdmResourceDaoSvc myMdmResourceDaoSvc;
	@Autowired
	MessageHelper myMessageHelper;
	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	@Override
	@Transactional
	public IAnyResource mergeGoldenResources(IAnyResource theFromGoldenResource, IAnyResource theMergedResource, IAnyResource theToGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		Long fromGoldenResourcePid = myIdHelperService.getPidOrThrowException(theFromGoldenResource);
		Long toGoldenResourcePid = myIdHelperService.getPidOrThrowException(theToGoldenResource);
		String resourceType = theMdmTransactionContext.getResourceType();

		if (theMergedResource != null) {
			if (myGoldenResourceHelper.hasIdentifier(theMergedResource)) {
				throw new IllegalArgumentException(Msg.code(751) + "Manually merged resource can not contain identifiers");
			}
			myGoldenResourceHelper.mergeIndentifierFields(theFromGoldenResource, theMergedResource, theMdmTransactionContext);
			myGoldenResourceHelper.mergeIndentifierFields(theToGoldenResource, theMergedResource, theMdmTransactionContext);

			theMergedResource.setId(theToGoldenResource.getId());
			theToGoldenResource = (IAnyResource) myMdmResourceDaoSvc.upsertGoldenResource(theMergedResource, resourceType).getResource();
		} else {
			myGoldenResourceHelper.mergeIndentifierFields(theFromGoldenResource, theToGoldenResource, theMdmTransactionContext);
			myGoldenResourceHelper.mergeNonIdentiferFields(theFromGoldenResource, theToGoldenResource, theMdmTransactionContext);
			//Save changes to the golden resource
			myMdmResourceDaoSvc.upsertGoldenResource(theToGoldenResource, resourceType);
		}

		// check if the golden resource and the source resource are in the same partition, throw error if not
		myMdmPartitionHelper.validateResourcesInSamePartition(theFromGoldenResource, theToGoldenResource);

		//Merge the links from the FROM to the TO resource. Clean up dangling links.
		mergeGoldenResourceLinks(theFromGoldenResource, theToGoldenResource, toGoldenResourcePid, theMdmTransactionContext);

		//Create the new REDIRECT link
		addMergeLink(toGoldenResourcePid, fromGoldenResourcePid, resourceType);

		//Strip the golden resource tag from the now-deprecated resource.
		myMdmResourceDaoSvc.removeGoldenResourceTag(theFromGoldenResource, resourceType);

		//Add the REDIRECT tag to that same deprecated resource.
		MdmResourceUtil.setGoldenResourceRedirected(theFromGoldenResource);

		//Save the deprecated resource.
		myMdmResourceDaoSvc.upsertGoldenResource(theFromGoldenResource, resourceType);

		log(theMdmTransactionContext, "Merged " + theFromGoldenResource.getIdElement().toVersionless()
			+ " into " + theToGoldenResource.getIdElement().toVersionless());
		return theToGoldenResource;
	}

	private void addMergeLink(Long theGoldenResourcePidAkaActive, Long theTargetResourcePidAkaDeactivated, String theResourceType) {
		MdmLink mdmLink = myMdmLinkDaoSvc
			.getOrCreateMdmLinkByGoldenResourcePidAndSourceResourcePid(theGoldenResourcePidAkaActive, theTargetResourcePidAkaDeactivated);

		mdmLink
			.setMdmSourceType(theResourceType)
			.setMatchResult(MdmMatchResultEnum.REDIRECT)
			.setLinkSource(MdmLinkSourceEnum.MANUAL);
		myMdmLinkDaoSvc.save(mdmLink);
	}


	/**
	 * Helper method which performs merger of links between resources, and cleans up dangling links afterwards.
	 * <p>
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
			Optional<MdmLink> optionalToLink = findFirstLinkWithMatchingSource(toLinks, fromLink);
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
								throw new InvalidRequestException(Msg.code(752) + "A MANUAL " + fromLink.getMatchResult() + " link may not be merged into a MANUAL " + toLink.getMatchResult() + " link for the same target");
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

	private Optional<MdmLink> findFirstLinkWithMatchingSource(List<MdmLink> theMdmLinks, MdmLink theLinkWithSourceToMatch) {
		return theMdmLinks.stream()
			.filter(mdmLink -> mdmLink.getSourcePid().equals(theLinkWithSourceToMatch.getSourcePid()))
			.findFirst();
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
