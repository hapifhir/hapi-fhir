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
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.svc.candidate.CandidateList;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiSourceResourceFindingSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.MatchedSourceResourceCandidate;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * EmpiMatchLinkSvc is the entrypoint for HAPI's EMPI system. An incoming resource can call
 * updateEmpiLinksForEmpiTarget and the underlying EMPI system will take care of matching it to a person, or creating a
 * new Person if a suitable one was not found.
 */
@Service
public class EmpiMatchLinkSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EmpiSourceResourceFindingSvc myEmpiSourceResourceFindingSvc;
	@Autowired
	private PersonHelper myPersonHelper;
	@Autowired
	private EmpiEidUpdateService myEidUpdateService;

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
		CandidateList candidateList = myEmpiSourceResourceFindingSvc.findSourceResourceCandidates(theResource);

		if (candidateList.isEmpty()) {
			handleEmpiWithNoCandidates(theResource, theEmpiTransactionContext);
		} else if (candidateList.exactlyOneMatch()) {
			handleEmpiWithSingleCandidate(theResource, candidateList.getOnlyMatch(), theEmpiTransactionContext);
		} else {
			handleEmpiWithMultipleCandidates(theResource, candidateList, theEmpiTransactionContext);
		}
		return theEmpiTransactionContext;
	}

	private void handleEmpiWithMultipleCandidates(IAnyResource theResource, CandidateList theCandidateList, EmpiTransactionContext theEmpiTransactionContext) {
		MatchedSourceResourceCandidate firstMatch = theCandidateList.getFirstMatch();
		Long samplePersonPid = firstMatch.getCandidatePersonPid().getIdAsLong();
		boolean allSamePerson = theCandidateList.stream()
			.allMatch(candidate -> candidate.getCandidatePersonPid().getIdAsLong().equals(samplePersonPid));

		if (allSamePerson) {
			log(theEmpiTransactionContext, "EMPI received multiple match candidates, but they are all linked to the same person.");
			handleEmpiWithSingleCandidate(theResource, firstMatch, theEmpiTransactionContext);
		} else {
			log(theEmpiTransactionContext, "EMPI received multiple match candidates, that were linked to different Persons. Setting POSSIBLE_DUPLICATES and POSSIBLE_MATCHES.");
			//Set them all as POSSIBLE_MATCH
			List<IAnyResource> persons = new ArrayList<>();
			for (MatchedSourceResourceCandidate matchedSourceResourceCandidate : theCandidateList.getCandidates()) {
				IAnyResource person = myEmpiSourceResourceFindingSvc
					.getSourceResourceFromMatchedSourceResourceCandidate(matchedSourceResourceCandidate, theEmpiTransactionContext.getResourceType());
				EmpiMatchOutcome outcome = EmpiMatchOutcome.POSSIBLE_MATCH;
				outcome.setEidMatch(theCandidateList.isEidMatch());
				myEmpiLinkSvc.updateLink(person, theResource, outcome, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
				persons.add(person);
			}

			//Set all Persons as POSSIBLE_DUPLICATE of the last person.
			IAnyResource firstPerson = persons.get(0);
			persons.subList(1, persons.size())
				.forEach(possibleDuplicatePerson -> {
					EmpiMatchOutcome outcome = EmpiMatchOutcome.POSSIBLE_DUPLICATE;
					outcome.setEidMatch(theCandidateList.isEidMatch());
					myEmpiLinkSvc.updateLink(firstPerson, possibleDuplicatePerson, outcome, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
				});
		}
	}

	private void handleEmpiWithNoCandidates(IAnyResource theResource, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, String.format("There were no matched candidates for EMPI, creating a new %s.", theResource.getIdElement().getResourceType()));
		IAnyResource newPerson = myPersonHelper.createSourceResourceFromEmpiTarget(theResource);
		// TODO GGG :)
		// 1. Get the right helper
		// 2. Create source resoruce for the EMPI target
		// 3. UPDATE EMPI LINK TABLE

		myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
	}

	private void handleEmpiCreate(IAnyResource theResource, MatchedSourceResourceCandidate thePersonCandidate, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, "EMPI has narrowed down to one candidate for matching.");
		IAnyResource person = myEmpiSourceResourceFindingSvc.getSourceResourceFromMatchedSourceResourceCandidate(thePersonCandidate, theEmpiTransactionContext.getResourceType());
		if (myPersonHelper.isPotentialDuplicate(person, theResource)) {
			log(theEmpiTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
			IAnyResource newPerson = myPersonHelper.createSourceResourceFromEmpiTarget(theResource);
			myEmpiLinkSvc.updateLink(newPerson, theResource, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
			myEmpiLinkSvc.updateLink(newPerson, person, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		} else {
			if (thePersonCandidate.isMatch()) {
				myPersonHelper.handleExternalEidAddition(person, theResource, theEmpiTransactionContext);
				//
				// myPersonHelper.updatePersonFromNewlyCreatedEmpiTarget(person, theResource, theEmpiTransactionContext);
			}
			myEmpiLinkSvc.updateLink(person, theResource, thePersonCandidate.getMatchResult(), EmpiLinkSourceEnum.AUTO, theEmpiTransactionContext);
		}
	}

	private void handleEmpiWithSingleCandidate(IAnyResource theResource, MatchedSourceResourceCandidate thePersonCandidate, EmpiTransactionContext theEmpiTransactionContext) {
		log(theEmpiTransactionContext, "EMPI has narrowed down to one candidate for matching.");
		if (theEmpiTransactionContext.getRestOperation().equals(EmpiTransactionContext.OperationType.UPDATE_RESOURCE)) {
			myEidUpdateService.handleEmpiUpdate(theResource, thePersonCandidate, theEmpiTransactionContext);
		} else {
			handleEmpiCreate(theResource, thePersonCandidate, theEmpiTransactionContext);
		}
	}

	private void log(EmpiTransactionContext theEmpiTransactionContext, String theMessage) {
		theEmpiTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
