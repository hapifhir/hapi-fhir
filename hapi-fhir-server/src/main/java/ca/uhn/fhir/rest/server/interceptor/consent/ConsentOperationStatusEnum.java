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

public enum ConsentOperationStatusEnum implements IConsentVote {

	/**
	 * The requested operation cannot proceed, and an operation outcome suitable for
	 * the user is available
	 */
	REJECT,

	/**
	 * The requested operation is allowed to proceed, but the engine will review each
	 * resource before sending to the client
	 */
	PROCEED,

	/**
	 * The engine has nothing to say about the operation  (same as proceed, but the
	 * host application need not consult the engine - can use more efficient
	 * counting/caching methods)
	 */
	AUTHORIZED,
	;

	/**
	 * Assigns ordinals to the verdicts by strength:
	 * REJECT > AUTHORIZED > PROCEED.
	 * @return 2/1/0 for REJECT/AUTHORIZED/PROCEED
	 */
	int getPrecedence() {
		switch (this) {
			case REJECT:
				return 2;
			case AUTHORIZED:
				return 1;
			case PROCEED:
			default:
				return 0;
		}
	}

	/**
	 * Does this vote abstain from the verdict?
	 * I.e. this == PROCEED
	 * @return true if this vote can be ignored
	 */
	boolean isAbstain() {
		return this == PROCEED;
	}

	/**
	 * Does this vote participate from the verdict?
	 * I.e. this != PROCEED
	 * @return false if this vote can be ignored
	 */
	boolean isActiveVote() {
		return this != PROCEED;
	}

	@Override
	public ConsentOperationStatusEnum getStatus() {
		return this;
	}

	/**
	 * Evaluate verdicts in order, taking the first "decision" (i.e. first non-PROCEED) verdict.
	 *
	 * @return the first decisive verdict, or PROCEED when empty or all PROCEED.
	 */
	public static ConsentOperationStatusEnum serialReduce(Stream<ConsentOperationStatusEnum> theVoteStream) {
		return IConsentVote.serialReduce(PROCEED, theVoteStream);
	}

	public static ConsentOperationStatusEnum parallelReduce(Stream<ConsentOperationStatusEnum> theVoteStream) {
		return IConsentVote.parallelReduce(PROCEED, theVoteStream);
	}

	/** @deprecated for rename */
	@Deprecated(forRemoval = true)
	public static ConsentOperationStatusEnum serialEvaluate(Stream<ConsentOperationStatusEnum> theVoteStream) {
		return serialReduce(theVoteStream);
	}

	/** @deprecated for rename */
	@Deprecated(forRemoval = true)
	public static ConsentOperationStatusEnum parallelEvaluate(Stream<ConsentOperationStatusEnum> theVoteStream) {
		return parallelReduce(theVoteStream);
	}
}
