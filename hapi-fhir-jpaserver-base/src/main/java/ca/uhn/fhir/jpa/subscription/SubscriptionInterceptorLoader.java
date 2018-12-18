package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SubscriptionInterceptorLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionInterceptorLoader.class);

	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	private SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;

	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	private ApplicationContext myAppicationContext;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	public void registerInterceptors() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (!supportedSubscriptionTypes.isEmpty()) {
			loadSubscriptions();

			ourLog.info("Registering subscription interceptors");
			myDaoConfig.registerInterceptor(mySubscriptionActivatingInterceptor);
			myDaoConfig.registerInterceptor(mySubscriptionMatcherInterceptor);
		}
	}

	private void loadSubscriptions() {
		ourLog.info("Loading subscriptions into the SubscriptionRegistry...");
		// Load subscriptions into the SubscriptionRegistry
		myAppicationContext.getBean(SubscriptionLoader.class);
		ourLog.info("...{} subscriptions loaded", mySubscriptionRegistry.size());

		// Once subscriptions have been loaded, now
		if (mySubscriptionActivatingInterceptor == null) {
			mySubscriptionActivatingInterceptor = myAppicationContext.getBean(SubscriptionActivatingInterceptor.class);
		}
		if (mySubscriptionMatcherInterceptor == null) {
			mySubscriptionMatcherInterceptor = myAppicationContext.getBean(SubscriptionMatcherInterceptor.class);
		}
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myDaoConfig.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		myDaoConfig.unregisterInterceptor(mySubscriptionMatcherInterceptor);
	}
}
