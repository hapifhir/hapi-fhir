package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.OracleEmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.annotation.OracleTest;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithOracleIT.TestConfig.class
})
@OracleTest
public class DatabaseVerificationWithOracleIT extends BaseDatabaseVerificationIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return new JpaDatabaseContextConfigParamObject(
				new OracleEmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}


}
