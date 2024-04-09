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
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class RenameTableTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(RenameTableTask.class);

	private final String myTableNewName;

	public RenameTableTask(String theProductVersion, String theSchemaVersion, String theTableNewName) {
		super(theProductVersion, theSchemaVersion);
		myTableNewName = theTableNewName;
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Rename table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {

		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		if (!tableNames.contains(getTableName())) {
			logInfo(ourLog, "Table {} does not exist - No action performed", getTableName());
			return;
		}

		String sql = buildRenameTableSqlStatement();
		logInfo(ourLog, "Renaming table: {}", getTableName());

		executeSql(getTableName(), sql);
	}

	String buildRenameTableSqlStatement() {
		String retVal;

		final String oldTableName = getTableName();
		final String newTableName = getTableNewName();

		switch (getDriverType()) {
			case MYSQL_5_7:
			case DERBY_EMBEDDED:
				retVal = "RENAME TABLE " + oldTableName + " TO " + newTableName;
				break;
			case ORACLE_12C:
			case MARIADB_10_1:
			case POSTGRES_9_4:
			case COCKROACHDB_21_1:
			case H2_EMBEDDED:
				retVal = "ALTER TABLE " + oldTableName + " RENAME TO " + newTableName;
				break;
			case MSSQL_2012:
				retVal = "sp_rename '" + oldTableName + "', '" + newTableName + "'";
				break;
			default:
				throw new IllegalStateException(Msg.code(58));
		}
		return retVal;

	}

	public String getTableNewName() {
		return myTableNewName;
	}
}
