package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithPostgresIT.TestConfig.class
})
public class DatabaseVerificationWithPostgresIT extends BaseDatabaseVerificationIT {


	@RegisterExtension
	private static HapiEmbeddedDatabasesExtension myExtension = HapiEmbeddedDatabasesExtension.forDatabase(DriverTypeEnum.POSTGRES_9_4);

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				myExtension.getOnlyDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}


}
