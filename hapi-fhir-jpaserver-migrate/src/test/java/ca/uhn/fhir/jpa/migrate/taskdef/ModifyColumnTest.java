package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ModifyColumnTest extends BaseTest {


	@Test
	public void testColumnAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		ModifyColumnTask task = new ModifyColumnTask();
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(300);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertEquals("varchar(300)", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@Test
	public void testColumnMakeNullable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255) not null)");
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals("bigint", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals("varchar(255)", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// PID
		ModifyColumnTask task = new ModifyColumnTask();
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask();
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
		assertEquals("bigint", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals("varchar(255)", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();


	}

	@Test
	public void testColumnMakeNotNullable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertEquals("bigint", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals("varchar(255)", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// PID
		ModifyColumnTask task = new ModifyColumnTask();
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(AddColumnTask.ColumnTypeEnum.LONG);
		task.setNullable(false);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask();
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
		assertEquals("bigint", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID"));
		assertEquals("varchar(255)", JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

}
