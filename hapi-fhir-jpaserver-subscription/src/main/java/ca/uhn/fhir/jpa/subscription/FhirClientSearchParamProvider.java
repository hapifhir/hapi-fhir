package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;

// FIXME KHS Test
public class FhirClientSearchParamProvider implements ISearchParamProvider {

	private final IGenericClient myClient;

	public FhirClientSearchParamProvider(IGenericClient theClient) {
		myClient = theClient;
	}

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		FhirContext fhirContext = myClient.getFhirContext();

		IBaseBundle bundle = myClient
			.search()
			.forResource(ResourceTypeEnum.SEARCHPARAMETER.getCode())
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();

		return new SimpleBundleProvider(BundleUtil.toListOfResources(fhirContext, bundle));
	}

	@Override
	public <SP extends IBaseResource> void refreshCache(BaseSearchParamRegistry<SP> theSearchParamRegistry, long theRefreshInterval) {
		theSearchParamRegistry.doRefresh(theRefreshInterval);
	}
}
