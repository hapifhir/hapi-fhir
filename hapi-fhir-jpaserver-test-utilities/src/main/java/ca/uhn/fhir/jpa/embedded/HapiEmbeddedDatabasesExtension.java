/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.util.DatabaseSupportUtil;
import ca.uhn.fhir.test.utilities.docker.DockerRequiredCondition;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class HapiEmbeddedDatabasesExtension implements AfterAllCallback {

	public final JpaEmbeddedDatabase h2Database = new H2EmbeddedDatabase();
	public final JpaEmbeddedDatabase postgresDatabase = new PostgresEmbeddedDatabase();
	public final JpaEmbeddedDatabase mssql2012Database = new MsSqlEmbeddedDatabase();
	public final JpaEmbeddedDatabase oracle21Database = new Oracle21EmbeddedDatabase();
	public final JpaEmbeddedDatabase oracle23Database = new Oracle23EmbeddedDatabase();

	public static final VersionEnum FIRST_TESTED_VERSION = VersionEnum.V5_1_0;

	private static final Logger ourLog = LoggerFactory.getLogger(HapiEmbeddedDatabasesExtension.class);

	private final Set<JpaEmbeddedDatabase> myEmbeddedDatabases = new HashSet<>();

	private final DatabaseInitializerHelper myDatabaseInitializerHelper = new DatabaseInitializerHelper();

	public HapiEmbeddedDatabasesExtension() {
		if (DockerRequiredCondition.isDockerAvailable()) {
			myEmbeddedDatabases.add(h2Database);
			myEmbeddedDatabases.add(postgresDatabase);
			myEmbeddedDatabases.add(mssql2012Database);
			if (DatabaseSupportUtil.canUseOracle()) {
				myEmbeddedDatabases.add(oracle21Database);
				myEmbeddedDatabases.add(oracle23Database);
			} else {
				String message =
						"Cannot add any of the Oracle Databases. If you are using a Mac you must configure the TestContainers API to run using Colima (https://www.testcontainers.org/supported_docker_environment#using-colima)";
				ourLog.warn(message);
			}
		} else {
			ourLog.warn("Docker is not available! Not going to start any embedded databases.");
		}
	}

	@Override
	public void afterAll(ExtensionContext theExtensionContext) {
		for (JpaEmbeddedDatabase database : getAllEmbeddedDatabases()) {
			database.stop();
		}
	}

	public void clearDatabases() {
		for (JpaEmbeddedDatabase database : getAllEmbeddedDatabases()) {
			// Only clear databases that are actually initialized
			if (database.isInitialized()) {
				ourLog.debug("Clearing database: {}", database.getDriverType());
				database.clearDatabase();
			} else {
				ourLog.debug("Skipping clear for uninitialized database: {}", database.getDriverType());
			}
		}
	}

	public Set<JpaEmbeddedDatabase> getAllEmbeddedDatabases() {
		return myEmbeddedDatabases;
	}

	public void initializePersistenceSchema(VersionEnum theSchemaVersion, JpaEmbeddedDatabase theDatabase) {
		myDatabaseInitializerHelper.initializePersistenceSchema(theDatabase, theSchemaVersion);
	}

	public void insertPersistenceTestData(VersionEnum theVersionEnum, JpaEmbeddedDatabase embeddedDatabase) {
		myDatabaseInitializerHelper.insertPersistenceTestData(embeddedDatabase, theVersionEnum);
	}

	public DatabaseInitializerHelper getDatabaseInitializerHelper() {
		return myDatabaseInitializerHelper;
	}

	public void maybeInsertPersistenceTestData(JpaEmbeddedDatabase theDatabase, VersionEnum theVersionEnum) {
		try {
			insertPersistenceTestData(theVersionEnum, theDatabase);
		} catch (Exception theE) {
			if (theE.getMessage().contains("Error loading file: migration/releases/")) {
				ourLog.info(
						"Could not insert persistence test data most likely because we don't have any for version {} and driver {}",
						theVersionEnum,
						theDatabase.getDriverType());
			} else {
				// throw sql execution Exceptions
				throw theE;
			}
		}
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
