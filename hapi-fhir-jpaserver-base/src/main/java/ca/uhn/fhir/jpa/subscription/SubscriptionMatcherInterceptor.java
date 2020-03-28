package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.interceptor.BaseResourceModifiedInterceptor;
import ca.uhn.fhir.jpa.subscription.module.channel.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionMatchingSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

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

@Component
@Lazy
@Interceptor
public class SubscriptionMatcherInterceptor extends BaseResourceModifiedInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);

	public static final String SUBSCRIPTION_MATCHING_CHANNEL_NAME = "subscription-matching";
	@Autowired
	protected SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Autowired
	private SubscriptionMatchingSubscriber mySubscriptionMatchingSubscriber;


	@Override
	protected String getMatchingChannelName() {
		return SUBSCRIPTION_MATCHING_CHANNEL_NAME;
	}

	@Override
	protected MessageHandler getSubscriber() {
		return mySubscriptionMatchingSubscriber;
	}

	@Override
	protected SubscribableChannel createMatchingChannel() {
		return mySubscriptionChannelFactory.newMatchingChannel(getMatchingChannelName());
	}

	@Nonnull
	@Override
	protected Pointcut getSubmitPointcut() {
		return Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED;
	}
}
