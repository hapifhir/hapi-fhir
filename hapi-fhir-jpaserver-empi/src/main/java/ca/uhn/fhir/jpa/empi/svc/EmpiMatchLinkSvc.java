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
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	 * @param theEmpiTransactionContext
	 * @return an {@link TransactionLogMessages} which contains all informational messages related to EMPI processing of this resource.
	 */
	public EmpiTransactionContext updateEmpiLinksForEmpiTarget(IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext) {
		if (EmpiUtil.isEmpiAccessible(theResource)) {
			return doEmpiUpdate(theResource, theEmpiTransactionContext);
		} else {
			return null;
		}
	}

	private EmpiTransactionContext doEmpiUpdate(IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext) {
		List<MatchedPersonCandidate> personCandidates = myEmpiPersonFindingSvc.findPersonCandidates(theResource);
		if (personCandidates.isEmpty()) {
			handleEmpiWithNoCandidates(theResource, theEmpiTransactionContext);
		} else if (personCandidates.size() == 1) {
			handleEmpiWithSingleCandidate(theResource, personCandidates, theEmpiTransactionContext);
		} else {
			handleEmpiWithMultipleCandidates(theResource, personCandidates, theEmpiTransactionContext);
		}
		return theEmpiTransactionContext;
	}

	private void handleEmpiWithMultipleCandidates(IAnyResource theResource, List<MatchedPersonCandidate> thePersonCandidates, EmpiTransactionContext theEmpiTransactionContext) {
		Long samplePersonPid = thePersonCandidates.get(0).getCandidatePersonPid().getIdAsLong();
		boolean allSamePerson = thePersonCandidates.stream()
			.allMatch(candidate -> candidate.getCandidatePersonPid().getIdAsLong().equals(samplePersonPid));

		if (allSamePerson) {
			log(theEmpiTransactionContext, "EMPI received multiple match candidates, but they are all linked to the same person.");
			handleEmpiWithSingleCandidate(theResource, thePersonCandidates, theEmpiTransactionContext);
		} else {
			log(theEmpiTransactionContext, "EMPI received multiple match candidates, that were linked to different Persons. Setting POSSIBLE_DUPLICATES and POSSIBLE_MATCHES.");
			//Set them all as POSSIBLE_MATCH
			List<IAnyResource> persons = thePersonCandidates.stream().map(this::getPersonFromMatchedPersonCandidate).collect(Collectors.toList());
				persons.forEach(person -> {
					myEmpiLinkSvc.updateLink(person, theResource, EmpiMatchResultEnum.POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
				});

			//Set all Persons as POSSIBLE_DUPLICATE of the first person.
			IAnyResource samplePerson = persons.get(0);
			persons.subList(1, persons.size()).stream()
				.forEach(possibleDuplicatePerson -> {
					myEmpiLinkSvc.updateLink(samplePerson, possibleDuplicatePerson, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
				});
		}
	}

	private void handleEmpiWithNoCandidates(IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, "There were no matched candidates for EMPI, creating a new Person.");
		IAnyResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
	}

	private void handleEmpiCreate(IAnyResource theResource, MatchedPersonCandidate thePersonCandidate, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, "EMPI has narrowed down to one candidate for matching.");
		IAnyResource person = getPersonFromMatchedPersonCandidate(thePersonCandidate);
		if (myPersonHelper.isPotentialDuplicate(person, theResource)) {
			log(theEmpiTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
			IAnyResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
			myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
			myEmpiLinkSvc.updateLink(newPerson, person, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		} else {
			if (thePersonCandidate.isMatch()) {
				handleExternalEidAddition(person, theResource);
			}
			myEmpiLinkSvc.updateLink(person, theResource, thePersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		}
	}

	private void handleEmpiWithSingleCandidate(IAnyResource theResource, List<MatchedPersonCandidate> thePersonCandidates, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, "EMPI has narrowed down to one candidate for matching.");
		MatchedPersonCandidate matchedPersonCandidate = thePersonCandidates.get(0);
		if (theEmpiTransactionContext.getRestOperation().equals(EmpiTransactionContext.OperationType.UPDATE)) {
			handleEmpiUpdate(theResource, matchedPersonCandidate, theEmpiTransactionContext);
		} else {
			handleEmpiCreate(theResource, matchedPersonCandidate, theEmpiTransactionContext);
		}
	}

	private void handleEmpiUpdate(IAnyResource theResource, MatchedPersonCandidate theMatchedPersonCandidate, EmpiTransactionContext theEmpiTransactionContext) {
		IAnyResource person = getPersonFromMatchedPersonCandidate(theMatchedPersonCandidate);
		boolean hasEidsInCommon = myEIDHelper.hasEidOverlap(person, theResource);
		boolean incomingResourceHasAnEid = !myEIDHelper.getExternalEid(theResource).isEmpty();
		Optional<EmpiLink> theExistingMatchLink = myEmpiLinkDaoSvc.getMatchedLinkForTarget(theResource);

		boolean remainsMatchedToSamePerson;
		if (theExistingMatchLink.isPresent()) {
			remainsMatchedToSamePerson = candidateIsSameAsEmpiLinkPerson(theExistingMatchLink.get(), theMatchedPersonCandidate);
		} else {
			remainsMatchedToSamePerson = false;
		}

		if (remainsMatchedToSamePerson && (!incomingResourceHasAnEid || hasEidsInCommon)) {
			//update to patient that uses internal EIDs only.
			myPersonHelper.updatePersonFromEmpiTarget(person, theResource, theEmpiTransactionContext);
			myEmpiLinkSvc.updateLink(person, theResource, theMatchedPersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		}
		if (!hasEidsInCommon && remainsMatchedToSamePerson) {
			// the user is simply updating their EID. We propagate this change to the Person.
			//overwrite. No EIDS in common, but still same person.
			if (theMatchedPersonCandidate.isMatch()) {
				handleExternalEidOverwrite(person, theResource);
			}
			myEmpiLinkSvc.updateLink(person, theResource, theMatchedPersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		} else if (!hasEidsInCommon && !remainsMatchedToSamePerson) {
			//This is a new linking scenario. we have to break the existing link and link to the new person. For now, we create duplicate.
			createNewPersonAndFlagAsDuplicate(theResource, theEmpiTransactionContext, person);
		} else if (hasEidsInCommon && !remainsMatchedToSamePerson) {
			//updated patient has an EID that matches to a new candidate. Link?
		}
	}

	private void handleExternalEidOverwrite(IBaseResource thePerson, IBaseResource theResource) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myPersonHelper.overwriteExternalEids(thePerson, eidFromResource);
		}
	}

	private boolean candidateIsSameAsEmpiLinkPerson(EmpiLink theOExistingMatchLink, MatchedPersonCandidate thePersonCandidate) {
		return theOExistingMatchLink.getPersonPid().equals(thePersonCandidate.getCandidatePersonPid().getIdAsLong());
	}

	private void createNewPersonAndFlagAsDuplicate(IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext, IAnyResource thePerson) {
		log(theEmpiTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
		IAnyResource newPerson = myPersonHelper.createPersonFromEmpiTarget(theResource);
		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		myEmpiLinkSvc.updateLink(newPerson, thePerson, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
	}

	private IAnyResource getPersonFromMatchedPersonCandidate(MatchedPersonCandidate theMatchedPersonCandidate) {
		ResourcePersistentId personPid = theMatchedPersonCandidate.getCandidatePersonPid();
		return myEmpiResourceDaoSvc.readPersonByPid(personPid);
	}

	private void handleExternalEidAddition(IBaseResource thePerson, IBaseResource theResource) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theResource);
		if (!eidFromResource.isEmpty()) {
			myPersonHelper.updatePersonExternalEidFromEmpiTarget(thePerson, theResource);
		}
	}

	private void log(EmpiTransactionContext theEmpiTransactionContext, String theMessage) {
		theEmpiTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
