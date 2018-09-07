package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.Migrator;
import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.util.List;
import java.util.Map;

public class BaseTest {

	private static int ourDatabaseUrl = 0;
	private String myUrl;
	private Migrator myMigrator;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;

	public String getUrl() {
		return myUrl;
	}

	public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return myConnectionProperties;
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

	public Migrator getMigrator() {
		return myMigrator;
	}

	@After
	public void after() {
		myConnectionProperties.close();
	}

	@Before()
	public void before() {
		myUrl = "jdbc:derby:memory:database " + (ourDatabaseUrl++) + ";create=true";

		myConnectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newConnectionProperties(myUrl, "SA", "SA");

		myMigrator = new Migrator();
		myMigrator.setConnectionUrl(myUrl);
		myMigrator.setDriverType(DriverTypeEnum.DERBY_EMBEDDED);
		myMigrator.setUsername("SA");
		myMigrator.setPassword("SA");
	}

}
