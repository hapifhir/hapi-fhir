package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.subscription.SubscriptionMessagePersistenceImpl;
import ca.uhn.fhir.subscription.api.ISubscriptionMessagePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaSubscriptionMessagePersistenceConfig {

	@Bean
	public ISubscriptionMessagePersistence subscriptionMessagePersistence(){
		return new SubscriptionMessagePersistenceImpl();
	}
}
