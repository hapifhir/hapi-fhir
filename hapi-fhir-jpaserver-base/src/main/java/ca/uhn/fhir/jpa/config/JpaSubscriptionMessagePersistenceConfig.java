package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessagePersistenceSvcImpl;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaSubscriptionMessagePersistenceConfig {

	@Bean
	public IResourceModifiedMessagePersistenceSvc subscriptionMessagePersistence(FhirContext theFhirContext, IResourceModifiedDao theIResourceModifiedDao, ObjectMapper theObjectMapper, DaoRegistry theDaoRegistry){
		return new ResourceModifiedMessagePersistenceSvcImpl(theFhirContext, theIResourceModifiedDao, theDaoRegistry, theObjectMapper);
	}
}
