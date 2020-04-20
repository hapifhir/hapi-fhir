package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidatorImpl;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.broker.EmpiMessageHandler;
import ca.uhn.fhir.jpa.empi.broker.EmpiQueueConsumerLoader;
import ca.uhn.fhir.jpa.empi.broker.EmpiSubscriptionLoader;
import ca.uhn.fhir.jpa.empi.svc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Configuration
public class EmpiConsumerConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiConsumerConfig.class);

	public static final String EMPI_CONSUMER_COUNT_DEFAULT = "5";

	@Autowired
    IEmpiSettings myEmpiProperties;
	@Autowired
	IEmpiRuleValidator myEmpiRuleValidator;
	@Autowired
	EmpiProviderLoader myEmpiProviderLoader;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;

	@Bean
	EmpiQueueConsumerLoader empiQueueConsumerLoader() {
		return new EmpiQueueConsumerLoader();
	}

	@Bean
	EmpiMessageHandler empiMessageHandler() {
		return new EmpiMessageHandler();
	}

	@Bean
	EmpiMatchLinkSvc empiMatchLinkSvc() {
		return new EmpiMatchLinkSvc();
	}

	@Bean
	EmpiResourceDaoSvc empiResourceDaoSvc() {
		return new EmpiResourceDaoSvc();
	}

	@Bean
	IEmpiLinkSvc empiLinkSvc() {
		return new EmpiLinkSvcImpl();
	}

	@Bean
	EmpiLinkDaoSvc empiLinkDaoSvc() {
		return new EmpiLinkDaoSvc();
	}

	@Bean
	PersonHelper personHelper(FhirContext theFhirContext) {
		return new PersonHelper(theFhirContext);
	}

	@Bean
	EIDHelper eidHelper(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		return new EIDHelper(theFhirContext, theEmpiConfig);
	}

	@Bean
	EmpiSubscriptionLoader empiSubscriptionLoader() {
		return new EmpiSubscriptionLoader();
	}

	@Bean
	EmpiPersonFindingSvc empiPersonFindingSvc() {
		return new EmpiPersonFindingSvc();
	}

	@Bean
	EmpiProviderLoader empiProviderLoader() {
		return new EmpiProviderLoader();
	}

	@Bean
	IEmpiRuleValidator empiRuleValidator() {
		return new EmpiRuleValidatorImpl();
	}

	@Bean
	IEmpiMatchFinderSvc empiMatchFinderSvc() {
		return new EmpiMatchFinderSvcImpl();
	}

	@Bean
	EmpiCandidateSearchSvc empiCandidateSearchSvc() {
		return new EmpiCandidateSearchSvc();
	}

	@Bean
	EmpiResourceComparatorSvc empiResourceComparatorSvc(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		return new EmpiResourceComparatorSvc(theFhirContext, theEmpiConfig);
	}

	@Bean
	ResourceTableHelper resourceTableHelper() {
		return new ResourceTableHelper();
	}

	@PostConstruct
	public void registerInterceptorAndProvider() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myEmpiRuleValidator.validate(myEmpiProperties.getEmpiRules());
	}

	@EventListener(classes = {ContextRefreshedEvent.class})
	// This @Order is here to ensure that MatchingQueueSubscriberLoader has initialized before we initialize this.
	// Otherwise the EMPI subscriptions won't get loaded into the SubscriptionRegistry
	@Order
	public void updateSubscriptions() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myEmpiProviderLoader.loadProvider();
		ourLog.info("EMPI provider registered");

		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
	}
}
