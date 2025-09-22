/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.channel.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelSettings;
import ca.uhn.fhir.broker.jms.ISpringMessagingChannelProducer;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.ThreadPoolUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LinkedBlockingChannelFactory {

	private final IChannelNamer myChannelNamer;
	private final Map<String, LinkedBlockingChannel> myChannels = Collections.synchronizedMap(new HashMap<>());

	protected RetryPolicyProvider myRetryPolicyProvider;

	public LinkedBlockingChannelFactory(IChannelNamer theChannelNamer, RetryPolicyProvider theRetryPolicyProvider) {
		myChannelNamer = theChannelNamer;
		myRetryPolicyProvider = theRetryPolicyProvider;
	}

	public LinkedBlockingChannel getOrCreateReceiver(
			String theChannelName, ChannelConsumerSettings theChannelSettings) {
		return getOrCreateChannel(theChannelName, theChannelSettings.getConcurrentConsumers(), theChannelSettings);
	}

	public ISpringMessagingChannelProducer getOrCreateProducer(
			String theChannelName, ChannelProducerSettings theChannelSettings) {
		return getOrCreateChannel(theChannelName, theChannelSettings.getConcurrentConsumers(), theChannelSettings);
	}

	public IChannelNamer getChannelNamer() {
		return myChannelNamer;
	}

	private LinkedBlockingChannel getOrCreateChannel(
			String theChannelName, int theConcurrentConsumers, IChannelSettings theChannelSettings) {
		// TODO - does this need retry settings?
		final String channelName = myChannelNamer.getChannelName(theChannelName, theChannelSettings);

		return myChannels.computeIfAbsent(
				channelName, t -> buildLinkedBlockingChannel(theConcurrentConsumers, channelName));
	}

	@Nonnull
	protected LinkedBlockingChannel buildLinkedBlockingChannel(int theConcurrentConsumers, String theChannelName) {
		String threadNamePrefix = theChannelName + "-";
		ThreadPoolTaskExecutor threadPoolExecutor = ThreadPoolUtil.newThreadPool(
				theConcurrentConsumers,
				theConcurrentConsumers,
				threadNamePrefix,
				SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE);

		return new LinkedBlockingChannel(
				theChannelName, threadPoolExecutor, threadPoolExecutor::getQueueSize, myRetryPolicyProvider);
	}

	@PreDestroy
	public void stop() {
		myChannels.clear();
	}
}
