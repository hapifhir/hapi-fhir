package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import java.time.Instant;

/**
 * This is a handle to the cache created by {@link IResourceChangeListenerRegistry} when a listener is registered.
 * This this handle can be used to refresh the cache if required.
 */
public interface IResourceChangeListenerCache {
	/**
	 * @return the search parameter map the listener was registered with
	 */
	SearchParameterMap getSearchParameterMap();

	/**
	 * @return whether the cache has been initialized.  (If not, the cache will be empty.)
	 */
	boolean isInitialized();

	/**
	 * @return the name of the resource type the listener was registered with
	 */
	String getResourceName();

	/**
	 * @return the next scheduled time the cache will search the repository, update its cache and notify
	 * its listener of any changes
	 */
	Instant getNextRefreshTime();

	/**
	 * sets the nextRefreshTime to {@link Instant.MIN} so that the cache will be refreshed and listeners notified in another thread
	 * the next time cache refresh times are checked (every {@link ResourceChangeListenerCacheRefresherImpl.LOCAL_REFRESH_INTERVAL_MS}.
	 */
	void requestRefresh();

	/**
	 * Refresh the cache immediately in the current thread and notify its listener if there are any changes
	 * @return counts of detected resource creates, updates and deletes
	 */
	ResourceChangeResult forceRefresh();

	/**
	 * If nextRefreshTime is in the past, then update the cache with the current repository contents and notify its listener of any changes
	 * @return counts of detected resource creates, updates and deletes
	 */
	ResourceChangeResult refreshCacheIfNecessary();

	// TODO KHS in the future support adding new listeners to existing caches
}
