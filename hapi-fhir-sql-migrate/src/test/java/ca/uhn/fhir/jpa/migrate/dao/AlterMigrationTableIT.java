package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.BaseMigrationTest;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlterMigrationTableIT {
	private static final Logger ourLog = LoggerFactory.getLogger(AlterMigrationTableIT.class);

	static final String TABLE_NAME = "TEST_MIGRATION_TABLE";
	protected static HapiMigrationDao ourHapiMigrationDao;
	static JdbcTemplate ourJdbcTemplate;
	protected static HapiMigrationStorageSvc ourHapiMigrationStorageSvc;

	@BeforeAll
	public static void beforeAll() {
		BasicDataSource dataSource = BaseMigrationTest.getDataSource();
		ourHapiMigrationDao = new HapiMigrationDao(dataSource, DriverTypeEnum.H2_EMBEDDED, TABLE_NAME);
		ourJdbcTemplate = new JdbcTemplate(dataSource);
		createOldTable();
	}

	private static void createOldTable() {
		String oldSchema = """
			CREATE TABLE "TEST_MIGRATION_TABLE" (
			"installed_rank" INTEGER NOT NULL,
			"version" VARCHAR(50),"description" VARCHAR(200) NOT NULL,
			"type" VARCHAR(20) NOT NULL,"script" VARCHAR(1000) NOT NULL,
			"checksum" INTEGER,
			"installed_by" VARCHAR(100) NOT NULL,
			"installed_on" DATE NOT NULL,
			"execution_time" INTEGER NOT NULL,
			"success" boolean NOT NULL)
			""";
		ourJdbcTemplate.execute(oldSchema);
	}

	@Test
	void testNewColumnAdded() {
		assertFalse(doesColumnExist("TEST_MIGRATION_TABLE", "RESULT"));
		ourHapiMigrationDao.createMigrationTableIfRequired();
		assertTrue(doesColumnExist("TEST_MIGRATION_TABLE", "RESULT"));
	}

	private boolean doesColumnExist(String theTableName, String theColumnName) {
		String sql = """
        SELECT COLUMN_NAME 
        FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_NAME = ?
    """;

		List<String> columns = ourJdbcTemplate.query(
			sql,
			new Object[] { theTableName.toUpperCase() },
			(rs, rowNum) -> rs.getString("COLUMN_NAME")
		);

		ourLog.info("Columns in table '{}': {}", theTableName, columns);

		return columns.stream()
			.map(String::toUpperCase)
			.anyMatch(columnName -> columnName.equals(theColumnName.toUpperCase()));
	}
}
