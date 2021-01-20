package ca.uhn.fhir.jpa.empi.svc.candidate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

public class MatchedPersonCandidate {
	private final ResourcePersistentId myCandidatePersonPid;
	private final EmpiMatchOutcome myEmpiMatchOutcome;

	public MatchedPersonCandidate(ResourcePersistentId theCandidate, EmpiMatchOutcome theEmpiMatchOutcome) {
		myCandidatePersonPid = theCandidate;
		myEmpiMatchOutcome = theEmpiMatchOutcome;
	}

	public MatchedPersonCandidate(ResourcePersistentId thePersonPid, EmpiLink theEmpiLink) {
		myCandidatePersonPid = thePersonPid;
		myEmpiMatchOutcome = new EmpiMatchOutcome(theEmpiLink.getVector(), theEmpiLink.getScore()).setMatchResultEnum(theEmpiLink.getMatchResult());
	}

	public ResourcePersistentId getCandidatePersonPid() {
		return myCandidatePersonPid;
	}

	public EmpiMatchOutcome getMatchResult() {
		return myEmpiMatchOutcome;
	}

	public boolean isMatch() {
		return myEmpiMatchOutcome.isMatch();
	}
}
