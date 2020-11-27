package ca.uhn.fhir.jpa.cache;

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
