package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.annotation.Nonnull;

@Testcontainers(disabledWithoutDocker=true)
public class SqlServerStandardCollectedMigrationTest extends BaseCollectedMigrationTaskSuite {

	@RegisterExtension
	static TestContainerDatabaseMigrationExtension ourContainerExtension =
		new TestContainerDatabaseMigrationExtension(
			DriverTypeEnum.MSSQL_2012,
			new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest")
		.withEnv("ACCEPT_EULA", "Y")
		.withEnv("MSSQL_PID", "Standard") // Product id: Sql Server Enterprise vs Standard vs Developer vs ????
		);

	@Override
	@Nonnull
	protected DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return ourContainerExtension.getConnectionProperties();
	}

}
