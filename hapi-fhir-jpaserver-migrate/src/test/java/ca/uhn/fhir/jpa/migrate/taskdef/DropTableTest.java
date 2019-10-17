package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class DropTableTest extends BaseTest {

	@Test
	public void testDropExistingTable() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		DropTableTask task = new DropTableTask();
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), (hasItems("SOMETABLE")));

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItems("SOMETABLE")));
	}

	@Test
	public void testDropTableWithForeignKey() throws SQLException {
		executeSql("create table FOREIGNTABLE (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table SOMETABLE (PID bigint not null, REMOTEPID bigint not null, primary key (PID))");
		executeSql("alter table SOMETABLE add constraint FK_MYFK foreign key (REMOTEPID) references FOREIGNTABLE;");

		DropTableTask task = new DropTableTask();
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), (hasItems("SOMETABLE")));

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItems("SOMETABLE")));
	}

	@Test
	public void testDropNonExistingTable() throws SQLException {

		DropTableTask task = new DropTableTask();
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItems("SOMETABLE")));
	}

}
