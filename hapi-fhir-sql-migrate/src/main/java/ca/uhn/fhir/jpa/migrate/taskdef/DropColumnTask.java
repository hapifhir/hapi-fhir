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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class DropColumnTask extends BaseTableColumnTask {

	private static final Logger ourLog = LoggerFactory.getLogger(DropColumnTask.class);

	/**
	 * Constructor
	 */
	public DropColumnTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Language("SQL")
	static String createSql(String theTableName, String theColumnName) {
		return "alter table " + theTableName + " drop column " + theColumnName;
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Drop column " + getColumnName() + " from table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
		if (!columnNames.contains(getColumnName())) {
			logInfo(ourLog, "Column {} does not exist on table {} - No action performed", getColumnName(), getTableName());
			return;
		}

		if (getDriverType().equals(DriverTypeEnum.MYSQL_5_7) || getDriverType().equals(DriverTypeEnum.MARIADB_10_1)
			|| getDriverType().equals(DriverTypeEnum.MSSQL_2012)) {
			// Some DBs such as MYSQL and Maria DB require that foreign keys depending on the column be dropped before the column itself is dropped.
			logInfo(ourLog, "Dropping any foreign keys on table {} depending on column {}", getTableName(), getColumnName());
			Set<String> foreignKeys = JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), getColumnName(), getTableName());
			if (foreignKeys != null) {
				for (String foreignKey : foreignKeys) {
					List<String> dropFkSqls = DropForeignKeyTask.generateSql(getTableName(), foreignKey, getDriverType());
					for (String dropFkSql : dropFkSqls) {
						executeSql(getTableName(), dropFkSql);
					}
				}
			}
		}

		String tableName = getTableName();
		String columnName = getColumnName();
		String sql = createSql(tableName, columnName);
		logInfo(ourLog, "Dropping column {} on table {}", getColumnName(), getTableName());
		executeSql(getTableName(), sql);
	}


}
