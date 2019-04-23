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
import ca.uhn.fhir.interceptor.api.IInterceptorService;
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

	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	private SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private ApplicationContext myApplicationContext;
	@Autowired
	private IInterceptorService myInterceptorRegistry;

	public void registerInterceptors() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (!supportedSubscriptionTypes.isEmpty()) {
			loadSubscriptions();
			ourLog.info("Registering subscription activating interceptor");
			myInterceptorRegistry.registerInterceptor(mySubscriptionActivatingInterceptor);
		}
		if (myDaoConfig.isSubscriptionMatchingEnabled()) {
			mySubscriptionMatcherInterceptor.start();
			ourLog.info("Registering subscription matcher interceptor");
			myInterceptorRegistry.registerInterceptor(mySubscriptionMatcherInterceptor);
		}
	}

	private void loadSubscriptions() {
		ourLog.info("Loading subscriptions into the SubscriptionRegistry...");
		// Activate scheduled subscription loads into the SubscriptionRegistry
		myApplicationContext.getBean(SubscriptionLoader.class);
		ourLog.info("...{} subscriptions loaded", mySubscriptionRegistry.size());
	}

	@VisibleForTesting
	void unregisterInterceptorsForUnitTest() {
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionMatcherInterceptor);
		mySubscriptionMatcherInterceptor.preDestroy();
	}
}
