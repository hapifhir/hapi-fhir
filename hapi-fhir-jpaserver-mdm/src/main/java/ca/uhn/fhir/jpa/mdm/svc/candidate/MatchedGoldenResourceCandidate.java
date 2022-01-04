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

import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

public class MatchedGoldenResourceCandidate {

	private final ResourcePersistentId myCandidateGoldenResourcePid;
	private final MdmMatchOutcome myMdmMatchOutcome;

	public MatchedGoldenResourceCandidate(ResourcePersistentId theCandidate, MdmMatchOutcome theMdmMatchOutcome) {
		myCandidateGoldenResourcePid = theCandidate;
		myMdmMatchOutcome = theMdmMatchOutcome;
	}

	public MatchedGoldenResourceCandidate(ResourcePersistentId theGoldenResourcePid, MdmLink theMdmLink) {
		myCandidateGoldenResourcePid = theGoldenResourcePid;
		myMdmMatchOutcome = new MdmMatchOutcome(theMdmLink.getVector(), theMdmLink.getScore()).setMatchResultEnum(theMdmLink.getMatchResult());
	}

	public ResourcePersistentId getCandidateGoldenResourcePid() {
		return myCandidateGoldenResourcePid;
	}

	public MdmMatchOutcome getMatchResult() {
		return myMdmMatchOutcome;
	}

	public boolean isMatch() {
		return myMdmMatchOutcome.isMatch();
	}
}
