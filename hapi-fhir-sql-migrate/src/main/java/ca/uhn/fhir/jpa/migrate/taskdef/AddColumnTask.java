/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class AddColumnTask extends BaseTableColumnTypeTask {

	private static final Logger ourLog = LoggerFactory.getLogger(AddColumnTask.class);

	public static AddColumnTask lowerCase(Set<ColumnDriverMappingOverride> theColumnDriverMappingOverrides) {
		return new AddColumnTask(null, null, ColumnNameCase.ALL_LOWER, theColumnDriverMappingOverrides);
	}

	public AddColumnTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	private AddColumnTask(
			String theProductVersion,
			String theSchemaVersion,
			ColumnNameCase theColumnNameCase,
			Set<ColumnDriverMappingOverride> theColumnDriverMappingOverrides) {
		super(theProductVersion, theSchemaVersion, theColumnNameCase, theColumnDriverMappingOverrides);
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Add column " + getColumnName() + " on table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		if (myCheckForExistingTables) {
			Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
			if (columnNames.contains(getColumnName())) {
				logInfo(
						ourLog,
						"Column {} already exists on table {} - No action performed",
						getColumnName(),
						getTableName());
				return;
			}
		}

		String sql = generateSql();
		logInfo(ourLog, "Adding column {} of type {} to table {}", getColumnName(), getSqlType(), getTableName());
		executeSql(getTableName(), sql);
	}

	String generateSql() {
		String typeStatement = getTypeStatement();

		String sql;
		switch (getDriverType()) {
			case MYSQL_5_7:
			case MARIADB_10_1:
				// Quote the column name as "SYSTEM" is a reserved word in MySQL
				sql = "alter table " + getTableName() + " add column `" + getColumnName() + "` " + typeStatement
						+ buildDefaultClauseIfApplicable();
				break;
			case DERBY_EMBEDDED:
			case POSTGRES_9_4:
			case COCKROACHDB_21_1:
				sql = "alter table " + getTableName() + " add column " + getColumnName() + " " + typeStatement
						+ buildDefaultClauseIfApplicable();
				break;
			case MSSQL_2012:
			case ORACLE_12C:
			case H2_EMBEDDED:
				sql = "alter table " + getTableName() + " add " + getColumnName() + " " + typeStatement
						+ buildDefaultClauseIfApplicable();
				break;
			default:
				throw new IllegalStateException(Msg.code(60));
		}

		return sql;
	}

	@Nonnull
	private String buildDefaultClauseIfApplicable() {
		return buildString(getDefaultValue(), (obj -> " default " + obj), "");
	}

	public String getTypeStatement() {
		String type = getSqlType();
		String nullable = getSqlNotNull();
		if (isNullable()) {
			nullable = "";
		}
		if (myPrettyPrint) {
			nullable = nullable.trim();
		}
		String space = isNullable() ? "" : " ";
		return type + space + nullable;
	}
}
