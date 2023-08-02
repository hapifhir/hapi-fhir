package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
	@Bean
	IRepositoryFactory repositoryFactory(DaoRegistry theDaoRegistry, RestfulServer theRestfulServer) {
		return rd -> new HapiFhirRepository(theDaoRegistry, rd, theRestfulServer);
	}
}
