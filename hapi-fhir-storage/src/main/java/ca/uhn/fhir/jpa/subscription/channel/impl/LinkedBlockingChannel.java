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

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingChannel extends ExecutorSubscribableChannel implements IChannelProducer, IChannelReceiver {

	private final String myName;
	private final BlockingQueue<?> myQueue;

	public LinkedBlockingChannel(String theName, Executor theExecutor, BlockingQueue<?> theQueue) {
		super(wrapExecutor(theExecutor));
		myName = theName;
		myQueue = theQueue;
	}

	public int getQueueSizeForUnitTest() {
		return myQueue.size();
	}

	public void clearInterceptorsForUnitTest() {
		setInterceptors(new ArrayList<>());
	}

	@Override
	public String getName() {
		return myName;
	}

	@Override
	public void destroy() {
		// nothing
	}

	private static class WrappedExecutor implements Executor {
		private final Executor myWrap;

		public WrappedExecutor(Executor theExecutor) {
			myWrap = theExecutor;
		}

		@Override
		public void execute(@Nonnull Runnable command) {
			myWrap.execute(new WrappedRunnable(command));
		}

		private class WrappedRunnable implements Runnable {
			private final Runnable myWrappedRunnable;

			public WrappedRunnable(Runnable theCommand) {
				myWrappedRunnable = theCommand;
			}

			@Override
			public void run() {
				RetryTemplate retryTemplate = new RetryTemplate();
				final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
				fixedBackOffPolicy.setBackOffPeriod(DateUtils.MILLIS_PER_MINUTE);
				retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

				final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(100, Collections.singletonMap(Exception.class, true));
				retryTemplate.setRetryPolicy(retryPolicy);
				retryTemplate.execute(new RetryCallback<Object, InternalErrorException>() {
					@Override
					public Object doWithRetry(RetryContext context) {
						myWrappedRunnable.run();
						return null;
					}
				});
			}
		}
	}

	private static Executor wrapExecutor(Executor theExecutor) {
		return new WrappedExecutor(theExecutor);
	}

	/**
	 * Creates a synchronous channel, mostly intended for testing
	 */
	public static LinkedBlockingChannel newSynchronous(String theName) {
		return new LinkedBlockingChannel(theName, null, new LinkedBlockingQueue<>(1));
	}

}
