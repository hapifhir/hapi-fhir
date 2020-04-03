package ca.uhn.fhir.jpa.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public class MatchedTargetCandidate {

	private final IBaseResource myCandidate;
	private final EmpiMatchResultEnum myMatchResult;

	public MatchedTargetCandidate(IBaseResource theCandidate, EmpiMatchResultEnum theMatchResult) {
		myCandidate = theCandidate;
		myMatchResult = theMatchResult;
	}

	public IBaseResource getCandidate() {
		return myCandidate;
	}

	public EmpiMatchResultEnum getMatchResult() {
		return myMatchResult;
	}
}
