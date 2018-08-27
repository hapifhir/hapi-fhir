package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.Migrator;
import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;

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


	protected void executeSql(@Language("SQL") String theSql) {
		myConnectionProperties.getTxTemplate().execute(t -> {
			myConnectionProperties.newJdbcTemplate().execute(theSql);
			return null;
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

		myConnectionProperties = DriverTypeEnum.DERBY_EMBEDDED.newJdbcTemplate(myUrl, "SA", "SA");

		myMigrator = new Migrator();
		myMigrator.setConnectionUrl(myUrl);
		myMigrator.setDriverType(DriverTypeEnum.DERBY_EMBEDDED);
		myMigrator.setUsername("SA");
		myMigrator.setPassword("SA");
	}

}
