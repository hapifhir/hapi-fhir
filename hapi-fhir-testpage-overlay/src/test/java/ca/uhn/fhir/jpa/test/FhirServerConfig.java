package ca.uhn.fhir.jpa.test;

//import javax.persistence.EntityManagerFactory;

//import org.hibernate.jpa.HibernatePersistenceProvider;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement()
public class FhirServerConfig {

	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		return retVal;
	}
}
