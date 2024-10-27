package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithMsSqlIT.TestConfig.class
})
public class DatabaseVerificationWithMsSqlIT extends BaseDatabaseVerificationIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new MsSqlEmbeddedDatabase(),
				HapiFhirSQLServerDialect.class.getName()
			);
		}
	}


}
