package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;

public interface IVersionChangeListenerRegistry {
	void registerResourceVersionChangeListener(String theResourceType, SearchParameterMap map, IVersionChangeListener theVersionChangeListener);

	boolean refreshAllCachesIfNecessary();

	void forceRefresh();

	void requestRefresh();

	@VisibleForTesting
	void clearListenersForUnitTest();

	@VisibleForTesting
	void clearCacheForUnitTest();
}
