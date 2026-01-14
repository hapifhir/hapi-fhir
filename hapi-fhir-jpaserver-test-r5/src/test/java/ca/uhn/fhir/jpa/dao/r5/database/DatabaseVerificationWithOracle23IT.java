package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import ca.uhn.fhir.jpa.embedded.Oracle23EmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithOracle23IT.TestConfig.class
})
@OracleTest
public class DatabaseVerificationWithOracle23IT extends BaseDatabaseVerificationIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return new JpaDatabaseContextConfigParamObject(
				new Oracle23EmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}


}
