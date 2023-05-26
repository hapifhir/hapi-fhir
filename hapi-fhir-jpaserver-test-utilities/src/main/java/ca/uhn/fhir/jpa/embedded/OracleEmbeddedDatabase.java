package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.testcontainers.containers.OracleContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link DriverTypeEnum#ORACLE_12C} driver
 * and a dockerized Testcontainer.
 *
 * @see <a href="https://www.testcontainers.org/modules/databases/oraclexe/">Oracle TestContainer</a>
 */
public class OracleEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final OracleContainer myContainer;

	public OracleEmbeddedDatabase() {
		myContainer = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
			.withPrivilegedMode(true);
		myContainer.start();
		super.initialize(DriverTypeEnum.ORACLE_12C, myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
	}

	@Override
	public void stop() {
		myContainer.stop();
	}

	@Override
	public void disableConstraints() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults = query("SELECT CONSTRAINT_NAME, TABLE_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE != 'P'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			sql.add(String.format("ALTER TABLE \"%s\" DISABLE CONSTRAINT \"%s\" \n", tableName, constraintName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void enableConstraints() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults = query("SELECT CONSTRAINT_NAME, TABLE_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE != 'P'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			sql.add(String.format("ALTER TABLE \"%s\" ENABLE CONSTRAINT \"%s\" \n", tableName, constraintName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void clearDatabase() {
		dropTables();
		dropSequences();
		purgeRecycleBin();
	}

	private void purgeRecycleBin() {
		getJdbcTemplate().execute("PURGE RECYCLEBIN");
	}

	private void dropTables() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> tableResult = query(String.format("SELECT object_name FROM all_objects WHERE object_type = 'TABLE' AND owner = '%s'", getOwner()));
		for (Map<String, Object> result : tableResult) {
			String tableName = result.get("object_name").toString();
			sql.add(String.format("DROP TABLE \"%s\" CASCADE CONSTRAINTS PURGE", tableName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropSequences() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> tableResult = query(String.format("SELECT object_name FROM all_objects WHERE object_type = 'SEQUENCE' AND owner = '%s'", getOwner()));
		for (Map<String, Object> result : tableResult) {
			String sequenceName = result.get("object_name").toString();
			sql.add(String.format("DROP SEQUENCE \"%s\"", sequenceName));
		}
		executeSqlAsBatch(sql);
	}

	private String getOwner() {
		return getUsername().toUpperCase();
	}
}
