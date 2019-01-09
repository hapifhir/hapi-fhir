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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class ModifyColumnTask extends BaseTableColumnTypeTask<ModifyColumnTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(ModifyColumnTask.class);


	@Override
	public void execute() {

		String existingType;
		boolean nullable;
		try {
			existingType = JdbcUtils.getColumnType(getConnectionProperties(), getTableName(), getColumnName());
			nullable = JdbcUtils.isColumnNullable(getConnectionProperties(), getTableName(), getColumnName());
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		}

		String wantedType = getColumnType().getDescriptor(getColumnLength());
		boolean alreadyOfCorrectType = existingType.equals(wantedType);
		boolean alreadyCorrectNullable = isNullable() == nullable;
		if (alreadyOfCorrectType && alreadyCorrectNullable) {
			ourLog.info("Column {} on table {} is already of type {} and has nullable {} - No action performed", getColumnName(), getTableName(), wantedType, nullable);
			return;
		}

		String type = getSqlType();
		String notNull = getSqlNotNull();

		String sql = null;
		String sqlNotNull = null;
		switch (getDriverType()) {
			case DERBY_EMBEDDED:
				if (!alreadyOfCorrectType) {
					sql = "alter table " + getTableName() + " alter column " + getColumnName() + " set data type " + type;
				}
				if (!alreadyCorrectNullable) {
					sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + notNull;
				}
				break;
			case MARIADB_10_1:
			case MYSQL_5_7:
				sql = "alter table " + getTableName() + " modify column " + getColumnName() + " " + type + notNull;
				break;
			case POSTGRES_9_4:
				if (!alreadyOfCorrectType) {
					sql = "alter table " + getTableName() + " alter column " + getColumnName() + " type " + type;
				}
				if (!alreadyCorrectNullable) {
					if (isNullable()) {
						sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + " drop not null";
					} else {
						sqlNotNull = "alter table " + getTableName() + " alter column " + getColumnName() + " set not null";
					}
				}
				break;
			case ORACLE_12C:
				String oracleNullableStmt = !alreadyCorrectNullable ? notNull : "";
				sql = "alter table " + getTableName() + " modify ( " + getColumnName() + " " + type + oracleNullableStmt + " )";
				break;
			case MSSQL_2012:
				sql = "alter table " + getTableName() + " alter column " + getColumnName() + " " + type + notNull;
				break;
			default:
				throw new IllegalStateException("Dont know how to handle " + getDriverType());
		}

		ourLog.info("Updating column {} on table {} to type {}", getColumnName(), getTableName(), type);
		if (sql != null) {
			executeSql(getTableName(), sql);
		}

		if (sqlNotNull != null) {
			ourLog.info("Updating column {} on table {} to not null", getColumnName(), getTableName());
			executeSql(getTableName(), sqlNotNull);
		}
	}

}
