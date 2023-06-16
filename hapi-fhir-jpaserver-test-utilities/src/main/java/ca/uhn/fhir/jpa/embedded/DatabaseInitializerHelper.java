package ca.uhn.fhir.jpa.embedded;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseInitializerHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseInitializerHelper.class);

	public void initializePersistenceSchema(JpaEmbeddedDatabase theDatabase) {
		String fileName = String.format("migration/releases/%s/schema/%s.sql", HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION, theDatabase.getDriverType());
		String sql = getSqlFromResourceFile(fileName);
		theDatabase.executeSqlAsBatch(sql);
	}


	public void insertPersistenceTestData(JpaEmbeddedDatabase theDatabase) {
		String fileName = String.format("migration/releases/%s/data/%s.sql", HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION, theDatabase.getDriverType());
		String sql = getSqlFromResourceFile(fileName);
		theDatabase.insertTestData(sql);
	}

	public String getSqlFromResourceFile(String theFileName) {
		try {
			ourLog.info("Loading file: {}", theFileName);
			final URL resource = this.getClass().getClassLoader().getResource(theFileName);
			return Files.readString(Paths.get(resource.toURI()));
		} catch (Exception e) {
			throw new RuntimeException("Error loading file: " + theFileName, e);
		}
	}
}
