package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Register a listener with this registry in order to be notified whenever a resource matching the provided SearchParameterMap
 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
 * within {@link VersionChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL_MS} of the change happening.  If the change happened
 * on a different jvm process, then the listener will be called within {@link VersionChangeListenerRegistryImpl#REMOTE_REFRESH_INTERVAL}
 * of the change happening.
 */
public interface IVersionChangeListenerRegistry {
	@VisibleForTesting
	void clearCacheForUnitTest();

	@VisibleForTesting
	void clearListenersForUnitTest();

	VersionChangeResult refreshAllCachesIfNecessary();

	/**
	 * Request that all caches be refreshed now, in the current thread
	 */
	VersionChangeResult refreshAllCachesImmediately();

	VersionChangeResult refreshCacheWithRetry(String theResourceName);

	void registerResourceVersionChangeListener(String theResourceType, SearchParameterMap theSearchParameterMap, IVersionChangeListener theVersionChangeListener);

	VersionChangeResult refreshCacheIfNecessary(String theResourceName);

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	void requestRefresh(String theResourceName);

	VersionChangeResult forceRefresh(String theResourceName);

	/**
	 * If any listeners have been registered with searchparams that match the incoming resource, then
	 * call requestRefresh(theResourceName) for that resource type.
	 * @param theResource the resource that changed that might trigger a refresh
	 */
	void requestRefreshIfWatching(IBaseResource theResource);

	void unregisterResourceVersionChangeListener(IVersionChangeListener theVersionChangeListener);
}
