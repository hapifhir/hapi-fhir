package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;

public class MatchedPersonCandidate {

	private final ResourceTable myCandidate;
	private final EmpiLink myEmpiLink;

	public MatchedPersonCandidate(ResourceTable theCandidate, EmpiLink theEmpiLink) {
		myCandidate = theCandidate;
		myEmpiLink = theEmpiLink;
	}

	private MatchedPersonCandidate(ResourceTable theCandidate) {
		myCandidate = theCandidate;
		myEmpiLink = null;
	}

	public ResourceTable getCandidate() {
		return myCandidate;
	}

	public EmpiLink getEmpiLink() {
		return myEmpiLink;
	}
}
