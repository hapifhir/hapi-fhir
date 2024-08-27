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

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import jakarta.annotation.Nonnull;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class LinkedBlockingChannel extends ExecutorSubscribableChannel implements IChannelProducer, IChannelReceiver {

	private final String myName;
	private final Supplier<Integer> myQueueSizeSupplier;

	public LinkedBlockingChannel(String theName, Executor theExecutor, Supplier<Integer> theQueueSizeSupplier) {
		super(theExecutor);
		myName = theName;
		myQueueSizeSupplier = theQueueSizeSupplier;
	}

	public int getQueueSizeForUnitTest() {
		return defaultIfNull(myQueueSizeSupplier.get(), 0);
	}

	public void clearInterceptorsForUnitTest() {
		setInterceptors(new ArrayList<>());
	}

	@Override
	public String getName() {
		return myName;
	}

	@Override
	public boolean hasSubscription(@Nonnull MessageHandler handler) {
		return getSubscribers().stream()
				.map(t -> (RetryingMessageHandlerWrapper) t)
				.anyMatch(t -> t.getWrappedHandler() == handler);
	}

	@Override
	public boolean subscribe(@Nonnull MessageHandler theHandler) {
		return super.subscribe(new RetryingMessageHandlerWrapper(theHandler, getName()));
	}

	@Override
	public boolean unsubscribe(@Nonnull MessageHandler handler) {
		Optional<RetryingMessageHandlerWrapper> match = getSubscribers().stream()
				.map(t -> (RetryingMessageHandlerWrapper) t)
				.filter(t -> t.getWrappedHandler() == handler)
				.findFirst();
		match.ifPresent(super::unsubscribe);
		return match.isPresent();
	}

	@Override
	public void destroy() {
		// nothing
	}

	/**
	 * Creates a synchronous channel, mostly intended for testing
	 */
	public static LinkedBlockingChannel newSynchronous(String theName) {
		return new LinkedBlockingChannel(theName, null, () -> 0);
	}
}
