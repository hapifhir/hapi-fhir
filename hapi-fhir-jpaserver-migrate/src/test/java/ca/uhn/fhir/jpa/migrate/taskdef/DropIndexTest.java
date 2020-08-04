package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class DropIndexTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testIndexAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		DropIndexTask task = new DropIndexTask("1", "1");
		task.setDescription("Drop an index");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), contains("IDX_DIFINDEX"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testIndexDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		DropIndexTask task = new DropIndexTask("1", "1");
		task.setDescription("Drop an index");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), contains("IDX_DIFINDEX"));
	}


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testConstraintAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		DropIndexTask task = new DropIndexTask("1", "1");
		task.setDescription("Drop an index");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), contains("IDX_DIFINDEX"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testConstraintDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		DropIndexTask task = new DropIndexTask("1", "1");
		task.setDescription("Drop an index");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), contains("IDX_DIFINDEX"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testDropConstraintIndex(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), TEXTCOL2 varchar(255))");
		executeSql("alter table SOMETABLE add constraint IDX_DIFINDEX unique (TEXTCOL, TEXTCOL2)");

		DropIndexTask task = new DropIndexTask("1", "1");
		task.setDescription("Drop an index");
		task.setIndexName("IDX_DIFINDEX");
		task.setTableName("SOMETABLE");
		getMigrator().addTask(task);

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), not(empty()));
		getMigrator().migrate();
		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), empty());
	}

}
