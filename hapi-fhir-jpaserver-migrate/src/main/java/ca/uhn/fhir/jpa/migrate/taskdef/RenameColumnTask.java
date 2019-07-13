package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class RenameColumnTask extends BaseTableTask<RenameColumnTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(RenameColumnTask.class);
	private String myOldName;
	private String myNewName;

	public void setOldName(String theOldName) {
		Validate.notBlank(theOldName);
		myOldName = theOldName;
	}

	public void setNewName(String theNewName) {
		Validate.notBlank(theNewName);
		myNewName = theNewName;
	}

	@Override
	public void execute() throws SQLException {
		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
		boolean haveOldName = columnNames.contains(myOldName.toUpperCase());
		boolean haveNewName = columnNames.contains(myNewName.toUpperCase());
		if (haveOldName && haveNewName) {
			throw new SQLException("Can not rename " + getTableName() + "." + myOldName + " to " + myNewName + " because both columns exist!");
		}
		if (!haveOldName && !haveNewName) {
			throw new SQLException("Can not rename " + getTableName() + "." + myOldName + " to " + myNewName + " because neither column exists!");
		}
		if (haveNewName) {
			ourLog.info("Column {} already exists on table {} - No action performed", myNewName, getTableName());
			return;
		}

		String sql = "";
		switch (getDriverType()) {
			case DERBY_EMBEDDED:
				sql = "RENAME COLUMN " + getTableName() + "." + myOldName + " TO " + myNewName;
				break;
			case MARIADB_10_1:
			case MYSQL_5_7:
				sql = "ALTER TABLE " + getTableName() + " CHANGE COLUMN " + myOldName + " TO " + myNewName;
				break;
			case POSTGRES_9_4:
				sql = "ALTER TABLE " + getTableName() + " RENAME COLUMN " + myOldName + " TO " + myNewName;
				break;
			case MSSQL_2012:
				sql = "sp_rename '" + getTableName() + "." + myOldName + "', '" + myNewName + "', 'COLUMN'";
				break;
			case ORACLE_12C:
				sql = "ALTER TABLE " + getTableName() + " RENAME COLUMN " + myOldName + " TO " + myNewName;
				break;
		}

		ourLog.info("Renaming column {} on table {} to {}", myOldName, getTableName(), myNewName);
		executeSql(getTableName(), sql);

	}
}
