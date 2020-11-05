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
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiPersonFindingSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.MatchedPersonCandidate;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpiEidUpdateService {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;
	@Autowired
	private IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EmpiPersonFindingSvc myEmpiPersonFindingSvc;
	@Autowired
	private PersonHelper myPersonHelper;
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	private IEmpiSettings myEmpiSettings;

	void handleEmpiUpdate(IAnyResource theResource, MatchedPersonCandidate theMatchedPersonCandidate, EmpiTransactionContext theEmpiTransactionContext) {

		EmpiUpdateContext updateContext = new EmpiUpdateContext(theMatchedPersonCandidate, theResource);

		if (updateContext.isRemainsMatchedToSamePerson()) {
			myPersonHelper.updatePersonFromUpdatedEmpiTarget(updateContext.getMatchedPerson(), theResource, theEmpiTransactionContext);
			if (!updateContext.isIncomingResourceHasAnEid() || updateContext.isHasEidsInCommon()) {
				//update to patient that uses internal EIDs only.
				myEmpiLinkSvc.updateLink(updateContext.getMatchedPerson(), theResource, theMatchedPersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
			} else if (!updateContext.isHasEidsInCommon()) {
				handleNoEidsInCommon(theResource, theMatchedPersonCandidate, theEmpiTransactionContext, updateContext);
			}
		} else {
			//This is a new linking scenario. we have to break the existing link and link to the new person. For now, we create duplicate.
			//updated patient has an EID that matches to a new candidate. Link them, and set the persons possible duplicates
			linkToNewPersonAndFlagAsDuplicate(theResource, updateContext.getExistingPerson(), updateContext.getMatchedPerson(), theEmpiTransactionContext);
		}
	}

	private void handleNoEidsInCommon(IAnyResource theResource, MatchedPersonCandidate theMatchedPersonCandidate, EmpiTransactionContext theEmpiTransactionContext, EmpiUpdateContext theUpdateContext) {
		// the user is simply updating their EID. We propagate this change to the Person.
		//overwrite. No EIDS in common, but still same person.
		if (myEmpiSettings.isPreventMultipleEids()) {
			if (myPersonHelper.getLinkCount(theUpdateContext.getMatchedPerson()) <= 1) { // If there is only 0/1 link on the person, we can safely overwrite the EID.
				handleExternalEidOverwrite(theUpdateContext.getMatchedPerson(), theResource, theEmpiTransactionContext);
			} else { // If the person has multiple patients tied to it, we can't just overwrite the EID, so we split the person.
				createNewPersonAndFlagAsDuplicate(theResource, theEmpiTransactionContext, theUpdateContext.getExistingPerson());
			}
		} else {
			myPersonHelper.handleExternalEidAddition(theUpdateContext.getMatchedPerson(), theResource, theEmpiTransactionContext);
		}
		myEmpiLinkSvc.updateLink(theUpdateContext.getMatchedPerson(), theResource, theMatchedPersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
	}

	private void handleExternalEidOverwrite(IAnyResource thePerson, IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myPersonHelper.overwriteExternalEids(thePerson, eidFromResource);
		}
	}

	private boolean candidateIsSameAsEmpiLinkPerson(EmpiLink theExistingMatchLink, MatchedPersonCandidate thePersonCandidate) {
		return theExistingMatchLink.getSourceResourcePid().equals(thePersonCandidate.getCandidatePersonPid().getIdAsLong());
	}

	private void createNewPersonAndFlagAsDuplicate(IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext, IAnyResource theOldPerson) {
		log(theEmpiTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		IAnyResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		myEmpiLinkSvc.updateLink(newPerson, theOldPerson, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
	}

	private void linkToNewPersonAndFlagAsDuplicate(IAnyResource theResource, IAnyResource theOldPerson, IAnyResource theNewPerson, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, "Changing a match link!");
		myEmpiLinkSvc.deleteLink(theOldPerson, theResource, theEmpiTransactionContext);
		myEmpiLinkSvc.updateLink(theNewPerson, theResource, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		log(theEmpiTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		myEmpiLinkSvc.updateLink(theNewPerson, theOldPerson, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
	}

	private void log(EmpiTransactionContext theEmpiTransactionContext, String theMessage) {
		theEmpiTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}

	/**
	 * Data class to hold context surrounding an update operation for an EMPI target.
	 */
	class EmpiUpdateContext {
		private final boolean myHasEidsInCommon;
		private final boolean myIncomingResourceHasAnEid;
		private IAnyResource myExistingPerson;
		private boolean myRemainsMatchedToSamePerson;

		public IAnyResource getMatchedPerson() {
			return myMatchedPerson;
		}

		private final IAnyResource myMatchedPerson;

		EmpiUpdateContext(MatchedPersonCandidate theMatchedPersonCandidate, IAnyResource theResource) {
			myMatchedPerson = myEmpiPersonFindingSvc.getPersonFromMatchedPersonCandidate(theMatchedPersonCandidate);

			myHasEidsInCommon = myEIDHelper.hasEidOverlap(myMatchedPerson, theResource);
			myIncomingResourceHasAnEid = !myEIDHelper.getExternalEid(theResource).isEmpty();

			Optional<EmpiLink> theExistingMatchLink = myEmpiLinkDaoSvc.getMatchedLinkForTarget(theResource);
			myExistingPerson = null;

			if (theExistingMatchLink.isPresent()) {
				Long existingPersonPid = theExistingMatchLink.get().getSourceResourcePid();
				myExistingPerson =  myEmpiResourceDaoSvc.readPersonByPid(new ResourcePersistentId(existingPersonPid));
				myRemainsMatchedToSamePerson = candidateIsSameAsEmpiLinkPerson(theExistingMatchLink.get(), theMatchedPersonCandidate);
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
