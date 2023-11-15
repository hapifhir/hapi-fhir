/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicValidatingInterceptor;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu2.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import javax.annotation.PostConstruct;

public class SubscriptionSubmitInterceptorLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionSubmitInterceptorLoader.class);

	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	@Autowired
	private SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;

	@Autowired(required = false)
	private SubscriptionTopicValidatingInterceptor mySubscriptionTopicValidatingInterceptor;

	@Autowired
	private StorageSettings myStorageSettings;

	@Autowired
	private IInterceptorService myInterceptorRegistry;

	private boolean mySubscriptionValidatingInterceptorRegistered;
	private boolean mySubscriptionMatcherInterceptorRegistered;
	private boolean mySubscriptionTopicValidatingInterceptorRegistered;

	@PostConstruct
	public void start() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes =
				myStorageSettings.getSupportedSubscriptionTypes();

		if (supportedSubscriptionTypes.isEmpty()) {
			ourLog.info(
					"Subscriptions are disabled on this server.  Subscriptions will not be activated and incoming resources will not be matched against subscriptions.");
		} else {
			if (!mySubscriptionMatcherInterceptorRegistered) {
				ourLog.info("Registering subscription matcher interceptor");
				myInterceptorRegistry.registerInterceptor(mySubscriptionMatcherInterceptor);
				mySubscriptionMatcherInterceptorRegistered = true;
			}
		}

		if (!mySubscriptionValidatingInterceptorRegistered) {
			myInterceptorRegistry.registerInterceptor(mySubscriptionValidatingInterceptor);
			mySubscriptionValidatingInterceptorRegistered = true;
		}

		if (mySubscriptionTopicValidatingInterceptor != null && !mySubscriptionTopicValidatingInterceptorRegistered) {
			myInterceptorRegistry.registerInterceptor(mySubscriptionTopicValidatingInterceptor);
			mySubscriptionTopicValidatingInterceptorRegistered = true;
		}
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionMatcherInterceptor);
		myInterceptorRegistry.unregisterInterceptor(mySubscriptionValidatingInterceptor);
		mySubscriptionValidatingInterceptorRegistered = false;
		mySubscriptionMatcherInterceptorRegistered = false;
	}
}
