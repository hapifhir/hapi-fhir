package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabasePartitionModeWithPostgresIT.TestConfig.class
})
public class DatabasePartitionModeWithPostgresIT extends BaseDatabasePartitionModeIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject(
				new PostgresEmbeddedDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}
}
