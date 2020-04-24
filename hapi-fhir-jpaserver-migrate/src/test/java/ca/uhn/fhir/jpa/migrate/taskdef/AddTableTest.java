package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class AddTableTest extends BaseTest {

	public AddTableTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testTableDoesntAlreadyExist() throws SQLException {

		AddTableRawSqlTask task = new AddTableRawSqlTask("1", "1");
		task.setTableName("SOMETABLE");
		task.addSql(getDriverType(), "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("SOMETABLE"));
	}

	@Test
	public void testTableAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("SOMETABLE"));

		AddTableRawSqlTask task = new AddTableRawSqlTask("1", "1");
		task.setTableName("SOMETABLE");
		task.addSql(getDriverType(), "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		getMigrator().addTask(task);
		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("SOMETABLE"));
	}

}
