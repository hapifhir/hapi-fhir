package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class FhirClientSearchParamProvider implements ISearchParamProvider {

	// FIXME KHS implement
	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return new SimpleBundleProvider();
	}

	@Override
	public <SP extends IBaseResource> void refreshCache(BaseSearchParamRegistry<SP> theSearchParamRegistry, long theRefreshInterval) {
		theSearchParamRegistry.doRefresh(theRefreshInterval);
	}
}
