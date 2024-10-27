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
package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FindCandidateByExampleSvc<P extends IResourcePersistentId> extends BaseCandidateFinder {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	IIdHelperService<P> myIdHelperService;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private MdmLinkDaoSvc<P, IMdmLink<P>> myMdmLinkDaoSvc;

	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

	@Autowired
	MdmPartitionHelper myMdmPartitionHelper;

	/**
	 * Attempt to find matching Golden Resources by resolving them from similar Matching target resources. Runs MDM logic
	 * over the existing target resources, then finds their entries in the MdmLink table, and returns all the matches
	 * found therein.
	 *
	 * @param theTarget the {@link IBaseResource} which we want to find candidate Golden Resources for.
	 * @return an Optional list of {@link MatchedGoldenResourceCandidate} indicating matches.
	 */
	@Override
	protected List<MatchedGoldenResourceCandidate> findMatchGoldenResourceCandidates(IAnyResource theTarget) {
		List<MatchedGoldenResourceCandidate> retval = new ArrayList<>();

		List<P> goldenResourcePidsToExclude = getNoMatchGoldenResourcePids(theTarget);

		List<MatchedTarget> matchedCandidates = myMdmMatchFinderSvc.getMatchedTargets(
				myFhirContext.getResourceType(theTarget),
				theTarget,
				myMdmPartitionHelper.getRequestPartitionIdFromResourceForSearch(theTarget));

		// Convert all possible match targets to their equivalent Golden Resources by looking up in the MdmLink table,
		// while ensuring that the matches aren't in our NO_MATCH list.
		// The data flow is as follows ->
		// MatchedTargetCandidate -> Golden Resource -> MdmLink -> MatchedGoldenResourceCandidate
		matchedCandidates = matchedCandidates.stream()
				.filter(mc -> mc.isMatch() || mc.isPossibleMatch())
				.collect(Collectors.toList());
		List<String> skippedLogMessages = new ArrayList<>();
		List<String> matchedLogMessages = new ArrayList<>();

		// we'll track the added ids so we don't add the same resources twice
		// note, all these resources are the same type, so we only need the Long value
		Set<String> currentIds = new HashSet<>();
		for (MatchedTarget match : matchedCandidates) {
			Optional<? extends IMdmLink> optionalMdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(
					myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), match.getTarget()));
			if (!optionalMdmLink.isPresent()) {
				if (ourLog.isDebugEnabled()) {
					skippedLogMessages.add(String.format(
							"%s does not link to a Golden Resource (it may be a Golden Resource itself).  Removing candidate.",
							match.getTarget().getIdElement().toUnqualifiedVersionless()));
				}
				continue;
			}

			IMdmLink matchMdmLink = optionalMdmLink.get();
			if (goldenResourcePidsToExclude.contains(matchMdmLink.getGoldenResourcePersistenceId())) {
				skippedLogMessages.add(String.format(
						"Skipping MDM on candidate Golden Resource with PID %s due to manual NO_MATCH",
						matchMdmLink.getGoldenResourcePersistenceId().toString()));
				continue;
			}

			MatchedGoldenResourceCandidate candidate = new MatchedGoldenResourceCandidate(
					matchMdmLink.getGoldenResourcePersistenceId(), match.getMatchResult());

			if (ourLog.isDebugEnabled()) {
				matchedLogMessages.add(String.format(
						"Navigating from matched resource %s to its Golden Resource %s",
						match.getTarget().getIdElement().toUnqualifiedVersionless(),
						matchMdmLink.getGoldenResourcePersistenceId().toString()));
			}

			// only add if it's not already in the list
			// NB: we cannot use hash of IResourcePersistentId because
			// BaseResourcePersistentId overrides this (and so is the same
			// for any class with the same version) :(
			if (currentIds.add(
					String.valueOf(candidate.getCandidateGoldenResourcePid().getId()))) {
				retval.add(candidate);
			}
		}

		if (ourLog.isDebugEnabled()) {
			for (String logMessage : skippedLogMessages) {
				ourLog.debug(logMessage);
			}
			for (String logMessage : matchedLogMessages) {
				ourLog.debug(logMessage);
			}
		}
		return retval;
	}

	private List<P> getNoMatchGoldenResourcePids(IBaseResource theBaseResource) {
		P targetPid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theBaseResource);
		return myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(targetPid, MdmMatchResultEnum.NO_MATCH).stream()
				.map(IMdmLink::getGoldenResourcePersistenceId)
				.collect(Collectors.toList());
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.SCORE;
	}
}
