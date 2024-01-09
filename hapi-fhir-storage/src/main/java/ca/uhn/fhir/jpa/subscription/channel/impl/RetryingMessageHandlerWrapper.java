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

import ca.uhn.fhir.util.BaseUnrecoverableRuntimeException;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.CannotCreateTransactionException;

class RetryingMessageHandlerWrapper implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(RetryingMessageHandlerWrapper.class);
	private final MessageHandler myWrap;
	private final String myChannelName;

	RetryingMessageHandlerWrapper(MessageHandler theWrap, String theChannelName) {
		myWrap = theWrap;
		myChannelName = theChannelName;
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		RetryTemplate retryTemplate = new RetryTemplate();
		final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(1000);
		backOffPolicy.setMultiplier(1.1d);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		final TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
		retryPolicy.setTimeout(DateUtils.MILLIS_PER_MINUTE);
		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setThrowLastExceptionOnExhausted(true);
		RetryListener retryListener = new RetryListenerSupport() {
			@Override
			public <T, E extends Throwable> void onError(
					RetryContext theContext, RetryCallback<T, E> theCallback, Throwable theThrowable) {
				ourLog.error(
						"Failure {} processing message in channel[{}]: {}",
						theContext.getRetryCount(),
						myChannelName,
						theThrowable.toString());
				ourLog.error("Failure", theThrowable);
				if (theThrowable instanceof BaseUnrecoverableRuntimeException) {
					theContext.setExhaustedOnly();
				}
				if (ExceptionUtils.indexOfThrowable(theThrowable, CannotCreateTransactionException.class) != -1) {
					/*
					 * This exception means that we can't open a transaction, which
					 * means the EntityManager is closed. This can happen if we are shutting
					 * down while there is still a message in the queue - No sense
					 * retrying indefinitely in that case
					 */
					theContext.setExhaustedOnly();
				}
			}
		};
		retryTemplate.setListeners(new RetryListener[] {retryListener});
		retryTemplate.execute(context -> {
			myWrap.handleMessage(theMessage);
			return null;
		});
	}

	public MessageHandler getWrappedHandler() {
		return myWrap;
	}
}
