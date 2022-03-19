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
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Set;

/**
 * This component holds an in-memory list of all registered {@link IResourceChangeListener} instances along
 * with their caches and other details needed to maintain those caches.  Register an {@link IResourceChangeListener} instance
 * with this service to be notified when resources you care about are changed.  This service quickly notifies listeners
 * of changes that happened on the local process and also eventually notifies listeners of changes that were made by
 * remote processes.
 */
public interface IResourceChangeListenerRegistry {

	/**
	 * Register a listener in order to be notified whenever a resource matching the provided SearchParameterMap
	 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
	 * within {@link ResourceChangeListenerCacheRefresherImpl#LOCAL_REFRESH_INTERVAL_MS} of the change happening.  If the change happened
	 * on a different jvm process, then the listener will be called within the time specified in theRemoteRefreshIntervalMs parameter.
	 * @param theResourceName           the type of the resource the listener should be notified about (e.g. "Subscription" or "SearchParameter")
	 * @param theSearchParameterMap     the listener will only be notified of changes to resources that match this map
	 * @param theResourceChangeListener the listener that will be called whenever resource changes are detected
	 * @param theRemoteRefreshIntervalMs the number of milliseconds between checking the database for changed resources that match the search parameter map
	 * @throws ca.uhn.fhir.parser.DataFormatException      if theResourceName is not a valid resource type in the FhirContext
	 * @throws IllegalArgumentException if theSearchParamMap cannot be evaluated in-memory
	 * @return RegisteredResourceChangeListener a handle to the created cache that can be used to manually refresh the cache if required
	 */
	IResourceChangeListenerCache registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParameterMap, IResourceChangeListener theResourceChangeListener, long theRemoteRefreshIntervalMs);

	/**
	 * Unregister a listener from this service
	 *
	 * @param theResourceChangeListener
	 */
	void unregisterResourceResourceChangeListener(IResourceChangeListener theResourceChangeListener);

	/**
	 * Unregister a listener from this service using its cache handle
	 *
	 * @param theResourceChangeListenerCache
	 */
	void unregisterResourceResourceChangeListener(IResourceChangeListenerCache theResourceChangeListenerCache);

	@VisibleForTesting
	void clearListenersForUnitTest();

	/**
	 *
	 * @param theCache
	 * @return true if theCache is registered
	 */
	boolean contains(IResourceChangeListenerCache theCache);

	/**
	 * Called by the {@link ResourceChangeListenerRegistryInterceptor} when a resource is changed to invalidate matching
	 * caches so their listeners are notified the next time the caches are refreshed.
	 * @param theResource the resource that changed that might trigger a refresh
	 */

	void requestRefreshIfWatching(IBaseResource theResource);

	/**
	 * @return a set of resource names watched by the registered listeners
	 */
	Set<String> getWatchedResourceNames();
}
