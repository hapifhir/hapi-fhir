package ca.uhn.fhir.jpa.empi.svc.candidate;

import java.util.ArrayList;
import java.util.Collections;
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

	public List<MatchedPersonCandidate> getCandidates() {
		return Collections.unmodifiableList(myList);
	}

	public MatchedPersonCandidate getFirstMatch() {
		return myList.get(0);
	}
}
