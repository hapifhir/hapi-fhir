package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionProvider;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionProviderFhirClient implements ISubscriptionProvider {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	IGenericClient myClient;

	@Autowired
	public SubscriptionProviderFhirClient(IGenericClient theClient) {
		myClient = theClient;
	}

	@Override
	public IBundleProvider search(SearchParameterMap theMap) {
		FhirContext fhirContext = myClient.getFhirContext();

		String searchURL = ResourceTypeEnum.SUBSCRIPTION.getCode() + theMap.toNormalizedQueryString(myFhirContext);

		IBaseBundle bundle = myClient
			.search()
			.byUrl(searchURL)
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.execute();

		return new SimpleBundleProvider(BundleUtil.toListOfResources(fhirContext, bundle));
	}

	@Override
	public boolean loadSubscription(IBaseResource theResource) {
		return mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(theResource);
	}
}
