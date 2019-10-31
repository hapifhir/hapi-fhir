package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.taskdef.AddTableRawSqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class MigrationValidatorTest extends BaseTest {

	@Test
	public void migrationRequired() {
		AddTableRawSqlTask task = new AddTableRawSqlTask("1", "1");
		task.setTableName("SOMETABLE");
		task.addSql(DriverTypeEnum.H2_EMBEDDED, "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		getMigrator().addTask(task);

		MigrationValidator migrationValidator = new MigrationValidator(getDataSource(), Collections.singletonList(task));
		try {
			migrationValidator.validate();
			fail();
		} catch (ConfigurationException e) {
			assertEquals("The database schema for " + getDatabaseName() + " is out of date.  Current database schema version is unknown.  Schema version required by application is " + task.getFlywayVersion() + ".  Please run the database migrator.", e.getMessage());
		}
		getMigrator().migrate();
		migrationValidator.validate();
	}
}
