package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.Oracle21.class
})
@OracleTest
public class DatabasePartitionModeWithOracle21IT extends BaseDatabasePartitionModeIT {
}
