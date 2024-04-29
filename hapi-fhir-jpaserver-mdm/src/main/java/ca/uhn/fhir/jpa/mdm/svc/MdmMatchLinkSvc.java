/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.mdm.models.FindGoldenResourceCandidatesParams;
import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateList;
import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateStrategyEnum;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockRuleEvaluationSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Autowired
	private IBlockRuleEvaluationSvc myBlockRuleEvaluationSvc;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

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
	public MdmTransactionContext updateMdmLinksForMdmSource(
			IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		if (MdmResourceUtil.isMdmAllowed(theResource)) {
			return doMdmUpdate(theResource, theMdmTransactionContext);
		} else {
			return null;
		}
	}

	private MdmTransactionContext doMdmUpdate(
			IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		// we initialize to an empty list
		// we require a candidatestrategy, but it doesn't matter
		// because empty lists are effectively no matches
		// (and so the candidate strategy doesn't matter)
		CandidateList candidateList = new CandidateList(CandidateStrategyEnum.ANY);

		/*
		 * If a resource is blocked, we will not conduct
		 * MDM matching. But we will still create golden resources
		 * (so that future resources may match to it).
		 */
		boolean isResourceBlocked = myBlockRuleEvaluationSvc.isMdmMatchingBlocked(theResource);
		// we will mark the golden resource special for this
		theMdmTransactionContext.setIsBlocked(isResourceBlocked);

		if (!isResourceBlocked) {
			FindGoldenResourceCandidatesParams params =
					new FindGoldenResourceCandidatesParams(theResource, theMdmTransactionContext);
			candidateList = myMdmGoldenResourceFindingSvc.findGoldenResourceCandidates(params);
		}

		if (isResourceBlocked || candidateList.isEmpty()) {
			handleMdmWithNoCandidates(theResource, theMdmTransactionContext);
		} else if (candidateList.exactlyOneMatch()) {
			handleMdmWithSingleCandidate(theResource, candidateList.getOnlyMatch(), theMdmTransactionContext);
		} else {
			handleMdmWithMultipleCandidates(theResource, candidateList, theMdmTransactionContext);
		}
		return theMdmTransactionContext;
	}

	private void handleMdmWithMultipleCandidates(
			IAnyResource theResource, CandidateList theCandidateList, MdmTransactionContext theMdmTransactionContext) {
		MatchedGoldenResourceCandidate firstMatch = theCandidateList.getFirstMatch();
		IResourcePersistentId<?> sampleGoldenResourcePid = firstMatch.getCandidateGoldenResourcePid();
		boolean allSameGoldenResource = theCandidateList.stream()
				.allMatch(candidate -> candidate.getCandidateGoldenResourcePid().equals(sampleGoldenResourcePid));

		if (allSameGoldenResource) {
			log(
					theMdmTransactionContext,
					"MDM received multiple match candidates, but they are all linked to the same Golden Resource.");
			handleMdmWithSingleCandidate(theResource, firstMatch, theMdmTransactionContext);
		} else {
			log(
					theMdmTransactionContext,
					"MDM received multiple match candidates, that were linked to different Golden Resources. Setting POSSIBLE_DUPLICATES and POSSIBLE_MATCHES.");

			// Set them all as POSSIBLE_MATCH
			List<IAnyResource> goldenResources =
					createPossibleMatches(theResource, theCandidateList, theMdmTransactionContext);

			// Set all GoldenResources as POSSIBLE_DUPLICATE of the last GoldenResource.
			IAnyResource firstGoldenResource = goldenResources.get(0);

			goldenResources.subList(1, goldenResources.size()).forEach(possibleDuplicateGoldenResource -> {
				MdmMatchOutcome outcome = MdmMatchOutcome.POSSIBLE_DUPLICATE;
				outcome.setEidMatch(theCandidateList.isEidMatch());
				myMdmLinkSvc.updateLink(
						firstGoldenResource,
						possibleDuplicateGoldenResource,
						outcome,
						MdmLinkSourceEnum.AUTO,
						theMdmTransactionContext);
			});
		}
	}

	private List<IAnyResource> createPossibleMatches(
			IAnyResource theResource, CandidateList theCandidateList, MdmTransactionContext theMdmTransactionContext) {
		List<IAnyResource> goldenResources = new ArrayList<>();

		for (MatchedGoldenResourceCandidate matchedGoldenResourceCandidate : theCandidateList.getCandidates()) {
			IAnyResource goldenResource =
					myMdmGoldenResourceFindingSvc.getGoldenResourceFromMatchedGoldenResourceCandidate(
							matchedGoldenResourceCandidate, theMdmTransactionContext.getResourceType());

			MdmMatchOutcome outcome = new MdmMatchOutcome(
							matchedGoldenResourceCandidate.getMatchResult().getVector(),
							matchedGoldenResourceCandidate.getMatchResult().getScore())
					.setMdmRuleCount(
							matchedGoldenResourceCandidate.getMatchResult().getMdmRuleCount());

			outcome.setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_MATCH);
			outcome.setEidMatch(theCandidateList.isEidMatch());
			myMdmLinkSvc.updateLink(
					goldenResource, theResource, outcome, MdmLinkSourceEnum.AUTO, theMdmTransactionContext);
			goldenResources.add(goldenResource);
		}

		return goldenResources;
	}

	private void handleMdmWithNoCandidates(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		log(
				theMdmTransactionContext,
				String.format(
						"There were no matched candidates for MDM, creating a new %s Golden Resource.",
						theResource.getIdElement().getResourceType()));
		IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
				theResource, theMdmTransactionContext, myMdmSurvivorshipService);
		// TODO GGG :)
		// 1. Get the right helper
		// 2. Create source resource for the MDM source
		// 3. UPDATE MDM LINK TABLE

		myMdmLinkSvc.updateLink(
				newGoldenResource,
				theResource,
				MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH,
				MdmLinkSourceEnum.AUTO,
				theMdmTransactionContext);
	}

	private void handleMdmCreate(
			IAnyResource theTargetResource,
			MatchedGoldenResourceCandidate theGoldenResourceCandidate,
			MdmTransactionContext theMdmTransactionContext) {
		IAnyResource goldenResource = myMdmGoldenResourceFindingSvc.getGoldenResourceFromMatchedGoldenResourceCandidate(
				theGoldenResourceCandidate, theMdmTransactionContext.getResourceType());

		if (myGoldenResourceHelper.isPotentialDuplicate(goldenResource, theTargetResource)) {
			log(
					theMdmTransactionContext,
					"Duplicate detected based on the fact that both resources have different external EIDs.");
			IAnyResource newGoldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
					theTargetResource, theMdmTransactionContext, myMdmSurvivorshipService);

			myMdmLinkSvc.updateLink(
					newGoldenResource,
					theTargetResource,
					MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH,
					MdmLinkSourceEnum.AUTO,
					theMdmTransactionContext);
			myMdmLinkSvc.updateLink(
					newGoldenResource,
					goldenResource,
					MdmMatchOutcome.POSSIBLE_DUPLICATE,
					MdmLinkSourceEnum.AUTO,
					theMdmTransactionContext);
		} else {
			log(theMdmTransactionContext, "MDM has narrowed down to one candidate for matching.");

			if (theGoldenResourceCandidate.isMatch()) {
				myGoldenResourceHelper.handleExternalEidAddition(
						goldenResource, theTargetResource, theMdmTransactionContext);
				myEidUpdateService.applySurvivorshipRulesAndSaveGoldenResource(
						theTargetResource, goldenResource, theMdmTransactionContext);
			}

			myMdmLinkSvc.updateLink(
					goldenResource,
					theTargetResource,
					theGoldenResourceCandidate.getMatchResult(),
					MdmLinkSourceEnum.AUTO,
					theMdmTransactionContext);
		}
	}

	private void handleMdmWithSingleCandidate(
			IAnyResource theResource,
			MatchedGoldenResourceCandidate theGoldenResourceCandidate,
			MdmTransactionContext theMdmTransactionContext) {
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
