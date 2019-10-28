package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AddColumnTest extends BaseTest {

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

}
