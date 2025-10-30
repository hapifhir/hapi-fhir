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
package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.i18n.Msg;
import com.google.common.annotations.VisibleForTesting;

public interface IResourceRepositoryCache {
	/**
	 * Request that the cache be refreshed at the next convenient time (possibly in a different thread) so that it will soon contain all the resources currently in the database.
	 */
	default void requestRefresh() {}

	/**
	 * Force an immediate cache refresh on the current thread so that when the method returns, the cache will contain all the resources currently in the database.
	 * ONLY USE IN TESTS.
	 */
	@VisibleForTesting
	default void forceRefresh() {
		throw new UnsupportedOperationException(Msg.code(2649));
	}
}
