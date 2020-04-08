package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.flywaydb.core.internal.command.DbMigrate;
import org.junit.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class ModifyColumnTest extends BaseTest {
	public ModifyColumnTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testColumnWithJdbcTypeClob() throws SQLException {
		if (getDriverType() == DriverTypeEnum.DERBY_EMBEDDED) {
			return;
		}

		executeSql("create table SOMETABLE (TEXTCOL clob)");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(250);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 250), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals(1, task.getExecutedStatements().size());

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@Test
	public void testColumnAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(300);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 300), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals(1, task.getExecutedStatements().size());

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@Test
	public void testNoShrink_SameNullable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		ModifyColumnTask task = new ModifyColumnTask("1", "123456.7");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(200);

		getMigrator().setNoColumnShrink(true);
		getMigrator().addTask(task);
		getMigrator().migrate();

		assertEquals(0, task.getExecutedStatements().size());
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 255), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@Test
	public void testColumnMakeNullable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255) not null)");
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, 19), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 255), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// PID
		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask("1", "2");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(255);
		getMigrator().addTask(task);

		// Do migration
		getMigrator().migrate();

		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, 19), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 255), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();


	}

	@Test
	public void testNoShrink_ColumnMakeDateNullable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, DATECOL timestamp not null)");
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "DATECOL"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, 19), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP, JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "DATECOL").getColumnTypeEnum());

		getMigrator().setNoColumnShrink(true);

		// PID
		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask("1", "2");
		task.setTableName("SOMETABLE");
		task.setColumnName("DATECOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.DATE_TIMESTAMP);
		task.setNullable(true);
		getMigrator().addTask(task);

		// Do migration
		getMigrator().migrate();

		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "DATECOL"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, 19), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals(BaseTableColumnTypeTask.ColumnTypeEnum.DATE_TIMESTAMP, JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "DATECOL").getColumnTypeEnum());

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	@Test
	public void testColumnMakeNotNullable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, 19), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 255), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// PID
		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		task.setNullable(false);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask("1", "2");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(false);
		task.setColumnLength(255);
		getMigrator().addTask(task);

		// Do migration
		getMigrator().migrate();

		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, 19), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 255), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@Test
	public void testColumnDoesntAlreadyExist() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("SOMECOLUMN");
		task.setDescription("Make nullable");
		task.setNullable(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL"));
	}

	@Test
	public void testFailureAllowed() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		executeSql("insert into SOMETABLE (TEXTCOL) values ('HELLO')");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		task.setNullable(true);
		task.setFailureAllowed(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
		assertEquals(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL").getColumnTypeEnum());

	}

	@Test
	public void testFailureNotAllowed() {
		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		executeSql("insert into SOMETABLE (TEXTCOL) values ('HELLO')");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (DbMigrate.FlywayMigrateException e) {
			// expected
		}

	}

	@Test
	public void dontCompareLengthIfNoneSpecifiedInTask() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
		task.setNullable(true);

		JdbcUtils.ColumnType existingColumnType = JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID");
		assertEquals(BaseTableColumnTypeTask.ColumnTypeEnum.LONG, existingColumnType.getColumnTypeEnum());
		assertEquals(19L, existingColumnType.getLength().longValue());
		assertTrue(existingColumnType.equals(task.getColumnType(), task.getColumnLength()));
	}


	@Test
	public void testShrinkDoesntFailIfShrinkCannotProceed() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(10))");
		executeSql("insert into SOMETABLE (PID, TEXTCOL) values (1, '0123456789')");

		ModifyColumnTask task = new ModifyColumnTask("1", "123456.7");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(5);

		getMigrator().addTask(task);
		getMigrator().migrate();

		assertEquals(1, task.getExecutedStatements().size());
		assertEquals(new JdbcUtils.ColumnType(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 10), JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

}
