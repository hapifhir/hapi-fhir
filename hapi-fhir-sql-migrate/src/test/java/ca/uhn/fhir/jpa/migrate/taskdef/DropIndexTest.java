package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
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

	@Nested
	public class OnlineNoLocks {
		private DropIndexTask myTask;
		private List<String> mySql;

		@BeforeEach
		public void beforeEach() {
			myTask = new DropIndexTask("1", "1");
			myTask.setIndexName("IDX_ANINDEX");
			myTask.setTableName("SOMETABLE");
		}

		@ParameterizedTest(name = "{index}: {0}")
		@EnumSource()
		public void noAffectOffUnique(DriverTypeEnum theDriver) throws SQLException {
			myTask.setDriverType(theDriver);
			mySql = myTask.doGenerateSql(true);
			switch (theDriver) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					assertThat(mySql, equalTo(asList("alter table SOMETABLE drop index `IDX_ANINDEX`")));
					break;
				case H2_EMBEDDED:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case DERBY_EMBEDDED:
					assertThat(mySql, equalTo(asList("alter table SOMETABLE drop constraint IDX_ANINDEX")));
					break;
				case ORACLE_12C:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case MSSQL_2012:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX on SOMETABLE")));
					break;
				case POSTGRES_9_4:
					assertThat(mySql, equalTo(asList(
						"alter table SOMETABLE drop constraint if exists IDX_ANINDEX cascade",
						"drop index if exists IDX_ANINDEX cascade")));
					break;
			}
		}

		@ParameterizedTest(name = "{index}: {0}")
		@EnumSource()
		public void noAffectOffNotUnique(DriverTypeEnum theDriver) throws SQLException {
			myTask.setDriverType(theDriver);
			mySql = myTask.doGenerateSql(false);
			switch (theDriver) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					assertThat(mySql, equalTo(asList("alter table SOMETABLE drop index IDX_ANINDEX")));
					break;
				case H2_EMBEDDED:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case DERBY_EMBEDDED:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case ORACLE_12C:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case MSSQL_2012:
					assertThat(mySql, equalTo(asList("drop index SOMETABLE.IDX_ANINDEX")));
					break;
				case POSTGRES_9_4:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
			}
		}

		@ParameterizedTest(name = "{index}: {0}")
		@EnumSource()
		public void onlineUnique(DriverTypeEnum theDriver) throws SQLException {
			myTask.setDriverType(theDriver);
			myTask.setOnline(true);
			mySql = myTask.doGenerateSql(true);
			switch (theDriver) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					assertThat(mySql, equalTo(asList("alter table SOMETABLE drop index `IDX_ANINDEX`")));
					break;
				case H2_EMBEDDED:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case DERBY_EMBEDDED:
					assertThat(mySql, equalTo(asList("alter table SOMETABLE drop constraint IDX_ANINDEX")));
					break;
				case ORACLE_12C:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX ONLINE")));
					break;
				case MSSQL_2012:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX on SOMETABLE WITH (ONLINE = ON)")));
					break;
				case POSTGRES_9_4:
					assertThat(mySql, equalTo(asList(
						"alter table SOMETABLE drop constraint if exists IDX_ANINDEX cascade",
						"drop index CONCURRENTLY if exists IDX_ANINDEX cascade")));
					break;
			}
		}

		@ParameterizedTest(name = "{index}: {0}")
		@EnumSource()
		public void onlineNotUnique(DriverTypeEnum theDriver) throws SQLException {
			myTask.setDriverType(theDriver);
			myTask.setOnline(true);
			mySql = myTask.doGenerateSql(false);
			switch (theDriver) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					assertThat(mySql, equalTo(asList("alter table SOMETABLE drop index IDX_ANINDEX")));
					break;
				case H2_EMBEDDED:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case DERBY_EMBEDDED:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX")));
					break;
				case ORACLE_12C:
					assertThat(mySql, equalTo(asList("drop index IDX_ANINDEX ONLINE")));
					break;
				case MSSQL_2012:
					assertThat(mySql, equalTo(asList("drop index SOMETABLE.IDX_ANINDEX")));
					break;
				case POSTGRES_9_4:
					assertThat(mySql, equalTo(asList("drop index CONCURRENTLY IDX_ANINDEX")));
					break;
			}
		}
	}


}
