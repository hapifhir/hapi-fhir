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

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
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
	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	void handleMdmUpdate(IAnyResource theTargetResource, MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, MdmTransactionContext theMdmTransactionContext) {
		MdmUpdateContext updateContext = new MdmUpdateContext(theMatchedGoldenResourceCandidate, theTargetResource);
		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(theTargetResource, updateContext.getMatchedGoldenResource(), theMdmTransactionContext);

		if (updateContext.isRemainsMatchedToSameGoldenResource()) {
			// Copy over any new external EIDs which don't already exist.
			if (!updateContext.isIncomingResourceHasAnEid() || updateContext.isHasEidsInCommon()) {
				//update to patient that uses internal EIDs only.
				myMdmLinkSvc.updateLink(updateContext.getMatchedGoldenResource(), theTargetResource, theMatchedGoldenResourceCandidate.getMatchResult(), MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
			} else if (!updateContext.isHasEidsInCommon()) {
				handleNoEidsInCommon(theTargetResource, theMatchedGoldenResourceCandidate, theMdmTransactionContext, updateContext);
			}
		} else {
			//This is a new linking scenario. we have to break the existing link and link to the new Golden Resource. For now, we create duplicate.
			//updated patient has an EID that matches to a new candidate. Link them, and set the Golden Resources possible duplicates
			linkToNewGoldenResourceAndFlagAsDuplicate(theTargetResource, theMatchedGoldenResourceCandidate.getMatchResult(), updateContext.getExistingGoldenResource(), updateContext.getMatchedGoldenResource(), theMdmTransactionContext);

			myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(theTargetResource, updateContext.getMatchedGoldenResource(), theMdmTransactionContext);
			myMdmResourceDaoSvc.upsertGoldenResource(updateContext.getMatchedGoldenResource(), theMdmTransactionContext.getResourceType());
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
		IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(theResource, theMdmTransactionContext);

		myMdmLinkSvc.updateLink(newGoldenResource, theResource, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		myMdmLinkSvc.updateLink(newGoldenResource, theOldGoldenResource, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void linkToNewGoldenResourceAndFlagAsDuplicate(IAnyResource theResource, MdmMatchOutcome theMatchResult, IAnyResource theOldGoldenResource, IAnyResource theNewGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, "Changing a match link!");
		myMdmLinkSvc.deleteLink(theOldGoldenResource, theResource, theMdmTransactionContext);
		myMdmLinkSvc.updateLink(theNewGoldenResource, theResource, theMatchResult, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		myMdmLinkSvc.updateLink(theNewGoldenResource, theOldGoldenResource, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}

	public void applySurvivorshipRulesAndSaveGoldenResource(IAnyResource theTargetResource, IAnyResource theGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(theTargetResource, theGoldenResource, theMdmTransactionContext);
		myMdmResourceDaoSvc.upsertGoldenResource(theGoldenResource, theMdmTransactionContext.getResourceType());
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

			Optional<MdmLink> theExistingMatchOrPossibleMatchLink = myMdmLinkDaoSvc.getMatchedOrPossibleMatchedLinkForSource(theResource);
			myExistingGoldenResource = null;

			if (theExistingMatchOrPossibleMatchLink.isPresent()) {
				MdmLink mdmLink = theExistingMatchOrPossibleMatchLink.get();
				Long existingGoldenResourcePid = mdmLink.getGoldenResourcePid();
				myExistingGoldenResource = myMdmResourceDaoSvc.readGoldenResourceByPid(new ResourcePersistentId(existingGoldenResourcePid), resourceType);
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

		@Nullable
		public IAnyResource getExistingGoldenResource() {
			return myExistingGoldenResource;
		}

		public boolean isRemainsMatchedToSameGoldenResource() {
			return myRemainsMatchedToSameGoldenResource;
		}
	}
}
