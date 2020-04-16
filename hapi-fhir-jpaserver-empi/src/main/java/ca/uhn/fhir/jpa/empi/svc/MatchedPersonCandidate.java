package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;

public class MatchedPersonCandidate {

	private final ResourcePersistentId myCandidatePersonPid;
	private final EmpiMatchResultEnum myEmpiMatchResultEnum;

	public MatchedPersonCandidate(ResourcePersistentId theCandidate, EmpiMatchResultEnum theEmpiMatchResultEnum) {
		myCandidatePersonPid = theCandidate;
		myEmpiMatchResultEnum = theEmpiMatchResultEnum;
	}

	private MatchedPersonCandidate(ResourcePersistentId theCandidate) {
		myCandidatePersonPid = theCandidate;
		myEmpiMatchResultEnum = null;
	}

	public ResourcePersistentId getCandidatePersonPid() {
		return myCandidatePersonPid;
	}

	public EmpiMatchResultEnum getMatchResult() {
		return myEmpiMatchResultEnum;
	}

}
