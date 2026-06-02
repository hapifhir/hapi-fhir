/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AddForeignKeyTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(AddForeignKeyTask.class);
	private String myConstraintName;
	private String myForeignTableName;
	private List<String> myForeignColumnNames;
	private List<String> myColumnNames;

	public AddForeignKeyTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public void setConstraintName(String theConstraintName) {
		myConstraintName = theConstraintName;
	}

	public void setForeignTableName(String theForeignTableName) {
		myForeignTableName = theForeignTableName;
	}

	public void setColumnNames(List<String> theForeignColumnName) {
		myColumnNames = theForeignColumnName;
	}

	public void setForeignColumnNames(List<String> theForeignColumnName) {
		myForeignColumnNames = theForeignColumnName;
	}

	public List<String> getForeignColumnNames() {
		return myForeignColumnNames;
	}

	public List<String> getColumnNames() {
		return myColumnNames;
	}

	@Override
	public void validate() {
		super.validate();

		Validate.isTrue(isNotBlank(myConstraintName));
		Validate.isTrue(isNotBlank(myForeignTableName));
		Validate.notEmpty(myColumnNames, "No column names specified for foreign key %s", myConstraintName);
		Validate.notEmpty(myForeignColumnNames, "No column names specified for foreign key %s", myConstraintName);
		Validate.isTrue(myColumnNames.size() == myForeignColumnNames.size(), "Number of column names must match for foreign key %s", myConstraintName);
		setDescription("Add foreign key " + myConstraintName + " from column(s) " + getColumnNames() + " of table "
				+ getTableName() + " to column(s) " + getForeignColumnNames() + " of table " + myForeignTableName);
	}

	@Override
	public void doExecute() throws SQLException {

		Set<String> existing = JdbcUtils.getForeignKeys(getConnectionProperties(), myForeignTableName, getTableName());
		if (existing.contains(myConstraintName)) {
			logInfo(ourLog, "Already have constraint named {} - No action performed", myConstraintName);
			return;
		}

		boolean quoteNames = switch (getDriverType()) {
			case MARIADB_10_1, MYSQL_5_7 ->
				// Quote the column names as "SYSTEM" is a reserved word in MySQL
				true;
			case COCKROACHDB_21_1, POSTGRES_9_4, DERBY_EMBEDDED, H2_EMBEDDED, ORACLE_12C, MSSQL_2012 -> false;
		};

		StringBuilder b = new StringBuilder();

		b.append("alter table ");
		b.append(getTableName());
		b.append(" add constraint ");
		b.append(myConstraintName);
		b.append(" foreign key (");
		appendColumnList(getColumnNames(), quoteNames, b);
		b.append(") references ");
		b.append(myForeignTableName);
		b.append(" (");
		appendColumnList(getForeignColumnNames(), quoteNames, b);
		b.append(")");

		@Language("SQL")
		String sql = b.toString();

		try {
			executeSql(getTableName(), sql);
		} catch (Exception e) {
			if (e.toString().contains("already exists")) {
				ourLog.warn("Index {} already exists", myConstraintName);
			} else {
				throw e;
			}
		}
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myConstraintName);
		theBuilder.append(myForeignTableName);
		if (myForeignColumnNames.size() == 1) {
			// This task used to only allow a single column, and we don't want the computed hashcodes
			// to change since we added the ability to have multiple columns
			theBuilder.append(myForeignColumnNames.get(0));
		} else {
			theBuilder.append(myForeignColumnNames);
		}
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		AddForeignKeyTask otherObject = (AddForeignKeyTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(myConstraintName, otherObject.myConstraintName);
		theBuilder.append(myForeignTableName, otherObject.myForeignTableName);
		theBuilder.append(myForeignColumnNames, otherObject.myForeignColumnNames);
	}

	private static void appendColumnList(List<String> theColumnNames, boolean theQuoteNames, StringBuilder theStringBuilder) {
		for (int i = 0; i < theColumnNames.size(); i++) {
			if (i > 0) {
				theStringBuilder.append(", ");
			}
			if (theQuoteNames) {
				theStringBuilder.append("`");
			}
			theStringBuilder.append(theColumnNames.get(i));
			if (theQuoteNames) {
				theStringBuilder.append("`");
			}
		}
	}
}
