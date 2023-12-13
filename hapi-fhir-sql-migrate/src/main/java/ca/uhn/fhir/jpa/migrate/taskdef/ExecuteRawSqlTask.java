/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.trim;

public class ExecuteRawSqlTask extends BaseTask {

	private static final Logger ourLog = LoggerFactory.getLogger(ExecuteRawSqlTask.class);
	private Map<DriverTypeEnum, List<String>> myDriverToSqls = new HashMap<>();
	private List<String> myDriverNeutralSqls = new ArrayList<>();
	// LUKETODO:  consider getting rid of this
	private List<Supplier<Boolean>> myExecuteConditions = new ArrayList<>();

	public ExecuteRawSqlTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
		setDescription("Execute raw sql");
	}

	public ExecuteRawSqlTask addSql(DriverTypeEnum theDriverType, @Language("SQL") String theSql) {
		Validate.notNull(theDriverType);
		Validate.notBlank(theSql);

		List<String> list = myDriverToSqls.computeIfAbsent(theDriverType, t -> new ArrayList<>());
		String sql = trim(theSql);

		// Trim the semicolon at the end if one is present
		while (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		list.add(sql);

		return this;
	}

	public ExecuteRawSqlTask addSql(String theSql) {
		Validate.notBlank("theSql must not be null", theSql);
		myDriverNeutralSqls.add(theSql);

		return this;
	}

	String queryForColumnCollationTemplate = "WITH defcoll AS (\n" + "	SELECT datcollate AS coll\n"
			+ "	FROM pg_database\n"
			+ "	WHERE datname = current_database()\n"
			+ ")\n"
			+ "SELECT a.attname,\n"
			+ "	CASE WHEN c.collname = 'default'\n"
			+ "		THEN defcoll.coll\n"
			+ "		ELSE c.collname\n"
			+ "	END AS collation\n"
			+ "FROM pg_attribute AS a\n"
			+ "	CROSS JOIN defcoll\n"
			+ "	LEFT JOIN pg_collation AS c ON a.attcollation = c.oid\n"
			+ "WHERE a.attrelid = '?'::regclass\n"
			+ "	AND a.attnum > 0\n"
			+ "ORDER BY attnum";

	// LUKETODO:  assume Boolean here?
	public ExecuteRawSqlTask onlyIf(String sql, Object[] theParams) {
		myExecuteConditions.add(() -> {
			final ResultSetExtractor<Boolean> rowCallbackHandler = theResultSet -> theResultSet.getBoolean(1);
			final PreparedStatementSetter preparedStatementSetter = thePreparedStatement -> {
				for (int index = 0; index < theParams.length; index++) {
					thePreparedStatement.setObject(index + 1, theParams[index]);
				}
			};

			return newJdbcTemplate().query(sql, preparedStatementSetter, rowCallbackHandler);
		});

		return this;
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	public void doExecute() {
		List<String> sqlStatements = myDriverToSqls.computeIfAbsent(getDriverType(), t -> new ArrayList<>());
		sqlStatements.addAll(myDriverNeutralSqls);

		ourLog.info("Evaluating Preconditions for executing SQL, if any exist");

		// LUKETODO: figure out how to unit test this
		// LUKETODO: consider adding a reason to this result
		for (Supplier<Boolean> executeCondition : myExecuteConditions) {
			final Boolean evalResult = executeCondition.get();

			if (!evalResult) {
				ourLog.info("Evaluation test is false.  Not executing SQL statements");
				return;
			}
		}

		logInfo(ourLog, "Going to execute {} SQL statements", sqlStatements.size());

		for (String nextSql : sqlStatements) {
			executeSql(null, nextSql);
		}
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		ExecuteRawSqlTask otherObject = (ExecuteRawSqlTask) theOtherObject;
		theBuilder.append(myDriverNeutralSqls, otherObject.myDriverNeutralSqls);
		theBuilder.append(myDriverToSqls, otherObject.myDriverToSqls);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(myDriverNeutralSqls);
		theBuilder.append(myDriverToSqls);
	}
}
