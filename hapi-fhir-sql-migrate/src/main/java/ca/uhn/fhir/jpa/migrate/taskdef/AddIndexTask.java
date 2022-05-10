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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddIndexTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(AddIndexTask.class);

	private String myIndexName;
	private List<String> myColumns;
	private Boolean myUnique;
	private List<String> myIncludeColumns = Collections.emptyList();
	private boolean myOnline;

	public AddIndexTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public void setIndexName(String theIndexName) {
		myIndexName = theIndexName.toUpperCase(Locale.US);
	}

	public void setColumns(List<String> theColumns) {
		myColumns = theColumns;
	}

	public void setUnique(boolean theUnique) {
		myUnique = theUnique;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myIndexName, "Index name not specified");
		Validate.isTrue(myColumns.size() > 0, "Columns not specified for AddIndexTask " + myIndexName + " on table " + getTableName());
		Validate.notNull(myUnique, "Uniqueness not specified");
		setDescription("Add " + myIndexName + " index to table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());
		if (indexNames.contains(myIndexName)) {
			logInfo(ourLog, "Index {} already exists on table {} - No action performed", myIndexName, getTableName());
			return;
		}

		logInfo(ourLog, "Going to add a {} index named {} on table {} for columns {}", (myUnique ? "UNIQUE" : "NON-UNIQUE"), myIndexName, getTableName(), myColumns);

		String sql = generateSql();
		String tableName = getTableName();

		try {
			executeSql(tableName, sql);
		} catch (Exception e) {
			if (e.toString().contains("already exists")) {
				ourLog.warn("Index {} already exists", myIndexName);
			} else {
				throw e;
			}
		}
	}

	@Nonnull
	String generateSql() {
		String unique = myUnique ? "unique " : "";
		String columns = String.join(", ", myColumns);
		String includeClause = "";
		String mssqlWhereClause = "";
		if (!myIncludeColumns.isEmpty()) {
			switch (getDriverType()) {
				case POSTGRES_9_4:
				case MSSQL_2012:
					includeClause = " INCLUDE (" + String.join(", ", myIncludeColumns) + ")";
					break;
				case H2_EMBEDDED:
				case DERBY_EMBEDDED:
				case MARIADB_10_1:
				case MYSQL_5_7:
				case ORACLE_12C:
					// These platforms don't support the include clause
					// Per:
					// https://use-the-index-luke.com/blog/2019-04/include-columns-in-btree-indexes#postgresql-limitations
					break;
			}
		}
		if (myUnique && getDriverType() == DriverTypeEnum.MSSQL_2012) {
			mssqlWhereClause = " WHERE (";
			for (int i = 0; i < myColumns.size(); i++) {
				mssqlWhereClause += myColumns.get(i) + " IS NOT NULL ";
				if (i < myColumns.size() - 1) {
					mssqlWhereClause += "AND ";
				}
			}
			mssqlWhereClause += ")";
		}
		String postgresOnline = "";
		String oracleOnlineDeferred = "";
		if (myOnline) {
			switch (getDriverType()) {
				case POSTGRES_9_4:
					postgresOnline = "CONCURRENTLY ";
					// This runs without a lock, and can't be done transactionally.
					setTransactional(false);
					break;
				case ORACLE_12C:
					oracleOnlineDeferred = " ONLINE DEFERRED INVALIDATION";
					break;
				case MSSQL_2012:
					oracleOnlineDeferred = " WITH (ONLINE = ON)";
					break;
				default:
			}
		}


		String sql =
			"create " + unique + "index " + postgresOnline + myIndexName +
			" on " + getTableName() + "(" + columns + ")" + includeClause +  mssqlWhereClause + oracleOnlineDeferred;
		return sql;
	}

	public void setColumns(String... theColumns) {
		setColumns(Arrays.asList(theColumns));
	}

	public void setIncludeColumns(String... theIncludeColumns) {
		setIncludeColumns(Arrays.asList(theIncludeColumns));
	}

	private void setIncludeColumns(List<String> theIncludeColumns) {
		Validate.notNull(theIncludeColumns);
		myIncludeColumns = theIncludeColumns;
	}

	/**
	 * Add Index without locking the table.
	 */
	public void setOnline(boolean theFlag) {
		myOnline = theFlag;
	}
	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		super.generateEquals(theBuilder, theOtherObject);

		AddIndexTask otherObject = (AddIndexTask) theOtherObject;
		theBuilder.append(myIndexName, otherObject.myIndexName);
		theBuilder.append(myColumns, otherObject.myColumns);
		theBuilder.append(myUnique, otherObject.myUnique);
		theBuilder.append(myIncludeColumns, otherObject.myIncludeColumns);
		theBuilder.append(myOnline, otherObject.myOnline);

	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myIndexName);
		theBuilder.append(myColumns);
		theBuilder.append(myUnique);
		theBuilder.append(myOnline);
	}

}
