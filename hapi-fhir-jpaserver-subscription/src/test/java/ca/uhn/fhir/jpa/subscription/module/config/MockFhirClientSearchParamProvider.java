package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class MockFhirClientSearchParamProvider implements ISearchParamProvider {
	private final MockProvider myMockProvider = new MockProvider();

	@Autowired
	private SearchParamRegistryImpl mySearchParamRegistry;

	public MockFhirClientSearchParamProvider() {
		super();
	}

	public void setBundleProvider(IBundleProvider theBundleProvider) { myMockProvider.setBundleProvider(theBundleProvider); }

	public void setReadResource(IBaseResource theReadResource) { myMockProvider.setReadResource(theReadResource);}

	public void setFailCount(int theFailCount) { myMockProvider.setFailCount(theFailCount); }

	public int getFailCount() { return myMockProvider.getFailCount(); }

	@Override
	public IBundleProvider search(SearchParameterMap theParams) { return myMockProvider.search(theParams); }

	@Override
	public IBaseResource read(IIdType theId) {
		return myMockProvider.read(theId);
	}
}
