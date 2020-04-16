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

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.MatchedTargetCandidate;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpiMatchFinderSvc implements IEmpiMatchFinderSvc {
	@Autowired
	private EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;
	@Autowired
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;

	@Override
	@Nonnull
	public List<MatchedTargetCandidate> getMatchedTargetCandidates(String theResourceType, IBaseResource theBaseResource) {
		Collection<IBaseResource> targetCandidates = myEmpiCandidateSearchSvc.findCandidates(theResourceType, theBaseResource);

		return targetCandidates.stream()
			.map(candidate -> new MatchedTargetCandidate(candidate, myEmpiResourceComparatorSvc.getMatchResult(theBaseResource, candidate)))
			.collect(Collectors.toList());
	}

	@Override
	@Nonnull
	public Collection<IBaseResource> findMatches(String theResourceType, IBaseResource theResource) {
		List<MatchedTargetCandidate> targetCandidates = getMatchedTargetCandidates(theResourceType, theResource);
		return targetCandidates.stream()
			.filter(candidate -> candidate.getMatchResult() == EmpiMatchResultEnum.MATCH)
			.map(MatchedTargetCandidate::getCandidate)
			.collect(Collectors.toList());
	}
}
