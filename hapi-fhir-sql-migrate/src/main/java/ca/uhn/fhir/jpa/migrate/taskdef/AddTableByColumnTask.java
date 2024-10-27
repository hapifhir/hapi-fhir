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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AddTableByColumnTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(AddTableByColumnTask.class);

	private final List<AddColumnTask> myAddColumnTasks = new ArrayList<>();
	private List<String> myPkColumns;
	private final List<ForeignKeyContainer> myFKColumns = new ArrayList<>();
	private final Comparator<AddColumnTask> myColumnSortingRules;

	public AddTableByColumnTask() {
		this(null);
	}

	public AddTableByColumnTask(Comparator<AddColumnTask> theColumnSortingRules) {
		this(null, null, theColumnSortingRules);
		setDryRun(true);
		myCheckForExistingTables = false;
	}

	public AddTableByColumnTask(String theProductVersion, String theSchemaVersion) {
		this(theProductVersion, theSchemaVersion, null);
	}

	public AddTableByColumnTask(
			String theProductVersion, String theSchemaVersion, Comparator<AddColumnTask> theColumnSortingRules) {
		super(theProductVersion, theSchemaVersion);
		myColumnSortingRules = theColumnSortingRules;
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Add table " + getTableName());
	}

	public void addAddColumnTask(AddColumnTask theTask) {
		Validate.notNull(theTask);
		myAddColumnTasks.add(theTask);
	}

	public void setPkColumns(List<String> thePkColumns) {
		myPkColumns = thePkColumns;
	}

	public void addForeignKey(ForeignKeyContainer theForeignKeyContainer) {
		myFKColumns.add(theForeignKeyContainer);
	}

	public List<String> getPkColumns() {
		return myPkColumns;
	}

	public String generateSQLCreateScript() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(getTableName());
		sb.append(" (");
		if (myPrettyPrint) {
			sb.append("\n");
		} else {
			sb.append(" ");
		}

		for (AddColumnTask next : getOrderedAddColumnTasks()) {
			next.setDriverType(getDriverType());
			next.setTableName(getTableName());
			next.validate();

			if (myPrettyPrint) {
				sb.append("\t");
			}

			sb.append(next.getColumnName());
			sb.append(" ");
			sb.append(next.getTypeStatement());
			sb.append(",");
			if (myPrettyPrint) {
				sb.append("\n");
			} else {
				sb.append(" ");
			}
		}

		// primary keys
		if (myPrettyPrint) {
			sb.append("\t");
		} else {
			sb.append(" ");
		}
		sb.append("PRIMARY KEY (");
		for (int i = 0; i < myPkColumns.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(myPkColumns.get(i));
		}

		boolean hasForeignKeys = !myFKColumns.isEmpty();

		sb.append(")");
		if (hasForeignKeys) {
			sb.append(",");
		}
		if (myPrettyPrint) {
			sb.append("\n");
		} else {
			sb.append(" ");
		}

		DriverTypeEnum sqlEngine = getDriverType();

		// foreign keys
		if (!myFKColumns.isEmpty()) {
			for (int i = 0; i < myFKColumns.size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				ForeignKeyContainer fk = myFKColumns.get(i);
				if (myPrettyPrint) {
					sb.append("\t");
				}
				sb.append(fk.generateSQL(sqlEngine, myPrettyPrint));
				if (myPrettyPrint) {
					sb.append("\n");
				} else {
					sb.append(" ");
				}
			}
		}

		sb.append(")");

		switch (sqlEngine) {
			case MARIADB_10_1:
			case MYSQL_5_7:
				sb.append(" engine=InnoDB");
				break;
			case DERBY_EMBEDDED:
			case POSTGRES_9_4:
			case ORACLE_12C:
			case MSSQL_2012:
			case H2_EMBEDDED:
			case COCKROACHDB_21_1:
				break;
		}

		return sb.toString();
	}

	@Override
	public void doExecute() throws SQLException {
		if (myCheckForExistingTables
				&& JdbcUtils.getTableNames(getConnectionProperties()).contains(getTableName())) {
			logInfo(ourLog, "Already have table named {} - No action performed", getTableName());
			return;
		}

		executeSql(getTableName(), generateSQLCreateScript());
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		super.generateEquals(theBuilder, theOtherObject);
		AddTableByColumnTask otherObject = (AddTableByColumnTask) theOtherObject;
		theBuilder.append(myAddColumnTasks, otherObject.myAddColumnTasks);
		theBuilder.append(myPkColumns, otherObject.myPkColumns);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myAddColumnTasks);
		theBuilder.append(myPkColumns);
	}

	private List<AddColumnTask> getOrderedAddColumnTasks() {
		if (myColumnSortingRules == null) {
			return myAddColumnTasks;
		}

		return myAddColumnTasks.stream().sorted(myColumnSortingRules).collect(Collectors.toUnmodifiableList());
	}
}
