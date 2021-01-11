package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MdmEidUpdateService {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmResourceDaoSvc myMdmResourceDaoSvc;
	@Autowired
	private IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	private MdmGoldenResourceFindingSvc myMdmGoldenResourceFindingSvc;
	@Autowired
	private GoldenResourceHelper myGoldenResourceHelper;
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	private IMdmSettings myMdmSettings;

	void handleMdmUpdate(IAnyResource theResource, MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, MdmTransactionContext theMdmTransactionContext) {
		MdmUpdateContext updateContext = new MdmUpdateContext(theMatchedGoldenResourceCandidate, theResource);
		if (updateContext.isRemainsMatchedToSameGoldenResource()) {
			// Copy over any new external EIDs which don't already exist.
			// TODO NG - Eventually this call will use terser to clone data in, once the surviorship rules for copying data will be confirmed
			// myPersonHelper.updatePersonFromUpdatedEmpiTarget(updateContext.getMatchedPerson(), theResource, theEmpiTransactionContext);
			if (!updateContext.isIncomingResourceHasAnEid() || updateContext.isHasEidsInCommon()) {
				//update to patient that uses internal EIDs only.
				myMdmLinkSvc.updateLink(updateContext.getMatchedGoldenResource(), theResource, theMatchedGoldenResourceCandidate.getMatchResult(), MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
			} else if (!updateContext.isHasEidsInCommon()) {
				handleNoEidsInCommon(theResource, theMatchedGoldenResourceCandidate, theMdmTransactionContext, updateContext);
			}
		} else {
			//This is a new linking scenario. we have to break the existing link and link to the new Golden Resource. For now, we create duplicate.
			//updated patient has an EID that matches to a new candidate. Link them, and set the Golden Resources possible duplicates
			linkToNewGoldenResourceAndFlagAsDuplicate(theResource, updateContext.getExistingGoldenResource(), updateContext.getMatchedGoldenResource(), theMdmTransactionContext);
		}
	}

	private void handleNoEidsInCommon(IAnyResource theResource, MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, MdmTransactionContext theMdmTransactionContext, MdmUpdateContext theUpdateContext) {
		// the user is simply updating their EID. We propagate this change to the GoldenResource.
		//overwrite. No EIDS in common, but still same GoldenResource.
		if (myMdmSettings.isPreventMultipleEids()) {
			if (myMdmLinkDaoSvc.findMdmMatchLinksByGoldenResource(theUpdateContext.getMatchedGoldenResource()).size() <= 1) { // If there is only 0/1 link on the GoldenResource, we can safely overwrite the EID.
				handleExternalEidOverwrite(theUpdateContext.getMatchedGoldenResource(), theResource, theMdmTransactionContext);
			} else { // If the GoldenResource has multiple targets tied to it, we can't just overwrite the EID, so we split the GoldenResource.
				createNewGoldenResourceAndFlagAsDuplicate(theResource, theMdmTransactionContext, theUpdateContext.getExistingGoldenResource());
			}
		} else {
			myGoldenResourceHelper.handleExternalEidAddition(theUpdateContext.getMatchedGoldenResource(), theResource, theMdmTransactionContext);
		}
		myMdmLinkSvc.updateLink(theUpdateContext.getMatchedGoldenResource(), theResource, theMatchedGoldenResourceCandidate.getMatchResult(), MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void handleExternalEidOverwrite(IAnyResource theGoldenResource, IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myGoldenResourceHelper.overwriteExternalEids(theGoldenResource, eidFromResource);
		}
	}

	private boolean candidateIsSameAsMdmLinkGoldenResource(MdmLink theExistingMatchLink, MatchedGoldenResourceCandidate theGoldenResourceCandidate) {
		return theExistingMatchLink.getGoldenResourcePid().equals(theGoldenResourceCandidate.getCandidateGoldenResourcePid().getIdAsLong());
	}

	private void createNewGoldenResourceAndFlagAsDuplicate(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext, IAnyResource theOldGoldenResource) {
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(theResource);

		myMdmLinkSvc.updateLink(newGoldenResource, theResource, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		myMdmLinkSvc.updateLink(newGoldenResource, theOldGoldenResource, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void linkToNewGoldenResourceAndFlagAsDuplicate(IAnyResource theResource, IAnyResource theOldGoldenResource, IAnyResource theNewGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, "Changing a match link!");
		myMdmLinkSvc.deleteLink(theOldGoldenResource, theResource, theMdmTransactionContext);
		myMdmLinkSvc.updateLink(theNewGoldenResource, theResource, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		myMdmLinkSvc.updateLink(theNewGoldenResource, theOldGoldenResource, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}

	/**
	 * Data class to hold context surrounding an update operation for an MDM target.
	 */
	class MdmUpdateContext {

		private final boolean myHasEidsInCommon;
		private final boolean myIncomingResourceHasAnEid;
		private IAnyResource myExistingGoldenResource;
		private boolean myRemainsMatchedToSameGoldenResource;
		private final IAnyResource myMatchedGoldenResource;

		public IAnyResource getMatchedGoldenResource() {
			return myMatchedGoldenResource;
		}

		MdmUpdateContext(MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, IAnyResource theResource) {
			final String resourceType = theResource.getIdElement().getResourceType();
			myMatchedGoldenResource = myMdmGoldenResourceFindingSvc.getGoldenResourceFromMatchedGoldenResourceCandidate(theMatchedGoldenResourceCandidate, resourceType);

			myHasEidsInCommon = myEIDHelper.hasEidOverlap(myMatchedGoldenResource, theResource);
			myIncomingResourceHasAnEid = !myEIDHelper.getExternalEid(theResource).isEmpty();

			Optional<MdmLink> theExistingMatchLink = myMdmLinkDaoSvc.getMatchedLinkForSource(theResource);
			myExistingGoldenResource = null;

			if (theExistingMatchLink.isPresent()) {
				MdmLink mdmLink = theExistingMatchLink.get();
				Long existingGoldenResourcePid = mdmLink.getGoldenResourcePid();
				myExistingGoldenResource =  myMdmResourceDaoSvc.readGoldenResourceByPid(new ResourcePersistentId(existingGoldenResourcePid), resourceType);
				myRemainsMatchedToSameGoldenResource = candidateIsSameAsMdmLinkGoldenResource(mdmLink, theMatchedGoldenResourceCandidate);
			} else {
				myRemainsMatchedToSameGoldenResource = false;
			}
		}

		public boolean isHasEidsInCommon() {
			return myHasEidsInCommon;
		}

		public boolean isIncomingResourceHasAnEid() {
			return myIncomingResourceHasAnEid;
		}

		public IAnyResource getExistingGoldenResource() {
			return myExistingGoldenResource;
		}

		public boolean isRemainsMatchedToSameGoldenResource() {
			return myRemainsMatchedToSameGoldenResource;
		}
	}
}
