package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HashTest {

	@Test
	public void testHash() {
		AddColumnTask task1 = buildTask();
		AddColumnTask task2 = buildTask();
		assertEquals(task1.hashCode(), task2.hashCode());
	}

	private AddColumnTask buildTask() {
		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("TRM_CODESYSTEM_VER");
		task.setColumnName("CS_VERSION_ID");
		task.setNullable(true);
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setColumnLength(255);
		return task;
	}

	@Test
	public void testCheckAllHashes() {
		List<BaseTask> tasks1 = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(VersionEnum.values());
		Map<String, Integer> hashesByVersion = new HashMap<>();
		for (BaseTask task : tasks1) {
			String version = task.getFlywayVersion();
			assertNull(hashesByVersion.get(version), "Duplicate flyway version " + version + " in " + HapiFhirJpaMigrationTasks.class.getName());
			hashesByVersion.put(version, task.hashCode());
		}

		List<BaseTask> tasks2 = new HapiFhirJpaMigrationTasks(Collections.emptySet()).getAllTasks(VersionEnum.values());
		for (BaseTask task : tasks2) {
			String version = task.getFlywayVersion();
			int origHash = hashesByVersion.get(version);
			assertEquals(origHash, task.hashCode(), "Hashes differ for task " + version);
		}
	}
}
