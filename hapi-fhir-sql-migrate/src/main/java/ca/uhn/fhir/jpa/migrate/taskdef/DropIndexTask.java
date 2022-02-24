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
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DropIndexTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(DropIndexTask.class);
	private String myIndexName;
	private boolean myOnline;

	public DropIndexTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	List<String> generateSql() throws SQLException {
		Validate.notBlank(myIndexName, "indexName must not be blank");
		Validate.notBlank(getTableName(), "tableName must not be blank");

		if (!JdbcUtils.getIndexNames(getConnectionProperties(), getTableName()).contains(myIndexName)) {
			return Collections.emptyList();
		}
		boolean isUnique = JdbcUtils.isIndexUnique(getConnectionProperties(), getTableName(), myIndexName);

		return doGenerateSql(isUnique);
	}

	// testable without jdbc
	@Nonnull
	List<String> doGenerateSql(boolean isUnique) {
		DriverTypeEnum driverType = getDriverType();
		List<String> sql = new ArrayList<>();

		if (isUnique) {
			// Drop constraint
			switch (driverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					// Need to quote the index name as the word "PRIMARY" is reserved in MySQL
					sql.add("alter table " + getTableName() + " drop index `" + myIndexName + "`");
					break;
				case H2_EMBEDDED:
					sql.add("drop index " + myIndexName);
					break;
				case DERBY_EMBEDDED:
					sql.add("alter table " + getTableName() + " drop constraint " + myIndexName);
					break;
				case ORACLE_12C:
					sql.add("drop index " + myIndexName + (myOnline?" ONLINE":""));
					break;
				case MSSQL_2012:
					sql.add("drop index " + myIndexName + " on " + getTableName() + (myOnline?" WITH (ONLINE = ON)":""));
					break;
				case POSTGRES_9_4:
					sql.add("alter table " + getTableName() + " drop constraint if exists " + myIndexName + " cascade");
					sql.add("drop index " + (myOnline?"CONCURRENTLY ":"") + "if exists " + myIndexName + " cascade");
					setTransactional(!myOnline);
					break;
			}
		} else {
			// Drop index
			switch (driverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql.add("alter table " + getTableName() + " drop index " + myIndexName);
					break;
				case POSTGRES_9_4:
					sql.add("drop index " + (myOnline?"CONCURRENTLY ":"") + myIndexName);
					setTransactional(!myOnline);
					break;
				case DERBY_EMBEDDED:
				case H2_EMBEDDED:
					sql.add("drop index " + myIndexName);
					break;
				case ORACLE_12C:
					sql.add("drop index " + myIndexName + (myOnline?" ONLINE":""));
					break;
				case MSSQL_2012:
					sql.add("drop index " + getTableName() + "." + myIndexName );
					break;
			}
		}
		return sql;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myIndexName, "The index name must not be blank");

		setDescription("Drop index " + myIndexName + " from table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		/*
		 * Derby and H2 both behave a bit weirdly if you create a unique constraint
		 * using the @UniqueConstraint annotation in hibernate - They will create a
		 * constraint with that name, but will then create a shadow index with a different
		 * name, and it's that different name that gets reported when you query for the
		 * list of indexes.
		 *
		 * For example, on H2 if you create a constraint named "IDX_FOO", the system
		 * will create an index named "IDX_FOO_INDEX_A" and a constraint named "IDX_FOO".
		 *
		 * The following is a solution that uses appropriate native queries to detect
		 * on the given platforms whether an index name actually corresponds to a
		 * constraint, and delete that constraint.
		 */

		if (getDriverType() == DriverTypeEnum.H2_EMBEDDED) {
			@Language("SQL") String findConstraintSql = "SELECT DISTINCT constraint_name FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE constraint_name = ? AND table_name = ?";
			@Language("SQL") String dropConstraintSql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT ?";
			findAndDropConstraint(findConstraintSql, dropConstraintSql);
		} else if (getDriverType() == DriverTypeEnum.DERBY_EMBEDDED) {
			@Language("SQL") String findConstraintSql = "SELECT c.constraintname FROM sys.sysconstraints c, sys.systables t WHERE c.tableid = t.tableid AND c.constraintname = ? AND t.tablename = ?";
			@Language("SQL") String dropConstraintSql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT ?";
			findAndDropConstraint(findConstraintSql, dropConstraintSql);
		} else if (getDriverType() == DriverTypeEnum.ORACLE_12C) {
			@Language("SQL") String findConstraintSql = "SELECT DISTINCT constraint_name FROM user_cons_columns WHERE constraint_name = ? AND table_name = ?";
			@Language("SQL") String dropConstraintSql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT ?";
			findAndDropConstraint(findConstraintSql, dropConstraintSql);
			findConstraintSql = "SELECT DISTINCT constraint_name FROM all_constraints WHERE index_name = ? AND table_name = ?";
			findAndDropConstraint(findConstraintSql, dropConstraintSql);
		} else if (getDriverType() == DriverTypeEnum.MSSQL_2012) {
			// Legacy deletion for SQL Server unique indexes
			@Language("SQL") String findConstraintSql = "SELECT tc.CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS tc WHERE tc.CONSTRAINT_NAME = ? AND tc.TABLE_NAME = ?";
			@Language("SQL") String dropConstraintSql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT ?";
			findAndDropConstraint(findConstraintSql, dropConstraintSql);
		}

		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());

		if (!indexNames.contains(myIndexName)) {
			logInfo(ourLog, "Index {} does not exist on table {} - No action needed", myIndexName, getTableName());
			return;
		}

		boolean isUnique = JdbcUtils.isIndexUnique(getConnectionProperties(), getTableName(), myIndexName);
		String uniquenessString = isUnique ? "unique" : "non-unique";

		List<String> sqls = generateSql();
		if (!sqls.isEmpty()) {
			logInfo(ourLog, "Dropping {} index {} on table {}", uniquenessString, myIndexName, getTableName());
		}
		for (@Language("SQL") String sql : sqls) {
			executeSql(getTableName(), sql);
		}
	}

	public void findAndDropConstraint(String theFindConstraintSql, String theDropConstraintSql) {
		DataSource dataSource = Objects.requireNonNull(getConnectionProperties().getDataSource());
		getConnectionProperties().getTxTemplate().executeWithoutResult(t -> {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			RowMapperResultSetExtractor<String> resultSetExtractor = new RowMapperResultSetExtractor<>(new SingleColumnRowMapper<>(String.class));
			List<String> outcome = jdbcTemplate.query(theFindConstraintSql, new Object[]{myIndexName, getTableName()}, resultSetExtractor);
			assert outcome != null;
			for (String next : outcome) {
				String sql = theDropConstraintSql.replace("?", next);
				executeSql(getTableName(), sql);
			}
		});
	}

	public DropIndexTask setIndexName(String theIndexName) {
		myIndexName = theIndexName;
		return this;
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		DropIndexTask otherObject = (DropIndexTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(myIndexName, otherObject.myIndexName);
		theBuilder.append(myOnline, otherObject.myOnline);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myIndexName);
		theBuilder.append(myOnline);
	}

	public void setOnline(boolean theFlag) {
		this.myOnline = theFlag;
	}
}
