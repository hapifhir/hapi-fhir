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
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmUtil;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateList;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedSourceResourceCandidate;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * MdmMatchLinkSvc is the entrypoint for HAPI's MDM system. An incoming resource can call
 * updateMdmLinksForMdmTarget and the underlying MDM system will take care of matching it to a person, or creating a
 * new Person if a suitable one was not found.
 */
@Service
public class MdmMatchLinkSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	private MdmGoldenResourceFindingSvc myMdmGoldenResourceFindingSvc;
	@Autowired
	private GoldenResourceHelper myGoldenResourceHelper;
	@Autowired
	private MdmEidUpdateService myEidUpdateService;

	/**
	 * Given an MDM Target (consisting of either a Patient or a Practitioner), find a suitable Person candidate for them,
	 * or create one if one does not exist. Performs matching based on rules defined in empi-rules.json.
	 * Does nothing if resource is determined to be not managed by MDM.
	 *
	 * @param theResource the incoming MDM target, which is either a Patient or Practitioner.
	 * @param theMdmTransactionContext
	 * @return an {@link TransactionLogMessages} which contains all informational messages related to MDM processing of this resource.
	 */
	public MdmTransactionContext updateMdmLinksForMdmTarget(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		if (MdmUtil.isMdmAllowed(theResource)) {
			return doMdmUpdate(theResource, theMdmTransactionContext);
		} else {
			return null;
		}
	}

	private MdmTransactionContext doMdmUpdate(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		CandidateList candidateList = myMdmGoldenResourceFindingSvc.findSourceResourceCandidates(theResource);

		if (candidateList.isEmpty()) {
			handleMdmWithNoCandidates(theResource, theMdmTransactionContext);
		} else if (candidateList.exactlyOneMatch()) {
			handleMdmWithSingleCandidate(theResource, candidateList.getOnlyMatch(), theMdmTransactionContext);
		} else {
			handleMdmWithMultipleCandidates(theResource, candidateList, theMdmTransactionContext);
		}
		return theMdmTransactionContext;
	}

	private void handleMdmWithMultipleCandidates(IAnyResource theResource, CandidateList theCandidateList, MdmTransactionContext theMdmTransactionContext) {
		MatchedSourceResourceCandidate firstMatch = theCandidateList.getFirstMatch();
		Long samplePersonPid = firstMatch.getCandidatePersonPid().getIdAsLong();
		boolean allSamePerson = theCandidateList.stream()
			.allMatch(candidate -> candidate.getCandidatePersonPid().getIdAsLong().equals(samplePersonPid));

		if (allSamePerson) {
			log(theMdmTransactionContext, "MDM received multiple match candidates, but they are all linked to the same person.");
			handleMdmWithSingleCandidate(theResource, firstMatch, theMdmTransactionContext);
		} else {
			log(theMdmTransactionContext, "MDM received multiple match candidates, that were linked to different Persons. Setting POSSIBLE_DUPLICATES and POSSIBLE_MATCHES.");
			//Set them all as POSSIBLE_MATCH
			List<IAnyResource> persons = new ArrayList<>();
			for (MatchedSourceResourceCandidate matchedSourceResourceCandidate : theCandidateList.getCandidates()) {
				IAnyResource person = myMdmGoldenResourceFindingSvc
					.getSourceResourceFromMatchedSourceResourceCandidate(matchedSourceResourceCandidate, theMdmTransactionContext.getResourceType());
				MdmMatchOutcome outcome = MdmMatchOutcome.POSSIBLE_MATCH;
				outcome.setEidMatch(theCandidateList.isEidMatch());
				myMdmLinkSvc.updateLink(person, theResource, outcome, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
				persons.add(person);
			}

			//Set all Persons as POSSIBLE_DUPLICATE of the last person.
			IAnyResource firstPerson = persons.get(0);
			persons.subList(1, persons.size())
				.forEach(possibleDuplicatePerson -> {
					MdmMatchOutcome outcome = MdmMatchOutcome.POSSIBLE_DUPLICATE;
					outcome.setEidMatch(theCandidateList.isEidMatch());
					myMdmLinkSvc.updateLink(firstPerson, possibleDuplicatePerson, outcome, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
				});
		}
	}

	private void handleMdmWithNoCandidates(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, String.format("There were no matched candidates for MDM, creating a new %s.", theResource.getIdElement().getResourceType()));
		IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmTarget(theResource);
		// TODO GGG :)
		// 1. Get the right helper
		// 2. Create source resoruce for the MDM target
		// 3. UPDATE MDM LINK TABLE

		myMdmLinkSvc.updateLink(newGoldenResource, theResource, MdmMatchOutcome.NEW_PERSON_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void handleMdmCreate(IAnyResource theTargetResource, MatchedSourceResourceCandidate thePersonCandidate, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, "MDM has narrowed down to one candidate for matching.");
		IAnyResource sourceResource = myMdmGoldenResourceFindingSvc.getSourceResourceFromMatchedSourceResourceCandidate(thePersonCandidate, theMdmTransactionContext.getResourceType());

		if (myGoldenResourceHelper.isPotentialDuplicate(sourceResource, theTargetResource)) {
			log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
			IAnyResource newSourceResource = myGoldenResourceHelper.createGoldenResourceFromMdmTarget(theTargetResource);
			myMdmLinkSvc.updateLink(newSourceResource, theTargetResource, MdmMatchOutcome.NEW_PERSON_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
			myMdmLinkSvc.updateLink(newSourceResource, sourceResource, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		} else {
			if (thePersonCandidate.isMatch()) {
				myGoldenResourceHelper.handleExternalEidAddition(sourceResource, theTargetResource, theMdmTransactionContext);
				//TODO MDM GGG/NG: eventually we need to add survivorship rules of attributes here. Currently no data is copied over except EIDs.
			}
			myMdmLinkSvc.updateLink(sourceResource, theTargetResource, thePersonCandidate.getMatchResult(), MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		}
	}

	private void handleMdmWithSingleCandidate(IAnyResource theResource, MatchedSourceResourceCandidate thePersonCandidate, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, "MDM has narrowed down to one candidate for matching.");
		if (theMdmTransactionContext.getRestOperation().equals(MdmTransactionContext.OperationType.UPDATE_RESOURCE)) {
			myEidUpdateService.handleMdmUpdate(theResource, thePersonCandidate, theMdmTransactionContext);
		} else {
			handleMdmCreate(theResource, thePersonCandidate, theMdmTransactionContext);
		}
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
