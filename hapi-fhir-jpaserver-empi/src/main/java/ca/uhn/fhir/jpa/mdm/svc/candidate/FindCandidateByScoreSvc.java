package ca.uhn.fhir.jpa.mdm.svc.candidate;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
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
public class FindCandidateByScoreSvc extends BaseCandidateFinder {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	private MdmLinkDaoSvc myMdmLinkDaoSvc;
	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

	/**
	 * Attempt to find matching Persons by resolving them from similar Matching target resources, where target resource
	 * can be either Patient or Practitioner. Runs MDM logic over the existing Patient/Practitioners, then finds their
	 * entries in the MdmLink table, and returns all the matches found therein.
	 *
	 * @param theTarget the {@link IBaseResource} which we want to find candidate Persons for.
	 * @return an Optional list of {@link MatchedSourceResourceCandidate} indicating matches.
	 */
	@Override
	protected List<MatchedSourceResourceCandidate> findMatchSourceResourceCandidates(IAnyResource theTarget) {
		List<MatchedSourceResourceCandidate> retval = new ArrayList<>();

		List<Long> personPidsToExclude = getNoMatchPersonPids(theTarget);

		List<MatchedTarget> matchedCandidates = myMdmMatchFinderSvc.getMatchedTargets(myFhirContext.getResourceType(theTarget), theTarget);

		//Convert all possible match targets to their equivalent Persons by looking up in the MdmLink table,
		//while ensuring that the matches aren't in our NO_MATCH list.
		// The data flow is as follows ->
		// MatchedTargetCandidate -> Person -> MdmLink -> MatchedPersonCandidate
		matchedCandidates = matchedCandidates.stream().filter(mc -> mc.isMatch() || mc.isPossibleMatch()).collect(Collectors.toList());
		for (MatchedTarget match : matchedCandidates) {
			Optional<MdmLink> optionalMdmLink = myMdmLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(match.getTarget()));
			if (!optionalMdmLink.isPresent()) {
				continue;
			}

			MdmLink matchMdmLink = optionalMdmLink.get();
			if (personPidsToExclude.contains(matchMdmLink.getGoldenResourcePid())) {
				ourLog.info("Skipping MDM on candidate person with PID {} due to manual NO_MATCH", matchMdmLink.getGoldenResourcePid());
				continue;
			}

			MatchedSourceResourceCandidate candidate = new MatchedSourceResourceCandidate(getResourcePersistentId(matchMdmLink.getGoldenResourcePid()), match.getMatchResult());
			retval.add(candidate);
		}
		return retval;
	}

	private List<Long> getNoMatchPersonPids(IBaseResource theBaseResource) {
		Long targetPid = myIdHelperService.getPidOrNull(theBaseResource);
		return myMdmLinkDaoSvc.getMdmLinksByTargetPidAndMatchResult(targetPid, MdmMatchResultEnum.NO_MATCH)
			.stream()
			.map(MdmLink::getGoldenResourcePid)
			.collect(Collectors.toList());
	}

	private ResourcePersistentId getResourcePersistentId(Long thePersonPid) {
		return new ResourcePersistentId(thePersonPid);
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.SCORE;
	}
}
