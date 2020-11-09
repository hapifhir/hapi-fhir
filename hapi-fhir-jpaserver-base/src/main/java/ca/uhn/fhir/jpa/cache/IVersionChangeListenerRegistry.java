package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import com.google.common.annotations.VisibleForTesting;

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
