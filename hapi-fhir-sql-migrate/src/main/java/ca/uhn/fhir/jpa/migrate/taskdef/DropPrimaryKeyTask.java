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
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.SQLException;

// LUKETODO:  make sure error handling is clear about what went wrong
public class DropPrimaryKeyTask extends BaseTableTask {
	private static final Logger ourLog = LoggerFactory.getLogger(DropPrimaryKeyTask.class);

	public DropPrimaryKeyTask(String theProductVersion, String theSchemaVersion, String theTableName) {
		super(theProductVersion, theSchemaVersion);
		setTableName(theTableName);
	}

	@Nonnull
	private String generateSql() {
		ourLog.info("6145: DropPrimaryKeyTask.generateSql()");
		final ResultSetExtractor<String> resultSetExtractor = rs -> {
			// LUKETODO:  error handling
			if (rs.next()) {
				return rs.getString(1);
			}
			return null;
		};

		final String primaryKeyName = executeSqlWithResult(
				generatePrimaryKeyIndexNameSql(), resultSetExtractor, getTableNameWithDatabaseExpectedCase());
		ourLog.info("6145: primaryKeyName: [{}]", primaryKeyName);
		return generateDropPrimaryKeySql(primaryKeyName);
	}

	private String getTableNameWithDatabaseExpectedCase() {
		if (DriverTypeEnum.ORACLE_12C == getDriverType()) {
			return getTableName().toUpperCase();
		}

		return getTableName().toLowerCase();
	}

	@Override
	protected void doExecute() throws SQLException {
		logInfo(ourLog, "Going to DROP the PRIMARY KEY on table {}", getTableName());

		// LUKETODO:  error handling?
		executeSql(getTableName(), generateSql());
	}

	private String generateDropPrimaryKeySql(String thePrimaryKeyName) {
		switch (getDriverType()) {
			case MARIADB_10_1:
			case DERBY_EMBEDDED:
			case H2_EMBEDDED:
				@Language("SQL")
				final String sqlH2 = "ALTER TABLE %s DROP PRIMARY KEY";
				return String.format(sqlH2, getTableName());
			case POSTGRES_9_4:
			case ORACLE_12C:
			case MSSQL_2012:
			case MYSQL_5_7:
				@Language("SQL")
				final String sql = "ALTER TABLE %s DROP CONSTRAINT %s";
				return String.format(sql, getTableName(), thePrimaryKeyName);
			default:
				throw new IllegalStateException(String.format(
						"%s Unknown driver type: %s.  Cannot drop primary key: %s for task %s",
						Msg.code(2529), getDriverType(), getMigrationVersion(), getTableName()));
		}
	}

	@Language("SQL")
	private String generatePrimaryKeyIndexNameSql() {
		switch (getDriverType()) {
			case MYSQL_5_7:
			case MARIADB_10_1:
			case DERBY_EMBEDDED:
			case COCKROACHDB_21_1:
			case H2_EMBEDDED:
				return "SELECT index_name " + "FROM information_schema.indexes "
						+ "WHERE table_schema = 'PUBLIC' "
						+ "AND index_type_name = 'PRIMARY KEY' "
						+ "AND table_name = ?";
			case POSTGRES_9_4:
				return "SELECT constraint_name " + "FROM information_schema.table_constraints "
						+ "WHERE table_schema = 'public' "
						+ "AND constraint_type = 'PRIMARY KEY' "
						+ "AND table_name = ?";
			case ORACLE_12C:
				return "SELECT constraint_name " + "FROM user_constraints "
						+ "WHERE constraint_type = 'P' "
						+ "AND table_name = ?";
			case MSSQL_2012:
				return "SELECT tc.constraint_name " + "FROM information_schema.table_constraints tc "
						+ "JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name "
						+ "WHERE tc.constraint_type = 'PRIMARY KEY' "
						+ "AND  tc.table_name = ?";
			default:
				throw new IllegalStateException(String.format(
						"%s Unknown driver type: %s  Cannot find primary key to drop for task %s",
						Msg.code(2530), getDriverType(), getMigrationVersion()));
		}
	}
}
