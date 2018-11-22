package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ISearchParamProvider {
	IBundleProvider search(SearchParameterMap theParams);

	<SP extends IBaseResource> void refreshCache(BaseSearchParamRegistry<SP> theSPBaseSearchParamRegistry, long theRefreshInterval);
}
