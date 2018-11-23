package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class FhirClientSearchParamProvider implements ISearchParamProvider {
	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return null;
	}

	@Override
	public <SP extends IBaseResource> void refreshCache(BaseSearchParamRegistry<SP> theSPBaseSearchParamRegistry, long theRefreshInterval) {

	}
}
