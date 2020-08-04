package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExecuteRawSqlTaskTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testExecuteSql(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

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

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testExecuteSql_AllowedToFail(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

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
