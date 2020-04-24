package ca.uhn.fhir.jpa.migrate.taskdef;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BaseTaskTest {

	@Test
	public void testValidateVersionCorrect() {
		DropTableTask task = new DropTableTask("1", "12345678.9");
		task.validateVersion();
	}

	@Test
	public void testValidateVersionShort() {
		DropTableTask task = new DropTableTask("1", "123.4");
		try {
			task.validateVersion();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("The version 123.4 does not match the expected pattern " + BaseTask.MIGRATION_VERSION_PATTERN, e.getMessage());
		}
	}

	@Test
	public void testValidateVersionNoPeriod() {
		DropTableTask task = new DropTableTask("1", "123456789");
		try {
			task.validateVersion();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("The version 123456789 does not match the expected pattern " + BaseTask.MIGRATION_VERSION_PATTERN, e.getMessage());
		}
	}

	@Test
	public void testValidateVersionTooManyPeriods() {
		DropTableTask task = new DropTableTask("1", "12345678.9.1");
		try {
			task.validateVersion();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("The version 12345678.9.1 does not match the expected pattern " + BaseTask.MIGRATION_VERSION_PATTERN, e.getMessage());
		}
	}


}
