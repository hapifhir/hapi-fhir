package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

public interface IResourceVersionSvc {
	ResourceVersionMap getVersionMap(String theResourceName, SearchParameterMap theSearchParamMap);
}
