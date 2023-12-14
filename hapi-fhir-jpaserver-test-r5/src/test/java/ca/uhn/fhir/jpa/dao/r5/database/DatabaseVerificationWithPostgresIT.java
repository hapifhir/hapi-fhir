package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithPostgresIT.TestConfig.class
})
public class DatabaseVerificationWithPostgresIT extends BaseDatabaseVerificationIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new PostgresEmbeddedDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}


}
