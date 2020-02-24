package ca.uhn.fhir.jpa.migrate.tasks;

import org.junit.Test;

import java.util.Collections;

public class HapiFhirJpaMigrationTasksTest {

	@Test
	public void testCreate() {
		new HapiFhirJpaMigrationTasks(Collections.emptySet());
	}
}
