package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
import ca.uhn.fhir.jpa.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;
	@Autowired
	EmpiInterceptor myEmpiInterceptor;

	@PostConstruct
	public void init() {
		if (!myEmpiConfig.isEnabled()) {
			return;
		}
		myEmpiRuleValidator.validate(myEmpiConfig.getEmpiRules());
		myEmpiSubscriptionLoader.registerEmpiSubscriptions();
		myInterceptorService.registerInterceptor(myEmpiInterceptor);
		ourLog.info("EMPI interceptor registered");

		// FIXME KHS reconcile this with subscription interceptor loader--probably uncomment the following line
//		myInterceptorService.registerInterceptor(mySubscriptionMatcherInterceptor);

		myApplicationContext.getBean(EmpiQueueConsumerLoader.class);
		EmpiProviderLoader empiProviderLoader = myApplicationContext.getBean(EmpiProviderLoader.class);
		empiProviderLoader.loadProvider();
	}
}
