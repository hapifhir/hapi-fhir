package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
import ca.uhn.fhir.jpa.empi.provider.EmpiProviderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class EmpiInitializer {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInitializer.class);
	public static final String EMPI_CONSUMER_COUNT_DEFAULT = "5";

	@Autowired
	ApplicationContext myApplicationContext;
	@Autowired
	IInterceptorService myInterceptorService;
	@Autowired
	IEmpiConfig myEmpiConfig;
	@Autowired
	IEmpiRuleValidator myEmpiRuleValidator;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;
	@Autowired
	EmpiInterceptor myEmpiInterceptor;

	@EventListener(classes = {ContextRefreshedEvent.class})
	// FIXME KHS make the dependency on subscriptions explicit
	@Order
	public void init() {
		if (!myEmpiConfig.isEnabled()) {
			return;
		}
		myEmpiRuleValidator.validate(myEmpiConfig.getEmpiRules());
		myInterceptorService.registerInterceptor(myEmpiInterceptor);
		ourLog.info("EMPI interceptor registered");

		myApplicationContext.getBean(EmpiQueueConsumerLoader.class);
		EmpiProviderLoader empiProviderLoader = myApplicationContext.getBean(EmpiProviderLoader.class);
		empiProviderLoader.loadProvider();

		// FIXME KHS we have to wait until MatchingQueueSubscriberLoader has initialized before calling this
		// the subscriptions else they won't get loaded into the subscriber registry
		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
	}
}
