package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

public class PostgresEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final DriverTypeEnum myDriverType = DriverTypeEnum.POSTGRES_9_4;
	private final PostgreSQLContainer myContainer;
	private final DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private final JdbcTemplate myJdbcTemplate;

	public PostgresEmbeddedDatabase(){
		myContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
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
		dropTables();
		dropSequences();
	}

	@Override
	public ConnectionDetails getConnectionDetails(){
		return new ConnectionDetails(myDriverType, myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
	}

	private void dropTables() {
		List<Map<String, Object>> tableResult = myJdbcTemplate.queryForList("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
		for(Map<String, Object> result : tableResult){
			String tableName = result.get("table_name").toString();
			myJdbcTemplate.execute(String.format("DROP TABLE \"%s\" CASCADE", tableName));
		}
	}

	private void dropSequences() {
		List<Map<String, Object>> sequenceResult = myJdbcTemplate.queryForList("SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public'");
		for(Map<String, Object> sequence : sequenceResult){
			String sequenceName = sequence.get("sequence_name").toString();
			myJdbcTemplate.execute(String.format("DROP SEQUENCE \"%s\" CASCADE", sequenceName));
		}
	}
}
