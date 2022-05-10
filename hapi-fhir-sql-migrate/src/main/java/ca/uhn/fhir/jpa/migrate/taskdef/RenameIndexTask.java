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

public class RenameIndexTask extends BaseTableTask {
	private static final Logger ourLog = LoggerFactory.getLogger(RenameIndexTask.class);
	private String myOldIndexName;
	private String myNewIndexName;

	public RenameIndexTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	static List<String> createRenameIndexSql(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theOldIndexName, String theNewIndexName, DriverTypeEnum theDriverType) throws SQLException {
		Validate.notBlank(theOldIndexName, "theOldIndexName must not be blank");
		Validate.notBlank(theNewIndexName, "theNewIndexName must not be blank");
		Validate.notBlank(theTableName, "theTableName must not be blank");

		if (!JdbcUtils.getIndexNames(theConnectionProperties, theTableName).contains(theOldIndexName)) {
			return Collections.emptyList();
		}

		List<String> sql = new ArrayList<>();

		// Drop constraint
		switch (theDriverType) {
			case MYSQL_5_7:
			case MARIADB_10_1:
				// Quote the index names as "PRIMARY" is a reserved word in MySQL
				sql.add("rename index `" + theOldIndexName + "` to `" + theNewIndexName + "`");
				break;
			case DERBY_EMBEDDED:
				sql.add("rename index " + theOldIndexName + " to " + theNewIndexName);
				break;
			case H2_EMBEDDED:
			case POSTGRES_9_4:
			case ORACLE_12C:
				sql.add("alter index " + theOldIndexName + " rename to " + theNewIndexName);
				break;
			case MSSQL_2012:
				sql.add("EXEC sp_rename '" + theTableName + "." + theOldIndexName + "', '" + theNewIndexName + "'");
				break;
		}
		return sql;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myOldIndexName, "The old index name must not be blank");
		Validate.notBlank(myNewIndexName, "The new index name must not be blank");

		setDescription("Rename index from " + myOldIndexName + " to " + myNewIndexName + " on table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());

		if (!indexNames.contains(myOldIndexName)) {
			logInfo(ourLog, "Index {} does not exist on table {} - No action needed", myOldIndexName, getTableName());
			return;
		}

		List<String> sqls = createRenameIndexSql(getConnectionProperties(), getTableName(), myOldIndexName, myNewIndexName, getDriverType());
		if (!sqls.isEmpty()) {
			logInfo(ourLog, "Renaming index from {} to {} on table {}", myOldIndexName, myNewIndexName, getTableName());
		}
		for (@Language("SQL") String sql : sqls) {
			executeSql(getTableName(), sql);
		}
	}

	public RenameIndexTask setNewIndexName(String theNewIndexName) {
		myNewIndexName = theNewIndexName;
		return this;
	}

	public RenameIndexTask setOldIndexName(String theOldIndexName) {
		myOldIndexName = theOldIndexName;
		return this;
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		RenameIndexTask otherObject = (RenameIndexTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(myOldIndexName, otherObject.myOldIndexName);
		theBuilder.append(myNewIndexName, otherObject.myNewIndexName);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myOldIndexName);
		theBuilder.append(myNewIndexName);
	}
}
