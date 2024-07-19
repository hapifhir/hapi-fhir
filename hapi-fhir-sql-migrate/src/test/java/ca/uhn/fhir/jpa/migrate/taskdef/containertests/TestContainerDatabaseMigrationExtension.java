package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;

import jakarta.annotation.Nonnull;

/**
 * Starts a database from TestContainers, and exposes ConnectionProperties for the migrator.
 */
public class TestContainerDatabaseMigrationExtension implements BeforeAllCallback, AfterAllCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(TestContainerDatabaseMigrationExtension.class);

	final JdbcDatabaseContainer<?> myJdbcDatabaseContainer;
	final DriverTypeEnum myDriverTypeEnum;

	public TestContainerDatabaseMigrationExtension(
			DriverTypeEnum theDriverTypeEnum,
			JdbcDatabaseContainer<?> theJdbcDatabaseContainer) {
		myDriverTypeEnum = theDriverTypeEnum;
		myJdbcDatabaseContainer = theJdbcDatabaseContainer
			// use a random password to avoid having open ports on hard-coded passwords
			.withPassword("!@Aa" + RandomStringUtils.randomAlphanumeric(20));
	}

	@Override
	public void beforeAll(ExtensionContext context) {
		ourLog.info("Starting container {}", myJdbcDatabaseContainer.getContainerInfo());
		myJdbcDatabaseContainer.start();
	}

	@Override
	public void afterAll(ExtensionContext context) {
		ourLog.info("Stopping container {}", myJdbcDatabaseContainer.getContainerInfo());
		myJdbcDatabaseContainer.stop();
	}


	@Nonnull
	public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return myDriverTypeEnum.newConnectionProperties(myJdbcDatabaseContainer.getJdbcUrl(), myJdbcDatabaseContainer.getUsername(), myJdbcDatabaseContainer.getPassword());
	}

}
