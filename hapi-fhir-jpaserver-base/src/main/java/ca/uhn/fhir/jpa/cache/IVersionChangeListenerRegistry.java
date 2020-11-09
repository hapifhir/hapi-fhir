package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import com.google.common.annotations.VisibleForTesting;

/**
 * Register a listener with this registry in order to be notified whenever a resource matching the provided SearchParameterMap
 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
 * within {@link VersionChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL} of the change happening.  If the change happened
 * on a different jvm process, then the listener will be called within {@link VersionChangeListenerRegistryImpl#REMOTE_REFRESH_INTERVAL}
 * of the change happening.
 */
public interface IVersionChangeListenerRegistry {
	void registerResourceVersionChangeListener(String theResourceType, SearchParameterMap theSearchParameterMap, IVersionChangeListener theVersionChangeListener);

	boolean refreshAllCachesIfNecessary();

	void forceRefresh();

	void requestRefresh();

	void requestRefresh(String theResourceName);

	boolean cacheContainsKey(IdDt theIdDt);

	@VisibleForTesting
	void clearListenersForUnitTest();

	@VisibleForTesting
	void clearCacheForUnitTest();
}
