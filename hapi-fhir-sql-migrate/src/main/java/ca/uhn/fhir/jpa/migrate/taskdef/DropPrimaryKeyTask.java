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
import com.google.common.collect.ImmutableList;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Migration task that handles cross-database logic for dropping a primary key.
 * <p>
 * The process involves 2 steps for most databases:
 * <ol>
 *     <li>Running SQL to introspect the metadata tables to determine the name of the primary key.</li>
 *     <li>Running an ALTER TABLE to drop the constraint found above by name.</li>
 * </ol>
 */
public class DropPrimaryKeyTask extends BaseTableTask {
	private static final Logger ourLog = LoggerFactory.getLogger(DropPrimaryKeyTask.class);

	public DropPrimaryKeyTask(String theProductVersion, String theSchemaVersion, String theTableName) {
		super(theProductVersion, theSchemaVersion);
		setTableName(theTableName);
	}

	@Nonnull
	private String generateSql() {
		ourLog.debug("DropPrimaryKeyTask.generateSql()");

		@Nullable
		@Language("SQL")
		final String primaryKeyName = getPrimaryKeyName();

		ourLog.debug("primaryKeyName: {} for driver: {}", primaryKeyName, getDriverType());

		return generateDropPrimaryKeySql(primaryKeyName);
	}

	@Override
	protected void doExecute() throws SQLException {
		logInfo(ourLog, "Going to DROP the PRIMARY KEY on table {}", getTableName());

		executeSql(getTableName(), generateSql());
	}

	private String generateDropPrimaryKeySql(@Nullable String thePrimaryKeyName) {
		try (Connection connection = getConnectionProperties().getDataSource().getConnection()) {
			switch (getDriverType()) {
				case MARIADB_10_1:
				case DERBY_EMBEDDED:
				case H2_EMBEDDED:
					@Language("SQL")
					final String sqlH2 = "ALTER TABLE %s DROP PRIMARY KEY";
					return String.format(
						sqlH2,
						Optional.of(connection.getSchema())
							.map(schema -> String.format("%s.%s", schema, getTableName()))
							.orElse(getTableName()));
				case POSTGRES_9_4:
				case ORACLE_12C:
				case MSSQL_2012:
				case MYSQL_5_7:
					assert thePrimaryKeyName != null;
					@Language("SQL")
					final String sql = "ALTER TABLE %s DROP CONSTRAINT %s";
					return String.format(
						sql,
						Optional.of(connection.getSchema())
							.map(schema -> String.format("%s.%s", schema, getTableName()))
							.orElse(getTableName()),
						thePrimaryKeyName);
				default:
					throw new IllegalStateException(String.format(
						"%s Unknown driver type: %s.  Cannot drop primary key: %s for task %s",
						Msg.code(2529), getDriverType(), getMigrationVersion(), getTableName()));
			}

		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings({"NestedTryStatement", "MethodWithMultipleLoops"})
	@Nullable
	private String getPrimaryKeyName() {
		String primaryKey = null;
		try (Connection connection = getConnectionProperties().getDataSource().getConnection()) {
			for (String tableName : ImmutableList.of(
				getTableName().toLowerCase(), getTableName().toUpperCase())) {
				try (ResultSet resultSet = connection
					.getMetaData()
					.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName)) {
					while (resultSet.next()) {
						primaryKey = resultSet.getString(6);
					}
				} catch (SQLException e) {
					throw new IllegalStateException(e);
				}
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}

		return primaryKey;
	}
}
