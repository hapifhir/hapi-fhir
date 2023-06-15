package ca.uhn.fhir.jpa.test;

// import javax.persistence.EntityManagerFactory;

// import org.hibernate.jpa.HibernatePersistenceProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;

@Configuration
@EnableTransactionManagement()
public class FhirServerConfig {

    @Bean
    public JpaStorageSettings storageSettings() {
        JpaStorageSettings retVal = new JpaStorageSettings();
        return retVal;
    }
}
