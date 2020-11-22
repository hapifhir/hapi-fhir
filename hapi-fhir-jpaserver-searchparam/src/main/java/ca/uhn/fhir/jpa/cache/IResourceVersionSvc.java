package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

/**
 * This interface is used by the {@link IResourceChangeListenerCacheRefresher} to read resources matching the provided
 * search parameter map in the repository and compare them to caches stored in the {@link IResourceChangeListenerRegistry}.
 */
public interface IResourceVersionSvc {
	ResourceVersionMap getVersionMap(String theResourceName, SearchParameterMap theSearchParamMap);
}
