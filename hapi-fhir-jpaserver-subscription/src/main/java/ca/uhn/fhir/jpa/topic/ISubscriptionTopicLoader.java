package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public interface ISubscriptionTopicLoader extends IResourceChangeListener {
	void registerListener();

	SearchParameterMap getSearchParameterMap();

	void handleInit(List<IBaseResource> resourceList);

	int syncResourcesIntoCache(List<IBaseResource> resourceList);

	void unregisterListener();

	void syncDatabaseToCache();

	int doSyncResourcesForUnitTest();

	void start();

	void shutdown();
}
