package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class AddTableTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testTableDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		AddTableRawSqlTask task = new AddTableRawSqlTask("1", "1");
		task.setTableName("SOMETABLE");
		task.addSql(getDriverType(), "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("SOMETABLE"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testTableAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

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
