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
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EmpiMatchLinkSvc is the entrypoint for HAPI's EMPI system. An incoming resource can call
 * updateEmpiLinksForEmpiTarget and the underlying EMPI system will take care of matching it to a person, or creating a
 * new Person if a suitable one was not found.
 */
@Service
public class EmpiMatchLinkSvc {
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

	/**
	 * Given an Empi Target (consisting of either a Patient or a Practitioner), find a suitable Person candidate for them,
	 * or create one if one does not exist. Performs matching based on rules defined in empi-rules.json.
	 * Does nothing if resource is determined to be not managed by EMPI.
	 *
	 * @param theResource the incoming EMPI target, which is either a Patient or Practitioner.
	 * @param theTransactionLogMessages
	 * @return an {@link TransactionLogMessages} which contains all informational messages related to EMPI processing of this resource.
	 */
	public TransactionLogMessages updateEmpiLinksForEmpiTarget(IBaseResource theResource, @Nullable TransactionLogMessages theTransactionLogMessages) {
		if (EmpiUtil.isManagedByEmpi(theResource)) {
			return doEmpiUpdate(theResource, theTransactionLogMessages);
		} else {
			return null;
		}
	}

	private TransactionLogMessages doEmpiUpdate(IBaseResource theResource, TransactionLogMessages theTransactionLogMessages) {
		List<MatchedPersonCandidate> personCandidates = myEmpiPersonFindingSvc.findPersonCandidates(theResource);
		if (personCandidates.isEmpty()) {
			handleEmpiWithNoCandidates(theResource, theTransactionLogMessages);
		} else if (personCandidates.size() == 1) {
			handleEmpiWithSingleCandidate(theResource, personCandidates, theTransactionLogMessages);
		} else {
			handleEmpiWithMultipleCandidates(theResource, personCandidates, theTransactionLogMessages);
		}
		return theTransactionLogMessages;
	}

	private void handleEmpiWithMultipleCandidates(IBaseResource theResource, List<MatchedPersonCandidate> thePersonCandidates, TransactionLogMessages theMessages) {
		Long samplePersonPid = thePersonCandidates.get(0).getCandidatePersonPid().getIdAsLong();
		boolean allSamePerson = thePersonCandidates.stream()
			.allMatch(candidate -> candidate.getCandidatePersonPid().getIdAsLong().equals(samplePersonPid));

		if (allSamePerson) {
			log(theMessages, "EMPI received multiple match candidates, but they are all linked to the same person.");
			handleEmpiWithSingleCandidate(theResource, thePersonCandidates, theMessages);
		} else {
			log(theMessages, "EMPI received multiple match candidates, that were linked to different Persons. Setting POSSIBLE_DUPLICATES and POSSIBLE_MATCHES.");
			//Set them all as POSSIBLE_MATCH
			List<IBaseResource> persons = thePersonCandidates.stream().map(this::getPersonFromMatchedPersonCandidate).collect(Collectors.toList());
				persons.forEach(person -> {
					myEmpiLinkSvc.updateLink(person, theResource, EmpiMatchResultEnum.POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, theMessages);
				});

			//Set all Persons as POSSIBLE_DUPLICATE of the first person.
			IBaseResource samplePerson = persons.get(0);
			persons.subList(1, persons.size()).stream()
				.forEach(possibleDuplicatePerson -> {
					myEmpiLinkSvc.updateLink(samplePerson, possibleDuplicatePerson, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theMessages);
				});
		}
	}

	private void handleEmpiWithNoCandidates(IBaseResource theResource, @Nullable TransactionLogMessages theMessages) {
		log(theMessages, "There were no matched candidates for EMPI, creating a new Person.");
		IBaseResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO, theMessages);
	}

	private void handleEmpiWithSingleCandidate(IBaseResource theResource, List<MatchedPersonCandidate> thePersonCandidates, @Nullable TransactionLogMessages theMessages) {
		log(theMessages, "EMPI has narrowed down to one candidate for matching.");
		MatchedPersonCandidate matchedPersonCandidate = thePersonCandidates.get(0);
		IBaseResource person = getPersonFromMatchedPersonCandidate(matchedPersonCandidate);

		Optional<EmpiLink> oExistingMatchLink = myEmpiLinkDaoSvc.getMatchedLinkForTarget(theResource);
		boolean matchHasChangedOrIsNew = matchHasChanged(oExistingMatchLink, matchedPersonCandidate);
		boolean hasEidsInCommon = myEIDHelper.eidMatchExists(person, theResource);

		if (!hasEidsInCommon){
			if (matchHasChangedOrIsNew) {
				//We matched to this person without being matched to them before, and we have a changed EID. Duplicate!
				createNewPersonAndFlagAsDuplicate(theResource, theMessages, person);
			} else {
				// the user is simply updating their EID. We propagate this change to the Person.
				//If there are multiple patients attached, we add.
				//handleExternalEidAddition(person, theResource);
				//If there is one patient attached, we overwrite.
				if (matchedPersonCandidate.getMatchResult().equals(EmpiMatchResultEnum.MATCH)) {
					handleExternalEidOverwrite(person, theResource);
				}
			}
		} else {
			if (matchedPersonCandidate.getMatchResult().equals(EmpiMatchResultEnum.MATCH)) {
				handleExternalEidAddition(person, theResource);
			}
		}
		myEmpiLinkSvc.updateLink(person, theResource, matchedPersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theMessages);
	}

	private void handleExternalEidOverwrite(IBaseResource thePerson, IBaseResource theResource) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myPersonHelper.overwriteExternalEids(thePerson, eidFromResource);
		}
	}

	private boolean matchHasChanged(Optional<EmpiLink> theOExistingMatchLink, MatchedPersonCandidate thePersonCandidate) {
		return !theOExistingMatchLink.isPresent()
			|| !theOExistingMatchLink.get().getPersonPid().equals(thePersonCandidate.getCandidatePersonPid().getIdAsLong());
	}

	private void createNewPersonAndFlagAsDuplicate(IBaseResource theResource, @Nullable TransactionLogMessages theMessages, IBaseResource thePerson) {
		log(theMessages, "Duplicate detected based on the fact that both resources have different external EIDs.");
		IBaseResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO, theMessages);
		myEmpiLinkSvc.updateLink(newPerson, thePerson, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theMessages);
	}

	private IBaseResource getPersonFromMatchedPersonCandidate(MatchedPersonCandidate theMatchedPersonCandidate) {
		ResourcePersistentId personPid = theMatchedPersonCandidate.getCandidatePersonPid();
		return myEmpiResourceDaoSvc.readPersonByPid(personPid);
	}

	private void handleExternalEidAddition(IBaseResource thePerson, IBaseResource theResource) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myPersonHelper.updatePersonExternalEidFromEmpiTarget(thePerson, theResource);
		}
	}

	private void log(@Nullable TransactionLogMessages theMessages, String theMessage) {
		TransactionLogMessages.addMessage(theMessages, theMessage);
		ourLog.debug(theMessage);
	}
}
