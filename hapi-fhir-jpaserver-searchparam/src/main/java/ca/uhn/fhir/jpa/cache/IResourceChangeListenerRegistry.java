package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IResourceChangeListenerRegistry {
	ResourceChangeResult refreshAllCachesIfNecessary();

	/**
	 * Request that all caches be refreshed now, in the current thread
	 */
	ResourceChangeResult refreshAllCachesImmediately();

	ResourceChangeResult refreshCacheWithRetry(String theResourceName);

	/**
	 * Register a listener with this registry in order to be notified whenever a resource matching the provided SearchParameterMap
	 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
	 * within {@link ResourceChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL_MS} of the change happening.  If the change happened
	 * on a different jvm process, then the listener will be called within {@link ResourceChangeListenerRegistryImpl#REMOTE_REFRESH_INTERVAL_MS}.
	 *
	 * @param theResourceType           the resource type
	 * @param theSearchParameterMap     a search parameter map defining a filter for which resources are of interest
	 * @param theResourceChangeListener the listener that will be called when a change is detected
	 */
	void registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParameterMap, IResourceChangeListener theResourceChangeListener);

	/**
	 * Unregister a listener from this service
	 *
	 * @param theResourceChangeListener
	 */
	void unregisterResourceResourceChangeListener(IResourceChangeListener theResourceChangeListener);

	ResourceChangeResult refreshCacheIfNecessary(String theResourceName);

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	void requestRefresh(String theResourceName);

	/**
	 * Request that a cache be refreshed now, in the current thread
	 */
	ResourceChangeResult forceRefresh(String theResourceName);

	/**
	 * If any listeners have been registered with searchparams that match the incoming resource, then
	 * call requestRefresh(theResourceName) for that resource type.
	 *
	 * @param theResource the resource that changed that might trigger a refresh
	 */
	void requestRefreshIfWatching(IBaseResource theResource);

	@VisibleForTesting
	void clearCacheForUnitTest();

	@VisibleForTesting
	void clearListenersForUnitTest();
}
