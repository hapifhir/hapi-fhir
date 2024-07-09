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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link ca.uhn.fhir.jpa.migrate.DriverTypeEnum#MSSQL_2012} driver
 * and a dockerized Testcontainer.
 *
 * @see <a href="https://www.testcontainers.org/modules/databases/mssqlserver/">MS SQL Server TestContainer</a>
 */
public class MsSqlEmbeddedDatabase extends JpaEmbeddedDatabase {
	private static final Logger ourLog = LoggerFactory.getLogger(MsSqlEmbeddedDatabase.class);

	private final MSSQLServerContainer myContainer;

	public MsSqlEmbeddedDatabase() {
		DockerImageName msSqlImage = DockerImageName.parse("mcr.microsoft.com/azure-sql-edge:latest")
				.asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server");
		myContainer = new MSSQLServerContainer(msSqlImage).acceptLicense();
		myContainer.start();
		super.initialize(
				DriverTypeEnum.MSSQL_2012,
				myContainer.getJdbcUrl(),
				myContainer.getUsername(),
				myContainer.getPassword());
	}

	@Override
	public void stop() {
		myContainer.stop();
	}

	@Override
	public void disableConstraints() {
		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("ALTER TABLE \"%s\" NOCHECK CONSTRAINT ALL;", tableName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void enableConstraints() {
		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("ALTER TABLE \"%s\" WITH CHECK CHECK CONSTRAINT ALL;", tableName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void clearDatabase() {
		dropForeignKeys();
		dropRemainingConstraints();
		dropTables();
		dropSequences();
	}

	private void dropForeignKeys() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults =
				query("SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			sql.add(String.format("ALTER TABLE \"%s\" DROP CONSTRAINT \"%s\"", tableName, constraintName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropRemainingConstraints() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults = query("SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS");
		for (Map<String, Object> row : queryResults) {
			Object tableNameEntry = row.get("TABLE_NAME");
			if (tableNameEntry == null) {
				ourLog.warn("Found a constraint with no table name: {}", row);
				continue;
			}
			String tableName = tableNameEntry.toString();
			Object constraintNameEntry = row.get("CONSTRAINT_NAME");
			if (constraintNameEntry == null) {
				ourLog.warn("Found a constraint with no constraint name: {}", row);
				continue;
			}
			String constraintName = constraintNameEntry.toString();
			sql.add(String.format("ALTER TABLE \"%s\" DROP CONSTRAINT \"%s\"", tableName, constraintName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropTables() {
		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("DROP TABLE \"%s\"", tableName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropSequences() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults = query("SELECT name FROM SYS.SEQUENCES WHERE is_ms_shipped = 'false'");
		for (Map<String, Object> row : queryResults) {
			String sequenceName = row.get("name").toString();
			sql.add(String.format("DROP SEQUENCE \"%s\"", sequenceName));
		}
		executeSqlAsBatch(sql);
	}

	private List<String> getAllTableNames() {
		List<String> allTableNames = new ArrayList<>();
		List<Map<String, Object>> queryResults = query("SELECT name FROM SYS.TABLES WHERE is_ms_shipped = 'false'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("name").toString();
			allTableNames.add(tableName);
		}
		return allTableNames;
	}
}
