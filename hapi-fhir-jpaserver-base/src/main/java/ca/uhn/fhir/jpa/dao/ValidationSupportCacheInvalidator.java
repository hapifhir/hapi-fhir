/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.support.IValidationSupport;
import org.springframework.core.Ordered;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Clears the validation support caches after a {@literal ValueSet} or {@literal CodeSystem} write commits, so that
 * validation answers from the new content instead of what it cached before the write. This only clears the caches
 * on this server.
 */
// Created by Claude Opus 5.5
final class ValidationSupportCacheInvalidator {

	private ValidationSupportCacheInvalidator() {}

	/**
	 * Clears the caches of {@code theValidationSupport} once the current transaction commits, or straight away if
	 * no transaction is active. However many writes a transaction holds, the caches are cleared once.
	 * <p>
	 * The clearing runs before any other after-commit work. Starting a pre-expansion job is such work, and it looks
	 * the {@literal ValueSet} up through these caches, so it would otherwise find an earlier "not found" answer.
	 * </p>
	 *
	 * @param theValidationSupport the validation support whose caches to clear
	 */
	static void invalidateCachesAfterCommit(IValidationSupport theValidationSupport) {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			theValidationSupport.invalidateCaches();
			return;
		}
		boolean alreadyRegistered = TransactionSynchronizationManager.getSynchronizations().stream()
				.anyMatch(InvalidateAfterCommit.class::isInstance);
		if (!alreadyRegistered) {
			TransactionSynchronizationManager.registerSynchronization(new InvalidateAfterCommit(theValidationSupport));
		}
	}

	private static class InvalidateAfterCommit implements TransactionSynchronization {
		private final IValidationSupport myValidationSupport;

		InvalidateAfterCommit(IValidationSupport theValidationSupport) {
			myValidationSupport = theValidationSupport;
		}

		@Override
		public int getOrder() {
			return Ordered.HIGHEST_PRECEDENCE;
		}

		@Override
		public void afterCommit() {
			myValidationSupport.invalidateCaches();
		}
	}
}
