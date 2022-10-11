package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.MigrationResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DropTableTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testDropExistingTable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		DropTableTask task = new DropTableTask("1", "1");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), (hasItems("SOMETABLE")));

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItem("SOMETABLE")));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testDropTableWithForeignKey(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table FOREIGNTABLE (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table SOMETABLE (PID bigint not null, REMOTEPID bigint not null, primary key (PID))");
		executeSql("alter table SOMETABLE add constraint FK_MYFK foreign key (REMOTEPID) references FOREIGNTABLE");

		DropTableTask task = new DropTableTask("1", "1");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), (hasItems("SOMETABLE")));

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItem("SOMETABLE")));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testDropNonExistingTable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		DropTableTask task = new DropTableTask("1", "1");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItem("SOMETABLE")));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testHapiMigrationResult(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		DropTableTask task = new DropTableTask("1", "1");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), (hasItems("SOMETABLE")));

		MigrationResult result = getMigrator().migrate();
		assertEquals(0, result.changes);
		assertEquals(1, result.executedStatements.size());
		assertEquals(1, result.succeededTasks.size());
		assertEquals(0, result.failedTasks.size());

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), not(hasItem("SOMETABLE")));
	}


}
