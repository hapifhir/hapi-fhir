package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.subscription.SubscriptionMessagePersistenceSvcImpl;
import ca.uhn.fhir.subscription.api.ISubscriptionMessagePersistenceSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaSubscriptionMessagePersistenceConfig {

	@Bean
	public ISubscriptionMessagePersistenceSvc subscriptionMessagePersistence(){
		return new SubscriptionMessagePersistenceSvcImpl();
	}
}
