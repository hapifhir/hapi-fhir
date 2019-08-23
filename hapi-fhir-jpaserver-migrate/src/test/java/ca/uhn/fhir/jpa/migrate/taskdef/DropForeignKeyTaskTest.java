package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class DropForeignKeyTaskTest extends BaseTest {

	@Test
	public void testDropForeignKey() throws SQLException {
		executeSql("create table HOME (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table FOREIGNTBL (PID bigint not null, HOMEREF bigint)");
		executeSql("alter table HOME add foreign key FK_FOO (PID) references FOREIGNTABLE(HOMEREF)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL"), empty());

		DropForeignKeyTask task = new DropForeignKeyTask();
		task.setTableName("FOREIGNTBL");
		task.setColumnName("HOMEREF");
		task.setConstraintName("FK_FOO");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL"), empty());

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}


}
