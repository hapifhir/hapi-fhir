package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class ExecuteRawSqlTaskTest extends BaseTest {

	public ExecuteRawSqlTaskTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testExecuteSql() {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.executeRawSql("2001.01", "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (123, 'abc')");

		getMigrator().addTasks(tasks.getTasks(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		List<Map<String, Object>> output = executeQuery("SELECT PID FROM SOMETABLE");
		assertEquals(1, output.size());
		assertEquals(123L, output.get(0).get("PID"));
	}

	@Test
	public void testExecuteSql_AllowedToFail() {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.executeRawSql("2001.01", "INSERT INTO SOMETABLE (PID_BAD_COLUMN, TEXTCOL) VALUES (123, 'abc')")
			.failureAllowed();

		getMigrator().addTasks(tasks.getTasks(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		List<Map<String, Object>> output = executeQuery("SELECT PID FROM SOMETABLE");
		assertEquals(0, output.size());
	}

}
