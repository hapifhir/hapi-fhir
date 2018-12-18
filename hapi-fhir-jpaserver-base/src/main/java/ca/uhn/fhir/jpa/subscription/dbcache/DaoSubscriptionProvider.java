package ca.uhn.fhir.jpa.subscription.dbcache;

import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.SubscriptionActivatingInterceptor;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaoSubscriptionProvider implements ISubscriptionProvider {
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	private SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;

	@Override
	public IBundleProvider search(SearchParameterMap theMap) {
		IFhirResourceDao subscriptionDao = myDaoRegistry.getSubscriptionDao();
		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		return subscriptionDao.search(theMap, req);
	}

	@Override
	public boolean loadSubscription(IBaseResource theResource) {
		return mySubscriptionActivatingInterceptor.activateOrRegisterSubscriptionIfRequired(theResource);
	}
}
