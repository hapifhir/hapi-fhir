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
package ca.uhn.fhir.jpa.migrate.util;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlUtil {
	private static final Pattern CREATE_TABLE = Pattern.compile(
			"create table ([a-zA-Z0-9_]+) .* primary key \\(([a-zA-Z_, ]+)\\).*",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	/**
	 * Non instantiable
	 */
	private SqlUtil() {
		// nothing
	}

	@Nonnull
	public static List<String> splitSqlFileIntoStatements(String theSql) {
		String sqlWithoutComments = Arrays.stream(theSql.split("\n"))
				.filter(t -> !t.trim().startsWith("--"))
				.collect(Collectors.joining("\n"));

		return Arrays.stream(sqlWithoutComments.split(";"))
				.filter(StringUtils::isNotBlank)
				.map(StringUtils::trim)
				.collect(Collectors.toList());
	}

	/**
	 * Accepts a SQL statement and parses it as a SQL <code>CREATE TABLE</code>
	 * statement, returning details about the table name and primary key.
	 * <b>This method has only been tested for Postgresql DDL format!</b>
	 *
	 * @param theStatement A single SQL statement
	 * @return Returns details about the table name and PK columns if the SQL statement
	 * 	contains a valid CREATE TABLE statement, returns {@literal null}
	 * 	otherwise.
	 */
	@Nonnull
	public static Optional<CreateTablePrimaryKey> parseCreateTableStatementPrimaryKey(String theStatement) {
		Matcher matcher = CREATE_TABLE.matcher(theStatement);
		if (matcher.find()) {
			String tableName = matcher.group(1).toUpperCase(Locale.US);
			String primaryKeyColumnsString = matcher.group(2);
			List<String> primaryKeyColumns = Arrays.asList(StringUtils.split(primaryKeyColumnsString, ", "));
			return Optional.of(new CreateTablePrimaryKey(tableName, primaryKeyColumns));
		}
		return Optional.empty();
	}

	public static class CreateTablePrimaryKey {
		private final String myTableName;
		private final List<String> myPrimaryKeyColumns;

		public CreateTablePrimaryKey(String theTableName, List<String> thePrimaryKeyColumns) {
			myTableName = theTableName;
			myPrimaryKeyColumns = thePrimaryKeyColumns;
		}

		public List<String> getPrimaryKeyColumns() {
			return myPrimaryKeyColumns;
		}

		public String getTableName() {
			return myTableName;
		}
	}
}
