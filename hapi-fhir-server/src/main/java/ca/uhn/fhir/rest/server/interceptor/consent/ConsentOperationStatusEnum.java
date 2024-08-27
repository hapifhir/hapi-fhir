/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.consent;

import java.util.stream.Stream;

public enum ConsentOperationStatusEnum {

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
	int getStrength() {
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
	 * Evaluate verdicts in order, taking the first "decision" (i.e. first non-PROCEED) verdict.
	 *
	 * @return the first decisive verdict, or PROCEED when empty or all PROCEED.
	 */
	public static ConsentOperationStatusEnum serialEvaluate(
			Stream<ConsentOperationStatusEnum> consentOperationStatusEnumStream) {
		return consentOperationStatusEnumStream
				.filter(verdict -> PROCEED != verdict)
				.findFirst()
				.orElse(PROCEED);
	}

	/**
	 * Evaluate verdicts in order, taking the first "decision" (i.e. first non-PROCEED) verdict.
	 *
	 * @param theNextVerdict the next verdict to consider
	 * @return the combined verdict
	 */
	public ConsentOperationStatusEnum serialReduce(ConsentOperationStatusEnum theNextVerdict) {
		switch (this) {
			case REJECT:
			case AUTHORIZED:
				return this;
			case PROCEED:
			default:
				return theNextVerdict;
		}
	}

	/**
	 * Evaluate all verdicts together, allowing any to veto (i.e. REJECT) the operation.
	 *
	 * @return REJECT if any reject, AUTHORIZED if no REJECT and some AUTHORIZED, PROCEED if empty or all PROCEED
	 */
	public static ConsentOperationStatusEnum parallelEvaluate(
			Stream<ConsentOperationStatusEnum> consentOperationStatusEnumStream) {
		return consentOperationStatusEnumStream.reduce(PROCEED, ConsentOperationStatusEnum::parallelReduce);
	}

	/**
	 * Evaluate two verdicts together, allowing either to veto (i.e. REJECT) the operation.
	 *
	 * @return REJECT if either reject, AUTHORIZED if no REJECT and some AUTHORIZED, PROCEED otherwise
	 */
	public ConsentOperationStatusEnum parallelReduce(ConsentOperationStatusEnum theNextVerdict) {
		if (theNextVerdict.getStrength() > this.getStrength()) {
			return theNextVerdict;
		} else {
			return this;
		}
	}
}
