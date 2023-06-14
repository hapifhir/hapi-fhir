package ca.uhn.fhir.jpa.migrate.tasks;

import java.util.Collections;
import org.junit.jupiter.api.Test;

public class HapiFhirJpaMigrationTasksTest {

    @Test
    public void testCreate() {
        new HapiFhirJpaMigrationTasks(Collections.emptySet());
    }
}
