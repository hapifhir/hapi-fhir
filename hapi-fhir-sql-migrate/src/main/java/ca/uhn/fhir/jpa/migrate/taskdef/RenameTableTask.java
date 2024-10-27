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
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class RenameTableTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(RenameTableTask.class);

	private final String myOldTableName;
	private final String myNewTableName;
	private boolean myDeleteTargetColumnFirstIfExist = true;

	public RenameTableTask(
			String theProductVersion, String theSchemaVersion, String theOldTableName, String theNewTableName) {
		super(theProductVersion, theSchemaVersion);
		myOldTableName = theOldTableName;
		myNewTableName = theNewTableName;
	}

	@Override
	public void validate() {
		setDescription("Rename table " + getOldTableName());
	}

	@Override
	public void doExecute() throws SQLException {

		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		boolean hasTableWithNewTableName = tableNames.contains(getNewTableName());

		String sql = buildRenameTableSqlStatement();
		logInfo(ourLog, "Renaming table: {}", getOldTableName());

		executeSql(getOldTableName(), sql);
	}

	public void setDeleteTargetColumnFirstIfExist(boolean theDeleteTargetColumnFirstIfExist) {
		myDeleteTargetColumnFirstIfExist = theDeleteTargetColumnFirstIfExist;
	}

	public String getNewTableName() {
		return myNewTableName;
	}

	public String getOldTableName() {
		return myOldTableName;
	}

	String buildRenameTableSqlStatement() {
		String retVal;

		final String oldTableName = getOldTableName();
		final String newTableName = getNewTableName();

		switch (getDriverType()) {
			case MYSQL_5_7:
			case DERBY_EMBEDDED:
				retVal = "rename table " + oldTableName + " to " + newTableName;
				break;
			case ORACLE_12C:
			case MARIADB_10_1:
			case POSTGRES_9_4:
			case COCKROACHDB_21_1:
			case H2_EMBEDDED:
				retVal = "alter table " + oldTableName + " rename to " + newTableName;
				break;
			case MSSQL_2012:
				retVal = "sp_rename '" + oldTableName + "', '" + newTableName + "'";
				break;
			default:
				throw new IllegalStateException(Msg.code(2513));
		}
		return retVal;
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myOldTableName);
		theBuilder.append(myNewTableName);
	}
}
