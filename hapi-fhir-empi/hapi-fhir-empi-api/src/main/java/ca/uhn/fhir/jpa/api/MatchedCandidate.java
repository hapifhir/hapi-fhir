package ca.uhn.fhir.jpa.api;

import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class MatchedCandidate {

	private final IBaseResource myCandidate;
	private final EmpiMatchResultEnum myMatchResult;

	public MatchedCandidate(IBaseResource theCandidate, EmpiMatchResultEnum theMatchResult) {
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
