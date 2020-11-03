package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.springframework.stereotype.Service;

@Service
public class ResourceVersionCacheSvc {
	public IResourceVersionMap getVersionLookup(String theResourceType, SearchParameterMap theSearchParamMap) {
		// FIXME KHS
		// Optimized database search
		return new ResourceVersionMap();
	}
}
