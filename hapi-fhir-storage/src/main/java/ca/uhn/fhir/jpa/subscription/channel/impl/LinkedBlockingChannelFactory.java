/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.subscription.channel.impl;

import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.ThreadPoolUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LinkedBlockingChannelFactory implements IChannelFactory {

	private final IChannelNamer myChannelNamer;
	private final Map<String, LinkedBlockingChannel> myChannels = Collections.synchronizedMap(new HashMap<>());

	public LinkedBlockingChannelFactory(IChannelNamer theChannelNamer) {
		myChannelNamer = theChannelNamer;
	}

	@Override
	public IChannelReceiver getOrCreateReceiver(
			String theChannelName, Class<?> theMessageType, ChannelConsumerSettings theChannelSettings) {
		return getOrCreateChannel(theChannelName, theChannelSettings.getConcurrentConsumers(), theChannelSettings);
	}

	@Override
	public IChannelProducer getOrCreateProducer(
			String theChannelName, Class<?> theMessageType, ChannelProducerSettings theChannelSettings) {
		return getOrCreateChannel(theChannelName, theChannelSettings.getConcurrentConsumers(), theChannelSettings);
	}

	@Override
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
	private LinkedBlockingChannel buildLinkedBlockingChannel(int theConcurrentConsumers, String theChannelName) {
		String threadNamePrefix = theChannelName + "-";
		ThreadPoolTaskExecutor threadPoolExecutor = ThreadPoolUtil.newThreadPool(
				theConcurrentConsumers,
				theConcurrentConsumers,
				threadNamePrefix,
				SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE);

		return new LinkedBlockingChannel(theChannelName, threadPoolExecutor, threadPoolExecutor::getQueueSize);
	}

	@PreDestroy
	public void stop() {
		myChannels.clear();
	}
}
