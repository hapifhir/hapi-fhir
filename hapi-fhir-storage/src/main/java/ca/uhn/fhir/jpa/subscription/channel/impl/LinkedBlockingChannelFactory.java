package ca.uhn.fhir.jpa.subscription.channel.impl;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
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
	private final IChannelNamer myChannelNamer;
	private final Map<String, LinkedBlockingChannel> myChannels = Collections.synchronizedMap(new HashMap<>());

	public LinkedBlockingChannelFactory(IChannelNamer theChannelNamer) {
		myChannelNamer = theChannelNamer;
	}

	@Override
	public IChannelReceiver getOrCreateReceiver(String theChannelName, Class<?> theMessageType, ChannelConsumerSettings theChannelSettings) {
		return getOrCreateChannel(theChannelName, theChannelSettings.getConcurrentConsumers(), theChannelSettings);
	}

	@Override
	public IChannelProducer getOrCreateProducer(String theChannelName, Class<?> theMessageType, ChannelProducerSettings theChannelSettings) {
		return getOrCreateChannel(theChannelName, theChannelSettings.getConcurrentConsumers(), theChannelSettings);
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return myChannelNamer;
	}

	private LinkedBlockingChannel getOrCreateChannel(String theChannelName,
																	 int theConcurrentConsumers,
																	 IChannelSettings theChannelSettings) {
		// TODO - does this need retry settings?
		final String channelName = myChannelNamer.getChannelName(theChannelName, theChannelSettings);

		return myChannels.computeIfAbsent(channelName, t -> {

			String threadNamingPattern = channelName + "-%d";

			ThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.namingPattern(threadNamingPattern)
				.uncaughtExceptionHandler(uncaughtExceptionHandler(channelName))
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
					throw new RejectedExecutionException(Msg.code(568) + "Task " + theRunnable.toString() +
						" rejected from " + e);
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			};
			ThreadPoolExecutor executor = new ThreadPoolExecutor(
				theConcurrentConsumers,
				theConcurrentConsumers,
				0L,
				TimeUnit.MILLISECONDS,
				queue,
				threadFactory,
				rejectedExecutionHandler);
			return new LinkedBlockingChannel(channelName, executor, queue);

		});
	}

	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler(String theChannelName) {
		return new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				ourLog.error("Failure handling message in channel {}", theChannelName, e);
			}
		};
	}


	@PreDestroy
	public void stop() {
		myChannels.clear();
	}

}
