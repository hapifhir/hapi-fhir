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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * This component holds an in-memory list of all registered {@link IResourceChangeListener} instances along
 * with their caches and other details needed to maintain those caches.  Register an {@link IResourceChangeListener} instance
 * with this service to be notified when resources you care about are changed.  This service quickly notifies listeners
 * of changes that happened on the local process and also eventually notifies listeners of changes that were made by
 * remote processes.
 */
@Component
public class ResourceChangeListenerRegistryImpl implements IResourceChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerRegistryImpl.class);
	private final Queue<ResourceChangeListenerCache> myListenerEntries = new ConcurrentLinkedQueue<>();
	private final FhirContext myFhirContext;
	private final ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	public ResourceChangeListenerRegistryImpl(FhirContext theFhirContext, ResourceChangeListenerCacheFactory theResourceChangeListenerCacheFactory, InMemoryResourceMatcher theInMemoryResourceMatcher) {
		myFhirContext = theFhirContext;
		myResourceChangeListenerCacheFactory = theResourceChangeListenerCacheFactory;
		myInMemoryResourceMatcher = theInMemoryResourceMatcher;
	}

	/**
	 * Register a listener in order to be notified whenever a resource matching the provided SearchParameterMap
	 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
	 * within {@link ResourceChangeListenerCacheRefresherImpl#LOCAL_REFRESH_INTERVAL_MS} of the change happening.  If the change happened
	 * on a different jvm process, then the listener will be called within theRemoteRefreshIntervalMs.
	 *
	 * @param theResourceName            the type of the resource the listener should be notified about (e.g. "Subscription" or "SearchParameter")
	 * @param theSearchParameterMap      the listener will only be notified of changes to resources that match this map
	 * @param theResourceChangeListener  the listener that will be called whenever resource changes are detected
	 * @param theRemoteRefreshIntervalMs the number of milliseconds between checking the database for changed resources that match the search parameter map
	 * @return RegisteredResourceChangeListener that stores the resource id cache, and the next refresh time
	 * @throws ca.uhn.fhir.parser.DataFormatException if theResourceName is not a valid resource type in our FhirContext
	 * @throws IllegalArgumentException               if theSearchParamMap cannot be evaluated in-memory
	 */
	@Override
	public IResourceChangeListenerCache registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParameterMap, IResourceChangeListener theResourceChangeListener, long theRemoteRefreshIntervalMs) {
		// Clone searchparameter map
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceName);
		InMemoryMatchResult inMemoryMatchResult = myInMemoryResourceMatcher.canBeEvaluatedInMemory(theSearchParameterMap, resourceDef);
		if (!inMemoryMatchResult.supported()) {
			throw new IllegalArgumentException(Msg.code(482) + "SearchParameterMap " + theSearchParameterMap + " cannot be evaluated in-memory: " + inMemoryMatchResult.getUnsupportedReason() + ".  Only search parameter maps that can be evaluated in-memory may be registered.");
		}
		return add(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
	}

	/**
	 * Unregister a listener from this service
	 *
	 * @param theResourceChangeListener
	 */
	@Override
	public void unregisterResourceResourceChangeListener(IResourceChangeListener theResourceChangeListener) {
		myListenerEntries.removeIf(l -> l.getResourceChangeListener().equals(theResourceChangeListener));
	}

	@Override
	public void unregisterResourceResourceChangeListener(IResourceChangeListenerCache theResourceChangeListenerCache) {
		myListenerEntries.remove(theResourceChangeListenerCache);
	}

	private IResourceChangeListenerCache add(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theMap, long theRemoteRefreshIntervalMs) {
		ResourceChangeListenerCache retval = myResourceChangeListenerCacheFactory.newResourceChangeListenerCache(theResourceName, theMap, theResourceChangeListener, theRemoteRefreshIntervalMs);
		myListenerEntries.add(retval);
		return retval;
	}

	@Nonnull
	public Iterator<ResourceChangeListenerCache> iterator() {
		return myListenerEntries.iterator();
	}

	public int size() {
		return myListenerEntries.size();
	}

	@VisibleForTesting
	public void clearCachesForUnitTest() {
		myListenerEntries.forEach(ResourceChangeListenerCache::clearForUnitTest);
	}

	@Override
	public boolean contains(IResourceChangeListenerCache theCache) {
		return myListenerEntries.contains(theCache);
	}

	@VisibleForTesting
	public int getResourceVersionCacheSizeForUnitTest() {
		int retval = 0;
		for (ResourceChangeListenerCache entry : myListenerEntries) {
			retval += entry.getResourceVersionCache().size();
		}
		return retval;
	}

	@Override
	public void requestRefreshIfWatching(IBaseResource theResource) {
		String resourceName = myFhirContext.getResourceType(theResource);
		for (ResourceChangeListenerCache entry : myListenerEntries) {
			if (resourceName.equals(entry.getResourceName())) {
				entry.requestRefreshIfWatching(theResource);
			}
		}
	}

	@Override
	public Set<String> getWatchedResourceNames() {
		return myListenerEntries.stream()
			.map(ResourceChangeListenerCache::getResourceName)
			.collect(Collectors.toSet());
	}

	@Override
	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenerEntries.clear();
	}
}
