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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddTableRawSqlTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(AddTableRawSqlTask.class);
	private Map<DriverTypeEnum, List<String>> myDriverToSqls = new HashMap<>();
	private List<String> myDriverNeutralSqls = new ArrayList<>();

	public AddTableRawSqlTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Add table using raw sql");
	}

	public void addSql(DriverTypeEnum theDriverType, @Language("SQL") String theSql) {
		Validate.notNull(theDriverType);
		Validate.notBlank(theSql);

		List<String> list = myDriverToSqls.computeIfAbsent(theDriverType, t -> new ArrayList<>());
		list.add(theSql);
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		if (tableNames.contains(getTableName())) {
			logInfo(ourLog, "Table {} already exists - No action performed", getTableName());
			return;
		}

		List<String> sqlStatements = myDriverToSqls.computeIfAbsent(getDriverType(), t -> new ArrayList<>());
		sqlStatements.addAll(myDriverNeutralSqls);

		logInfo(ourLog, "Going to create table {} using {} SQL statements", getTableName(), sqlStatements.size());
		getConnectionProperties().getTxTemplate().execute(t -> {

			JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
			for (String nextSql : sqlStatements) {
				jdbcTemplate.execute(nextSql);
			}

			return null;
		});

	}

	public void addSql(String theSql) {
		Validate.notBlank("theSql must not be null", theSql);
		myDriverNeutralSqls.add(theSql);
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		super.generateEquals(theBuilder, theOtherObject);
		AddTableRawSqlTask otherObject = (AddTableRawSqlTask) theOtherObject;
		theBuilder.append(myDriverNeutralSqls, otherObject.myDriverNeutralSqls);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myDriverNeutralSqls);
	}
}
