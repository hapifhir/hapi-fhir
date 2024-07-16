package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import jakarta.annotation.Nonnull;

public class SqlServerAzureMigrationTests extends AbstractMigrationTaskSuite {
	@RegisterExtension
	static TestContainerDatabaseMigrationExtension ourContainerExtension =
		new TestContainerDatabaseMigrationExtension(
			DriverTypeEnum.MSSQL_2012,
			new MSSQLServerContainer<>(
				DockerImageName.parse("mcr.microsoft.com/azure-sql-edge:latest")
					.asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server"))
				.withEnv("ACCEPT_EULA", "Y")
				.withEnv("MSSQL_PID", "Premium"));

	@Override
	@Nonnull
	protected DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return ourContainerExtension.getConnectionProperties();
	}
}
