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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DropIdGeneratorTask extends BaseTask {

	private static final Logger ourLog = LoggerFactory.getLogger(DropIdGeneratorTask.class);
	private final String myGeneratorName;

	public DropIdGeneratorTask(String theProductVersion, String theSchemaVersion, String theGeneratorName) {
		super(theProductVersion, theSchemaVersion);
		myGeneratorName = theGeneratorName;
	}

	@Override
	public void validate() {
		Validate.notBlank(myGeneratorName);
		setDescription("Drop id generator " + myGeneratorName);
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		String sql = null;

		switch (getDriverType()) {
			case MARIADB_10_1:
			case MYSQL_5_7:
				// These require a separate table
				if (tableNames.contains(myGeneratorName)) {

					String initSql = "delete from " + myGeneratorName;
					executeSql(myGeneratorName, initSql);

					String creationSql = "drop table " + myGeneratorName;
					executeSql(myGeneratorName, creationSql);

				}
				break;
			case DERBY_EMBEDDED:
				sql = "drop sequence " + myGeneratorName + " restrict";
				break;
			case H2_EMBEDDED:
				sql = "drop sequence " + myGeneratorName;
				break;
			case POSTGRES_9_4:
				sql = "drop sequence " + myGeneratorName;
				break;
			case ORACLE_12C:
				sql = "drop sequence " + myGeneratorName;
				break;
			case MSSQL_2012:
				sql = "drop sequence " + myGeneratorName;
				break;
			default:
				throw new IllegalStateException(Msg.code(64));
		}

		if (isNotBlank(sql)) {
			Set<String> sequenceNames =
				JdbcUtils.getSequenceNames(getConnectionProperties())
					.stream()
					.map(String::toLowerCase)
					.collect(Collectors.toSet());
			ourLog.debug("Currently have sequences: {}", sequenceNames);
			if (!sequenceNames.contains(myGeneratorName.toLowerCase())) {
				logInfo(ourLog, "Sequence {} does not exist - No action performed", myGeneratorName);
				return;
			}

			executeSql(myGeneratorName, sql);
		}

	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		DropIdGeneratorTask otherObject = (DropIdGeneratorTask) theOtherObject;
		theBuilder.append(myGeneratorName, otherObject.myGeneratorName);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(myGeneratorName);
	}
}
