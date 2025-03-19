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

import ca.uhn.fhir.broker.jms.ISpringMessagingChannelProducer;
import ca.uhn.fhir.broker.jms.ISpringMessagingChannelReceiver;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.VisibleForTesting;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class LinkedBlockingChannel extends ExecutorSubscribableChannel
		implements ISpringMessagingChannelProducer, ISpringMessagingChannelReceiver {

	private final String myName;
	private final Supplier<Integer> myQueueSizeSupplier;

	private final RetryPolicyProvider myRetryPolicyProvider;

	public LinkedBlockingChannel(
			String theName,
			Executor theExecutor,
			Supplier<Integer> theQueueSizeSupplier,
			RetryPolicyProvider theRetryPolicyProvider) {
		super(theExecutor);
		myName = theName;
		myQueueSizeSupplier = theQueueSizeSupplier;
		myRetryPolicyProvider = theRetryPolicyProvider;
	}

	@VisibleForTesting
	public int getQueueSizeForUnitTest() {
		return defaultIfNull(myQueueSizeSupplier.get(), 0);
	}

	@VisibleForTesting
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
		return super.subscribe(new RetryingMessageHandlerWrapper(theHandler, getName(), myRetryPolicyProvider));
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
	@VisibleForTesting
	public static LinkedBlockingChannel newSynchronous(String theName, RetryPolicyProvider theRetryPolicyProvider) {
		return new LinkedBlockingChannel(theName, null, () -> 0, theRetryPolicyProvider);
	}
}
