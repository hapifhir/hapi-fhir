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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateSearcher.idOrType;

@Service
public class MdmMatchFinderSvcImpl implements IMdmMatchFinderSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmCandidateSearchSvc myMdmCandidateSearchSvc;

	@Autowired
	private MdmResourceMatcherSvc myMdmResourceMatcherSvc;

	@Override
	@Nonnull
	@Transactional
	public List<MatchedTarget> getMatchedTargets(
			String theResourceType, IAnyResource theResource, RequestPartitionId theRequestPartitionId) {
		Collection<IAnyResource> targetCandidates =
				myMdmCandidateSearchSvc.findCandidates(theResourceType, theResource, theRequestPartitionId);

		List<MatchedTarget> matches = targetCandidates.stream()
				.map(candidate ->
						new MatchedTarget(candidate, myMdmResourceMatcherSvc.getMatchResult(theResource, candidate)))
				.collect(Collectors.toList());

		ourLog.trace("Found {} matched targets for {}.", matches.size(), idOrType(theResource, theResourceType));
		return matches;
	}
}
