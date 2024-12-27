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
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlUtil {
	private static final Pattern CREATE_TABLE = Pattern.compile(
			"create table ([a-zA-Z0-9_]+).*(\\s|[a-zA-Z0-9,()_])+?primary key\\s+\\(([a-zA-Z_, ]+)\\).*",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern ALTER_TABLE_ADD_CONSTRAINT_FOREIGN_KEY = Pattern.compile(
			"alter table\\s+(if exists)?\\s+([a-zA-Z_]+)\\s+add constraint\\s+([a-zA-Z0-9_]+)\\s+foreign key \\(([a-zA-Z_, ]+)\\)\\s+references ([a-zA-Z_]+).*",
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
			String primaryKeyColumnsString = matcher.group(3);
			List<String> primaryKeyColumns = splitCommaSeparatedList(primaryKeyColumnsString);
			return Optional.of(new CreateTablePrimaryKey(tableName, primaryKeyColumns));
		}
		return Optional.empty();
	}

	/**
	 * Accepts a DDL statement containing
	 * <code>ALTER TABLE [IF EXISTS]? table_name ADD CONSTRAINT constraint_name FOREIGN KEY (column_list)</code>
	 * and returns the parsed details.
	 */
	@Nonnull
	public static Optional<AlterTableAddConstraint> parseAlterTableAddConstraintConstraintForeignKey(
			String theStatement) {
		Matcher matcher = ALTER_TABLE_ADD_CONSTRAINT_FOREIGN_KEY.matcher(theStatement);
		if (matcher.find()) {
			String tableName = matcher.group(2);
			String constraintName = matcher.group(3);
			String columnsString = matcher.group(4);
			String references = matcher.group(5);
			List<String> columns = splitCommaSeparatedList(columnsString);

			return Optional.of(new AlterTableAddConstraint(tableName, constraintName, columns, references));
		}
		return Optional.empty();
	}

	@Nonnull
	private static List<String> splitCommaSeparatedList(String primaryKeyColumnsString) {
		return Arrays.asList(StringUtils.split(primaryKeyColumnsString, ", "));
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

	public static class AlterTableAddConstraint {
		private final String myConstraintName;
		private final List<String> myColumns;
		private final String myTableName;
		private final String myReferences;

		public AlterTableAddConstraint(
				String theTableName, String theConstraintName, List<String> theColumns, String theReferences) {
			Validate.isTrue(theTableName.matches("^[a-zA-Z0-9_]+$"), "Invalid table name '%s'", theTableName);
			Validate.isTrue(
					theConstraintName.matches("^[a-zA-Z0-9_]+$"), "Invalid constraint name '%s'", theConstraintName);
			Validate.isTrue(theReferences.matches("^[a-zA-Z0-9_]+$"), "Invalid reference '%s'", theReferences);
			Validate.isTrue(!theColumns.isEmpty(), "Invalid columns '%s'", theColumns);
			Validate.isTrue(
					theColumns.stream()
									.map(t -> t.matches("^[a-zA-Z0-9_]+$"))
									.filter(t -> t)
									.count()
							== theColumns.size(),
					"Invalid columns '%s'",
					theColumns);
			myTableName = theTableName;
			myConstraintName = theConstraintName;
			myColumns = theColumns;
			myReferences = theReferences;
		}

		public String getReferences() {
			return myReferences;
		}

		public List<String> getColumns() {
			return myColumns;
		}

		public String getTableName() {
			return myTableName;
		}

		public String getConstraintName() {
			return myConstraintName;
		}
	}
}
