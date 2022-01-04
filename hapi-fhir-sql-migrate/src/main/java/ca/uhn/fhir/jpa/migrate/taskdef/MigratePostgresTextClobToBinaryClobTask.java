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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Locale;

public class MigratePostgresTextClobToBinaryClobTask extends BaseTableColumnTask {
	private static final Logger ourLog = LoggerFactory.getLogger(MigratePostgresTextClobToBinaryClobTask.class);

	/**
	 * Constructor
	 */
	public MigratePostgresTextClobToBinaryClobTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Migrate text clob " + getColumnName() + " from table " + getTableName() + " (only affects Postgresql)");
	}

	@Override
	protected void doExecute() throws SQLException {
		if (getConnectionProperties().getDriverType() != DriverTypeEnum.POSTGRES_9_4) {
			return;
		}

		String tableName = getTableName();
		String columnName = getColumnName();
		JdbcUtils.ColumnType columnType = JdbcUtils.getColumnType(getConnectionProperties(), tableName, columnName);
		if (columnType.getColumnTypeEnum() == ColumnTypeEnum.LONG) {
			ourLog.info("Table {} column {} is already of type LONG, no migration needed", tableName, columnName);
			return;
		}

		String tempColumnName = columnName + "_m".toLowerCase();
		tableName = tableName.toLowerCase();
		columnName = columnName.toLowerCase();

		executeSql(tableName, "alter table " + tableName + " add column " + tempColumnName + " oid");
		executeSql(tableName, "update " + tableName + " set " + tempColumnName + " = cast(" + columnName + " as oid) where " + columnName + " is not null");
		executeSql(tableName, "alter table " + tableName + " drop column " + columnName);
		executeSql(tableName, "alter table " + tableName + " rename column " + tempColumnName + " to " + columnName);

	}
}
