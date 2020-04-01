package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DropIndexTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(DropIndexTask.class);
	private String myIndexName;

	public DropIndexTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myIndexName, "The index name must not be blank");

		setDescription("Drop index " + myIndexName + " from table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());

		if (!indexNames.contains(myIndexName)) {
			logInfo(ourLog, "Index {} does not exist on table {} - No action needed", myIndexName, getTableName());
			return;
		}

		boolean isUnique = JdbcUtils.isIndexUnique(getConnectionProperties(), getTableName(), myIndexName);
		String uniquenessString = isUnique ? "unique" : "non-unique";

		List<String> sqls = createDropIndexSql(getConnectionProperties(), getTableName(), myIndexName, getDriverType());
		if (!sqls.isEmpty()) {
			logInfo(ourLog, "Dropping {} index {} on table {}", uniquenessString, myIndexName, getTableName());
		}
		for (@Language("SQL") String sql : sqls) {
			executeSql(getTableName(), sql);
		}
	}

	public DropIndexTask setIndexName(String theIndexName) {
		myIndexName = theIndexName;
		return this;
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		DropIndexTask otherObject = (DropIndexTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(myIndexName, otherObject.myIndexName);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myIndexName);
	}

	static List<String> createDropIndexSql(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theIndexName, DriverTypeEnum theDriverType) throws SQLException {
		Validate.notBlank(theIndexName, "theIndexName must not be blank");
		Validate.notBlank(theTableName, "theTableName must not be blank");

		if (!JdbcUtils.getIndexNames(theConnectionProperties, theTableName).contains(theIndexName)) {
			return Collections.emptyList();
		}

		boolean isUnique = JdbcUtils.isIndexUnique(theConnectionProperties, theTableName, theIndexName);

		List<String> sql = new ArrayList<>();

		if (isUnique) {
			// Drop constraint
			switch (theDriverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql.add("alter table " + theTableName + " drop index " + theIndexName);
					break;
				case H2_EMBEDDED:
					sql.add("drop index " + theIndexName);
					break;
				case DERBY_EMBEDDED:
					sql.add("alter table " + theTableName + " drop constraint " + theIndexName);
					break;
				case POSTGRES_9_4:
					sql.add("alter table " + theTableName + " drop constraint if exists " + theIndexName + " cascade");
					sql.add("drop index if exists " + theIndexName + " cascade");
					break;
				case ORACLE_12C:
				case MSSQL_2012:
					sql.add("alter table " + theTableName + " drop constraint " + theIndexName);
					break;
			}
		} else {
			// Drop index
			switch (theDriverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql.add("alter table " + theTableName + " drop index " + theIndexName);
					break;
				case POSTGRES_9_4:
				case DERBY_EMBEDDED:
				case H2_EMBEDDED:
				case ORACLE_12C:
					sql.add("drop index " + theIndexName);
					break;
				case MSSQL_2012:
					sql.add("drop index " + theTableName + "." + theIndexName);
					break;
			}
		}
		return sql;
	}
}
