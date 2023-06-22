package ca.uhn.fhir.jpa.subscription.submit.config;

import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SynchronousSubscriptionMatcherInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static ca.uhn.fhir.jpa.sched.BaseSchedulerServiceImpl.SCHEDULING_DISABLED;

@Configuration
public class SubscriptionMatcherInterceptorConfig {

	@Autowired
	private Environment myEnvironment;

	@Bean
	public SubscriptionMatcherInterceptor subscriptionMatcherInterceptor() {
		if(isSchedulingDisabledForTests()){
			return new SynchronousSubscriptionMatcherInterceptor();
		}

		return new SubscriptionMatcherInterceptor();
	}

	private boolean isSchedulingDisabledForTests() {
		String schedulingDisabled = myEnvironment.getProperty(SCHEDULING_DISABLED);
		return "true".equals(schedulingDisabled);
	}


}
