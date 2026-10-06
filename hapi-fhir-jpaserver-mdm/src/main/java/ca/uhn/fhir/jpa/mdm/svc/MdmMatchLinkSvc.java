/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.mdm.models.FindGoldenResourceCandidatesParams;
import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateList;
import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateStrategyEnum;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MatchedGoldenResourceCandidate;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.TooManyCandidatesException;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockRuleEvaluationSvc;
import ca.uhn.fhir.mdm.dao.IMdmMatchClaimSvc;
import ca.uhn.fhir.mdm.dao.MdmMatchClaimKey;
import ca.uhn.fhir.mdm.dao.NoOpMdmMatchClaimSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.util.SleepUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MdmMatchLinkSvc is the entrypoint for HAPI's MDM system. An incoming resource can call
 * updateMdmLinksForMdmSource and the underlying MDM system will take care of matching it to a GoldenResource,
 * or creating a new GoldenResource if a suitable one was not found.
 * <p>
 * Concurrent processing (several MDM consumers, or several nodes) is made safe with match claims: see
 * {@link IMdmMatchClaimSvc}. Each unit of work claims its source and the external EIDs it carries before it
 * searches for candidates. If the search finds a matching source with no MATCH link yet, which another
 * thread may be processing, the unit of work rolls back and restarts with that source claimed too. A claim
 * conflict, or any other retriable storage failure, rolls back and retries the unit of work.
 */
@Service
public class MdmMatchLinkSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	/**
	 * Restarts allowed because the candidate search found more matching sources to claim. Each restart claims
	 * everything found so far, so this only runs out if new matching sources keep arriving.
	 */
	static final int MAX_CLAIM_RESTARTS = 5;

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

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private IIdHelperService<?> myIdHelperService;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IMdmSettings myMdmSettings;

	@Autowired
	private MdmMatchClaimKeySvc myMdmMatchClaimKeySvc;

	@Autowired(required = false)
	private IMdmMatchClaimSvc myMdmMatchClaimSvc = new NoOpMdmMatchClaimSvc();

	private SleepUtil mySleepUtil = new SleepUtil();

	/**
	 * Given an MDM source (consisting of any supported MDM type), find a suitable Golden Resource candidate for them,
	 * or create one if one does not exist. Performs matching based on rules defined in mdm-rules.json.
	 * Does nothing if resource is determined to be not managed by MDM.
	 * <p>
	 * When called outside a transaction, this runs in its own transactions, restarting and retrying as described
	 * on this class. When called inside an existing transaction, it joins it and can neither restart nor retry,
	 * so a conflict is thrown to the caller as a {@link ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException}.
	 *
	 * @param theResource              the incoming MDM source, which can be any supported MDM type.
	 * @param theMdmTransactionContext
	 * @return an {@link TransactionLogMessages} which contains all informational messages related to MDM processing of this resource.
	 */
	public MdmTransactionContext updateMdmLinksForMdmSource(
			IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		if (!MdmResourceUtil.isMdmAllowed(theResource)) {
			return null;
		}

		if (!myMdmSettings.isMatchClaimsEnabled()) {
			return myTxService.withSystemRequest().execute(() -> doMdmUpdate(theResource, theMdmTransactionContext));
		}

		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			return doMdmUpdateInExistingTransaction(theResource, theMdmTransactionContext);
		}

		IResourcePersistentId<?> sourcePid = myTxService
				.withSystemRequest()
				.readOnly()
				.execute(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theResource));
		if (sourcePid == null) {
			return myTxService.withSystemRequest().execute(() -> doMdmUpdate(theResource, theMdmTransactionContext));
		}
		return doMdmUpdateWithClaims(theResource, sourcePid, theMdmTransactionContext);
	}

	private MdmTransactionContext doMdmUpdateWithClaims(
			IAnyResource theResource,
			IResourcePersistentId<?> theSourcePid,
			MdmTransactionContext theMdmTransactionContext) {
		String resourceType = theResource.getIdElement().getResourceType();
		IAnyResource pristineSource = copyOf(theResource);
		Set<MdmMatchClaimKey> claims =
				new LinkedHashSet<>(myMdmMatchClaimKeySvc.buildInitialClaims(theResource, theSourcePid));
		int maxRetries = myMdmSettings.getMatchConflictMaxRetries();
		int restarts = 0;
		int failures = 0;

		for (int attempt = 0; ; attempt++) {
			MdmTransactionContext.Checkpoint checkpoint = theMdmTransactionContext.createCheckpoint();
			IAnyResource source = attempt == 0 ? theResource : copyOf(pristineSource);
			boolean mayRestart = restarts < MAX_CLAIM_RESTARTS;
			try {
				List<MdmMatchClaimKey> unclaimed = myTxService
						.withSystemRequest()
						.execute(theStatus -> {
							Map<MdmMatchClaimKey, Long> existing = myMdmMatchClaimSvc.findExistingClaims(claims);
							myMdmMatchClaimSvc.claim(claims, theSourcePid, existing);

							CandidateList candidates = findCandidates(source, theMdmTransactionContext);
							List<MdmMatchClaimKey> missing = findUnclaimed(resourceType, candidates, claims);
							if (!missing.isEmpty()) {
								if (mayRestart) {
									theStatus.setRollbackOnly();
									return missing;
								}
								ourLog.warn(
										"MDM processing of {} still found {} unclaimed matching source(s) after {} restarts;"
												+ " proceeding without claiming them",
										source.getIdElement().toUnqualifiedVersionless(),
										missing.size(),
										MAX_CLAIM_RESTARTS);
							}
							applyOutcome(source, candidates, theMdmTransactionContext);
							return List.of();
						});

				if (unclaimed.isEmpty()) {
					return theMdmTransactionContext;
				}
				theMdmTransactionContext.restoreCheckpoint(checkpoint);
				ourLog.debug(
						"MDM processing of {} found {} matching source(s) with no MATCH link; restarting to claim them",
						theResource.getIdElement().toUnqualifiedVersionless(),
						unclaimed.size());
				claims.addAll(unclaimed);
				restarts++;

			} catch (RuntimeException e) {
				theMdmTransactionContext.restoreCheckpoint(checkpoint);
				if (!isRetriable(e) || failures >= maxRetries) {
					throw e;
				}
				failures++;
				ourLog.info(
						"MDM processing of {} hit a conflict ({}); retrying (attempt {} of {})",
						theResource.getIdElement().toUnqualifiedVersionless(),
						e.getMessage(),
						failures,
						maxRetries);
				sleepBeforeRetry(failures);
			}
		}
	}

	/**
	 * The caller's transaction can't be restarted, so any newly found matching sources are claimed in place
	 * and the search is repeated once so that it runs after any competitor holding them has committed.
	 */
	private MdmTransactionContext doMdmUpdateInExistingTransaction(
			IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		IResourcePersistentId<?> sourcePid =
				myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theResource);
		if (sourcePid == null) {
			return doMdmUpdate(theResource, theMdmTransactionContext);
		}
		String resourceType = theResource.getIdElement().getResourceType();
		Set<MdmMatchClaimKey> claims =
				new LinkedHashSet<>(myMdmMatchClaimKeySvc.buildInitialClaims(theResource, sourcePid));
		myMdmMatchClaimSvc.claim(claims, sourcePid, myMdmMatchClaimSvc.findExistingClaims(claims));

		CandidateList candidates = findCandidates(theResource, theMdmTransactionContext);
		List<MdmMatchClaimKey> missing = findUnclaimed(resourceType, candidates, claims);
		if (!missing.isEmpty()) {
			myMdmMatchClaimSvc.claim(missing, sourcePid, myMdmMatchClaimSvc.findExistingClaims(missing));
			candidates = findCandidates(theResource, theMdmTransactionContext);
		}
		applyOutcome(theResource, candidates, theMdmTransactionContext);
		return theMdmTransactionContext;
	}

	private List<MdmMatchClaimKey> findUnclaimed(
			String theResourceType, CandidateList theCandidates, Set<MdmMatchClaimKey> theClaims) {
		List<MdmMatchClaimKey> retVal = new ArrayList<>();
		for (MdmMatchClaimKey next : myMdmMatchClaimKeySvc.buildSourceClaims(
				theResourceType, theCandidates.getUnlinkedMatchedSourcePids())) {
			if (!theClaims.contains(next)) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	private static boolean isRetriable(RuntimeException theException) {
		return !(theException instanceof TooManyCandidatesException)
				&& HapiTransactionService.isRetriable(theException);
	}

	/**
	 * Back off a little more each time, with random jitter so that competing threads don't collide again.
	 */
	private void sleepBeforeRetry(int theFailureCount) {
		long sleepMillis = (long) (100.0d * theFailureCount * (0.5d + Math.random()));
		mySleepUtil.sleepAtLeast(sleepMillis, false);
	}

	/**
	 * Copies the source for a retry. An attempt can change the source in memory (for example by adding a
	 * HAPI EID), and a rolled-back attempt must not leak those changes into the next one.
	 */
	@Nonnull
	private IAnyResource copyOf(IAnyResource theResource) {
		IAnyResource retVal = myFhirContext.newTerser().clone(theResource);
		retVal.setId(theResource.getIdElement());
		retVal.setUserData(Constants.RESOURCE_PARTITION_ID, theResource.getUserData(Constants.RESOURCE_PARTITION_ID));
		retVal.setUserData(IDao.RESOURCE_PID_KEY, theResource.getUserData(IDao.RESOURCE_PID_KEY));
		return retVal;
	}

	private MdmTransactionContext doMdmUpdate(
			IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		CandidateList candidateList = findCandidates(theResource, theMdmTransactionContext);
		applyOutcome(theResource, candidateList, theMdmTransactionContext);
		return theMdmTransactionContext;
	}

	private CandidateList findCandidates(IAnyResource theResource, MdmTransactionContext theMdmTransactionContext) {
		/*
		 * If a resource is blocked, we will not conduct
		 * MDM matching. But we will still create golden resources
		 * (so that future resources may match to it).
		 */
		boolean isResourceBlocked = myBlockRuleEvaluationSvc.isMdmMatchingBlocked(theResource);
		// we will mark the golden resource special for this
		theMdmTransactionContext.setIsBlocked(isResourceBlocked);

		if (isResourceBlocked) {
			// we require a candidatestrategy, but it doesn't matter
			// because empty lists are effectively no matches
			// (and so the candidate strategy doesn't matter)
			return new CandidateList(CandidateStrategyEnum.ANY);
		}
		FindGoldenResourceCandidatesParams params =
				new FindGoldenResourceCandidatesParams(theResource, theMdmTransactionContext);
		return myMdmGoldenResourceFindingSvc.findGoldenResourceCandidates(params);
	}

	private void applyOutcome(
			IAnyResource theResource, CandidateList theCandidateList, MdmTransactionContext theMdmTransactionContext) {
		if (theMdmTransactionContext.getIsBlocked() || theCandidateList.isEmpty()) {
			handleMdmWithNoCandidates(theResource, theMdmTransactionContext);
		} else if (theCandidateList.exactlyOneMatch()) {
			handleMdmWithSingleCandidate(theResource, theCandidateList.getOnlyMatch(), theMdmTransactionContext);
		} else {
			handleMdmWithMultipleCandidates(theResource, theCandidateList, theMdmTransactionContext);
		}
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
				MdmMatchOutcome outcome = MdmMatchOutcome.possibleDuplicate(theCandidateList.isEidMatch());
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
			// This branch is reached only because the two resources carry different external EIDs, so the
			// duplicate is by definition an EID-based determination.
			myMdmLinkSvc.updateLink(
					newGoldenResource,
					goldenResource,
					MdmMatchOutcome.possibleDuplicate(true),
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
