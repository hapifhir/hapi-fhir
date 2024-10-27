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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link ca.uhn.fhir.jpa.migrate.DriverTypeEnum#POSTGRES_9_4} driver
 * and a dockerized Testcontainer.
 *
 * @see <a href="https://www.testcontainers.org/modules/databases/postgres/">Postgres TestContainer</a>
 */
public class PostgresEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final PostgreSQLContainer myContainer;

	public PostgresEmbeddedDatabase() {
		myContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
		myContainer.start();
		super.initialize(
				DriverTypeEnum.POSTGRES_9_4,
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
			sql.add(String.format("ALTER TABLE \"%s\" DISABLE TRIGGER ALL", tableName));
		}
		executeSqlAsBatch(sql);
	}

	public void validateConstraints() {
		getJdbcTemplate()
				.execute(
						"""
			do $$
			declare r record;
			BEGIN
			FOR r IN  (
			SELECT FORMAT(
				'UPDATE pg_constraint SET convalidated=false WHERE conname = ''%I''; ALTER TABLE %I VALIDATE CONSTRAINT %I;',
				tc.constraint_name,
				tc.table_name,
				tc.constraint_name
			) AS x
			FROM information_schema.table_constraints AS tc
			JOIN information_schema.tables t ON t.table_name = tc.table_name and t.table_type = 'BASE TABLE'
			JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name
			JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name
			WHERE  constraint_type = 'FOREIGN KEY'
				AND tc.constraint_schema = 'public'
			)
			LOOP
				EXECUTE (r.x);
			END LOOP;
			END;
			$$;""");
	}

	@Override
	public void enableConstraints() {

		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("ALTER TABLE \"%s\" ENABLE TRIGGER ALL", tableName));
		}
		executeSqlAsBatch(sql);
		validateConstraints();
	}

	@Override
	public void clearDatabase() {
		dropTables();
		dropSequences();
	}

	private void dropTables() {
		List<String> sql = new ArrayList<>();
		for (String tableName : getAllTableNames()) {
			sql.add(String.format("DROP TABLE \"%s\" CASCADE", tableName));
		}
		executeSqlAsBatch(sql);
	}

	private void dropSequences() {
		List<String> sql = new ArrayList<>();
		List<Map<String, Object>> sequenceResult = getJdbcTemplate()
				.queryForList(
						"SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public'");
		for (Map<String, Object> sequence : sequenceResult) {
			String sequenceName = sequence.get("sequence_name").toString();
			sql.add(String.format("DROP SEQUENCE \"%s\" CASCADE", sequenceName));
		}
		executeSqlAsBatch(sql);
	}

	private List<String> getAllTableNames() {
		List<String> allTableNames = new ArrayList<>();
		List<Map<String, Object>> queryResults =
				query("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
		for (Map<String, Object> row : queryResults) {
			String tableName = row.get("table_name").toString();
			allTableNames.add(tableName);
		}
		return allTableNames;
	}
}
