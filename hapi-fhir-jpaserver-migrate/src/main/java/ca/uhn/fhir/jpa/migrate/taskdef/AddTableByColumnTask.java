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

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddTableByColumnTask extends BaseTableTask<AddTableByColumnTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddTableByColumnTask.class);

	private List<AddColumnTask> myAddColumnTasks = new ArrayList<>();
	private String myPkColumn;

	public void addAddColumnTask(AddColumnTask theTask) {
		Validate.notNull(theTask);
		myAddColumnTasks.add(theTask);
	}

	public void setPkColumn(String thePkColumn) {
		myPkColumn = thePkColumn;
	}

	@Override
	public void execute() throws SQLException {

		if (JdbcUtils.getTableNames(getConnectionProperties()).contains(getTableName())) {
			ourLog.info("Already have table named {} - No action performed", getTableName());
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(getTableName());
		sb.append(" ( ");

		for (AddColumnTask next : myAddColumnTasks) {
			next.setDriverType(getDriverType());
			next.setTableName(getTableName());
			next.validate();

			sb.append(next.getColumnName());
			sb.append(" ");
			sb.append(next.getTypeStatement());
			sb.append(", ");
		}

		sb.append(" PRIMARY KEY (");
		sb.append(myPkColumn);
		sb.append(")");

		sb.append(" ) ");

		switch (getDriverType()) {
			case MARIADB_10_1:
			case MYSQL_5_7:
				sb.append("engine=InnoDB");
				break;
			case DERBY_EMBEDDED:
			case POSTGRES_9_4:
			case ORACLE_12C:
			case MSSQL_2012:
				break;
		}

		executeSql(getTableName(), sb.toString());

	}
}
