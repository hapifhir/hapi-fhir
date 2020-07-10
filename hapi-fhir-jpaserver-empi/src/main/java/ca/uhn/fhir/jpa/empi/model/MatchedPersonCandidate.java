package ca.uhn.fhir.jpa.empi.model;

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
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

public class MatchedPersonCandidate {

	private final ResourcePersistentId myCandidatePersonPid;
	private final EmpiMatchResultEnum myEmpiMatchResult;

	public MatchedPersonCandidate(ResourcePersistentId theCandidate, EmpiMatchResultEnum theEmpiMatchResult) {
		myCandidatePersonPid = theCandidate;
		myEmpiMatchResult = theEmpiMatchResult;
	}

	public ResourcePersistentId getCandidatePersonPid() {
		return myCandidatePersonPid;
	}

	public EmpiMatchResultEnum getMatchResult() {
		return myEmpiMatchResult;
	}

	public boolean isMatch() {
		return myEmpiMatchResult == EmpiMatchResultEnum.MATCH;
	}
}
