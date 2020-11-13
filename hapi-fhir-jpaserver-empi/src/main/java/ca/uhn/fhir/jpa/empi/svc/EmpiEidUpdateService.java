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
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiSourceResourceFindingSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.MatchedSourceResourceCandidate;
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
	private EmpiSourceResourceFindingSvc myEmpiSourceResourceFindingSvc;
	@Autowired
	private PersonHelper myPersonHelper;
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	private IEmpiSettings myEmpiSettings;

	void handleEmpiUpdate(IAnyResource theResource, MatchedSourceResourceCandidate theMatchedSourceResourceCandidate, MdmTransactionContext theMdmTransactionContext) {

		EmpiUpdateContext updateContext = new EmpiUpdateContext(theMatchedSourceResourceCandidate, theResource);

		if (updateContext.isRemainsMatchedToSamePerson()) {
			// Copy over any new external EIDs which don't already exist.
			// TODO NG - Eventually this call will use terser to clone data in, once the surviorship rules for copying data will be confirmed
			// myPersonHelper.updatePersonFromUpdatedEmpiTarget(updateContext.getMatchedPerson(), theResource, theEmpiTransactionContext);
			if (!updateContext.isIncomingResourceHasAnEid() || updateContext.isHasEidsInCommon()) {
				//update to patient that uses internal EIDs only.
				myEmpiLinkSvc.updateLink(updateContext.getMatchedSourceResource(), theResource, theMatchedSourceResourceCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theMdmTransactionContext);
			} else if (!updateContext.isHasEidsInCommon()) {
				handleNoEidsInCommon(theResource, theMatchedSourceResourceCandidate, theMdmTransactionContext, updateContext);
			}
		} else {
			//This is a new linking scenario. we have to break the existing link and link to the new person. For now, we create duplicate.
			//updated patient has an EID that matches to a new candidate. Link them, and set the persons possible duplicates
			linkToNewPersonAndFlagAsDuplicate(theResource, updateContext.getExistingPerson(), updateContext.getMatchedSourceResource(), theMdmTransactionContext);
		}
	}

	private void handleNoEidsInCommon(IAnyResource theResource, MatchedSourceResourceCandidate theMatchedSourceResourceCandidate, MdmTransactionContext theMdmTransactionContext, EmpiUpdateContext theUpdateContext) {
		// the user is simply updating their EID. We propagate this change to the Person.
		//overwrite. No EIDS in common, but still same person.
		if (myEmpiSettings.isPreventMultipleEids()) {
			if (myEmpiLinkDaoSvc.findEmpiMatchLinksBySource(theUpdateContext.getMatchedSourceResource()).size() <= 1) { // If there is only 0/1 link on the person, we can safely overwrite the EID.
			// if (myPersonHelper.getLinkCount(theUpdateContext.getMatchedSourceResource()) <= 1) { // If there is only 0/1 link on the person, we can safely overwrite the EID.
				handleExternalEidOverwrite(theUpdateContext.getMatchedSourceResource(), theResource, theMdmTransactionContext);
			} else { // If the person has multiple patients tied to it, we can't just overwrite the EID, so we split the person.
				createNewPersonAndFlagAsDuplicate(theResource, theMdmTransactionContext, theUpdateContext.getExistingPerson());
			}
		} else {
			myPersonHelper.handleExternalEidAddition(theUpdateContext.getMatchedSourceResource(), theResource, theMdmTransactionContext);
		}
		myEmpiLinkSvc.updateLink(theUpdateContext.getMatchedSourceResource(), theResource, theMatchedSourceResourceCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void handleExternalEidOverwrite(IAnyResource thePerson, IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myPersonHelper.overwriteExternalEids(thePerson, eidFromResource);
		}
	}

	private boolean candidateIsSameAsEmpiLinkPerson(EmpiLink theExistingMatchLink, MatchedSourceResourceCandidate thePersonCandidate) {
		return theExistingMatchLink.getSourceResourcePid().equals(thePersonCandidate.getCandidatePersonPid().getIdAsLong());
	}

	private void createNewPersonAndFlagAsDuplicate(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext, IAnyResource theOldPerson) {
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		IAnyResource newPerson = myPersonHelper.createGoldenResourceFromMdmTarget(theResource);

		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, theMdmTransactionContext);
		myEmpiLinkSvc.updateLink(newPerson, theOldPerson, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void linkToNewPersonAndFlagAsDuplicate(IAnyResource theResource, IAnyResource theOldPerson, IAnyResource theNewPerson, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, "Changing a match link!");
		myEmpiLinkSvc.deleteLink(theOldPerson, theResource, theMdmTransactionContext);
		myEmpiLinkSvc.updateLink(theNewPerson, theResource, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, theMdmTransactionContext);
		log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		myEmpiLinkSvc.updateLink(theNewPerson, theOldPerson, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
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

		public IAnyResource getMatchedSourceResource() {
			return myMatchedSourceResource;
		}

		private final IAnyResource myMatchedSourceResource;

		EmpiUpdateContext(MatchedSourceResourceCandidate theMatchedSourceResourceCandidate, IAnyResource theResource) {
			final String resourceType = theResource.getIdElement().getResourceType();
			myMatchedSourceResource = myEmpiSourceResourceFindingSvc.getSourceResourceFromMatchedSourceResourceCandidate(theMatchedSourceResourceCandidate, resourceType);

			myHasEidsInCommon = myEIDHelper.hasEidOverlap(myMatchedSourceResource, theResource);
			myIncomingResourceHasAnEid = !myEIDHelper.getExternalEid(theResource).isEmpty();

			Optional<EmpiLink> theExistingMatchLink = myEmpiLinkDaoSvc.getMatchedLinkForTarget(theResource);
			myExistingPerson = null;

			if (theExistingMatchLink.isPresent()) {
				EmpiLink empiLink = theExistingMatchLink.get();
				Long existingPersonPid = empiLink.getSourceResourcePid();
				myExistingPerson =  myEmpiResourceDaoSvc.readSourceResourceByPid(new ResourcePersistentId(existingPersonPid), resourceType);
				myRemainsMatchedToSamePerson = candidateIsSameAsEmpiLinkPerson(empiLink, theMatchedSourceResourceCandidate);
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
