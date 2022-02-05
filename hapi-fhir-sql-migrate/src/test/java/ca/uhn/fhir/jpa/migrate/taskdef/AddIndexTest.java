package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddIndexTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testUniqueConstraintAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("ALTER TABLE SOMETABLE ADD CONSTRAINT IDX_ANINDEX UNIQUE(TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("TEXTCOL");
		task.setUnique(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), hasItem("IDX_ANINDEX"));

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testUniqueIndexAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create unique index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create unique index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testNonUniqueIndexAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testIndexDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create unique index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

	@Nested
	public class SqlFeatures {
		private AddIndexTask myTask;
		private String sql;


		@Nested
		public class IncludeColumns {
			@BeforeEach
			public void beforeEach() {
				myTask = new AddIndexTask("1", "1");
				myTask.setIndexName("IDX_ANINDEX");
				myTask.setTableName("SOMETABLE");
				myTask.setColumns("PID", "TEXTCOL");
				myTask.setIncludeColumns("FOO1", "FOO2");
				myTask.setUnique(false);
			}

			@Test
			public void testIncludeColumns() {

				// MSSQL supports include clause
				myTask.setDriverType(DriverTypeEnum.MSSQL_2012);
				sql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) INCLUDE (FOO1, FOO2)", sql);

				// Oracle does not support include clause
				myTask.setDriverType(DriverTypeEnum.ORACLE_12C);
				sql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", sql);
			}

			@Test
			public void includeForce_addsToUnsuppored() {
				myTask.setDriverType(DriverTypeEnum.ORACLE_12C);
				myTask.setForceInclude(true);
				sql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL, FOO1, FOO2)", sql);
			}

			@Test
			public void includeForce_usesIncludeWhenSupported() {
				myTask.setDriverType(DriverTypeEnum.MSSQL_2012);
				myTask.setForceInclude(true);
				sql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) INCLUDE (FOO1, FOO2)", sql);
			}
		}

		@Nested
		public class OnlineNoLocks {

			@BeforeEach
			public void beforeEach() {
				myTask = new AddIndexTask("1", "1");
				myTask.setIndexName("IDX_ANINDEX");
				myTask.setTableName("SOMETABLE");
				myTask.setColumns("PID", "TEXTCOL");
				myTask.setUnique(false);
			}

			@ParameterizedTest(name = "{index}: {0}")
			@EnumSource()
			public void noAffectOff(DriverTypeEnum theDriver) {
				myTask.setDriverType(theDriver);
				sql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", sql);
			}

			@ParameterizedTest(name = "{index}: {0}")
			@EnumSource()
			public void platformSyntaxWhenOn(DriverTypeEnum theDriver) {
				myTask.setDriverType(theDriver);
				myTask.setOnline(true);
				sql = myTask.generateSql();
				switch (theDriver) {
					case POSTGRES_9_4:
						assertEquals("create index CONCURRENTLY IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", sql);
						break;
					case ORACLE_12C:
						assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) ONLINE DEFERRED", sql);
						break;
					case MSSQL_2012:
						assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) WITH (ONLINE = ON)", sql);
						break;
					default:
						// unsupported is ok.  But it means we lock the table for a bit.
						assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", sql);
						break;
				}
			}
		}
	}
}
