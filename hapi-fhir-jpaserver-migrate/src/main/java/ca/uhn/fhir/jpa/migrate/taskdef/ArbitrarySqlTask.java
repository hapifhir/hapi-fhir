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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ArbitrarySqlTask extends BaseTask<ArbitrarySqlTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(ArbitrarySqlTask.class);
	private final String myDescription;
	private final String myTableName;
	private List<Task> myTask = new ArrayList<>();
	private int myBatchSize = 1000;
	private String myExecuteOnlyIfTableExists;
	private List<TableAndColumn> myConditionalOnExistenceOf = new ArrayList<>();

	public ArbitrarySqlTask(String theTableName, String theDescription) {
		myTableName = theTableName;
		myDescription = theDescription;
	}

	public void addQuery(String theSql, QueryModeEnum theMode, Consumer<Map<String, Object>> theConsumer) {
		myTask.add(new QueryTask(theSql, theMode, theConsumer));
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	public void execute() throws SQLException {
		ourLog.info("Starting: {}", myDescription);

		if (StringUtils.isNotBlank(myExecuteOnlyIfTableExists)) {
			Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
			if (!tableNames.contains(myExecuteOnlyIfTableExists.toUpperCase())) {
				ourLog.info("Table {} does not exist - No action performed", myExecuteOnlyIfTableExists);
				return;
			}
		}

		for (TableAndColumn next : myConditionalOnExistenceOf) {
			String columnType = JdbcUtils.getColumnType(getConnectionProperties(), next.getTable(), next.getColumn());
			if (columnType == null) {
				ourLog.info("Table {} does not have column {} - No action performed", next.getTable(), next.getColumn());
				return;
			}
		}

		for (Task next : myTask) {
			next.execute();
		}

	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public void setExecuteOnlyIfTableExists(String theExecuteOnlyIfTableExists) {
		myExecuteOnlyIfTableExists = theExecuteOnlyIfTableExists;
	}

	/**
	 * This task will only execute if the following column exists
	 */
	public void addExecuteOnlyIfColumnExists(String theTableName, String theColumnName) {
		myConditionalOnExistenceOf.add(new TableAndColumn(theTableName, theColumnName));
	}

	public enum QueryModeEnum {
		BATCH_UNTIL_NO_MORE
	}

	private abstract class Task {
		public abstract void execute();
	}

	private class QueryTask extends Task {
		private final String mySql;
		private final QueryModeEnum myMode;
		private final Consumer<Map<String, Object>> myConsumer;

		public QueryTask(String theSql, QueryModeEnum theMode, Consumer<Map<String, Object>> theConsumer) {
			mySql = theSql;
			myMode = theMode;
			myConsumer = theConsumer;
		}


		@Override
		public void execute() {
			if (isDryRun()) {
				return;
			}

			List<Map<String, Object>> rows;
			do {
				ourLog.info("Querying for up to {} rows", myBatchSize);
				rows = getTxTemplate().execute(t -> {
					JdbcTemplate jdbcTemplate = newJdbcTemnplate();
					jdbcTemplate.setMaxRows(myBatchSize);
					return jdbcTemplate.query(mySql, new ColumnMapRowMapper());
				});

				ourLog.info("Processing {} rows", rows.size());
				List<Map<String, Object>> finalRows = rows;
				getTxTemplate().execute(t -> {
					for (Map<String, Object> nextRow : finalRows) {
						myConsumer.accept(nextRow);
					}
					return null;
				});
			} while (rows.size() > 0);
		}
	}

	private static class TableAndColumn {
		private final String myTable;
		private final String myColumn;

		private TableAndColumn(String theTable, String theColumn) {
			myTable = theTable;
			myColumn = theColumn;
		}

		public String getTable() {
			return myTable;
		}

		public String getColumn() {
			return myColumn;
		}
	}
}
