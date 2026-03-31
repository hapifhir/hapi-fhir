package ca.uhn.fhir.jpa.dao.r5.database;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.MsSql.class
})
public class DatabaseVerificationWithMsSqlIT extends BaseDatabaseVerificationIT {
}
