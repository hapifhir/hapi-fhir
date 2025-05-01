/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.consent;

import java.util.stream.Stream;

/**
 * Something that produces a vote, along with static
 * tools for combining votes.
 */
public interface IConsentVote {
	/**
	 * Get the vote
	 * @return the vote
	 */
	ConsentOperationStatusEnum getStatus();

	/**
	 * Evaluate all verdicts together, allowing any to veto (i.e. REJECT) the operation.
	 * <ul>
	 * <li>If any vote is REJECT, then the result is REJECT.
	 * <li>If no vote is REJECT, and any vote is AUTHORIZED, then the result is AUTHORIZED.
	 * <li>If no vote is REJECT or AUTHORIZED, the result is PROCEED.
	 * </ul>
	 *
	 * @return REJECT if any reject, AUTHORIZED if no REJECT and some AUTHORIZED, PROCEED if empty or all PROCEED
	 */
	static <T extends IConsentVote> T parallelReduce(T theSeed, Stream<T> theVoteStream) {
		return theVoteStream.reduce(theSeed, IConsentVote::parallelReduce);
	}

	/**
	 * Evaluate two votes together, allowing either to veto (i.e. REJECT) the operation.
	 *
	 * @return REJECT if either reject, AUTHORIZED if no REJECT and some AUTHORIZED, PROCEED otherwise
	 */
	static <T extends IConsentVote> T parallelReduce(T theAccumulator, T theNextVoter) {
		if (theNextVoter.getStatus().getPrecedence()
				< theAccumulator.getStatus().getPrecedence()) {
			return theAccumulator;
		} else {
			return theNextVoter;
		}
	}

	/**
	 * Evaluate verdicts in order, taking the first "decision" (i.e. first non-PROCEED) verdict.
	 *
	 * @return the first decisive verdict, or theSeed when empty or all PROCEED.
	 */
	static <T extends IConsentVote> T serialReduce(T theSeed, Stream<T> theVoterStream) {
		return theVoterStream.filter(IConsentVote::isActiveVote).findFirst().orElse(theSeed);
	}

	/**
	 * Evaluate verdicts in order, taking the first "decision" (i.e. first non-PROCEED) verdict.
	 *
	 * @param theAccumulator the verdict so fat
	 * @param theNextVoter the next verdict to consider
	 * @return the combined verdict
	 */
	static <T extends IConsentVote> T serialReduce(T theAccumulator, T theNextVoter) {
		if (theAccumulator.getStatus().isAbstain()) {
			return theNextVoter;
		} else {
			return theAccumulator;
		}
	}

	private static <T extends IConsentVote> boolean isActiveVote(T nextVoter) {
		return nextVoter.getStatus().isActiveVote();
	}
}
