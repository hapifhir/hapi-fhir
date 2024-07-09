/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link ca.uhn.fhir.jpa.migrate.DriverTypeEnum#H2_EMBEDDED} driver and a .h2 file in the target directory.
 */
public class H2EmbeddedDatabase extends JpaEmbeddedDatabase {

	private static final String SCHEMA_NAME = "test";
	private static final String USERNAME = "SA";
	private static final String PASSWORD = "SA";
	private static final String DATABASE_DIRECTORY = "target/h2-migration-tests/";

	private String myUrl;

	public H2EmbeddedDatabase() {
		deleteDatabaseDirectoryIfExists();
		String databasePath = DATABASE_DIRECTORY + SCHEMA_NAME;
		myUrl = "jdbc:h2:" + new File(databasePath).getAbsolutePath();
		super.initialize(DriverTypeEnum.H2_EMBEDDED, myUrl, USERNAME, PASSWORD);
	}

	@Override
	public void stop() {
		deleteDatabaseDirectoryIfExists();
	}

	private List<String> getAllTableNames() {
		List<String> allTableNames = new ArrayList<>();
		List<Map<String, Object>> queryResults =
				query("SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA = 'PUBLIC'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			allTableNames.add(tableName);
		}
		return allTableNames;
	}

	@Override
	public void disableConstraints() {
		getJdbcTemplate().execute("SET REFERENTIAL_INTEGRITY = FALSE");
	}

	@Override
	public void enableConstraints() {
		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("ALTER TABLE \"%s\" SET REFERENTIAL_INTEGRITY TRUE CHECK", tableName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void clearDatabase() {
		dropTables();
		dropSequences();
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
		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("DROP TABLE %s CASCADE", tableName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropSequences() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> sequenceResult =
				query("SELECT * FROM information_schema.sequences WHERE SEQUENCE_SCHEMA = 'PUBLIC'");
		for (Map<String, Object> sequence : sequenceResult) {
			String sequenceName = sequence.get("SEQUENCE_NAME").toString();
			sql.add(String.format("DROP SEQUENCE %s", sequenceName));
		}
		executeSqlAsBatch(sql);
	}
}
