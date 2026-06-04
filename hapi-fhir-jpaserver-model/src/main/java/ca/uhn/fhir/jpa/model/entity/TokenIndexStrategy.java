/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.Validate;

import java.util.EnumSet;
import java.util.Set;

/**
 * Routing strategy for token-index reads and writes between the legacy
 * {@code HFJ_SPIDX_TOKEN} table and the compressed token index tables
 * ({@code HFJ_SPIDX2_TOKEN_COMMON}, {@code HFJ_SPIDX2_TOKEN_COMMON_RES},
 * {@code HFJ_SPIDX2_TOKEN_IDENTIFIER}).
 *
 * @see ca.uhn.fhir.jpa.api.config.JpaStorageSettings#setTokenIndexStrategy(TokenIndexStrategy)
 */
public class TokenIndexStrategy {

	/**
	 * Token index table types.
	 */
	public enum TokenIndex {
		/** Legacy {@code HFJ_SPIDX_TOKEN} table with separate system/code columns. */
		LEGACY,
		/** Compressed tables: {@code HFJ_SPIDX2_TOKEN_COMMON}, {@code HFJ_SPIDX2_TOKEN_COMMON_RES}, {@code HFJ_SPIDX2_TOKEN_IDENTIFIER}. */
		COMPRESSED
	}

	private final Set<TokenIndex> myWriteTargets;
	private final TokenIndex myReadTarget;

	private TokenIndexStrategy(Set<TokenIndex> theWriteTargets, TokenIndex theReadTarget) {
		myWriteTargets = EnumSet.copyOf(theWriteTargets);
		myReadTarget = theReadTarget;
	}

	public static TokenIndexStrategy of(Set<TokenIndex> theWriteTargets, TokenIndex theReadTarget) {
		Validate.notEmpty(theWriteTargets, "Write targets must not be empty");
		Validate.notNull(theReadTarget, "Read target must not be null");
		Validate.isTrue(
				theWriteTargets.contains(theReadTarget),
				"Read target %s must be included in write targets %s",
				theReadTarget,
				theWriteTargets);
		return new TokenIndexStrategy(theWriteTargets, theReadTarget);
	}

	public boolean writeToLegacyTokenTable() {
		return myWriteTargets.contains(TokenIndex.LEGACY);
	}

	public boolean writeToCompressedTokenTables() {
		return myWriteTargets.contains(TokenIndex.COMPRESSED);
	}

	public boolean readFromCompressedTokenTables() {
		return myReadTarget == TokenIndex.COMPRESSED;
	}
}
