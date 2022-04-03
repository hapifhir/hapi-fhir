package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModifyColumnTask extends BaseTableColumnTypeTask {

	private static final Logger ourLog = LoggerFactory.getLogger(ModifyColumnTask.class);

	public ModifyColumnTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Modify column " + getColumnName() + " on table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {

		JdbcUtils.ColumnType existingType;
		boolean nullable;

		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
		if (!columnNames.contains(getColumnName())) {
			logInfo(ourLog, "Column {} doesn't exist on table {} - No action performed", getColumnName(), getTableName());
			return;
		}

		try {
			existingType = JdbcUtils.getColumnType(getConnectionProperties(), getTableName(), getColumnName());
			nullable = isColumnNullable(getTableName(), getColumnName());
		} catch (SQLException e) {
			throw new InternalErrorException(Msg.code(66) + e);
		}

		Long taskColumnLength = getColumnLength();
		boolean isShrinkOnly = false;
		if (taskColumnLength != null) {
			long existingLength = existingType.getLength() != null ? existingType.getLength() : 0;
			if (existingLength > taskColumnLength) {
				if (isNoColumnShrink()) {
					taskColumnLength = existingLength;
				} else {
					if (existingType.getColumnTypeEnum() == getColumnType()) {
						isShrinkOnly = true;
					}
				}
			}
		}

		boolean alreadyOfCorrectType = existingType.equals(getColumnType(), taskColumnLength);
		boolean alreadyCorrectNullable = isNullable() == nullable;
		if (alreadyOfCorrectType && alreadyCorrectNullable) {
			logInfo(ourLog, "Column {} on table {} is already of type {} and has nullable {} - No action performed", getColumnName(), getTableName(), existingType, nullable);
			return;
		}

		String type = getSqlType(taskColumnLength);
		String notNull = getSqlNotNull();

		String sql = null;
		String sqlNotNull = null;
		switch (getDriverType()) {
			case DERBY_EMBEDDED:
				if (!alreadyOfCorrectType) {
					sql = "alter table " + getTableName() + " alter column " + getColumnName() + " set data type " + type;
				}
				if (!alreadyCorrectNullable) {
					sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + notNull;
				}
				break;
			case MARIADB_10_1:
			case MYSQL_5_7:
				// Quote the column name as "SYSTEM" is a reserved word in MySQL
				sql = "alter table " + getTableName() + " modify column `" + getColumnName() + "` " + type + notNull;
				break;
			case POSTGRES_9_4:
				if (!alreadyOfCorrectType) {
					sql = "alter table " + getTableName() + " alter column " + getColumnName() + " type " + type;
				}
				if (!alreadyCorrectNullable) {
					if (isNullable()) {
						sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + " drop not null";
					} else {
						sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + " set not null";
					}
				}
				break;
			case ORACLE_12C:
				String oracleNullableStmt = !alreadyCorrectNullable ? notNull : "";
				sql = "alter table " + getTableName() + " modify ( " + getColumnName() + " " + type + oracleNullableStmt + " )";
				break;
			case MSSQL_2012:
				sql = "alter table " + getTableName() + " alter column " + getColumnName() + " " + type + notNull;
				break;
			case H2_EMBEDDED:
				if (!alreadyOfCorrectType) {
					sql = "alter table " + getTableName() + " alter column " + getColumnName() + " type " + type;
				}
				if (!alreadyCorrectNullable) {
					if (isNullable()) {
						sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + " drop not null";
					} else {
						sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + " set not null";
					}
				}
				break;
			default:
				throw new IllegalStateException(Msg.code(67) + "Dont know how to handle " + getDriverType());
		}

		if (!isFailureAllowed() && isShrinkOnly) {
			setFailureAllowed(true);
		}

		logInfo(ourLog, "Updating column {} on table {} to type {}", getColumnName(), getTableName(), type);
		if (sql != null) {
			executeSql(getTableName(), sql);
		}

		if (sqlNotNull != null) {
			logInfo(ourLog, "Updating column {} on table {} to not null", getColumnName(), getTableName());
			executeSql(getTableName(), sqlNotNull);
		}
	}

	private boolean isColumnNullable(String tableName, String columnName) throws SQLException {
		boolean result = JdbcUtils.isColumnNullable(getConnectionProperties(), tableName, columnName);
		// Oracle sometimes stores the NULLABLE property in a Constraint, so override the result if this is an Oracle DB
		switch (getDriverType()) {
			case ORACLE_12C:
				@Language("SQL") String findNullableConstraintSql =
					"SELECT acc.owner, acc.table_name, acc.column_name, search_condition_vc " +
						"FROM all_cons_columns acc, all_constraints ac " +
						"WHERE acc.constraint_name = ac.constraint_name " +
						"AND acc.table_name = ac.table_name " +
						"AND ac.constraint_type = ? " +
						"AND acc.table_name = ? " +
						"AND acc.column_name = ? " +
						"AND search_condition_vc = ? ";
				String[] params = new String[4];
				params[0] = "C";
				params[1] = tableName.toUpperCase();
				params[2] = columnName.toUpperCase();
				params[3] = "\"" + columnName.toUpperCase() + "\" IS NOT NULL";
				List<Map<String, Object>> queryResults = getConnectionProperties().getTxTemplate().execute(t -> {
					return getConnectionProperties().newJdbcTemplate().query(findNullableConstraintSql, params, new ColumnMapRowMapper());
				});
				// If this query returns a row then the existence of that row indicates that a NOT NULL constraint exists
				// on this Column and we must override whatever result was previously calculated and set it to false
				if (queryResults != null && queryResults.size() > 0 && queryResults.get(0) != null && !queryResults.get(0).isEmpty()) {
					result = false;
				}
				break;
			default:
				// Do nothing since we already initialized the variable above
				break;
		}
		return result;
	}
}
