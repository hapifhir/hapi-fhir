package ca.uhn.fhir.jpa.subscription.channel.impl;

import ca.uhn.fhir.util.BaseUnrecoverableRuntimeException;
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

import javax.annotation.Nonnull;

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
			public <T, E extends Throwable> void onError(RetryContext theContext, RetryCallback<T, E> theCallback, Throwable theThrowable) {
				ourLog.error("Failure {} processing message in channel[{}]: {}", theContext.getRetryCount(), myChannelName, theThrowable.toString());
				ourLog.error("Failure", theThrowable);
				if (theThrowable instanceof BaseUnrecoverableRuntimeException) {
					theContext.setExhaustedOnly();
				}
			}
		};
		retryTemplate.setListeners(new RetryListener[]{retryListener});
		retryTemplate.execute(context -> {
			myWrap.handleMessage(theMessage);
			return null;
		});
	}

	public MessageHandler getWrappedHandler() {
		return myWrap;
	}
}
