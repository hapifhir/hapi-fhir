package ca.uhn.fhir.jpa.empi.broker;

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

import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.empi.api.IEmpiRuleValidator;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiInterceptor;
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
	IEmpiProperties myEmpiConfig;
	@Autowired
	IEmpiRuleValidator myEmpiRuleValidator;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;
	@Autowired
	EmpiInterceptor myEmpiInterceptor;

	@EventListener(classes = {ContextRefreshedEvent.class})
	// FIXME KHS this can probably go now
	// This @Order is here to ensure that MatchingQueueSubscriberLoader has initialized before we initialize this.
	// Otherwise the EMPI subscriptions won't get loaded into the SubscriptionRegistry
	@Order
	public void init() {
		if (!myEmpiConfig.isEnabled()) {
			return;
		}
		myEmpiRuleValidator.validate(myEmpiConfig.getEmpiRules());
		myInterceptorService.registerInterceptor(myEmpiInterceptor);
		ourLog.info("EMPI interceptor registered");

		EmpiProviderLoader empiProviderLoader = myApplicationContext.getBean(EmpiProviderLoader.class);
		empiProviderLoader.loadProvider();

		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
	}
}
