package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;

import java.util.List;

// Created by Claude Sonnet 4
@RequiresDocker
@Disabled
public class HapiEmbeddedDatabasesExtensionTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiEmbeddedDatabasesExtensionTest.class);

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myExtension = new HapiEmbeddedDatabasesExtension();

	@AfterEach
	public void afterEach() {
		try {
			ourLog.info("AfterEach: Clearing databases");
			myExtension.clearDatabases();
			
			// Give a brief moment for containers to fully shut down
			Thread.sleep(1000);
			
			int remainingContainers = countActiveTestContainers();
			ourLog.info("AfterEach: Remaining containers after cleanup: {}", remainingContainers);
		} catch (Exception e) {
			ourLog.error("Failed to clear databases", e);
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
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
