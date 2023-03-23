package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link ca.uhn.fhir.jpa.migrate.DriverTypeEnum#POSTGRES_9_4} driver
 * and a dockerized Testcontainer.
 * @see <a href="https://www.testcontainers.org/modules/databases/postgres/">Postgres TestContainer</a>
 */
public class PostgresEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final PostgreSQLContainer myContainer;

	public PostgresEmbeddedDatabase(){
		myContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
		myContainer.start();
		super.initialize(DriverTypeEnum.POSTGRES_9_4, myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
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
		List<Map<String, Object>> tableResult = getJdbcTemplate().queryForList("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
		for(Map<String, Object> result : tableResult){
			String tableName = result.get("table_name").toString();
			getJdbcTemplate().execute(String.format("DROP TABLE \"%s\" CASCADE", tableName));
		}
	}

	private void dropSequences() {
		List<Map<String, Object>> sequenceResult = getJdbcTemplate().queryForList("SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public'");
		for(Map<String, Object> sequence : sequenceResult){
			String sequenceName = sequence.get("sequence_name").toString();
			getJdbcTemplate().execute(String.format("DROP SEQUENCE \"%s\" CASCADE", sequenceName));
		}
	}
}
