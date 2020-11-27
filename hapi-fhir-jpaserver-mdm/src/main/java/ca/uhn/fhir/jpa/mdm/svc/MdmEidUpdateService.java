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
		if (updateContext.isRemainsMatchedToSamePerson()) {
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
			//This is a new linking scenario. we have to break the existing link and link to the new person. For now, we create duplicate.
			//updated patient has an EID that matches to a new candidate. Link them, and set the persons possible duplicates
			linkToNewPersonAndFlagAsDuplicate(theResource, updateContext.getExistingPerson(), updateContext.getMatchedGoldenResource(), theMdmTransactionContext);
		}
	}

	private void handleNoEidsInCommon(IAnyResource theResource, MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, MdmTransactionContext theMdmTransactionContext, MdmUpdateContext theUpdateContext) {
		// the user is simply updating their EID. We propagate this change to the Person.
		//overwrite. No EIDS in common, but still same person.
		if (myMdmSettings.isPreventMultipleEids()) {
			if (myMdmLinkDaoSvc.findMdmMatchLinksBySource(theUpdateContext.getMatchedGoldenResource()).size() <= 1) { // If there is only 0/1 link on the person, we can safely overwrite the EID.
				handleExternalEidOverwrite(theUpdateContext.getMatchedGoldenResource(), theResource, theMdmTransactionContext);
			} else { // If the person has multiple patients tied to it, we can't just overwrite the EID, so we split the person.
				createNewPersonAndFlagAsDuplicate(theResource, theMdmTransactionContext, theUpdateContext.getExistingPerson());
			}
		} else {
			myGoldenResourceHelper.handleExternalEidAddition(theUpdateContext.getMatchedGoldenResource(), theResource, theMdmTransactionContext);
		}
		myMdmLinkSvc.updateLink(theUpdateContext.getMatchedGoldenResource(), theResource, theMatchedGoldenResourceCandidate.getMatchResult(), MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void handleExternalEidOverwrite(IAnyResource thePerson, IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myGoldenResourceHelper.overwriteExternalEids(thePerson, eidFromResource);
		}
	}

	private boolean candidateIsSameAsMdmLinkPerson(MdmLink theExistingMatchLink, MatchedGoldenResourceCandidate thePersonCandidate) {
		return theExistingMatchLink.getGoldenResourcePid().equals(thePersonCandidate.getCandidatePersonPid().getIdAsLong());
	}

	private void createNewPersonAndFlagAsDuplicate(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext, IAnyResource theOldPerson) {
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		IAnyResource newPerson = myGoldenResourceHelper.createGoldenResourceFromMdmTarget(theResource);

		myMdmLinkSvc.updateLink(newPerson, theResource, MdmMatchOutcome.NEW_PERSON_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		myMdmLinkSvc.updateLink(newPerson, theOldPerson, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void linkToNewPersonAndFlagAsDuplicate(IAnyResource theResource, IAnyResource theOldPerson, IAnyResource theNewPerson, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, "Changing a match link!");
		myMdmLinkSvc.deleteLink(theOldPerson, theResource, theMdmTransactionContext);
		myMdmLinkSvc.updateLink(theNewPerson, theResource, MdmMatchOutcome.NEW_PERSON_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		myMdmLinkSvc.updateLink(theNewPerson, theOldPerson, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
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
		private IAnyResource myExistingPerson;
		private boolean myRemainsMatchedToSamePerson;
		private final IAnyResource myMatchedGoldenResource;

		public IAnyResource getMatchedGoldenResource() {
			return myMatchedGoldenResource;
		}

		MdmUpdateContext(MatchedGoldenResourceCandidate theMatchedGoldenResourceCandidate, IAnyResource theResource) {
			final String resourceType = theResource.getIdElement().getResourceType();
			myMatchedGoldenResource = myMdmGoldenResourceFindingSvc.getGoldenResourceFromMatchedGoldenResourceCandidate(theMatchedGoldenResourceCandidate, resourceType);

			myHasEidsInCommon = myEIDHelper.hasEidOverlap(myMatchedGoldenResource, theResource);
			myIncomingResourceHasAnEid = !myEIDHelper.getExternalEid(theResource).isEmpty();

			Optional<MdmLink> theExistingMatchLink = myMdmLinkDaoSvc.getMatchedLinkForTarget(theResource);
			myExistingPerson = null;

			if (theExistingMatchLink.isPresent()) {
				MdmLink mdmLink = theExistingMatchLink.get();
				Long existingPersonPid = mdmLink.getGoldenResourcePid();
				myExistingPerson =  myMdmResourceDaoSvc.readGoldenResourceByPid(new ResourcePersistentId(existingPersonPid), resourceType);
				myRemainsMatchedToSamePerson = candidateIsSameAsMdmLinkPerson(mdmLink, theMatchedGoldenResourceCandidate);
			} else {
				myRemainsMatchedToSamePerson = false;
			}
		}

		public boolean isHasEidsInCommon() {
			return myHasEidsInCommon;
		}

		public boolean isIncomingResourceHasAnEid() {
			return myIncomingResourceHasAnEid;
		}

		public IAnyResource getExistingPerson() {
			return myExistingPerson;
		}

		public boolean isRemainsMatchedToSamePerson() {
			return myRemainsMatchedToSamePerson;
		}
	}
}
