package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.jpa.cache.ResourceChangeResult;

public interface ISearchParamRegistryController {

	ResourceChangeResult refreshCacheIfNecessary();

}
