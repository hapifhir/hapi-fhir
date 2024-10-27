/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CandidateList {
	private final CandidateStrategyEnum myStrategy;

	// no multimap - ordering matters
	private final Map<CandidateStrategyEnum, List<MatchedGoldenResourceCandidate>> myStrategyToCandidateList =
			new HashMap<>();

	public CandidateList(CandidateStrategyEnum theStrategy) {
		myStrategy = theStrategy;
		myStrategyToCandidateList.put(CandidateStrategyEnum.EID, new ArrayList<>());
		myStrategyToCandidateList.put(CandidateStrategyEnum.LINK, new ArrayList<>());
		myStrategyToCandidateList.put(CandidateStrategyEnum.SCORE, new ArrayList<>());
	}

	public CandidateStrategyEnum getStrategy() {
		return myStrategy;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void addAll(CandidateStrategyEnum theStrategy, List<MatchedGoldenResourceCandidate> theList) {
		switch (theStrategy) {
			case EID:
			case LINK:
			case SCORE:
				myStrategyToCandidateList.get(theStrategy).addAll(theList);
				break;
			default:
				throw new InternalErrorException(
						Msg.code(2424) + " Existing resources cannot be added for strategy " + theStrategy.name());
		}
	}

	public MatchedGoldenResourceCandidate getOnlyMatch() {
		assert size() == 1;
		return getCandidates().get(0);
	}

	public boolean exactlyOneMatch() {
		return size() == 1;
	}

	/**
	 * Returns a stream of all types.
	 * If multiple streams are present,
	 * they will be ordered by strategy type
	 */
	public Stream<MatchedGoldenResourceCandidate> stream() {
		return Stream.concat(
				myStrategyToCandidateList.get(CandidateStrategyEnum.EID).stream(),
				Stream.concat(
						myStrategyToCandidateList.get(CandidateStrategyEnum.LINK).stream(),
						myStrategyToCandidateList.get(CandidateStrategyEnum.SCORE).stream()));
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
				return Stream.of(
								myStrategyToCandidateList.get(CandidateStrategyEnum.EID),
								myStrategyToCandidateList.get(CandidateStrategyEnum.LINK),
								myStrategyToCandidateList.get(CandidateStrategyEnum.SCORE))
						.flatMap(Collection::stream)
						.collect(Collectors.toList());
		}
	}

	public MatchedGoldenResourceCandidate getFirstMatch() {
		assert size() > 0;

		switch (myStrategy) {
			case EID:
			case LINK:
			case SCORE:
				return myStrategyToCandidateList.get(myStrategy).get(0);
			default:
				return getCandidates().get(0);
		}
	}

	public boolean isEidMatch() {
		return myStrategy.isEidMatch();
	}

	public int size() {
		switch (myStrategy) {
			case EID:
			case LINK:
			case SCORE:
				return myStrategyToCandidateList.get(myStrategy).size();
			default:
				return myStrategyToCandidateList.get(CandidateStrategyEnum.EID).size()
						+ myStrategyToCandidateList
								.get(CandidateStrategyEnum.LINK)
								.size()
						+ myStrategyToCandidateList
								.get(CandidateStrategyEnum.SCORE)
								.size();
		}
	}
}
