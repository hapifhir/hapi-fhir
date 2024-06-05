package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MockitoSettings(strictness = Strictness.LENIENT)
public class AddIndexTaskTest extends BaseTest {

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

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE")).contains("IDX_ANINDEX");

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

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE")).containsExactlyInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX");
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

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE")).containsExactlyInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX");
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

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE")).containsExactlyInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX");
	}

	@Nested
	public class SqlFeatures {
		private AddIndexTask myTask;
		private String mySql;


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
				mySql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) INCLUDE (FOO1, FOO2)", mySql);

				// Oracle does not support include clause
				myTask.setDriverType(DriverTypeEnum.ORACLE_12C);
				mySql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", mySql);
			}

		}

		@Nested
		public class OnlineNoLocks {

			@Mock
			private MetadataSource mockMetadataSource;

			@BeforeEach
			public void beforeEach() {
				myTask = new AddIndexTask("1", "1");
				myTask.setIndexName("IDX_ANINDEX");
				myTask.setTableName("SOMETABLE");
				myTask.setColumns("PID", "TEXTCOL");
				myTask.setUnique(false);
				myTask.setMetadataSource(mockMetadataSource);
			}

			@ParameterizedTest(name = "{index}: {0}")
			@EnumSource()
			public void noAffectOff(DriverTypeEnum theDriver) {
				myTask.setDriverType(theDriver);
				mySql = myTask.generateSql();
				assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", mySql);
			}

			@ParameterizedTest(name = "{index}: {0}")
			@EnumSource()
			public void platformSyntaxWhenOn(DriverTypeEnum theDriver) {
				myTask.setDriverType(theDriver);
				myTask.setOnline(true);
				DriverTypeEnum.ConnectionProperties props;
				Mockito.when(mockMetadataSource.isOnlineIndexSupported(Mockito.any())).thenReturn(true);
				mySql = myTask.generateSql();
				switch (theDriver) {
					case POSTGRES_9_4:
					case COCKROACHDB_21_1:
						assertEquals("create index CONCURRENTLY IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", mySql);
						break;
					case ORACLE_12C:
						assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) ONLINE DEFERRED INVALIDATION", mySql);
						break;
					case MSSQL_2012:
						assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) WITH (ONLINE = ON)", mySql);
						break;
					default:
						// unsupported is ok.  But it means we lock the table for a bit.
						assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", mySql);
						break;
				}
			}

			@ParameterizedTest(name = "{index}: {0}")
			@ValueSource(booleans = { true, false } )
			public void offForUnsupportedVersionsOfSqlServer(boolean theSupportedFlag) {
				myTask.setDriverType(DriverTypeEnum.MSSQL_2012);
				myTask.setOnline(true);
				myTask.setMetadataSource(mockMetadataSource);
				Mockito.when(mockMetadataSource.isOnlineIndexSupported(Mockito.any())).thenReturn(theSupportedFlag);

				mySql = myTask.generateSql();
				if (theSupportedFlag) {
					assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) WITH (ONLINE = ON)", mySql);
				} else {
					assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", mySql);
				}
			}

			@ParameterizedTest(name = "{index}: {0}")
			@ValueSource(booleans = { true, false } )
			public void offForUnsupportedVersionsOfOracleServer(boolean theSupportedFlag) {
				myTask.setDriverType(DriverTypeEnum.ORACLE_12C);
				myTask.setOnline(true);
				myTask.setMetadataSource(mockMetadataSource);
				Mockito.when(mockMetadataSource.isOnlineIndexSupported(Mockito.any())).thenReturn(theSupportedFlag);

				mySql = myTask.generateSql();
				if (theSupportedFlag) {
					assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL) ONLINE DEFERRED INVALIDATION", mySql);
				} else {
					assertEquals("create index IDX_ANINDEX on SOMETABLE(PID, TEXTCOL)", mySql);
				}
			}
		}
	}
}
