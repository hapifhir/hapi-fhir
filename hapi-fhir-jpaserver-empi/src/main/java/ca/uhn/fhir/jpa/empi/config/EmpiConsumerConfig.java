package ca.uhn.fhir.jpa.empi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidatorImpl;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.broker.EmpiMessageHandler;
import ca.uhn.fhir.jpa.empi.broker.EmpiQueueConsumerLoader;
import ca.uhn.fhir.jpa.empi.svc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

public class EmpiConsumerConfig {
	public static final String EMPI_CONSUMER_COUNT_DEFAULT = "5";

	@Autowired
	IEmpiProperties myEmpiProperties;
	@Autowired
	IEmpiRuleValidator myEmpiRuleValidator;

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
	ResourceTableHelper resourceTableHelper() {
		return new ResourceTableHelper();
	}

	@Bean
	PersonHelper personHelper(FhirContext theFhirContext) {
		return new PersonHelper(theFhirContext);
	}

	@Bean
	EIDHelper eidHelper(FhirContext theFhirContext, IEmpiProperties theEmpiConfig) {
		return new EIDHelper(theFhirContext, theEmpiConfig);
	}

	@Bean
	EmpiPersonFindingSvc empiPersonFindingSvc() {
		return new EmpiPersonFindingSvc();
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
	EmpiResourceComparatorSvc empiResourceComparatorSvc(FhirContext theFhirContext, IEmpiProperties theEmpiConfig) {
		return new EmpiResourceComparatorSvc(theFhirContext, theEmpiConfig);
	}

	@Bean
	IEmpiRuleValidator empiRuleValidator() {
		return new EmpiRuleValidatorImpl();
	}

	@PostConstruct
	public void registerInterceptorAndProvider() {
		if (!myEmpiProperties.isEnabled()) {
			return;
		}

		myEmpiRuleValidator.validate(myEmpiProperties.getEmpiRules());
	}
}
