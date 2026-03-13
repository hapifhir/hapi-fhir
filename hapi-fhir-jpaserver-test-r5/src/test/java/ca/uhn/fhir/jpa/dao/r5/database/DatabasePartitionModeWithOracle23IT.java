package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import ca.uhn.fhir.jpa.embedded.Oracle23EmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabasePartitionModeWithOracle23IT.TestConfig.class
})
@OracleTest
public class DatabasePartitionModeWithOracle23IT extends BaseDatabasePartitionModeIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject(
				new Oracle23EmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}
}
