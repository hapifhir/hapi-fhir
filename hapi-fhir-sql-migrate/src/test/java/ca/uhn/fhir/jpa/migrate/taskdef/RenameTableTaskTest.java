package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

public class RenameTableTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testRenameTableTask_whenTableExists_willRenameTheTable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		// given
		before(theTestDatabaseDetails);
		final String newTableName = "NEWTABLE";
		final String oldTableName = "SOMETABLE";

		executeSql("create table " + oldTableName + " (PID bigint not null, TEXTCOL varchar(255))");

		RenameTableTask task = new RenameTableTask("1", "1", oldTableName, newTableName);
		task.setTableName(oldTableName);
		getMigrator().addTask(task);

		// when
		getMigrator().migrate();

		// then
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		assertThat(tableNames, hasItem(newTableName));
		assertThat(tableNames, not(hasItem(oldTableName)));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testRenameTableTask_whenTableDoesNotExists_willSkipTask(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		// given
		before(theTestDatabaseDetails);
		final String newTableName = "NEWTABLE";
		final String oldTableName = "SOMETABLE";
		final String anotherTableName = "ANOTHERTABLE";

		executeSql("create table " + anotherTableName + " (PID bigint not null, TEXTCOL varchar(255))");

		RenameTableTask task = new RenameTableTask("1", "1", oldTableName, newTableName);
		getMigrator().addTask(task);

		// when
		getMigrator().migrate();

		// then
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		assertThat(tableNames, hasItem(anotherTableName));
		assertThat(tableNames, hasSize(1));
	}

}
