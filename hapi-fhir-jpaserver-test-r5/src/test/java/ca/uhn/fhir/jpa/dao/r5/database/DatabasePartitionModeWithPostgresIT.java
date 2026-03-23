package ca.uhn.fhir.jpa.dao.r5.database;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.Postgres.class
})
public class DatabasePartitionModeWithPostgresIT extends BaseDatabasePartitionModeIT {
}
