/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicValidatingInterceptor;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.dstu2.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SubscriptionSubmitInterceptorLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionSubmitInterceptorLoader.class);

	@Nonnull
	private IInterceptorService myInterceptorService;

	@Nonnull
	private final SubscriptionSettings mySubscriptionSettings;

	@Nonnull
	private final SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	@Nonnull
	private final SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;

	@Nullable
	private final SubscriptionTopicValidatingInterceptor mySubscriptionTopicValidatingInterceptor;

	private boolean mySubscriptionValidatingInterceptorRegistered;
	private boolean mySubscriptionMatcherInterceptorRegistered;
	private boolean mySubscriptionTopicValidatingInterceptorRegistered;

	public SubscriptionSubmitInterceptorLoader(
			@Nonnull IInterceptorService theInterceptorService,
			@Nonnull SubscriptionSettings theSubscriptionSettings,
			@Nonnull SubscriptionMatcherInterceptor theSubscriptionMatcherInterceptor,
			@Nonnull SubscriptionValidatingInterceptor theSubscriptionValidatingInterceptor,
			@Nullable SubscriptionTopicValidatingInterceptor theSubscriptionTopicValidatingInterceptor) {
		setInterceptorService(theInterceptorService);
		mySubscriptionSettings = theSubscriptionSettings;
		mySubscriptionMatcherInterceptor = theSubscriptionMatcherInterceptor;
		mySubscriptionValidatingInterceptor = theSubscriptionValidatingInterceptor;
		mySubscriptionTopicValidatingInterceptor = theSubscriptionTopicValidatingInterceptor;
	}

	@PostConstruct
	public void start() {
		Set<Subscription.SubscriptionChannelType> supportedSubscriptionTypes =
				mySubscriptionSettings.getSupportedSubscriptionTypes();

		if (supportedSubscriptionTypes.isEmpty()) {
			ourLog.info(
					"Subscriptions are disabled on this server.  Subscriptions will not be activated and incoming resources will not be matched against subscriptions.");
		} else {
			if (!mySubscriptionMatcherInterceptorRegistered) {
				ourLog.info("Registering subscription matcher interceptor");
				myInterceptorService.registerInterceptor(mySubscriptionMatcherInterceptor);
				mySubscriptionMatcherInterceptorRegistered = true;
			}
		}

		if (!mySubscriptionValidatingInterceptorRegistered) {
			myInterceptorService.registerInterceptor(mySubscriptionValidatingInterceptor);
			mySubscriptionValidatingInterceptorRegistered = true;
		}

		if (mySubscriptionTopicValidatingInterceptor != null && !mySubscriptionTopicValidatingInterceptorRegistered) {
			myInterceptorService.registerInterceptor(mySubscriptionTopicValidatingInterceptor);
			mySubscriptionTopicValidatingInterceptorRegistered = true;
		}
	}

	protected void setInterceptorService(IInterceptorService theInterceptorService) {
		myInterceptorService = theInterceptorService;
	}

	protected IInterceptorService getInterceptorService() {
		return myInterceptorService;
	}

	@VisibleForTesting
	public void unregisterInterceptorsForUnitTest() {
		myInterceptorService.unregisterInterceptor(mySubscriptionMatcherInterceptor);
		myInterceptorService.unregisterInterceptor(mySubscriptionValidatingInterceptor);
		mySubscriptionValidatingInterceptorRegistered = false;
		mySubscriptionMatcherInterceptorRegistered = false;
	}
}
