package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.flywaydb.core.internal.command.DbMigrate;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AddColumnTest extends BaseTest {

	public AddColumnTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testColumnDoesntAlreadyExist() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("newcol");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL", "NEWCOL"));
	}

	@Test
	public void testAddColumnInt() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("newcolint");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.INT);
		task.setNullable(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		JdbcUtils.ColumnType type = JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "newcolint");
		assertEquals(BaseTableColumnTypeTask.ColumnTypeEnum.INT, type.getColumnTypeEnum());
	}

	@Test
	public void testColumnAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		AddColumnTask task = new AddColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("newcol");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL", "NEWCOL"));
	}

	@Test
	public void testAddColumnToNonExistantTable_Failing() {
		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.onTable("FOO_TABLE")
			.addColumn("2001.01", "FOO_COLUMN")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.INT);

		getMigrator().addTasks(tasks.getTasks(VersionEnum.V0_1, VersionEnum.V4_0_0));
		try {
			getMigrator().migrate();
			fail();
		} catch (DbMigrate.FlywayMigrateException e) {
			assertEquals("Migration failed !", e.getMessage());
		}
	}


	@Test
	public void testAddColumnToNonExistantTable_FailureAllowed() {
		BaseMigrationTasks<VersionEnum> tasks = new BaseMigrationTasks<>();
		tasks
			.forVersion(VersionEnum.V4_0_0)
			.onTable("FOO_TABLE")
			.addColumn("2001.01", "FOO_COLUMN")
			.nullable()
			.type(BaseTableColumnTypeTask.ColumnTypeEnum.INT)
			.failureAllowed();

		getMigrator().addTasks(tasks.getTasks(VersionEnum.V0_1, VersionEnum.V4_0_0));
		getMigrator().migrate();

	}

}
