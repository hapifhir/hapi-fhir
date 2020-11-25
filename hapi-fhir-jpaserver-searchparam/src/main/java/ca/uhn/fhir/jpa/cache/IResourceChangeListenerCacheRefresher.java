package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This is an internal service and is not intended to be used outside this package.  Implementers should only directly
 * call the {@link IResourceChangeListenerRegistry}.
 *
 * This service refreshes the {@link ResourceChangeListenerCache} caches and notifies their listener when
 * those caches change.
 */
public interface IResourceChangeListenerCacheRefresher {
	/**
	 * If the current time is past the next refresh time of the registered listener, then check if any of its
	 * resources have changed and notify the listener accordingly
	 * @return an aggregate of all changes sent to all listeners
	 */
	ResourceChangeResult refreshExpiredCachesAndNotifyListeners();

	/**
	 * If any listeners have been registered with searchparams that match the incoming resource, then
	 * call requestRefresh(theResourceName) for that resource type.
	 *
	 * @param theResource the resource that changed that might trigger a refresh
	 */
	void requestRefreshIfWatching(IBaseResource theResource);
}
