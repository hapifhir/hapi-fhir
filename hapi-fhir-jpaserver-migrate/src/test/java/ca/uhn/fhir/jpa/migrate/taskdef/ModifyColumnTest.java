package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
	}

}
