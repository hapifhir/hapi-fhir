package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class H2EmbeddedDatabase extends JpaEmbeddedDatabase {

	private static final DriverTypeEnum myDriverType = DriverTypeEnum.H2_EMBEDDED;
	private static final String SCHEMA_NAME = "test";
	private static final String USERNAME = "SA";
	private static final String PASSWORD = "SA";
	public static final String DATABASE_DIRECTORY = "target/h2-migration-tests/";

	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private JdbcTemplate myJdbcTemplate;
	private String myUrl;

	public H2EmbeddedDatabase(){
		deleteDatabaseDirectoryIfExists();
		String databasePath = DATABASE_DIRECTORY + SCHEMA_NAME;
		myUrl = "jdbc:h2:" + new File(databasePath).getAbsolutePath();
		myConnectionProperties = myDriverType.newConnectionProperties(myUrl, USERNAME, PASSWORD);
		myJdbcTemplate = myConnectionProperties.newJdbcTemplate();
	}

	@Override
	public void stop() {
		deleteDatabaseDirectoryIfExists();
	}

	@Override
	public void clearDatabase() {
		dropTables();
		dropSequences();
	}

	@Override
	public ConnectionDetails getConnectionDetails(){
		return new ConnectionDetails(myDriverType, myUrl, USERNAME, PASSWORD);
	}

	private void deleteDatabaseDirectoryIfExists() {
		File directory = new File(DATABASE_DIRECTORY);
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException theE) {
				throw new RuntimeException("Could not delete database directory: " + DATABASE_DIRECTORY);
			}
		}
	}

	private void dropTables() {
		List<Map<String, Object>> tableResult = myJdbcTemplate.queryForList("SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA = 'PUBLIC'");
		for(Map<String, Object> result : tableResult){
			String tableName = result.get("TABLE_NAME").toString();
			myJdbcTemplate.execute(String.format("DROP TABLE %s CASCADE", tableName));
		}
	}

	private void dropSequences() {
		List<Map<String, Object>> sequenceResult = myJdbcTemplate.queryForList("SELECT * FROM information_schema.sequences WHERE SEQUENCE_SCHEMA = 'PUBLIC'");
		for(Map<String, Object> sequence : sequenceResult){
			String sequenceName = sequence.get("SEQUENCE_NAME").toString();
			myJdbcTemplate.execute(String.format("DROP SEQUENCE %s", sequenceName));
		}
	}
}
