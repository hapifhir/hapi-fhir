package ca.uhn.fhir.jpa.subscription.channel.impl;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingChannelFactory implements IChannelFactory {

	private static final Logger ourLog = LoggerFactory.getLogger(LinkedBlockingChannelFactory.class);
	private Map<String, LinkedBlockingChannel> myChannels = Collections.synchronizedMap(new HashMap<>());

	/**
	 * Constructor
	 */
	public LinkedBlockingChannelFactory() {
		super();
	}

	@Override
	public IChannelReceiver getOrCreateReceiver(String theChannelName, Class<?> theMessageType, ChannelConsumerSettings theConfig) {
		return getOrCreateChannel(theChannelName, theConfig.getConcurrentConsumers());
	}

	@Override
	public IChannelProducer getOrCreateProducer(String theChannelName, Class<?> theMessageType, ChannelConsumerSettings theConfig) {
		return getOrCreateChannel(theChannelName, theConfig.getConcurrentConsumers());
	}

	private LinkedBlockingChannel getOrCreateChannel(String theChannelName, int theConcurrentConsumers) {
		return myChannels.computeIfAbsent(theChannelName, t -> {

			String threadNamingPattern = theChannelName + "-%d";

			ThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.namingPattern(threadNamingPattern)
				.daemon(false)
				.priority(Thread.NORM_PRIORITY)
				.build();

			LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(SubscriptionConstants.DELIVERY_EXECUTOR_QUEUE_SIZE);
			RejectedExecutionHandler rejectedExecutionHandler = (theRunnable, theExecutor) -> {
				ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", queue.size());
				StopWatch sw = new StopWatch();
				try {
					queue.put(theRunnable);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new RejectedExecutionException("Task " + theRunnable.toString() +
						" rejected from " + e.toString());
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			};
			ThreadPoolExecutor executor = new ThreadPoolExecutor(
				1,
				theConcurrentConsumers,
				0L,
				TimeUnit.MILLISECONDS,
				queue,
				threadFactory,
				rejectedExecutionHandler);
			return new LinkedBlockingChannel(executor, queue);

		});
	}


	@PreDestroy
	public void stop() {
		myChannels.clear();
	}

}
