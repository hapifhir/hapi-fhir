package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

public interface IVersionChangeConsumerRegistry {
	void registerResourceVersionChangeConsumer(String theResourceType, SearchParameterMap map, IVersionChangeConsumer theVersionChangeConsumer);

	boolean refreshAllCachesIfNecessary();

	void clearConsumersForUnitTest();

	void forceRefresh();

	void requestRefresh();
}
