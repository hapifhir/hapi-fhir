package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.AbstractMigrationTaskSuite;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers(disabledWithoutDocker=true)
public class SqlServerDeveloperMigrationTest extends AbstractMigrationTaskSuite {

	@RegisterExtension
	static TestContainerDatabaseMigrationExtension ourContainerExtension =
		new TestContainerDatabaseMigrationExtension(
			DriverTypeEnum.MSSQL_2012,
			new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest")
		.withEnv("ACCEPT_EULA", "Y")
		.withEnv("MSSQL_PID", "Developer"));

	@Override
	@Nonnull
	protected DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return ourContainerExtension.getConnectionProperties();
	}

}
