package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import java.time.Instant;

public interface IResourceChangeListenerCache {
	// FIXME KHS Javadoc
	SearchParameterMap getSearchParameterMap();

	boolean isInitialized();

	String getResourceName();

	Instant getNextRefreshTime();

	void requestRefresh();

	ResourceChangeResult forceRefresh();

	ResourceChangeResult refreshCacheIfNecessary();
}
