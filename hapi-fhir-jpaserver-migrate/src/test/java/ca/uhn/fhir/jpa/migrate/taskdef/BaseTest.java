package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.FlywayMigrator;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseTest {

	public static final String TEST_MIGRATION_TABLE = "TEST_MIGRATION";
	private static final String DATABASE_NAME = "DATABASE";
	private static int ourDatabaseUrl = 0;
	private String myUrl;
	private FlywayMigrator myMigrator;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private final BasicDataSource myDataSource = new BasicDataSource();

	public String getUrl() {
		return myUrl;
	}

	public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return myConnectionProperties;
	}

	@Before()
	public void before() {
		org.h2.Driver.class.toString();
		myUrl = "jdbc:h2:mem:" + DATABASE_NAME + ourDatabaseUrl++;

		myConnectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(myUrl, "SA", "SA");
		myDataSource.setUrl(myUrl);
		myDataSource.setUsername("SA");
		myDataSource.setPassword("SA");
		myDataSource.setDriverClassName(DriverTypeEnum.H2_EMBEDDED.getDriverClassName());
		myMigrator = new FlywayMigrator(TEST_MIGRATION_TABLE, myDataSource);
	}

	protected BasicDataSource getDataSource() {
		return myDataSource;
	}

	@After
	public void resetMigrationVersion() throws SQLException {
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		if (tableNames.contains(TEST_MIGRATION_TABLE)) {
			executeSql("DELETE from " + TEST_MIGRATION_TABLE + " where \"installed_rank\" > 0");
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

	@After
	public void after() {
		myConnectionProperties.close();
	}


}
