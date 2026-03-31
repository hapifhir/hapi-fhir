package ca.uhn.fhir.jpa.dao.r5.database;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.MsSql.class
})
public class DatabasePartitionModeWithMsSqlIT extends BaseDatabasePartitionModeIT {
}
