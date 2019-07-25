package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public class DropIndexTask extends BaseTableTask<DropIndexTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(DropIndexTask.class);
	private String myIndexName;

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myIndexName, "The index name must not be blank");

		if (getDescription() == null) {
			setDescription("Drop index " + myIndexName + " on table " + getTableName());
		}
	}

	@Override
	public void execute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());

		if (!indexNames.contains(myIndexName)) {
			ourLog.info("Index {} does not exist on table {} - No action needed", myIndexName, getTableName());
			return;
		}

		boolean isUnique = JdbcUtils.isIndexUnique(getConnectionProperties(), getTableName(), myIndexName);
		String uniquenessString = isUnique ? "unique" : "non-unique";
		ourLog.info("Dropping {} index {} on table {}", uniquenessString, myIndexName, getTableName());

		String sql = null;

		if (isUnique) {
			// Drop constraint
			switch (getDriverType()) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql = "ALTER TABLE " + getTableName() + " DROP INDEX " + myIndexName;
					break;
				case DERBY_EMBEDDED:
					sql = "DROP INDEX " + myIndexName;
					break;
				case POSTGRES_9_4:
				case ORACLE_12C:
				case MSSQL_2012:
					sql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT " + myIndexName;
					break;
			}
		} else {
			// Drop index
			switch (getDriverType()) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql = "ALTER TABLE " + getTableName() + " DROP INDEX " + myIndexName;
					break;
				case POSTGRES_9_4:
				case DERBY_EMBEDDED:
				case ORACLE_12C:
					sql = "DROP INDEX " + myIndexName;
					break;
				case MSSQL_2012:
					sql = "DROP INDEX " + getTableName() + "." + myIndexName;
					break;
			}
		}
		executeSql(sql);

	}


	public DropIndexTask setIndexName(String theIndexName) {
		myIndexName = theIndexName;
		return this;
	}
}
