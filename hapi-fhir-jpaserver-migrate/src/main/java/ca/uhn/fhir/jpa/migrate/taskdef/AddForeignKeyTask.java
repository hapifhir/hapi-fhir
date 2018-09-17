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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AddForeignKeyTask extends BaseTableColumnTask<AddForeignKeyTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddForeignKeyTask.class);
	private String myConstraintName;
	private String myForeignTableName;
	private String myForeignColumnName;

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
	}

	@Override
	public void execute() throws SQLException {

		Set<String> existing = JdbcUtils.getForeignKeys(getConnectionProperties(), myForeignTableName, getTableName());
		if (existing.contains(myConstraintName)) {
			ourLog.info("Already have constraint named {} - No action performed", myConstraintName);
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
			case ORACLE_12C:
			case MSSQL_2012:
				sql = "alter table " + getTableName() + " add constraint " + myConstraintName + " foreign key (" + getColumnName() + ") references " + myForeignTableName;
				break;
		}
		executeSql(sql);
	}

}
