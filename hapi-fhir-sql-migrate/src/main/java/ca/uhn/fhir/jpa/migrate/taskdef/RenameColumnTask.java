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
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class RenameColumnTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(RenameColumnTask.class);
	private String myOldName;
	private String myNewName;
	private boolean myIsOkayIfNeitherColumnExists;
	private boolean myDeleteTargetColumnFirstIfBothExist;

	private boolean mySimulateMySQLForTest = false;

	public RenameColumnTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public void setDeleteTargetColumnFirstIfBothExist(boolean theDeleteTargetColumnFirstIfBothExist) {
		myDeleteTargetColumnFirstIfBothExist = theDeleteTargetColumnFirstIfBothExist;
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Rename column " + myOldName + " to " + myNewName + " on table " + getTableName());
	}

	public void setOldName(String theOldName) {
		Validate.notBlank(theOldName);
		myOldName = theOldName;
	}

	public void setNewName(String theNewName) {
		Validate.notBlank(theNewName);
		myNewName = theNewName;
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
		boolean haveOldName = columnNames.contains(myOldName.toUpperCase());
		boolean haveNewName = columnNames.contains(myNewName.toUpperCase());
		if (haveOldName && haveNewName) {
			if (myDeleteTargetColumnFirstIfBothExist) {

				Integer rowsWithData = getConnectionProperties().getTxTemplate().execute(t -> {
					String sql = "SELECT * FROM " + getTableName() + " WHERE " + myNewName + " IS NOT NULL";
					JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
					jdbcTemplate.setMaxRows(1);
					return jdbcTemplate.query(sql, new ColumnMapRowMapper()).size();
				});
				if (rowsWithData != null && rowsWithData > 0) {
					throw new SQLException(Msg.code(54) + "Can not rename " + getTableName() + "." + myOldName + " to " + myNewName + " because both columns exist and data exists in " + myNewName);
				}

				if (getDriverType().equals(DriverTypeEnum.MYSQL_5_7) || mySimulateMySQLForTest) {
					// Some DBs such as MYSQL require that foreign keys depending on the column be explicitly dropped before the column itself is dropped.
					logInfo(ourLog, "Table {} has columns {} and {} - Going to drop any foreign keys depending on column {} before renaming", getTableName(), myOldName, myNewName, myNewName);
					Set<String> foreignKeys = JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), myNewName, getTableName());
					if (foreignKeys != null) {
						for (String foreignKey : foreignKeys) {
							List<String> dropFkSqls = DropForeignKeyTask.generateSql(getTableName(), foreignKey, getDriverType());
							for (String dropFkSql : dropFkSqls) {
								executeSql(getTableName(), dropFkSql);
							}
						}
					}
				}

				logInfo(ourLog, "Table {} has columns {} and {} - Going to drop {} before renaming", getTableName(), myOldName, myNewName, myNewName);
				String sql = DropColumnTask.createSql(getTableName(), myNewName);
				executeSql(getTableName(), sql);
			} else {
				throw new SQLException(Msg.code(55) + "Can not rename " + getTableName() + "." + myOldName + " to " + myNewName + " because both columns exist!");
			}
		} else if (!haveOldName && !haveNewName) {
			if (isOkayIfNeitherColumnExists()) {
				return;
			}
			throw new SQLException(Msg.code(56) + "Can not rename " + getTableName() + "." + myOldName + " to " + myNewName + " because neither column exists!");
		} else if (haveNewName) {
			logInfo(ourLog, "Column {} already exists on table {} - No action performed", myNewName, getTableName());
			return;
		}

		String existingType;
		String notNull;
		try {
			JdbcUtils.ColumnType existingColumnType = JdbcUtils.getColumnType(getConnectionProperties(), getTableName(), myOldName);
			existingType = getSqlType(existingColumnType.getColumnTypeEnum(), existingColumnType.getLength());
			notNull = JdbcUtils.isColumnNullable(getConnectionProperties(), getTableName(), myOldName) ? " null " : " not null";
		} catch (SQLException e) {
			throw new InternalErrorException(Msg.code(57) + e);
		}
		String sql = buildRenameColumnSqlStatement(existingType, notNull);

		logInfo(ourLog, "Renaming column {} on table {} to {}", myOldName, getTableName(), myNewName);
		executeSql(getTableName(), sql);

	}

	String buildRenameColumnSqlStatement(String theExistingType, String theExistingNotNull) {
		String sql;
		switch (getDriverType()) {
			case DERBY_EMBEDDED:
				sql = "RENAME COLUMN " + getTableName() + "." + myOldName + " TO " + myNewName;
				break;
			case MYSQL_5_7:
			case MARIADB_10_1:
				// Quote the column names as "SYSTEM" is a reserved word in MySQL
				sql = "ALTER TABLE " + getTableName() + " CHANGE COLUMN `" + myOldName + "` `" + myNewName + "` " + theExistingType + " " + theExistingNotNull;
				break;
			case POSTGRES_9_4:
			case ORACLE_12C:
				sql = "ALTER TABLE " + getTableName() + " RENAME COLUMN " + myOldName + " TO " + myNewName;
				break;
			case MSSQL_2012:
				sql = "sp_rename '" + getTableName() + "." + myOldName + "', '" + myNewName + "', 'COLUMN'";
				break;
			case H2_EMBEDDED:
				sql = "ALTER TABLE " + getTableName() + " ALTER COLUMN " + myOldName + " RENAME TO " + myNewName;
				break;
			default:
				throw new IllegalStateException(Msg.code(58));
		}
		return sql;
	}

	public boolean isOkayIfNeitherColumnExists() {
		return myIsOkayIfNeitherColumnExists;
	}

	public void setOkayIfNeitherColumnExists(boolean theOkayIfNeitherColumnExists) {
		myIsOkayIfNeitherColumnExists = theOkayIfNeitherColumnExists;
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myOldName);
		theBuilder.append(myNewName);
	}

	@VisibleForTesting
	void setSimulateMySQLForTest(boolean theSimulateMySQLForTest) {
		mySimulateMySQLForTest = theSimulateMySQLForTest;
	}
}
