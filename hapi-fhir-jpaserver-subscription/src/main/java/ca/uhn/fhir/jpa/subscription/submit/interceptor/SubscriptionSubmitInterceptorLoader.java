package ca.uhn.fhir.jpa.subscription.submit.interceptor;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu2.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Set;

public class SubscriptionSubmitInterceptorLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionSubmitInterceptorLoader.class);

	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	private SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorService myInterceptorRegistry;

	@PostConstruct
	public void start() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes = myDaoConfig.getSupportedSubscriptionTypes();

		if (supportedSubscriptionTypes.isEmpty()) {
			ourLog.info("Subscriptions are disabled on this server.  Subscriptions will not be activated and incoming resources will not be matched against subscriptions.");
		} else {
			ourLog.info("Registering subscription matcher interceptor");
			myInterceptorRegistry.registerInterceptor(mySubscriptionMatcherInterceptor);
		}

		myInterceptorRegistry.registerInterceptor(mySubscriptionValidatingInterceptor);
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionMatcherInterceptor);
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionValidatingInterceptor);
	}
}
