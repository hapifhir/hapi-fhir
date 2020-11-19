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

import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

public class MatchedSourceResourceCandidate {

	private final ResourcePersistentId myCandidateSourceResourcePid;
	private final MdmMatchOutcome myMdmMatchOutcome;

	public MatchedSourceResourceCandidate(ResourcePersistentId theCandidate, MdmMatchOutcome theMdmMatchOutcome) {
		myCandidateSourceResourcePid = theCandidate;
		myMdmMatchOutcome = theMdmMatchOutcome;
	}

	public MatchedSourceResourceCandidate(ResourcePersistentId thePersonPid, MdmLink theMdmLink) {
		myCandidateSourceResourcePid = thePersonPid;
		myMdmMatchOutcome = new MdmMatchOutcome(theMdmLink.getVector(), theMdmLink.getScore()).setMatchResultEnum(theMdmLink.getMatchResult());
	}

	public ResourcePersistentId getCandidatePersonPid() {
		return myCandidateSourceResourcePid;
	}

	public MdmMatchOutcome getMatchResult() {
		return myMdmMatchOutcome;
	}

	public boolean isMatch() {
		return myMdmMatchOutcome.isMatch();
	}
}
