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

import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateList;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * MdmMatchLinkSvc is the entrypoint for HAPI's MDM system. An incoming resource can call
 * updateMdmLinksForMdmSource and the underlying MDM system will take care of matching it to a GoldenResource,
 * or creating a new GoldenResource if a suitable one was not found.
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
	 * Given an MDM source (consisting of any supported MDM type), find a suitable Golden Resource candidate for them,
	 * or create one if one does not exist. Performs matching based on rules defined in mdm-rules.json.
	 * Does nothing if resource is determined to be not managed by MDM.
	 *
	 * @param theResource              the incoming MDM source, which can be any supported MDM type.
	 * @param theMdmTransactionContext
	 * @return an {@link TransactionLogMessages} which contains all informational messages related to MDM processing of this resource.
	 */
	@Transactional
	public MdmTransactionContext updateMdmLinksForMdmSource(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		if (MdmResourceUtil.isMdmAllowed(theResource)) {
			return doMdmUpdate(theResource, theMdmTransactionContext);
		} else {
			return null;
		}
	}

	private MdmTransactionContext doMdmUpdate(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		CandidateList candidateList = myMdmGoldenResourceFindingSvc.findGoldenResourceCandidates(theResource);

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
		MatchedGoldenResourceCandidate firstMatch = theCandidateList.getFirstMatch();
		Long sampleGoldenResourcePid = firstMatch.getCandidateGoldenResourcePid().getIdAsLong();
		boolean allSameGoldenResource = theCandidateList.stream()
			.allMatch(candidate -> candidate.getCandidateGoldenResourcePid().getIdAsLong().equals(sampleGoldenResourcePid));

		if (allSameGoldenResource) {
			log(theMdmTransactionContext, "MDM received multiple match candidates, but they are all linked to the same Golden Resource.");
			handleMdmWithSingleCandidate(theResource, firstMatch, theMdmTransactionContext);
		} else {
			log(theMdmTransactionContext, "MDM received multiple match candidates, that were linked to different Golden Resources. Setting POSSIBLE_DUPLICATES and POSSIBLE_MATCHES.");

			//Set them all as POSSIBLE_MATCH
			List<IAnyResource> goldenResources = new ArrayList<>();
			for (MatchedGoldenResourceCandidate matchedGoldenResourceCandidate : theCandidateList.getCandidates()) {
				IAnyResource goldenResource = myMdmGoldenResourceFindingSvc
					.getGoldenResourceFromMatchedGoldenResourceCandidate(matchedGoldenResourceCandidate, theMdmTransactionContext.getResourceType());
				MdmMatchOutcome outcome = MdmMatchOutcome.POSSIBLE_MATCH;
				outcome.setEidMatch(theCandidateList.isEidMatch());
				myMdmLinkSvc.updateLink(goldenResource, theResource, outcome, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
				goldenResources.add(goldenResource);
			}

			//Set all GoldenResources as POSSIBLE_DUPLICATE of the last GoldenResource.
			IAnyResource firstGoldenResource = goldenResources.get(0);

			goldenResources.subList(1, goldenResources.size())
				.forEach(possibleDuplicateGoldenResource -> {
					MdmMatchOutcome outcome = MdmMatchOutcome.POSSIBLE_DUPLICATE;
					outcome.setEidMatch(theCandidateList.isEidMatch());
					myMdmLinkSvc.updateLink(firstGoldenResource, possibleDuplicateGoldenResource, outcome, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
				});
		}
	}

	private void handleMdmWithNoCandidates(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		log(theMdmTransactionContext, String.format("There were no matched candidates for MDM, creating a new %s.", theResource.getIdElement().getResourceType()));
		IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(theResource, theMdmTransactionContext);
		// TODO GGG :)
		// 1. Get the right helper
		// 2. Create source resource for the MDM source
		// 3. UPDATE MDM LINK TABLE

		myMdmLinkSvc.updateLink(newGoldenResource, theResource, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
	}

	private void handleMdmCreate(IAnyResource theTargetResource, MatchedGoldenResourceCandidate theGoldenResourceCandidate, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource goldenResource = myMdmGoldenResourceFindingSvc.getGoldenResourceFromMatchedGoldenResourceCandidate(theGoldenResourceCandidate, theMdmTransactionContext.getResourceType());

		if (myGoldenResourceHelper.isPotentialDuplicate(goldenResource, theTargetResource)) {
			log(theMdmTransactionContext, "Duplicate detected based on the fact that both resources have different external EIDs.");
			IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(theTargetResource, theMdmTransactionContext);

			myMdmLinkSvc.updateLink(newGoldenResource, theTargetResource, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
			myMdmLinkSvc.updateLink(newGoldenResource, goldenResource, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		} else {
			log(theMdmTransactionContext, "MDM has narrowed down to one candidate for matching.");

			if (theGoldenResourceCandidate.isMatch()) {
				myGoldenResourceHelper.handleExternalEidAddition(goldenResource, theTargetResource, theMdmTransactionContext);
				myEidUpdateService.applySurvivorshipRulesAndSaveGoldenResource(theTargetResource, goldenResource, theMdmTransactionContext);
			}

			myMdmLinkSvc.updateLink(goldenResource, theTargetResource, theGoldenResourceCandidate.getMatchResult(), MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
		}
	}

	private void handleMdmWithSingleCandidate(IAnyResource theResource, MatchedGoldenResourceCandidate theGoldenResourceCandidate, MdmTransactionContext theMdmTransactionContext) {
		if (theMdmTransactionContext.getRestOperation().equals(MdmTransactionContext.OperationType.UPDATE_RESOURCE)) {
			log(theMdmTransactionContext, "MDM has narrowed down to one candidate for matching.");
			myEidUpdateService.handleMdmUpdate(theResource, theGoldenResourceCandidate, theMdmTransactionContext);
		} else {
			handleMdmCreate(theResource, theGoldenResourceCandidate, theMdmTransactionContext);
		}
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}
}
