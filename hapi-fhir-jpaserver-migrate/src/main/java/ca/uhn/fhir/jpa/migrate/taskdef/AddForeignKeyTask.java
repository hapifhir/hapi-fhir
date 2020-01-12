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

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AddForeignKeyTask extends BaseTableColumnTask<AddForeignKeyTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddForeignKeyTask.class);
	private String myConstraintName;
	private String myForeignTableName;
	private String myForeignColumnName;

	public AddForeignKeyTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public void setConstraintName(String theConstraintName) {
		myConstraintName = theConstraintName;
	}

	public void setForeignTableName(String theForeignTableName) {
		myForeignTableName = theForeignTableName;
	}

	public void setForeignColumnName(String theForeignColumnName) {
		myForeignColumnName = theForeignColumnName;
	}

	@Override
	public void validate() {
		super.validate();

		Validate.isTrue(isNotBlank(myConstraintName));
		Validate.isTrue(isNotBlank(myForeignTableName));
		Validate.isTrue(isNotBlank(myForeignColumnName));
		setDescription("Add foreign key " + myConstraintName + " from column " + getColumnName() + " of table " + getTableName() + " to column " + myForeignColumnName + " of table " + myForeignTableName);
	}

	@Override
	public void doExecute() throws SQLException {

		Set<String> existing = JdbcUtils.getForeignKeys(getConnectionProperties(), myForeignTableName, getTableName());
		if (existing.contains(myConstraintName)) {
			logInfo(ourLog, "Already have constraint named {} - No action performed", myConstraintName);
			return;
		}

		String sql = null;
		switch (getDriverType()) {
			case MARIADB_10_1:
			case MYSQL_5_7:
				sql = "alter table " + getTableName() + " add constraint " + myConstraintName + " foreign key (" + getColumnName() + ") references " + myForeignTableName + " (" + myForeignColumnName + ")";
				break;
			case POSTGRES_9_4:
			case DERBY_EMBEDDED:
			case H2_EMBEDDED:
			case ORACLE_12C:
			case MSSQL_2012:
				sql = "alter table " + getTableName() + " add constraint " + myConstraintName + " foreign key (" + getColumnName() + ") references " + myForeignTableName;
				break;
			default:
				throw new IllegalStateException();
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

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		AddForeignKeyTask that = (AddForeignKeyTask) theO;

		return new EqualsBuilder()
			.appendSuper(super.equals(theO))
			.append(myConstraintName, that.myConstraintName)
			.append(myForeignTableName, that.myForeignTableName)
			.append(myForeignColumnName, that.myForeignColumnName)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.appendSuper(super.hashCode())
			.append(myConstraintName)
			.append(myForeignTableName)
			.append(myForeignColumnName)
			.toHashCode();
	}
}
