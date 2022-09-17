package ca.uhn.fhir.jpa.migrate.config;

import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HapiMigrationConfig {
	@Bean
	HapiMigrationStorageSvc myHapiMigrationStorageSvc(HapiMigrationDao theHapiMigrationDao) {
		return new HapiMigrationStorageSvc(theHapiMigrationDao);
	}
}
