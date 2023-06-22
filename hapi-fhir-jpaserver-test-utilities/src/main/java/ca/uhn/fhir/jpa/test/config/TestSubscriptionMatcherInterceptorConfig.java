package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Production environments submit modified resources to the subscription processing pipeline asynchronously, ie, a
 * modified resource is 'planned' for submission which is performed at a later time by a scheduled task.
 *
 * The purpose of this class is to provide submission of modified resources during tests since task scheduling required
 * for asynchronous submission are either disabled or not present in testing context.
 *
 * Careful consideration is advised when configuring test context as the SubscriptionMatcherInterceptor Bean instantiated
 * below will overwrite the Bean provided by class SubscriptionMatcherInterceptorConfig if both configuration classes
 * are present in the context.
 */
@Configuration
public class TestSubscriptionMatcherInterceptorConfig {

	@Primary
	@Bean
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		return new SynchronousSubscriptionMatcherInterceptor();
	}

}
