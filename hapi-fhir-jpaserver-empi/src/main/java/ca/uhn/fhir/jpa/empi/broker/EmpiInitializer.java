package ca.uhn.fhir.jpa.empi.broker;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmpiInitializer {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInitializer.class);
	public static final String EMPI_CONSUMER_COUNT_DEFAULT = "5";

	@Autowired
	BeanFactory myBeanFactory;
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

	@PostConstruct
	public void init() {
		if (!myEmpiConfig.isEnabled()) {
			return;
		}
		myEmpiRuleValidator.validate(myEmpiConfig.getEmpiRules());
		myEmpiSubscriptionLoader.loadEmpiSubscriptions();
		myInterceptorService.registerInterceptor(mySubscriptionMatcherInterceptor);
		ourLog.info("EMPI interceptor registered");

		EmpiProviderLoader empiProviderLoader = myBeanFactory.getBean(EmpiProviderLoader.class);
		empiProviderLoader.loadProvider();
	}
}
