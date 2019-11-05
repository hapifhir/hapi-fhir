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
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DropTableTask extends BaseTableTask<DropTableTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(DropTableTask.class);

	@Override
	public void execute() throws SQLException {
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		if (!tableNames.contains(getTableName())) {
			return;
		}

		Set<String> foreignKeys = JdbcUtils.getForeignKeys(getConnectionProperties(), null, getTableName());
		ourLog.info("Table {} has the following foreign keys: {}", getTableName(), foreignKeys);

		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());
		ourLog.info("Table {} has the following indexes: {}", getTableName(), indexNames);

		for (String next : foreignKeys) {
			List<String> sql = DropForeignKeyTask.generateSql(getTableName(), next, getDriverType());
			for (@Language("SQL") String nextSql : sql) {
				executeSql(getTableName(), nextSql);
			}
		}

		for (String nextIndex : indexNames) {
			List<String> sqls = DropIndexTask.createDropIndexSql(getConnectionProperties(), getTableName(), nextIndex, getDriverType());
			if (!sqls.isEmpty()) {
				ourLog.info("Dropping index {} on table {} in preparation for table delete", nextIndex, getTableName());
			}
			for (@Language("SQL") String sql : sqls) {
				executeSql(getTableName(), sql);
			}
		}

		ourLog.info("Dropping table: {}", getTableName());

		@Language("SQL")
		String sql = "DROP TABLE " + getTableName();
		executeSql(getTableName(), sql);

	}


}
