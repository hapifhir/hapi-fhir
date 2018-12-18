package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.DaoConfig;
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

	public void registerInterceptors() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (!supportedSubscriptionTypes.isEmpty()) {
			myDaoConfig.registerInterceptor(mySubscriptionActivatingInterceptor);
			myDaoConfig.registerInterceptor(mySubscriptionMatcherInterceptor);
		}
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myDaoConfig.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		myDaoConfig.unregisterInterceptor(mySubscriptionMatcherInterceptor);
	}
}
