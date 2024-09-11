package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.junit.jupiter.api.AfterAll;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(classes = {
	DatabaseVerificationWithCitusIT.TestConfig.class
})
public class DatabaseVerificationWithCitusIT extends BaseDatabaseVerificationIT {

	static void beforeSpringRunsTheMigrator() {
		HapiSystemProperties.enableRunAllMigrations(true);
	}

	@AfterAll
	static void afterAll() {
		HapiSystemProperties.enableRunAllMigrations(false);
	}

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			// sneak this in before Spring starts and runs the migration.
			beforeSpringRunsTheMigrator();

			return new JpaDatabaseContextConfigParamObject(
				new PostgresEmbeddedDatabase(new PostgreSQLContainer<>(DockerImageName.parse("citusdata/citus:latest").asCompatibleSubstituteFor("postgres"))),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}


}
