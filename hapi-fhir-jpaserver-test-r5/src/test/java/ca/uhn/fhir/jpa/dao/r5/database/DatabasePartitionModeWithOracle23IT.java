package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.Oracle23.class
})
@OracleTest
public class DatabasePartitionModeWithOracle23IT extends BaseDatabasePartitionModeIT {
}
