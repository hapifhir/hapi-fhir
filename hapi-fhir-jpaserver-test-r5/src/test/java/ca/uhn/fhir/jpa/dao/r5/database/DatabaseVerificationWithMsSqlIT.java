package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.HapiEmbeddedDatabasesExtension;
import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithMsSqlIT.TestConfig.class
})
public class DatabaseVerificationWithMsSqlIT extends BaseDatabaseVerificationIT {


	@RegisterExtension
	private static HapiEmbeddedDatabasesExtension myExtension = HapiEmbeddedDatabasesExtension.forDatabase(DriverTypeEnum.MSSQL_2012);

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				myExtension.getOnlyDatabase(),
				HapiFhirSQLServerDialect.class.getName()
			);
		}
	}


}
