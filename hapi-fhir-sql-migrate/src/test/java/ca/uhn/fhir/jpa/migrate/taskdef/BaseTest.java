package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.HapiMigrator;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.Driver;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.provider.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;


public abstract class BaseTest {

	private static final String DATABASE_NAME = "DATABASE";
	static final String H2 = "H2";
	static final String DERBY = "Derby";
	private static final Logger ourLog = LoggerFactory.getLogger(BaseTest.class);
	private static int ourDatabaseUrl = 0;
	private static final Supplier<TestDatabaseDetails> TEST_DATABASE_DETAILS_DERBY_SUPPLIER = new Supplier<>() {
		@Override
		public TestDatabaseDetails get() {
			ourLog.info("Derby: {}", DriverTypeEnum.DERBY_EMBEDDED.getDriverClassName());

			String url = "jdbc:derby:memory:" + DATABASE_NAME + ourDatabaseUrl++ + ";create=true";
			DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(url, "SA", "SA");
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setUrl(url);
			dataSource.setUsername("SA");
			dataSource.setPassword("SA");
			dataSource.setDriverClassName(DriverTypeEnum.DERBY_EMBEDDED.getDriverClassName());
			HapiMigrator migrator = new HapiMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, dataSource, DriverTypeEnum.DERBY_EMBEDDED);
			return new TestDatabaseDetails(url, connectionProperties, dataSource, migrator);
		}

		@Override
		public String toString() {
			return DERBY;
		}
	};

	private static final Supplier<TestDatabaseDetails> TEST_DATABASE_DETAILS_H2_SUPPLIER = new Supplier<>() {
		@Override
		public TestDatabaseDetails get() {
			ourLog.info("H2: {}", Driver.class);
			String url = "jdbc:h2:mem:" + DATABASE_NAME + ourDatabaseUrl++;
			DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "SA", "SA");
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setUrl(url);
			dataSource.setUsername("SA");
			dataSource.setPassword("SA");
			dataSource.setDriverClassName(DriverTypeEnum.H2_EMBEDDED.getDriverClassName());
			HapiMigrator migrator = new HapiMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, dataSource, DriverTypeEnum.H2_EMBEDDED);
			return new TestDatabaseDetails(url, connectionProperties, dataSource, migrator);
		}

		@Override
		public String toString() {
			return H2;
		}
	};

	private BasicDataSource myDataSource;
	private String myUrl;
	private HapiMigrator myMigrator;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	protected HapiMigrationDao myHapiMigrationDao;
	protected HapiMigrationStorageSvc myHapiMigrationStorageSvc;

	public static Stream<Arguments> dataWithEvaluationResults() {
		return Stream.of(
			Arguments.of(TEST_DATABASE_DETAILS_H2_SUPPLIER, List.of(true, true), true),
			Arguments.of(TEST_DATABASE_DETAILS_H2_SUPPLIER, List.of(false, true), false),
			Arguments.of(TEST_DATABASE_DETAILS_H2_SUPPLIER, List.of(true, false), false),
			Arguments.of(TEST_DATABASE_DETAILS_H2_SUPPLIER, List.of(false, false), false),
			Arguments.of(TEST_DATABASE_DETAILS_DERBY_SUPPLIER, List.of(true, true), true),
			Arguments.of(TEST_DATABASE_DETAILS_DERBY_SUPPLIER, List.of(false, true), false),
			Arguments.of(TEST_DATABASE_DETAILS_DERBY_SUPPLIER, List.of(true, false), false),
			Arguments.of(TEST_DATABASE_DETAILS_DERBY_SUPPLIER, List.of(false, false), false)
		);
	}

	public static Stream<Supplier<TestDatabaseDetails>> data() {

		ArrayList<Supplier<TestDatabaseDetails>> retVal = new ArrayList<>();

		// H2
		retVal.add(TEST_DATABASE_DETAILS_H2_SUPPLIER);

		// Derby
		retVal.add(TEST_DATABASE_DETAILS_DERBY_SUPPLIER);

		return retVal.stream();
	}

	public DriverTypeEnum before(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		TestDatabaseDetails testDatabaseDetails = theTestDatabaseDetails.get();
		myUrl = testDatabaseDetails.myUrl;
		myConnectionProperties = testDatabaseDetails.myConnectionProperties;
		myDataSource = testDatabaseDetails.myDataSource;
		myMigrator = testDatabaseDetails.myMigrator;
		myHapiMigrationDao = new HapiMigrationDao(testDatabaseDetails.myDataSource, testDatabaseDetails.getDriverType(), SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME);

		myHapiMigrationDao.createMigrationTableIfRequired();
		myHapiMigrationStorageSvc = new HapiMigrationStorageSvc(myHapiMigrationDao);

		return testDatabaseDetails.getDriverType();
	}

	public String getUrl() {
		return myUrl;
	}

	public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return myConnectionProperties;
	}

	protected BasicDataSource getDataSource() {
		return myDataSource;
	}

	@AfterEach
	public void resetMigrationVersion() throws SQLException {
		if (getConnectionProperties() != null) {
			Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
			if (tableNames.contains(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME)) {
				ourLog.info("Deleting entries in " + SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME);
				executeSql("DELETE from " + SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME + " where \"installed_rank\" > 0");
			}
		}
	}

	protected void executeSql(@Language("SQL") String theSql, Object... theArgs) {
		myConnectionProperties.getTxTemplate().execute(t -> {
			myConnectionProperties.newJdbcTemplate().update(theSql, theArgs);
			return null;
		});
	}

	protected List<Map<String, Object>> executeQuery(@Language("SQL") String theSql, Object... theArgs) {
		return myConnectionProperties.getTxTemplate().execute(t -> {
			return myConnectionProperties.newJdbcTemplate().query(theSql, theArgs, new ColumnMapRowMapper());
		});
	}

	public HapiMigrator getMigrator() {
		return myMigrator;
	}

	@AfterEach
	public void after() {
		if (myConnectionProperties != null) {
			myConnectionProperties.close();
			ourLog.info("connectionProperties was closed");
		}
	}

	protected DriverTypeEnum getDriverType() {
		return myConnectionProperties.getDriverType();
	}

	public static class TestDatabaseDetails {

		private final String myUrl;
		private final DriverTypeEnum.ConnectionProperties myConnectionProperties;
		private final BasicDataSource myDataSource;
		private final HapiMigrator myMigrator;

		public TestDatabaseDetails(String theUrl, DriverTypeEnum.ConnectionProperties theConnectionProperties, BasicDataSource theDataSource, HapiMigrator theMigrator) {
			myUrl = theUrl;
			myConnectionProperties = theConnectionProperties;
			myDataSource = theDataSource;
			myMigrator = theMigrator;
		}

		public DriverTypeEnum getDriverType() {
			return myConnectionProperties.getDriverType();
		}

	}

}
