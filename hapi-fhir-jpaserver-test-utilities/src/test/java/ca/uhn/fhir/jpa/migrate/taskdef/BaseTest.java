package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.FlywayMigrator;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

// TODO KHS copied from hapi-fhir-sql-migrate
public abstract class BaseTest {

	private static final String DATABASE_NAME = "DATABASE";
	private static final Logger ourLog = LoggerFactory.getLogger(BaseTest.class);
	private BasicDataSource myDataSource;
	private String myUrl;
	private FlywayMigrator myMigrator;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;

	public static Stream<Supplier<TestDatabaseDetails>> data() {
		ourLog.info("H2: {}", org.h2.Driver.class);

		ArrayList<Supplier<TestDatabaseDetails>> retVal = new ArrayList<>();

		// H2
		retVal.add(new Supplier<TestDatabaseDetails>() {
			@Override
			public TestDatabaseDetails get() {
				String url = "jdbc:h2:mem:" + DATABASE_NAME + UUID.randomUUID();
				DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(url, "SA", "SA");
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setUrl(url);
				dataSource.setUsername("SA");
				dataSource.setPassword("SA");
				dataSource.setDriverClassName(DriverTypeEnum.H2_EMBEDDED.getDriverClassName());
				FlywayMigrator migrator = new FlywayMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, dataSource, DriverTypeEnum.H2_EMBEDDED);
				return new TestDatabaseDetails(url, connectionProperties, dataSource, migrator);
			}

			@Override
			public String toString() {
				return "H2";
			}
		});

		// Derby
		retVal.add(new Supplier<TestDatabaseDetails>() {
			@Override
			public TestDatabaseDetails get() {
				String url = "jdbc:derby:memory:" + DATABASE_NAME + UUID.randomUUID() + ";create=true";
				DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(url, "SA", "SA");
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setUrl(url);
				dataSource.setUsername("SA");
				dataSource.setPassword("SA");
				dataSource.setDriverClassName(DriverTypeEnum.DERBY_EMBEDDED.getDriverClassName());
				FlywayMigrator migrator = new FlywayMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, dataSource, DriverTypeEnum.DERBY_EMBEDDED);
				return new TestDatabaseDetails(url, connectionProperties, dataSource, migrator);
			}

			@Override
			public String toString() {
				return "Derby";
			}
		});

		return retVal.stream();
	}

	public void before(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		TestDatabaseDetails testDatabaseDetails = theTestDatabaseDetails.get();
		myUrl = testDatabaseDetails.myUrl;
		myConnectionProperties = testDatabaseDetails.myConnectionProperties;
		myDataSource = testDatabaseDetails.myDataSource;
		myMigrator = testDatabaseDetails.myMigrator;
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

	public FlywayMigrator getMigrator() {
		return myMigrator;
	}

	@AfterEach
	public void after() {
		if (myConnectionProperties != null) {
			myConnectionProperties.close();
		}
	}

	protected DriverTypeEnum getDriverType() {
		return myConnectionProperties.getDriverType();
	}

	public static class TestDatabaseDetails {

		private final String myUrl;
		private final DriverTypeEnum.ConnectionProperties myConnectionProperties;
		private final BasicDataSource myDataSource;
		private final FlywayMigrator myMigrator;

		public TestDatabaseDetails(String theUrl, DriverTypeEnum.ConnectionProperties theConnectionProperties, BasicDataSource theDataSource, FlywayMigrator theMigrator) {
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
