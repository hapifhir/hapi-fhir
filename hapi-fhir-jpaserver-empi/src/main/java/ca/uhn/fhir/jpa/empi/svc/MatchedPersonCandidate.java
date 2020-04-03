package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;

public class MatchedPersonCandidate {

	private final ResourcePersistentId myCandidatePersonPid;
	private final EmpiLink myEmpiLink;

	public MatchedPersonCandidate(ResourcePersistentId theCandidate, EmpiLink theEmpiLink) {
		myCandidatePersonPid = theCandidate;
		myEmpiLink = theEmpiLink;
	}

	private MatchedPersonCandidate(ResourcePersistentId theCandidate) {
		myCandidatePersonPid = theCandidate;
		myEmpiLink = null;
	}

	public ResourcePersistentId getCandidatePersonPid() {
		return myCandidatePersonPid;
	}

	public EmpiLink getEmpiLink() {
		return myEmpiLink;
	}
}
