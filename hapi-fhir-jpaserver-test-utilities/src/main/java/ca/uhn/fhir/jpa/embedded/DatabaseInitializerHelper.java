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

import ca.uhn.fhir.util.VersionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseInitializerHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseInitializerHelper.class);

	public void initializePersistenceSchema(JpaEmbeddedDatabase theDatabase) {
		String fileName = String.format(
				"migration/releases/%s/schema/%s.sql",
				HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION, theDatabase.getDriverType());
		String sql = getSqlFromResourceFile(fileName);
		theDatabase.executeSqlAsBatch(sql);
	}

	public void insertPersistenceTestData(JpaEmbeddedDatabase theDatabase, VersionEnum theVersionEnum) {
		String fileName =
				String.format("migration/releases/%s/data/%s.sql", theVersionEnum, theDatabase.getDriverType());
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
