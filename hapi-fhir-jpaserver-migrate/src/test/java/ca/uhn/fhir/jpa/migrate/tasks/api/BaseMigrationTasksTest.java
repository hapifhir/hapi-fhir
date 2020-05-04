package ca.uhn.fhir.jpa.migrate.tasks.api;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropTableTask;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BaseMigrationTasksTest {
	static class MyMigrationTasks extends BaseMigrationTasks {
	}

	@Test
	public void testValidateCorrectOrder() {
		MyMigrationTasks migrationTasks = new MyMigrationTasks();
		List<BaseTask> tasks = new ArrayList<>();
		tasks.add(new DropTableTask("1", "20191029.1"));
		tasks.add(new DropTableTask("1", "20191029.2"));
		migrationTasks.validate(tasks);
	}

	@Test
	public void testValidateVersionWrongOrder() {
		MyMigrationTasks migrationTasks = new MyMigrationTasks();
		List<BaseTask> tasks = new ArrayList<>();
		tasks.add(new DropTableTask("1", "20191029.2"));
		tasks.add(new DropTableTask("1", "20191029.1"));
		try {
			migrationTasks.validate(tasks);
			fail();
		} catch (IllegalStateException e) {
			assertEquals("Migration version 1.20191029.1 found after migration version 1.20191029.2.  Migrations need to be in order by version number.", e.getMessage());
		}
	}

	@Test
	public void testValidateSameVersion() {
		MyMigrationTasks migrationTasks = new MyMigrationTasks();
		List<BaseTask> tasks = new ArrayList<>();
		tasks.add(new DropTableTask("1", "20191029.1"));
		tasks.add(new DropTableTask("1", "20191029.1"));
		try {
			migrationTasks.validate(tasks);
			fail();
		} catch (IllegalStateException e) {
			assertEquals("Migration version 1.20191029.1 found after migration version 1.20191029.1.  Migrations need to be in order by version number.", e.getMessage());
		}
	}

	@Test
	public void testValidateWrongDateOrder() {
		MyMigrationTasks migrationTasks = new MyMigrationTasks();
		List<BaseTask> tasks = new ArrayList<>();
		tasks.add(new DropTableTask("1", "20191029.1"));
		tasks.add(new DropTableTask("1", "20191028.1"));
		try {
			migrationTasks.validate(tasks);
			fail();
		} catch (IllegalStateException e) {
			assertEquals("Migration version 1.20191028.1 found after migration version 1.20191029.1.  Migrations need to be in order by version number.", e.getMessage());
		}
	}

}
