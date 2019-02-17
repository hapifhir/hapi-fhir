package ca.uhn.fhir.jpa.subscription.module;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import java.util.ArrayList;
import java.util.concurrent.*;

public class LinkedBlockingQueueSubscribableChannel implements SubscribableChannel {
	private Logger ourLog = LoggerFactory.getLogger(LinkedBlockingQueueSubscribableChannel.class);

	private final ExecutorSubscribableChannel mySubscribableChannel;
	private final BlockingQueue<Runnable> myQueue;

	public LinkedBlockingQueueSubscribableChannel(BlockingQueue<Runnable> theQueue, String theThreadNamingPattern, int theConcurrentConsumers) {

		ThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern(theThreadNamingPattern)
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = (theRunnable, theExecutor) -> {
			ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", theQueue.size());
			StopWatch sw = new StopWatch();
			try {
				theQueue.put(theRunnable);
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
			theQueue,
			threadFactory,
			rejectedExecutionHandler);
		myQueue = theQueue;
		mySubscribableChannel = new ExecutorSubscribableChannel(executor);
	}

	@Override
	public boolean subscribe(MessageHandler handler) {
		return mySubscribableChannel.subscribe(handler);
	}

	@Override
	public boolean unsubscribe(MessageHandler handler) {
		return mySubscribableChannel.unsubscribe(handler);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return mySubscribableChannel.send(message, timeout);
	}

	@VisibleForTesting
	public void clearInterceptorsForUnitTest() {
		mySubscribableChannel.setInterceptors(new ArrayList<>());
	}

	@VisibleForTesting
	public void addInterceptorForUnitTest(ChannelInterceptor theInterceptor) {
		mySubscribableChannel.addInterceptor(theInterceptor);
	}

	@VisibleForTesting
	public int getQueueSizeForUnitTest() {
		return myQueue.size();
	}
}
