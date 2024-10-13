package ca.uhn.fhir.rest.server.interceptor.consent;

import java.util.stream.Stream;

public interface IConsentVoter {
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
	static <T extends IConsentVoter> T parallelReduce(T theSeed, Stream<T> theVoteStream) {
		return theVoteStream.reduce(theSeed, IConsentVoter::parallelReduce);
	}

	/**
	 * Evaluate two votes together, allowing either to veto (i.e. REJECT) the operation.
	 *
	 * @return REJECT if either reject, AUTHORIZED if no REJECT and some AUTHORIZED, PROCEED otherwise
	 */
	static <T extends IConsentVoter> T parallelReduce(T theAccumulator, T theNextVoter) {
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
	static <T extends IConsentVoter> T serialReduce(T theSeed, Stream<T> theVoterStream) {
		return theVoterStream.filter(IConsentVoter::isActiveVote).findFirst().orElse(theSeed);
	}

	/**
	 * Evaluate verdicts in order, taking the first "decision" (i.e. first non-PROCEED) verdict.
	 *
	 * @param theAccumulator the verdict so fat
	 * @param theNextVoter the next verdict to consider
	 * @return the combined verdict
	 */
	static <T extends IConsentVoter> T serialReduce(T theAccumulator, T theNextVoter) {
		if (theAccumulator.getStatus().isAbstain()) {
			return theNextVoter;
		} else {
			return theAccumulator;
		}
	}

	private static <T extends IConsentVoter> boolean isActiveVote(T nextVoter) {
		return nextVoter.getStatus().isActiveVote();
	}
}
