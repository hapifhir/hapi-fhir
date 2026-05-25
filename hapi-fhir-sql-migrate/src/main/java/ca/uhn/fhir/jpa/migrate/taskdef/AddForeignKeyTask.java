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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	public AddForeignKeyTask setColumnName(String theColumnName) {
		myColumnNames = List.of(theColumnName);
		return this;
	}

	public void setConstraintName(String theConstraintName) {
		myConstraintName = theConstraintName;
	}

	public void setForeignTableName(String theForeignTableName) {
		myForeignTableName = theForeignTableName;
	}

	public void setForeignColumnName(String theForeignColumnName) {
		myForeignColumnNames = List.of(theForeignColumnName);
	}

	public void setForeignColumnNames(List<String> theForeignColumnNames) {
		myForeignColumnNames = theForeignColumnNames;
	}

	public void setColumnNames(List<String> theColumnNames) {
		myColumnNames = List.copyOf(theColumnNames);
	}

	@Override
	public void validate() {
		super.validate();

		Validate.isTrue(isNotBlank(myConstraintName));
		Validate.isTrue(isNotBlank(myForeignTableName));
		Validate.isTrue(myColumnNames != null && !myColumnNames.isEmpty());
		Validate.isTrue(myForeignColumnNames != null && !myForeignColumnNames.isEmpty());
		String sourceColumns = String.join(", ", myColumnNames);
		String foreignColumns = String.join(", ", myForeignColumnNames);
		setDescription("Add foreign key " + myConstraintName + " from columns " + sourceColumns + " of table "
				+ getTableName() + " to columns " + foreignColumns + " of table " + myForeignTableName);
	}

	@Override
	public void doExecute() throws SQLException {

		Set<String> existing = JdbcUtils.getForeignKeys(getConnectionProperties(), myForeignTableName, getTableName());
		if (existing.contains(myConstraintName)) {
			logInfo(ourLog, "Already have constraint named {} - No action performed", myConstraintName);
			return;
		}

		String sourceColumns = String.join(", ", myColumnNames);
		String sql;
		switch (getDriverType()) {
			case MARIADB_10_1:
			case MYSQL_5_7:
				// Quote the column names as "SYSTEM" is a reserved word in MySQL
				String quotedSourceColumns = quoteColumnsForMySql(myColumnNames);
				String quotedForeignColumns = quoteColumnsForMySql(myForeignColumnNames);
				sql = "alter table " + getTableName() + " add constraint " + myConstraintName + " foreign key ("
						+ quotedSourceColumns + ") references " + myForeignTableName + " (" + quotedForeignColumns
						+ ")";
				break;
			case COCKROACHDB_21_1:
			case POSTGRES_9_4:
			case DERBY_EMBEDDED:
			case H2_EMBEDDED:
			case ORACLE_12C:
			case MSSQL_2012:
				String foreignColumns = String.join(", ", myForeignColumnNames);
				sql = "alter table " + getTableName() + " add constraint " + myConstraintName + " foreign key ("
						+ sourceColumns + ") references " + myForeignTableName + " (" + foreignColumns + ")";
				break;
			default:
				throw new IllegalStateException(Msg.code(68));
		}

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

	private String quoteColumnsForMySql(List<String> theColumns) {
		return theColumns.stream().map(c -> "`" + c + "`").collect(Collectors.joining(", "));
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myConstraintName);
		theBuilder.append(myForeignTableName);
		theBuilder.append(myForeignColumnNames);
		theBuilder.append(myColumnNames);
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		AddForeignKeyTask otherObject = (AddForeignKeyTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(myConstraintName, otherObject.myConstraintName);
		theBuilder.append(myForeignTableName, otherObject.myForeignTableName);
		theBuilder.append(myForeignColumnNames, otherObject.myForeignColumnNames);
		theBuilder.append(myColumnNames, otherObject.myColumnNames);
	}
}
