package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.testcontainers.containers.OracleContainer;

import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link DriverTypeEnum#ORACLE_12C} driver
 * and a dockerized Testcontainer.
 * @see <a href="https://www.testcontainers.org/modules/databases/oraclexe/">Oracle TestContainer</a>
 */
public class OracleEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final OracleContainer myContainer;

	public OracleEmbeddedDatabase(){
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
	public void clearDatabase() {
		dropTables();
		dropSequences();
	}

	private void dropTables() {
        String tableQuery = String.format("SELECT object_name FROM all_objects WHERE object_type = 'TABLE' AND owner = '%s'", getOwner());
        List<Map<String, Object>> tableResult = getJdbcTemplate().queryForList(tableQuery);
        for(Map<String, Object> result : tableResult){
            String tableName = result.get("object_name").toString();
            getJdbcTemplate().execute(String.format("DROP TABLE \"%s\" CASCADE CONSTRAINTS PURGE", tableName));
        }
	}

	private void dropSequences() {
        String sequenceQuery = String.format("SELECT object_name FROM all_objects WHERE object_type = 'SEQUENCE' AND owner = '%s'", getOwner());
        List<Map<String, Object>> tableResult = getJdbcTemplate().queryForList(sequenceQuery);
        for(Map<String, Object> result : tableResult){
            String sequenceName = result.get("object_name").toString();
            getJdbcTemplate().execute(String.format("DROP SEQUENCE \"%s\"", sequenceName));
        }
	}

    private String getOwner() {
        return getUsername().toUpperCase();
    }
}
