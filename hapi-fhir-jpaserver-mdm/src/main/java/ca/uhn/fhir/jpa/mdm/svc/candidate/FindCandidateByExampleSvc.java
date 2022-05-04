package ca.uhn.fhir.jpa.mdm.svc.candidate;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FindCandidateByExampleSvc extends BaseCandidateFinder {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();
	@Autowired
	IJpaIdHelperService myIdHelperService;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

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

		List<Long> goldenResourcePidsToExclude = getNoMatchGoldenResourcePids(theTarget);

		List<MatchedTarget> matchedCandidates = myMdmMatchFinderSvc.getMatchedTargets(myFhirContext.getResourceType(theTarget), theTarget, (RequestPartitionId) theTarget.getUserData(Constants.RESOURCE_PARTITION_ID));

		// Convert all possible match targets to their equivalent Golden Resources by looking up in the MdmLink table,
		// while ensuring that the matches aren't in our NO_MATCH list.
		// The data flow is as follows ->
		// MatchedTargetCandidate -> Golden Resource -> MdmLink -> MatchedGoldenResourceCandidate
		matchedCandidates = matchedCandidates.stream().filter(mc -> mc.isMatch() || mc.isPossibleMatch()).collect(Collectors.toList());
		for (MatchedTarget match : matchedCandidates) {
			Optional<MdmLink> optionalMdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(match.getTarget()));
			if (!optionalMdmLink.isPresent()) {
				continue;
			}

			MdmLink matchMdmLink = optionalMdmLink.get();
			if (goldenResourcePidsToExclude.contains(matchMdmLink.getGoldenResourcePid())) {
				ourLog.info("Skipping MDM on candidate Golden Resource with PID {} due to manual NO_MATCH", matchMdmLink.getGoldenResourcePid());
				continue;
			}

			MatchedGoldenResourceCandidate candidate = new MatchedGoldenResourceCandidate(getResourcePersistentId(matchMdmLink.getGoldenResourcePid()), match.getMatchResult());
			retval.add(candidate);
		}
		return retval;
	}

	private List<Long> getNoMatchGoldenResourcePids(IBaseResource theBaseResource) {
		Long targetPid = myIdHelperService.getPidOrNull(theBaseResource);
		return myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(targetPid, MdmMatchResultEnum.NO_MATCH)
			.stream()
			.map(MdmLink::getGoldenResourcePid)
			.collect(Collectors.toList());
	}

	private ResourcePersistentId getResourcePersistentId(Long theGoldenResourcePid) {
		return new ResourcePersistentId(theGoldenResourcePid);
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.SCORE;
	}
}
