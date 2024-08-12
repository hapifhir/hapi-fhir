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
import ca.uhn.fhir.test.utilities.docker.DockerRequiredCondition;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.sql.DataSource;

public class HapiEmbeddedDatabasesExtension implements AfterAllCallback {

	public static final VersionEnum FIRST_TESTED_VERSION = VersionEnum.V5_1_0;

	private static final Logger ourLog = LoggerFactory.getLogger(HapiEmbeddedDatabasesExtension.class);

	private final Set<JpaEmbeddedDatabase> myEmbeddedDatabases = new HashSet<>();

	private final DatabaseInitializerHelper myDatabaseInitializerHelper = new DatabaseInitializerHelper();

	public HapiEmbeddedDatabasesExtension() {
		if (DockerRequiredCondition.isDockerAvailable()) {
			myEmbeddedDatabases.add(new H2EmbeddedDatabase());
			myEmbeddedDatabases.add(new PostgresEmbeddedDatabase());
			myEmbeddedDatabases.add(new MsSqlEmbeddedDatabase());
			if (OracleCondition.canUseOracle()) {
				myEmbeddedDatabases.add(new OracleEmbeddedDatabase());
			} else {
				String message =
						"Cannot add OracleEmbeddedDatabase. If you are using a Mac you must configure the TestContainers API to run using Colima (https://www.testcontainers.org/supported_docker_environment#using-colima)";
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

	public JpaEmbeddedDatabase getEmbeddedDatabase(DriverTypeEnum theDriverType) {
		return getAllEmbeddedDatabases().stream()
				.filter(db -> theDriverType.equals(db.getDriverType()))
				.findFirst()
				.orElseThrow();
	}

	public void clearDatabases() {
		for (JpaEmbeddedDatabase database : getAllEmbeddedDatabases()) {
			database.clearDatabase();
		}
	}

	public DataSource getDataSource(DriverTypeEnum theDriverTypeEnum) {
		return getEmbeddedDatabase(theDriverTypeEnum).getDataSource();
	}

	private Set<JpaEmbeddedDatabase> getAllEmbeddedDatabases() {
		return myEmbeddedDatabases;
	}

	public void initializePersistenceSchema(DriverTypeEnum theDriverType) {
		myDatabaseInitializerHelper.initializePersistenceSchema(getEmbeddedDatabase(theDriverType));
	}

	public void insertPersistenceTestData(DriverTypeEnum theDriverType, VersionEnum theVersionEnum) {
		myDatabaseInitializerHelper.insertPersistenceTestData(getEmbeddedDatabase(theDriverType), theVersionEnum);
	}

	public void maybeInsertPersistenceTestData(DriverTypeEnum theDriverType, VersionEnum theVersionEnum) {
		try {
			myDatabaseInitializerHelper.insertPersistenceTestData(getEmbeddedDatabase(theDriverType), theVersionEnum);
		} catch (Exception theE) {
			ourLog.info(
					"Could not insert persistence test data most likely because we don't have any for version {} and driver {}",
					theVersionEnum,
					theDriverType);
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

	public static class DatabaseVendorProvider implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
			List<Arguments> arguments = new ArrayList<>();
			arguments.add(Arguments.of(DriverTypeEnum.H2_EMBEDDED));
			arguments.add(Arguments.of(DriverTypeEnum.POSTGRES_9_4));
			arguments.add(Arguments.of(DriverTypeEnum.MSSQL_2012));

			if (OracleCondition.canUseOracle()) {
				arguments.add(Arguments.of(DriverTypeEnum.ORACLE_12C));
			}

			return arguments.stream();
		}
	}
}
