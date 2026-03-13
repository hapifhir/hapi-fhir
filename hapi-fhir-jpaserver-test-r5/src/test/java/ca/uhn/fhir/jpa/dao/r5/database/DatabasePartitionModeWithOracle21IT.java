package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import ca.uhn.fhir.jpa.embedded.Oracle21EmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabasePartitionModeWithOracle21IT.TestConfig.class
})
@OracleTest
public class DatabasePartitionModeWithOracle21IT extends BaseDatabasePartitionModeIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject(
				new Oracle21EmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}
}
