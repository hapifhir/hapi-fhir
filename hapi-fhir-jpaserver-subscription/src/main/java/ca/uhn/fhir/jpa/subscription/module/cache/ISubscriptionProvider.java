package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ISubscriptionProvider {
	IBundleProvider search(SearchParameterMap theMap);

	boolean loadSubscription(IBaseResource theResource);
}
