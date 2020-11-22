package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IResourceChangeListenerCacheRefresher {
	ResourceChangeResult refreshAllCachesIfNecessary();

	ResourceChangeResult refreshCacheWithRetry(RegisteredResourceChangeListener theEntry);

	/**
	 * If any listeners have been registered with searchparams that match the incoming resource, then
	 * call requestRefresh(theResourceName) for that resource type.
	 *
	 * @param theResource the resource that changed that might trigger a refresh
	 */
	void requestRefreshIfWatching(IBaseResource theResource);
}
