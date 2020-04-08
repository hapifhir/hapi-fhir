package ca.uhn.fhir.jpa.test;

//import javax.persistence.EntityManagerFactory;

//import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
	import org.springframework.transaction.annotation.EnableTransactionManagement;

	import ca.uhn.fhir.jpa.api.config.DaoConfig;

@Configuration
@EnableTransactionManagement()
public class FhirServerConfig {

	@Bean
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		return retVal;
	}


}
