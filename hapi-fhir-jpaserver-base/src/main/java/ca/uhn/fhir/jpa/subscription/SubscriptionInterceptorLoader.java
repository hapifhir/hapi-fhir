package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.channel.SubscriptionChannelRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu2.model.Subscription;
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
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;
	@Autowired
	private ApplicationContext myApplicationContext;
	@Autowired
	private IInterceptorService myInterceptorRegistry;

	public void registerInterceptors() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (supportedSubscriptionTypes.isEmpty()) {
			ourLog.info("Subscriptions are disabled on this server.  Subscriptions will not be activated and incoming resources will not be matched against subscriptions.");
		} else {
			loadSubscriptions();
			ourLog.info("Registering subscription activating interceptor");
			myInterceptorRegistry.registerInterceptor(mySubscriptionActivatingInterceptor);
			if (myDaoConfig.isSubscriptionMatchingEnabled()) {
				mySubscriptionMatcherInterceptor.start();
				ourLog.info("Registering subscription matcher interceptor");
				myInterceptorRegistry.registerInterceptor(mySubscriptionMatcherInterceptor);
			}
		}
	}

	private void loadSubscriptions() {
		ourLog.info("Loading subscriptions into the SubscriptionRegistry...");
		// Load active subscriptions into the SubscriptionRegistry and activate their channels
		SubscriptionLoader loader = myApplicationContext.getBean(SubscriptionLoader.class);
		loader.syncSubscriptions();
		ourLog.info("...{} subscriptions loaded", mySubscriptionRegistry.size());
		ourLog.info("...{} subscription channels started", mySubscriptionChannelRegistry.size());
	}

	@VisibleForTesting
	void unregisterInterceptorsForUnitTest() {
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionActivatingInterceptor);
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionMatcherInterceptor);
		mySubscriptionMatcherInterceptor.preDestroy();
	}
}
