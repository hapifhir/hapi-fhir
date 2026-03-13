package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabasePartitionModeWithMsSqlIT.TestConfig.class
})
public class DatabasePartitionModeWithMsSqlIT extends BaseDatabasePartitionModeIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject(
				new MsSqlEmbeddedDatabase(),
				HapiFhirSQLServerDialect.class.getName()
			);
		}
	}
}
