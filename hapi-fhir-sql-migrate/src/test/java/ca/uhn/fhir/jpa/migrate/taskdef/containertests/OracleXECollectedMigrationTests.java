package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.embedded.annotation.OracleTest;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.utility.DockerImageName;

@OracleTest
public class OracleXECollectedMigrationTests extends BaseCollectedMigrationTaskSuite {
	@RegisterExtension
	static TestContainerDatabaseMigrationExtension ourContainerExtension =
		new TestContainerDatabaseMigrationExtension(
			DriverTypeEnum.ORACLE_12C,
			new OracleContainer(DockerImageName.parse("gvenzl/oracle-xe:21-slim-faststart")));

	@Override
	@Nonnull
	protected DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return ourContainerExtension.getConnectionProperties();
	}
}
