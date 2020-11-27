package ca.uhn.fhir.jpa.cache;

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
