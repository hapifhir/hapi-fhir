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
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AddIdGeneratorTask extends BaseTask<AddIdGeneratorTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddIdGeneratorTask.class);
	private final String myGeneratorName;

	public AddIdGeneratorTask(String theGeneratorName) {
		myGeneratorName = theGeneratorName;
	}

	@Override
	public void validate() {
		Validate.notBlank(myGeneratorName);
	}

	@Override
	public void execute() throws SQLException {
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		String sql = null;

		switch (getDriverType()) {
			case MARIADB_10_1:
			case MYSQL_5_7:
				// These require a separate table
				if (!tableNames.contains(myGeneratorName)) {

					String creationSql = "create table " + myGeneratorName + " ( next_val bigint ) engine=InnoDB";
					executeSql(myGeneratorName, creationSql);

					String initSql = "insert into " + myGeneratorName + " values ( 1 )";
					executeSql(myGeneratorName, initSql);

				}
				break;
			case DERBY_EMBEDDED:
			case H2_EMBEDDED:
				sql = "create sequence " + myGeneratorName + " start with 1 increment by 50";
				break;
			case POSTGRES_9_4:
				sql = "create sequence " + myGeneratorName + " start 1 increment 50";
				break;
			case ORACLE_12C:
				sql = "create sequence " + myGeneratorName + " start with 1 increment by 50";
				break;
			case MSSQL_2012:
				sql = "create sequence " + myGeneratorName + " start with 1 increment by 50";
				break;
			default:
				throw new IllegalStateException();
		}

		if (isNotBlank(sql)) {
			Set<String> sequenceNames =
				JdbcUtils.getSequenceNames(getConnectionProperties())
				.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toSet());
			ourLog.debug("Currently have sequences: {}", sequenceNames);
			if (sequenceNames.contains(myGeneratorName.toLowerCase())) {
				ourLog.info("Sequence {} already exists - No action performed", myGeneratorName);
				return;
			}

			executeSql(myGeneratorName, sql);
		}

	}

}
