package ca.uhn.fhir.jpa.empi.model;

import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class CandidateList {
	private final boolean myEidMatch;
	private final List<MatchedPersonCandidate> myList = new ArrayList<>();

	public static CandidateList newEidMatchCandidateList() {
		return new CandidateList(true);
	}

	public static CandidateList newCandidateList() {
		return new CandidateList(false);
	}

	private CandidateList(Boolean theEidMatch) {
		myEidMatch = theEidMatch;
	}

	public static CandidateList newCandidateListFromPersonPidAndEmpiLink(ResourcePersistentId thePersonPid, EmpiLink theEmpiLink) {
		CandidateList candidateList = new CandidateList(theEmpiLink.getEidMatch());
		candidateList.add(new MatchedPersonCandidate(thePersonPid, theEmpiLink.getMatchResult()));
		return candidateList;
	}

	public boolean isEidMatch() {
		return myEidMatch;
	}

	public boolean isEmpty() {
		return myList.isEmpty();
	}

	public void add(MatchedPersonCandidate theMpc) {
		myList.add(theMpc);
	}

	public MatchedPersonCandidate getOnlyMatch() {
		assert myList.size() == 1;
		return myList.get(0);
	}

	public boolean exactlyOneMatch() {
		return myList.size()== 1;
	}

	public Stream<MatchedPersonCandidate> stream() {
		return myList.stream();
	}

	public MatchedPersonCandidate getFirstMatch() {
		return myList.get(0);
	}
}
