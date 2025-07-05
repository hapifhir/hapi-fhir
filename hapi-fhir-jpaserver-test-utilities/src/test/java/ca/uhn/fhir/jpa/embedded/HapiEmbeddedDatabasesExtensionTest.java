package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Sonnet 4
@RequiresDocker
public class HapiEmbeddedDatabasesExtensionTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiEmbeddedDatabasesExtensionTest.class);

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myExtension = new HapiEmbeddedDatabasesExtension();

	@AfterEach
	public void afterEach() {
		try {
			myExtension.clearDatabases();
		} catch (Exception e) {
			ourLog.error("Failed to clear databases", e);
			throw e;
		}
	}

	@ParameterizedTest
	@ArgumentsSource(HapiEmbeddedDatabasesExtension.DatabaseVendorProvider.class)
	public void testOnlyOneContainerActiveAtATime(DriverTypeEnum theDriverType) {
		ourLog.info("Testing database type: {}", theDriverType);

		// Get the database (this triggers startup in current implementation)
		JpaEmbeddedDatabase database = myExtension.getEmbeddedDatabase(theDriverType);
		assertThat(database).isNotNull();
		assertThat(database.getDriverType()).isEqualTo(theDriverType);

		// Count active TestContainers
		int activeContainerCount = countActiveTestContainers();
		ourLog.info("Active TestContainers count: {}", activeContainerCount);

		// This test should FAIL with current implementation since all containers start immediately
		// With lazy initialization, only 1 container should be active
		assertThat(activeContainerCount)
			.as("Only one container should be active at a time for driver type: " + theDriverType)
			.isEqualTo(1);
	}

	private int countActiveTestContainers() {
		try {
			// Get all containers with TestContainers labels
			List<com.github.dockerjava.api.model.Container> containers = DockerClientFactory.instance()
				.client()
				.listContainersCmd()
				.withShowAll(false) // Only running containers
				.exec();

			// Count containers with TestContainers labels
			int testContainerCount = 0;
			for (com.github.dockerjava.api.model.Container container : containers) {
				if (container.getLabels() != null && 
					container.getLabels().containsKey("org.testcontainers")) {
					testContainerCount++;
					ourLog.debug("Found TestContainer: {} with labels: {}", 
						container.getNames()[0], container.getLabels());
				}
			}
			return testContainerCount;
		} catch (Exception e) {
			ourLog.warn("Could not count active containers", e);
			return -1; // Return -1 to indicate counting failed
		}
	}
}