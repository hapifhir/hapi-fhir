package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.broker.EmpiSubscriptionLoader;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiDaoInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

public class EmpiSubmitterInitializer {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiSubmitterInitializer.class);

	@Autowired
	IEmpiProperties myEmpiProperties;
	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	EmpiDaoInterceptor myEmpiDaoInterceptor;
	@Autowired
	EmpiProviderLoader myEmpiProviderLoader;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;

	@EventListener(classes = {ContextRefreshedEvent.class})
	// This @Order is here to ensure that MatchingQueueSubscriberLoader has initialized before we initialize this.
	// Otherwise the EMPI subscriptions won't get loaded into the SubscriptionRegistry
	@Order
	public void updateSubscriptions() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myInterceptorService.registerInterceptor(myEmpiDaoInterceptor);
		ourLog.info("EMPI interceptor registered");

		myEmpiProviderLoader.loadProvider();
		ourLog.info("EMPI provider registered");

		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
	}
}
