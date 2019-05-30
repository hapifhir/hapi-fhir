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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.*;

public class AddTableRawSqlTask extends BaseTableTask<AddTableRawSqlTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddTableRawSqlTask.class);
	private Map<DriverTypeEnum, List<String>> myDriverToSqls = new HashMap<>();
	private List<String> myDriverNeutralSqls = new ArrayList<>();

	public void addSql(DriverTypeEnum theDriverType, @Language("SQL") String theSql) {
		Validate.notNull(theDriverType);
		Validate.notBlank(theSql);

		List<String> list = myDriverToSqls.computeIfAbsent(theDriverType, t -> new ArrayList<>());
		list.add(theSql);
	}

	@Override
	public void execute() throws SQLException {
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		if (tableNames.contains(getTableName())) {
			ourLog.info("Table {} already exists - No action performed", getTableName());
			return;
		}

		List<String> sqlStatements = myDriverToSqls.computeIfAbsent(getDriverType(), t -> new ArrayList<>());
		sqlStatements.addAll(myDriverNeutralSqls);

		ourLog.info("Going to create table {} using {} SQL statements", getTableName(), sqlStatements.size());
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
}
