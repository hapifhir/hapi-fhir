package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;

/**
 * Register a listener with this registry in order to be notified whenever a resource matching the provided SearchParameterMap
 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
 * within {@link VersionChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL} of the change happening.  If the change happened
 * on a different jvm process, then the listener will be called within {@link VersionChangeListenerRegistryImpl#REMOTE_REFRESH_INTERVAL}
 * of the change happening.
 */
public interface IVersionChangeListenerRegistry {
	@VisibleForTesting
	void clearCacheForUnitTest();

	@VisibleForTesting
	void clearListenersForUnitTest();

	boolean refreshAllCachesIfNecessary();

	/**
	 * Request that all caches be refreshed now, in the current thread
	 */
	long refreshAllCachesImmediately();

	long refreshCacheWithRetry(String theResourceName);

	void registerResourceVersionChangeListener(String theResourceType, SearchParameterMap theSearchParameterMap, IVersionChangeListener theVersionChangeListener);


	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	void requestRefresh(String theResourceName);
}
