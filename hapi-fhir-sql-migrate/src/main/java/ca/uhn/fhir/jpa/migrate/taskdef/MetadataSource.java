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
package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

/**
 * Helper to extract database information about supported migrations.
 */
public class MetadataSource {

	/**
	 * Does this database support index operations without write-locking the table?
	 */
	public boolean isOnlineIndexSupported(DriverTypeEnum.ConnectionProperties theConnectionProperties) {

		switch (theConnectionProperties.getDriverType()) {
			case POSTGRES_9_4:
			case COCKROACHDB_21_1:
				return true;
			case MSSQL_2012:
				String mssqlEdition = getEdition(theConnectionProperties);
				return mssqlEdition.startsWith("Enterprise");
			case ORACLE_12C:
				String oracleEdition = getEdition(theConnectionProperties);
				return oracleEdition.contains("Enterprise");
			default:
				return false;
		}
	}

	/**
	 * Get the MS Sql Server or Oracle Server edition.  Other databases are not supported yet.
	 *
	 * @param theConnectionProperties the database to inspect
	 * @return the edition string (e.g. Standard, Enterprise, Developer, etc.)
	 */
	private String getEdition(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		final String result;
		if (theConnectionProperties.getDriverType() == DriverTypeEnum.MSSQL_2012) {
			result = theConnectionProperties
					.newJdbcTemplate()
					.queryForObject("SELECT SERVERPROPERTY ('edition')", String.class);
		} else if (theConnectionProperties.getDriverType() == DriverTypeEnum.ORACLE_12C) {
			result = theConnectionProperties
					.newJdbcTemplate()
					.queryForObject("SELECT BANNER FROM v$version WHERE banner LIKE 'Oracle%'", String.class);
		} else {
			throw new UnsupportedOperationException(Msg.code(2084) + "We only know about MSSQL editions.");
		}
		return result;
	}
}
