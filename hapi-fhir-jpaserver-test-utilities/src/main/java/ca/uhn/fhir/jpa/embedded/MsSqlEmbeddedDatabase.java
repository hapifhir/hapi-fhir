package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;


public class MsSqlEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final DriverTypeEnum myDriverType = DriverTypeEnum.MSSQL_2012;
	private final MSSQLServerContainer myContainer;
	private final DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private final JdbcTemplate myJdbcTemplate;

	public MsSqlEmbeddedDatabase(){
		DockerImageName msSqlImage = DockerImageName.parse("mcr.microsoft.com/azure-sql-edge:latest").asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server");
		myContainer = new MSSQLServerContainer(msSqlImage).acceptLicense();
		myContainer.start();
		myConnectionProperties = myDriverType.newConnectionProperties(myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
		myJdbcTemplate = myConnectionProperties.newJdbcTemplate();
	}

	@Override
	public void stop() {
		myContainer.stop();
	}

	@Override
	public void clearDatabase() {
		dropForeignKeys();
		dropRemainingConstraints();
		dropTables();
		dropSequences();
	}

	@Override
	public ConnectionDetails getConnectionDetails(){
		return new ConnectionDetails(myDriverType, myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
	}

	private void dropForeignKeys() {
		List<Map<String, Object>> queryResults = myJdbcTemplate.queryForList("SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'");
		for(Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			myJdbcTemplate.execute(String.format("ALTER TABLE \"%s\" DROP CONSTRAINT \"%s\"", tableName, constraintName));
		}
	}

	private void dropRemainingConstraints() {
		List<Map<String, Object>> queryResults = myJdbcTemplate.queryForList("SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS");
		for(Map<String, Object> row : queryResults){
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			myJdbcTemplate.execute(String.format("ALTER TABLE \"%s\" DROP CONSTRAINT \"%s\"", tableName, constraintName));
		}
	}

	private void dropTables() {
		List<Map<String, Object>> queryResults = myJdbcTemplate.queryForList("SELECT name FROM SYS.TABLES WHERE is_ms_shipped = 'false'");
		for(Map<String, Object> row : queryResults){
			String tableName = row.get("name").toString();
			myJdbcTemplate.execute(String.format("DROP TABLE \"%s\"", tableName));
		}
	}

	private void dropSequences() {
		List<Map<String, Object>> queryResults = myJdbcTemplate.queryForList("SELECT name FROM SYS.SEQUENCES WHERE is_ms_shipped = 'false'");
		for(Map<String, Object> row : queryResults){
			String sequenceName = row.get("name").toString();
			myJdbcTemplate.execute(String.format("DROP SEQUENCE \"%s\"", sequenceName));
		}
	}
}
