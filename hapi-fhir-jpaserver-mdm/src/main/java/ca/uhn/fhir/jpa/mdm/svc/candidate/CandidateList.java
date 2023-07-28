/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CandidateList {
	private final CandidateStrategyEnum myStrategy;

	private final Multimap<CandidateStrategyEnum, MatchedGoldenResourceCandidate> myStrategyToCandidateList = HashMultimap.create();

	public CandidateList(CandidateStrategyEnum theStrategy) {
		myStrategy = theStrategy;
	}

	public CandidateStrategyEnum getStrategy() {
		return myStrategy;
	}

	public boolean isEmpty() {
		return myStrategyToCandidateList.isEmpty();
	}

	public void addAll(CandidateStrategyEnum theStrategy, List<MatchedGoldenResourceCandidate> theList) {
		switch (theStrategy) {
			case EID:
			case LINK:
			case SCORE:
				myStrategyToCandidateList.putAll(theStrategy, theList);
				break;
			default:
				throw new InternalErrorException("Existing resources cannot be added for strategy " + theStrategy.name());
		}
	}

	public MatchedGoldenResourceCandidate getOnlyMatch() {
		assert size() == 1;
		return myStrategyToCandidateList.values()
			.stream().findFirst().get();
	}

	public boolean exactlyOneMatch() {
		return size() == 1;
	}

	public Stream<MatchedGoldenResourceCandidate> stream() {
		return myStrategyToCandidateList.values().stream();
	}

	public Stream<MatchedGoldenResourceCandidate> stream(CandidateStrategyEnum theStrategy) {
		return myStrategyToCandidateList.get(theStrategy).stream();
	}

	public List<MatchedGoldenResourceCandidate> getCandidates() {
		switch (myStrategy) {
			case LINK:
			case EID:
			case SCORE:
				return new ArrayList<>(myStrategyToCandidateList.get(myStrategy));
			default:
				return new ArrayList<>(myStrategyToCandidateList.values());
		}
	}

	public MatchedGoldenResourceCandidate getFirstMatch() {
		assert size() > 0;

		return myStrategyToCandidateList.values()
			.stream().findFirst().get();
	}

	public boolean isEidMatch() {
		return myStrategy.isEidMatch();
	}

	public int size() {
		return myStrategyToCandidateList.size();
	}
}
