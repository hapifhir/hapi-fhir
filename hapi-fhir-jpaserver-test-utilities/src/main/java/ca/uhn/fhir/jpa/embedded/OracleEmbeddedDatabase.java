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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.OracleContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link DriverTypeEnum#ORACLE_12C} driver
 * and a dockerized Testcontainer with lazy initialization.
 *
 * @see <a href="https://www.testcontainers.org/modules/databases/oraclexe/">Oracle TestContainer</a>
 */
public class OracleEmbeddedDatabase extends JpaEmbeddedDatabase {
	private JdbcDatabaseContainer<?> myContainer;

	public OracleEmbeddedDatabase(JdbcDatabaseContainer<?> theContainer) {
		myContainer = theContainer;
		this.setInitializionSupplier(() -> {
			myContainer.start();
			return new InitializationData(
					DriverTypeEnum.ORACLE_12C,
					myContainer.getJdbcUrl(),
					myContainer.getUsername(),
					myContainer.getPassword(),
					myContainer);
		});
	}

	@Override
	public DriverTypeEnum getDriverType() {
		return DriverTypeEnum.ORACLE_12C;
	}

	@Override
	public void stop() {
		OracleContainer container = (OracleContainer) getContainerReference();
		if (container != null && container.isRunning()) {
			container.stop();
		}
	}

	@Override
	public void disableConstraints() {
		purgeRecycleBin();
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults =
				query("SELECT CONSTRAINT_NAME, TABLE_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE != 'P'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			sql.add(String.format("ALTER TABLE \"%s\" DISABLE CONSTRAINT \"%s\" \n", tableName, constraintName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void enableConstraints() {
		purgeRecycleBin();
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> queryResults =
				query("SELECT CONSTRAINT_NAME, TABLE_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE != 'P'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("TABLE_NAME").toString();
			String constraintName = row.get("CONSTRAINT_NAME").toString();
			sql.add(String.format("ALTER TABLE \"%s\" ENABLE CONSTRAINT \"%s\" \n", tableName, constraintName));
		}
		executeSqlAsBatch(sql);
	}

	@Override
	public void clearDatabase() {
		dropTables();
		dropSequences();
		purgeRecycleBin();
	}

	private void purgeRecycleBin() {
		getJdbcTemplate().execute("PURGE RECYCLEBIN");
	}

	private void dropTables() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> tableResult = query(String.format(
				"SELECT object_name FROM all_objects WHERE object_type = 'TABLE' AND owner = '%s'", getOwner()));
		for (Map<String, Object> result : tableResult) {
			String tableName = result.get("object_name").toString();
			sql.add(String.format("DROP TABLE \"%s\" CASCADE CONSTRAINTS PURGE", tableName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropSequences() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> tableResult = query(String.format(
				"SELECT object_name FROM all_objects WHERE object_type = 'SEQUENCE' AND owner = '%s'", getOwner()));
		for (Map<String, Object> result : tableResult) {
			String sequenceName = result.get("object_name").toString();
			sql.add(String.format("DROP SEQUENCE \"%s\"", sequenceName));
		}
		executeSqlAsBatch(sql);
	}

	private String getOwner() {
		return getUsername().toUpperCase();
	}
}
