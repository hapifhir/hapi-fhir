package ca.uhn.fhir.jpa.subscription.channel.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RetryPolicyProvider {

	public RetryTemplate getRetryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setBackOffPolicy(backOffPolicy());
		retryTemplate.setRetryPolicy(retryPolicy());
		retryTemplate.setThrowLastExceptionOnExhausted(true);

		return retryTemplate;
	}

	protected RetryPolicy retryPolicy() {
		TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
		retryPolicy.setTimeout(DateUtils.MILLIS_PER_MINUTE);
		return retryPolicy;
	}

	protected BackOffPolicy backOffPolicy() {
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(1000);
		backOffPolicy.setMultiplier(1.1d);
		return backOffPolicy;
	}
}
