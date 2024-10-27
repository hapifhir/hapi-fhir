package ca.uhn.fhir.jpa.test;

//import jakarta.persistence.EntityManagerFactory;

//import org.hibernate.jpa.HibernatePersistenceProvider;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement()
public class FhirServerConfig {

	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings retVal = new JpaStorageSettings();
		return retVal;
	}
}
