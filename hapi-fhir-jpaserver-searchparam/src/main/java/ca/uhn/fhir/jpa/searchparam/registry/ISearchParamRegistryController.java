/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.jpa.cache.ResourceChangeResult;
import jakarta.annotation.Nonnull;

public interface ISearchParamRegistryController {

	ResourceChangeResult refreshCacheIfNecessary();

	/**
	 * Runs {@code theCallback} as a hint that resource-change-driven rebuilds
	 * triggered during the callback should be coalesced into a single rebuild
	 * at scope exit. Implementations <i>may</i> coalesce; the default
	 * implementation does not — it simply invokes the callback.
	 * <p>
	 * This is a single-caller, single-thread optimization aimed at the package
	 * install path. The callback must remain correct when each change triggers
	 * an immediate rebuild, and callers must not rely on coalescing for
	 * correctness.
	 *
	 * @since 8.12.0
	 */
	default void withDeferredRebuild(@Nonnull Runnable theCallback) {
		theCallback.run();
	}
}
