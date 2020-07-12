package ca.uhn.fhir.jpa.empi.svc.candidate;

import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CandidateList {
	private final CandidateStrategyEnum mySource;
	private final List<MatchedPersonCandidate> myList = new ArrayList<>();

	public CandidateList(CandidateStrategyEnum theSource) {
		mySource = theSource;
	}

	public CandidateStrategyEnum getSource() {
		return mySource;
	}

	public boolean isEmpty() {
		return myList.isEmpty();
	}

	// FIXME KHS
	public void add(MatchedPersonCandidate theMpc) {
		myList.add(theMpc);
	}

	public void addAll(List<MatchedPersonCandidate> theList) { myList.addAll(theList); }

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

	public boolean isEid() {
		return mySource == CandidateStrategyEnum.EID;
	}
}
