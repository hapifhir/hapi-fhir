package ca.uhn.fhir.jpa.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public class MatchedTargetCandidate {

	private final IBaseResource myCandidate;
	private final EmpiMatchResultEnum myMatchResult;

	public MatchedTargetCandidate(IBaseResource theCandidate, EmpiMatchResultEnum theMatchResult) {
		myCandidate = theCandidate;
		myMatchResult = theMatchResult;
	}

	private IBaseResource getCandidate() {
		return myCandidate;
	}

	private EmpiMatchResultEnum getMatchResult() {
		return myMatchResult;
	}
}
