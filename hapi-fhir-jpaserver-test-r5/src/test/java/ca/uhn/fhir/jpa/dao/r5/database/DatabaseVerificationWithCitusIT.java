package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private PartitionSettings myPartitionSettings;

	@BeforeEach
	void setUp() {
		// we can't insert null in partition_id.
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(0);
		myRequestDetails.setRequestPartitionId(requestPartitionId);
		myTestDaoSearch.setRequestPartitionId(requestPartitionId);
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
