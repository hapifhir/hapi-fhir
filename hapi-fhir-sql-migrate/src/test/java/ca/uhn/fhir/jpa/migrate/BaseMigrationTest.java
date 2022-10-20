package ca.uhn.fhir.jpa.migrate;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import javax.sql.DataSource;

public abstract class BaseMigrationTest {
	private static final String TABLE_NAME = "TEST_MIGRATION_TABLE";
	protected static SimpleFlywayExecutor ourSimpleFlywayExecutor;
	@BeforeAll
	public static void beforeAll() {
		ourSimpleFlywayExecutor = new SimpleFlywayExecutor(getDataSource(), DriverTypeEnum.H2_EMBEDDED, TABLE_NAME);
	}

	static DataSource getDataSource() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:test_migration");
		retVal.setMaxWaitMillis(30000);
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(5);

		return retVal;
	}

	@AfterEach
	void after() {
		ourSimpleFlywayExecutor.dropTable();
	}

}
