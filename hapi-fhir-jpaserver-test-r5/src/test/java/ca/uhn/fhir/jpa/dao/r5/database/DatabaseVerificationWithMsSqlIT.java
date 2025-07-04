package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.HapiSequentialDatabaseTestExtension;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithMsSqlIT.TestConfig.class
})
public class DatabaseVerificationWithMsSqlIT extends BaseDatabaseVerificationIT {

	private static ExtensionContext extensionContext;
	private static DriverTypeEnum databaseType = DriverTypeEnum.MSSQL_2012;

	public static void setExtensionContext(ExtensionContext context) {
		extensionContext = context;
	}

	public static void setDatabaseType(DriverTypeEnum type) {
		databaseType = type;
	}

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			if (extensionContext != null) {
				// Sequential mode - use the existing database
				JpaEmbeddedDatabase database = HapiSequentialDatabaseTestExtension.getCurrentDatabase(extensionContext, databaseType);
				return new JpaDatabaseContextConfigParamObject(
					database,
					HapiFhirSQLServerDialect.class.getName()
				);
			} else {
				// Normal mode - create new database
				return new JpaDatabaseContextConfigParamObject(
					new MsSqlEmbeddedDatabase(),
					HapiFhirSQLServerDialect.class.getName()
				);
			}
		}
	}


}
