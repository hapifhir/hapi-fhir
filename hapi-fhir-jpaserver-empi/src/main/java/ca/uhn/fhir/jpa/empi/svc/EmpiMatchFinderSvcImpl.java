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

import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.MatchedTarget;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceMatcherSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiCandidateSearchSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpiMatchFinderSvcImpl implements IEmpiMatchFinderSvc {

	@Autowired
	private EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;
	@Autowired
	private EmpiResourceMatcherSvc myEmpiResourceMatcherSvc;

	@Override
	@Nonnull
	public List<MatchedTarget> getMatchedTargets(String theResourceType, IAnyResource theResource) {
		Collection<IAnyResource> targetCandidates = myEmpiCandidateSearchSvc.findCandidates(theResourceType, theResource);

		return targetCandidates.stream()
			.map(candidate -> new MatchedTarget(candidate, myEmpiResourceMatcherSvc.getMatchResult(theResource, candidate)))
			.collect(Collectors.toList());
	}

}
