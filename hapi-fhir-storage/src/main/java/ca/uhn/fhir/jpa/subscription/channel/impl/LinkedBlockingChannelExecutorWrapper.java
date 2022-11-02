package ca.uhn.fhir.jpa.subscription.channel.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * This class wraps an {@link Executor} used by the channel in order to provide automatic
 * failure retries.
 */
class LinkedBlockingChannelExecutorWrapper implements Executor {
	private static final Logger ourLog = LoggerFactory.getLogger(LinkedBlockingChannelExecutorWrapper.class);
	private final Executor myWrap;
	private final String myChannelName;

	public LinkedBlockingChannelExecutorWrapper(Executor theExecutor, String theChannelName) {
		myWrap = theExecutor;
		myChannelName = theChannelName;
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
					ourLog.error("Failure {} processing message in channel[{}]", theContext.getRetryCount() + 1, myChannelName, theThrowable);
				}
			};
			retryTemplate.setListeners(new RetryListener[]{retryListener});
			retryTemplate.execute(context -> {
				myWrappedRunnable.run();
				return null;
			});
		}
	}
}

