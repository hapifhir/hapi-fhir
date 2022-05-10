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

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindCandidateByLinkSvc extends BaseCandidateFinder {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	/**
	 * Attempt to find a currently matching Golden Resource, based on the presence of an {@link MdmLink} entity.
	 *
	 * @param theTarget the {@link IAnyResource} that we want to find candidate Golden Resources for.
	 * @return an Optional list of {@link MatchedGoldenResourceCandidate} indicating matches.
	 */
	@Override
	protected List<MatchedGoldenResourceCandidate> findMatchGoldenResourceCandidates(IAnyResource theTarget) {
		List<MatchedGoldenResourceCandidate> retval = new ArrayList<>();

		Long targetPid = myIdHelperService.getPidOrNull(theTarget);
		if (targetPid != null) {
			Optional<MdmLink> oLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(targetPid);
			if (oLink.isPresent()) {
				ResourcePersistentId goldenResourcePid = new ResourcePersistentId(oLink.get().getGoldenResourcePid());
				ourLog.debug("Resource previously linked. Using existing link.");
					retval.add(new MatchedGoldenResourceCandidate(goldenResourcePid, oLink.get()));
			}
		}
		return retval;
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.LINK;
	}
}
