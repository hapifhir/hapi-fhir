package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.HapiMigrationException;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

public class AddColumnTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("newcol");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE")).containsExactlyInAnyOrder("PID", "TEXTCOL", "NEWCOL");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddColumnInt(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("newcolint");
		task.setColumnType(ColumnTypeEnum.INT);
		task.setNullable(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		JdbcUtils.ColumnType type = JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "newcolint");
		assertThat(type.getColumnTypeEnum()).isEqualTo(ColumnTypeEnum.INT);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("newcol");
		task.setColumnType(ColumnTypeEnum.LONG);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE")).containsExactlyInAnyOrder("PID", "TEXTCOL", "NEWCOL");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddColumnToNonExistentTable_Failing(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.onTable("FOO_TABLE")
			.addColumn("2001.01", "FOO_COLUMN")
			.nullable()
			.type(ColumnTypeEnum.INT);

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		try {
			getMigrator().migrate();
			fail("");
		} catch (HapiMigrationException e) {
			assertThat(e.getMessage()).startsWith("HAPI-0047: Failure executing task \"Add column FOO_COLUMN on table FOO_TABLE\", aborting! Cause: ca.uhn.fhir.jpa.migrate.HapiMigrationException: HAPI-0061: Failed during task 4.0.0.2001.01: ");
		}
	}


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddColumnToNonExistantTable_FailureAllowed(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.onTable("FOO_TABLE")
			.addColumn("2001.01", "FOO_COLUMN")
			.nullable()
			.type(ColumnTypeEnum.INT)
			.failureAllowed();

		getMigrator().addTasks(tasks.getTaskList(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

	}

}
