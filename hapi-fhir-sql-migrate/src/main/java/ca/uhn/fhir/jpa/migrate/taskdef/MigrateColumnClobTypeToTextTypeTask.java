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

import java.sql.SQLException;

public class MigrateColumnClobTypeToTextTypeTask extends BaseTableColumnTask {

	private final String myFromColumName;
	private final String myToColumName;

	public MigrateColumnClobTypeToTextTypeTask(
			String theProductVersion,
			String theSchemaVersion,
			String theTableName,
			String theFromColumName,
			String theToColumName) {
		super(theProductVersion, theSchemaVersion);
		myFromColumName = theFromColumName;
		myToColumName = theToColumName;

		setTableName(theTableName);
	}

	@Override
	public void validate() {
		setDescription("Migrating CLob (oid) from colum  " + myFromColumName + " to " + myToColumName
				+ ".TEXT for table " + getTableName() + " (only affects Postgresql)");
	}

	@Override
	protected void doExecute() throws SQLException {
		String sql = buildSqlStatement();
		executeSql(getTableName(), sql);
	}

	String buildSqlStatement() {
		String tableName = getTableName().toLowerCase();
		String fromColumName = myFromColumName.toLowerCase();
		String toColumName = myToColumName.toLowerCase();

		String retVal;

		switch (getDriverType()) {
			case MYSQL_5_7:
			case DERBY_EMBEDDED:
			case ORACLE_12C:
			case MARIADB_10_1:
			case COCKROACHDB_21_1:
			case H2_EMBEDDED:
			case MSSQL_2012:
				retVal = "update " + tableName + " set " + toColumName + " = " + fromColumName + " where "
						+ fromColumName + " is not null";
				break;
			case POSTGRES_9_4:
				retVal = "update " + tableName + " set " + toColumName + " = convert_from(lo_get(" + fromColumName
						+ "), 'UTF8') where " + fromColumName + " is not null";
				break;
			default:
				throw new IllegalStateException(Msg.code(2515));
		}

		return retVal;
	}
}
