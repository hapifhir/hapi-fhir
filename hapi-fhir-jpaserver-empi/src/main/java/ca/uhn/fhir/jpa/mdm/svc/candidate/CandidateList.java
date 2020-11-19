package ca.uhn.fhir.jpa.mdm.svc.candidate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CandidateList {
	private final CandidateStrategyEnum myStrategy;
	private final List<MatchedSourceResourceCandidate> myList = new ArrayList<>();

	public CandidateList(CandidateStrategyEnum theStrategy) {
		myStrategy = theStrategy;
	}

	public CandidateStrategyEnum getStrategy() {
		return myStrategy;
	}

	public boolean isEmpty() {
		return myList.isEmpty();
	}

	public void addAll(List<MatchedSourceResourceCandidate> theList) { myList.addAll(theList); }

	public MatchedSourceResourceCandidate getOnlyMatch() {
		assert myList.size() == 1;
		return myList.get(0);
	}

	public boolean exactlyOneMatch() {
		return myList.size()== 1;
	}

	public Stream<MatchedSourceResourceCandidate> stream() {
		return myList.stream();
	}

	public List<MatchedSourceResourceCandidate> getCandidates() {
		return Collections.unmodifiableList(myList);
	}

	public MatchedSourceResourceCandidate getFirstMatch() {
		return myList.get(0);
	}

    public boolean isEidMatch() {
		return myStrategy.isEidMatch();
    }
}
