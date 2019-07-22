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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
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

		String sql = createDropIndexSql(getConnectionProperties(), getTableName(), myIndexName, getDriverType());
		executeSql(getTableName(), sql);

	}

	@Nonnull
	static String createDropIndexSql(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theIndexName, DriverTypeEnum theDriverType) throws SQLException {
		Validate.notBlank(theIndexName, "theIndexName must not be blank");
		Validate.notBlank(theTableName, "theTableName must not be blank");

		boolean isUnique = JdbcUtils.isIndexUnique(theConnectionProperties, theTableName, theIndexName);

		String sql = null;

		if (isUnique) {
			// Drop constraint
			switch (theDriverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql = "alter table " + theTableName + " drop index " + theIndexName;
					break;
				case H2_EMBEDDED:
				case DERBY_EMBEDDED:
					sql = "drop index " + theIndexName;
					break;
				case POSTGRES_9_4:
				case ORACLE_12C:
				case MSSQL_2012:
					sql = "alter table " + theTableName + " drop constraint " + theIndexName;
					break;
			}
		} else {
			// Drop index
			switch (theDriverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql = "alter table " + theTableName + " drop index " + theIndexName;
					break;
				case POSTGRES_9_4:
				case DERBY_EMBEDDED:
				case H2_EMBEDDED:
				case ORACLE_12C:
					sql = "drop index " + theIndexName;
					break;
				case MSSQL_2012:
					sql = "drop index " + theTableName + "." + theIndexName;
					break;
			}
		}
		return sql;
	}


	public DropIndexTask setIndexName(String theIndexName) {
		myIndexName = theIndexName;
		return this;
	}
}
