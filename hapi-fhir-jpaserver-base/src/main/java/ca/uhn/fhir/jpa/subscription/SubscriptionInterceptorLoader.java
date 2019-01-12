package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

	// TODO KHS these beans are late loaded because we don't want to run their @PostConstruct and @Scheduled method if they're
	// not required.  Recommend removing @PostConstruct from these classes and instead call those methods in register interceptors below.
	// @Schedule will be tricker to resolve

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
			if (mySubscriptionActivatingInterceptor == null) {
				mySubscriptionActivatingInterceptor = myAppicationContext.getBean(SubscriptionActivatingInterceptor.class);
			}
			ourLog.info("Registering subscription activating interceptor");
			myDaoConfig.registerInterceptor(mySubscriptionActivatingInterceptor);
		}
		if (myDaoConfig.isSubscriptionMatchingEnabled()) {
			if (mySubscriptionMatcherInterceptor == null) {
				mySubscriptionMatcherInterceptor = myAppicationContext.getBean(SubscriptionMatcherInterceptor.class);
			}
			ourLog.info("Registering subscription matcher interceptor");
			myDaoConfig.registerInterceptor(mySubscriptionMatcherInterceptor);

		}
	}

	private void loadSubscriptions() {
		ourLog.info("Loading subscriptions into the SubscriptionRegistry...");
		// Load subscriptions into the SubscriptionRegistry
		myAppicationContext.getBean(SubscriptionLoader.class);
		ourLog.info("...{} subscriptions loaded", mySubscriptionRegistry.size());
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myDaoConfig.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		myDaoConfig.unregisterInterceptor(mySubscriptionMatcherInterceptor);
	}
}
