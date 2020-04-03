package ca.uhn.fhir.jpa.subscription.submit.config;

import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubmitInterceptorLoader;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This Spring config should be imported by a system that submits resources to the
 * matching queue for processing
 */
@Configuration
public class SubscriptionSubmitterConfig {

	@Bean
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		return new SubscriptionMatcherInterceptor();
	}

	@Bean
	public SubscriptionValidatingInterceptor subscriptionValidatingInterceptor() {
		return new SubscriptionValidatingInterceptor();
	}

	@Bean
	public SubmitInterceptorLoader subscriptionMatcherInterceptorLoader() {
		return new SubmitInterceptorLoader();
	}

}
