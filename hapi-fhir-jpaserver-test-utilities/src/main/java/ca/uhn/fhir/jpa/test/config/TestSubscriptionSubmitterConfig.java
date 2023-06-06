package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.jpa.subscription.SynchronousSubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSubscriptionSubmitterConfig extends SubscriptionSubmitterConfig {

	@Override
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		return new SynchronousSubscriptionMatcherInterceptor();
	}

}
