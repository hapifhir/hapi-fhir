package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SubscriptionInterceptorLoader {
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;

	public void registerInterceptors(RestfulServer theRestfulServer) {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (!supportedSubscriptionTypes.isEmpty()) {
			theRestfulServer.registerInterceptor(mySubscriptionActivatingInterceptor);
			theRestfulServer.registerInterceptor(mySubscriptionMatcherInterceptor);
		}
	}

	@VisibleForTesting
	public void unregisterInterceptors(RestfulServer theRestfulServer) {
		theRestfulServer.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		theRestfulServer.unregisterInterceptor(mySubscriptionMatcherInterceptor);
	}
}
