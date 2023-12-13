package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExecuteRawSqlTaskTest extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(ExecuteRawSqlTaskTest.class);

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testExecuteSql(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.executeRawSql("2001.01", "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (123, 'abc')");

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
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

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		List<Map<String, Object>> output = executeQuery("SELECT PID FROM SOMETABLE");
		assertEquals(0, output.size());
	}


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testOnlyAppliesToPlatforms(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.executeRawSql("2001.01", "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (123, 'abc')")
			.onlyAppliesToPlatforms(DriverTypeEnum.H2_EMBEDDED);

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		List<Map<String, Object>> output = executeQuery("SELECT PID FROM SOMETABLE");
		if (theTestDatabaseDetails.get().getDriverType() == DriverTypeEnum.H2_EMBEDDED) {
			assertEquals(1, output.size());
			assertEquals(123L, output.get(0).get("PID"));
		} else {
			assertEquals(0, output.size());
		}
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testDriverTypeBasedRawSqlExecution(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		//Given
		before(theTestDatabaseDetails);
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		Map<DriverTypeEnum, String> driverToSql = new HashMap<>();

		//When
		driverToSql.put(DriverTypeEnum.H2_EMBEDDED, "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (123, 'abc')");
		driverToSql.put(DriverTypeEnum.DERBY_EMBEDDED, "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (456, 'def')");
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.executeRawSql("2001.01", driverToSql);

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		List<Map<String, Object>> output = executeQuery("SELECT PID,TEXTCOL FROM SOMETABLE");
		//Then
		if (theTestDatabaseDetails.get().getDriverType() == DriverTypeEnum.H2_EMBEDDED) {
			assertEquals(1, output.size());
			assertEquals(123L, output.get(0).get("PID"));
			assertEquals("abc", output.get(0).get("TEXTCOL"));
		} else if (theTestDatabaseDetails.get().getDriverType() == DriverTypeEnum.DERBY_EMBEDDED) {
			assertEquals(1, output.size());
			assertEquals(456L, output.get(0).get("PID"));
			assertEquals("def", output.get(0).get("TEXTCOL"));
		} else {
			assertEquals(0, output.size());
		}
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testExecuteRawSqlStub(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		//Given
		before(theTestDatabaseDetails);
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks.forVersion(VersionEnum.V4_0_0)
			.executeRawSqlStub("2001.01", "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (123, 'abc')");

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		List<Map<String, Object>> output = executeQuery("SELECT PID,TEXTCOL FROM SOMETABLE");

		assertEquals(0, output.size());
	}

	@ParameterizedTest()
	@MethodSource("dataH2Only")
	public void testExecuteRawSqlTaskWithPrecondition(Supplier<TestDatabaseDetails> theTestDatabaseDetails, boolean theIsExecutionExpected) {
		before(theTestDatabaseDetails);
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		final List<Map<String, Object>> outputPreMigrate = executeQuery("SELECT PID,TEXTCOL FROM SOMETABLE");

		assertTrue(outputPreMigrate.isEmpty());

		final String someFakeUpdateSql = "INSERT INTO SOMETABLE (PID, TEXTCOL) VALUES (123, 'abc')";
		// LUKETODO:  Derby and fake table
		final String someFakeSelectSql = String.format("SELECT %s", theIsExecutionExpected);
		final String someReason = "I don''t feel like it!";

		final BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks.forVersion(VersionEnum.V4_0_0)
			.executeRawSql("2024.02", someFakeUpdateSql)
			.onlyIf(someFakeSelectSql, someReason);

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

		final List<Map<String, Object>> outputPostMigrate = executeQuery("SELECT PID,TEXTCOL FROM SOMETABLE");

		if (theIsExecutionExpected) {
			assertEquals(1, outputPostMigrate.size());
			assertEquals(123L, outputPostMigrate.get(0).get("PID"));
			assertEquals("abc", outputPostMigrate.get(0).get("TEXTCOL"));
		} else {
			assertTrue(outputPreMigrate.isEmpty());
		}
	}
}
