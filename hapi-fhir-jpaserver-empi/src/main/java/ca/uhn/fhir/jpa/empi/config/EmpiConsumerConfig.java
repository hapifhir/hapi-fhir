package ca.uhn.fhir.jpa.empi.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvc;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.broker.EmpiMessageHandler;
import ca.uhn.fhir.jpa.empi.broker.EmpiQueueConsumerLoader;
import ca.uhn.fhir.jpa.empi.broker.EmpiSubscriptionLoader;
import ca.uhn.fhir.jpa.empi.svc.EmpiCandidateSearchSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchFinderSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiPersonFindingSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiPersonMergerSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiResourceDaoSvc;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Configuration
@Import(EmpiSharedConfig.class)
public class EmpiConsumerConfig {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	IEmpiSettings myEmpiProperties;
	@Autowired
	EmpiRuleValidator myEmpiRuleValidator;
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
	PersonHelper personHelper(FhirContext theFhirContext) {
		return new PersonHelper(theFhirContext);
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
	EmpiRuleValidator empiRuleValidator() {
		return new EmpiRuleValidator();
	}

	@Bean
	IEmpiMatchFinderSvc empiMatchFinderSvc() {
		return new EmpiMatchFinderSvcImpl();
	}

	@Bean
	IEmpiPersonMergerSvc empiPersonMergerSvc() {
		return new EmpiPersonMergerSvcImpl();
	}

	@Bean
	EmpiCandidateSearchSvc empiCandidateSearchSvc() {
		return new EmpiCandidateSearchSvc();
	}

	@Bean
	EmpiResourceComparatorSvc empiResourceComparatorSvc(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		return new EmpiResourceComparatorSvc(theFhirContext, theEmpiConfig);
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
