/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AddIndexTask extends BaseTableTask {

	static final Logger ourLog = LoggerFactory.getLogger(AddIndexTask.class);

	private String myIndexName;
	private List<String> myColumns;
	private List<String> myNullableColumns;
	private Boolean myUnique = false;
	private List<String> myIncludeColumns = Collections.emptyList();
	/** Should the operation avoid taking a lock on the table */
	private boolean myOnline;

	private MetadataSource myMetadataSource = new MetadataSource();

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

	public List<String> getNullableColumns() {
		return myNullableColumns;
	}

	public void setNullableColumns(List<String> theNullableColumns) {
		this.myNullableColumns = theNullableColumns;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myIndexName, "Index name not specified");
		Validate.isTrue(
				!myColumns.isEmpty(),
				"Columns not specified for AddIndexTask " + myIndexName + " on table " + getTableName());
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

		logInfo(
				ourLog,
				"Going to add a {} index named {} on table {} for columns {}",
				(myUnique ? "UNIQUE" : "NON-UNIQUE"),
				myIndexName,
				getTableName(),
				myColumns);

		String sql = generateSql();
		String tableName = getTableName();

		try {
			executeSql(tableName, sql);
		} catch (Exception e) {
			String message = e.toString();
			if (message.contains("already exists")
					||
					// The Oracle message is ORA-01408: such column list already indexed
					// TODO KHS consider db-specific handling here that uses the error code instead of the message so
					// this is language independent
					//  e.g. if the db is Oracle than checking e.getErrorCode() == 1408 should detect this case
					message.contains("already indexed")) {
				ourLog.warn("Index {} already exists: {}", myIndexName, e.getMessage());
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
				case COCKROACHDB_21_1:
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
			mssqlWhereClause = buildMSSqlNotNullWhereClause();
		}
		// Should we do this non-transactionally?  Avoids a write-lock, but introduces weird failure modes.
		String postgresOnlineClause = "";
		String oracleOnlineClause = "";
		if (myOnline) {
			switch (getDriverType()) {
				case POSTGRES_9_4:
				case COCKROACHDB_21_1:
					postgresOnlineClause = "CONCURRENTLY ";
					// This runs without a lock, and can't be done transactionally.
					setTransactional(false);
					break;
				case MSSQL_2012:
					// handled below in buildOnlineCreateWithTryCatchFallback()
					break;
				case ORACLE_12C:
					// todo: delete this once we figure out how run Oracle try-catch to match MSSQL.
					if (myMetadataSource.isOnlineIndexSupported(getConnectionProperties())) {
						oracleOnlineClause = " ONLINE DEFERRED INVALIDATION";
					}
					break;
				default:
			}
		}

		String bareCreateSql = "create " + unique + "index " + postgresOnlineClause + myIndexName + " on "
				+ getTableName() + "(" + columns + ")" + includeClause + mssqlWhereClause + oracleOnlineClause;

		String sql;
		if (myOnline && DriverTypeEnum.MSSQL_2012 == getDriverType()) {
			sql = buildOnlineCreateWithTryCatchFallback(bareCreateSql);
		} else {
			sql = bareCreateSql;
		}
		return sql;
	}

	/**
	 * Wrap a Sql Server create index in a try/catch to try it first ONLINE
	 * (meaning no table locks), and on failure, without ONLINE (locking the table).
	 *
	 * This try-catch syntax was manually tested via sql
	 * {@code
	 * BEGIN TRY
	 * 	EXEC('create index FOO on TABLE_A (col1)  WITH (ONLINE = ON)');
	 * 	select 'Online-OK';
	 * END TRY
	 * BEGIN CATCH
	 * 	create index FOO on TABLE_A (col1);
	 * 	select 'Offline';
	 * END CATCH;
	 * -- Then inspect the result set - Online-OK means it ran the ONLINE version.
	 * -- Note: we use EXEC() in the online path to lower the severity of the error
	 * -- so the CATCH can catch it.
	 * }
	 *
	 * @param bareCreateSql
	 * @return
	 */
	static @Nonnull String buildOnlineCreateWithTryCatchFallback(String bareCreateSql) {
		// Some "Editions" of Sql Server do not support ONLINE.
		// @format:off
		return "BEGIN TRY -- try first online, without locking the table \n"
				+ "    EXEC('" + bareCreateSql + " WITH (ONLINE = ON)');\n"
				+ "END TRY \n"
				+ "BEGIN CATCH -- for Editions of Sql Server that don't support ONLINE, run with table locks \n"
				+ bareCreateSql
				+ "; \n"
				+ "END CATCH;";
		// @format:on
	}

	@Nonnull
	private String buildMSSqlNotNullWhereClause() {
		String mssqlWhereClause = "";
		if (myNullableColumns == null || myNullableColumns.isEmpty()) {
			return mssqlWhereClause;
		}

		mssqlWhereClause = " WHERE (";
		mssqlWhereClause += myNullableColumns.stream()
				.map(column -> column + " IS NOT NULL ")
				.collect(Collectors.joining("AND"));
		mssqlWhereClause += ")";
		return mssqlWhereClause;
	}

	public void setColumns(String... theColumns) {
		setColumns(Arrays.asList(theColumns));
	}

	public void setNullableColumns(String... theColumns) {
		setNullableColumns(Arrays.asList(theColumns));
	}

	public void setIncludeColumns(String... theIncludeColumns) {
		setIncludeColumns(Arrays.asList(theIncludeColumns));
	}

	private void setIncludeColumns(List<String> theIncludeColumns) {
		Objects.requireNonNull(theIncludeColumns);
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

	public void setMetadataSource(MetadataSource theMetadataSource) {
		myMetadataSource = theMetadataSource;
	}
}
