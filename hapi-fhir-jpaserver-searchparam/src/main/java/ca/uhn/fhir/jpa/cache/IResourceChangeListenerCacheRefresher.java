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

/**
 * This is an internal service and is not intended to be used outside this package.  Implementers should only directly
 * call the {@link IResourceChangeListenerRegistry}.
 *
 * This service refreshes a {@link ResourceChangeListenerCache} cache and notifies its listener when
 * the cache changes.
 */
public interface IResourceChangeListenerCacheRefresher {
	/**
	 * If the current time is past the next refresh time of the registered listener, then check if any of its
	 * resources have changed and notify the listener accordingly
	 * @return an aggregate of all changes sent to all listeners
	 */
	ResourceChangeResult refreshExpiredCachesAndNotifyListeners();

	/**
	 * Refresh the cache in this entry and notify the entry's listener if the cache changed
	 * @param theEntry the {@link IResourceChangeListenerCache} with the cache and the listener
	 * @return the number of resources that have been created, updated and deleted since the last time the cache was refreshed
	 */
	ResourceChangeResult refreshCacheAndNotifyListener(IResourceChangeListenerCache theEntry);
}
