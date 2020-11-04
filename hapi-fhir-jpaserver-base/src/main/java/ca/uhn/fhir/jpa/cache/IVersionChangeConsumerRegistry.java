package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;

public interface IVersionChangeConsumerRegistry {
	void registerResourceVersionChangeConsumer(String theResourceType, SearchParameterMap map, IVersionChangeListener theVersionChangeConsumer);

	boolean refreshAllCachesIfNecessary();

	void forceRefresh();

	void requestRefresh();

	@VisibleForTesting
	void clearConsumersForUnitTest();

	@VisibleForTesting
	void clearCacheForUnitTest();
}
