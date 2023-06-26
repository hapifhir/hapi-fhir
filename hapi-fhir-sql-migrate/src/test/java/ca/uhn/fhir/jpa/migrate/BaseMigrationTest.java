package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseMigrationTest {
	static final String TABLE_NAME = "TEST_MIGRATION_TABLE";
	protected static HapiMigrationDao ourHapiMigrationDao;
	protected static HapiMigrationStorageSvc ourHapiMigrationStorageSvc;

	@BeforeAll
	public static void beforeAll() {
		ourHapiMigrationDao = new HapiMigrationDao(getDataSource(), DriverTypeEnum.H2_EMBEDDED, TABLE_NAME);
		ourHapiMigrationDao.createMigrationTableIfRequired();
		ourHapiMigrationStorageSvc = new HapiMigrationStorageSvc(ourHapiMigrationDao);
	}

	 static BasicDataSource getDataSource() {
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
		ourHapiMigrationDao.deleteAll();
	}

}
