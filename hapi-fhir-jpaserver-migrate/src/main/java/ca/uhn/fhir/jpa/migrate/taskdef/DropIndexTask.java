package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

	public DropIndexTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
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

		@Language("SQL") String findConstraintSql;
		@Language("SQL") String dropConstraintSql;
		if (getDriverType() == DriverTypeEnum.H2_EMBEDDED) {
			findConstraintSql = "SELECT DISTINCT INDEX_NAME FROM INFORMATION_SCHEMA.INDEXES WHERE constraint_name = ? AND table_name = ?";
			dropConstraintSql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT " + myIndexName;
		} else if (getDriverType() == DriverTypeEnum.DERBY_EMBEDDED) {
			findConstraintSql = "SELECT c.constraintname FROM sys.sysconstraints c, sys.systables t WHERE c.tableid = t.tableid AND c.constraintname = ? AND t.tablename = ?";
			dropConstraintSql = "ALTER TABLE " + getTableName() + " DROP CONSTRAINT " + myIndexName;
		} else {
			findConstraintSql = null;
			dropConstraintSql = null;
		}

		if (findConstraintSql != null) {
			DataSource dataSource = Objects.requireNonNull(getConnectionProperties().getDataSource());
			Boolean handled = getConnectionProperties().getTxTemplate().execute(t -> {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				RowMapperResultSetExtractor<String> resultSetExtractor = new RowMapperResultSetExtractor<>(new SingleColumnRowMapper<>(String.class));
				List<String> outcome = jdbcTemplate.query(findConstraintSql, new Object[]{myIndexName, getTableName()}, resultSetExtractor);
				assert outcome != null;
				if (outcome.size() > 0) {
					executeSql(getTableName(), dropConstraintSql);
					return true;
				}
				return false;
			});
			if (Boolean.TRUE.equals(handled)) {
				return;
			}
		}

		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());

		if (!indexNames.contains(myIndexName)) {
			logInfo(ourLog, "Index {} does not exist on table {} - No action needed", myIndexName, getTableName());
			return;
		}

		boolean isUnique = JdbcUtils.isIndexUnique(getConnectionProperties(), getTableName(), myIndexName);
		String uniquenessString = isUnique ? "unique" : "non-unique";

		List<String> sqls = createDropIndexSql(getConnectionProperties(), getTableName(), myIndexName, getDriverType());
		if (!sqls.isEmpty()) {
			logInfo(ourLog, "Dropping {} index {} on table {}", uniquenessString, myIndexName, getTableName());
		}
		for (@Language("SQL") String sql : sqls) {
			executeSql(getTableName(), sql);
		}
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
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myIndexName);
	}

	static List<String> createDropIndexSql(DriverTypeEnum.ConnectionProperties theConnectionProperties, String theTableName, String theIndexName, DriverTypeEnum theDriverType) throws SQLException {
		Validate.notBlank(theIndexName, "theIndexName must not be blank");
		Validate.notBlank(theTableName, "theTableName must not be blank");

		if (!JdbcUtils.getIndexNames(theConnectionProperties, theTableName).contains(theIndexName)) {
			return Collections.emptyList();
		}

		boolean isUnique = JdbcUtils.isIndexUnique(theConnectionProperties, theTableName, theIndexName);

		List<String> sql = new ArrayList<>();

		if (isUnique) {
			// Drop constraint
			switch (theDriverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					// Need to quote the index name as the word "PRIMARY" is reserved in MySQL
					sql.add("alter table " + theTableName + " drop index `" + theIndexName + "`");
					break;
				case H2_EMBEDDED:
					sql.add("drop index " + theIndexName);
					break;
				case DERBY_EMBEDDED:
				case ORACLE_12C:
					sql.add("alter table " + theTableName + " drop constraint " + theIndexName);
					break;
				case MSSQL_2012:
					sql.add("drop index " + theIndexName + " on " + theTableName);
					break;
				case POSTGRES_9_4:
					sql.add("alter table " + theTableName + " drop constraint if exists " + theIndexName + " cascade");
					sql.add("drop index if exists " + theIndexName + " cascade");
					break;
			}
		} else {
			// Drop index
			switch (theDriverType) {
				case MYSQL_5_7:
				case MARIADB_10_1:
					sql.add("alter table " + theTableName + " drop index " + theIndexName);
					break;
				case POSTGRES_9_4:
				case DERBY_EMBEDDED:
				case H2_EMBEDDED:
				case ORACLE_12C:
					sql.add("drop index " + theIndexName);
					break;
				case MSSQL_2012:
					sql.add("drop index " + theTableName + "." + theIndexName);
					break;
			}
		}
		return sql;
	}
}
