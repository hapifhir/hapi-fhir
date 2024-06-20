/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
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
package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Migration task that handles cross-database logic for adding a new primary key.
 */
public class AddPrimaryKeyTask extends BaseTableTask {
	private static final Logger ourLog = LoggerFactory.getLogger(AddPrimaryKeyTask.class);

	private final List<String> myPrimaryKeyColumnsInOrder;

	public AddPrimaryKeyTask(
			String theProductVersion, String theSchemaVersion, String theTableName, String... theColumnsInOrder) {
		super(theProductVersion, theSchemaVersion);
		setTableName(theTableName);

		myPrimaryKeyColumnsInOrder = Arrays.asList(theColumnsInOrder);
	}

	@Nonnull
	private String generateSql() {
		switch (getDriverType()) {
			case MYSQL_5_7:
			case MARIADB_10_1:
			case POSTGRES_9_4:
			case DERBY_EMBEDDED:
			case H2_EMBEDDED:
			case ORACLE_12C:
			case MSSQL_2012:
			case COCKROACHDB_21_1:
				return String.format(
						"ALTER TABLE %s ADD PRIMARY KEY (%s)",
						getTableName(), String.join(", ", myPrimaryKeyColumnsInOrder));
			default:
				throw new IllegalStateException(String.format(
						"%s Unknown driver type.  Cannot add primary key for task %s",
						Msg.code(2531), getMigrationVersion()));
		}
	}

	@Override
	protected void doExecute() throws SQLException {
		logInfo(
				ourLog,
				"Going to add a primary key on table {} for columns {}",
				getTableName(),
				myPrimaryKeyColumnsInOrder);

		executeSql(getTableName(), generateSql());
	}
}
