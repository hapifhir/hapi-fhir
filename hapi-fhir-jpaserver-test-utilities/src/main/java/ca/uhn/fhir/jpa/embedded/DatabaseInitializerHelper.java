/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.util.VersionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.H2_EMBEDDED;

public class DatabaseInitializerHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseInitializerHelper.class);

	public void initializePersistenceSchema(JpaEmbeddedDatabase theDatabase) {
		String fileName = String.format(
				"migration/releases/%s/schema/%s.sql",
				HapiEmbeddedDatabasesExtension.FIRST_TESTED_VERSION, theDatabase.getDriverType());
		String sql = getSqlFromResourceFile(fileName);
		theDatabase.executeSqlAsBatch(sql);
	}

	public void insertPersistenceTestData(JpaEmbeddedDatabase theDatabase, VersionEnum theVersionEnum) {
		String fileName =
				String.format("migration/releases/%s/data/%s.sql", theVersionEnum, H2_EMBEDDED);
		String sql = getSqlFromResourceFile(fileName);
		String newSql = convertSql(sql, theDatabase.getDriverType());
		theDatabase.insertTestData(newSql);
	}

	private String convertSql(String sql, DriverTypeEnum driverType) {
		switch (driverType) {
			case MSSQL_2012 -> {
				String result = sql.replace("true", "'true'").replace("TRUE", "'true'");
				result = result.replace("false","'false'").replace("FALSE", "'false'");

				// replace BLOB NUMBER with MSSQL BLOB
				result = convertToBinary(result, "");
				return result;
			}
			case POSTGRES_9_4 -> {
				// replace BLOB NUMBER with Postgres BLOB
				return convertToBinary(sql, "'");
			}
			case ORACLE_12C -> {
				// Regular expression to match the date format 'YYYY-MM-DD HH:MI:SS.SSSSS'
				String blobPattern = "'\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+'";
				String result = sql.replace("true", "1").replace("TRUE", "1");
				result = result.replace("false","0").replace("FALSE", "0");

				// Regular expression to match the date format 'YYYY-MM-DD HH:MI:SS.SSSSS'
				String datePattern = "'\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+'";

				// Replace all occurrences of the date format with SYSDATE
				result = result.replaceAll(datePattern, "SYSDATE");

				// replace BLOB NUMBER with ORACLE BLOB
				result = convertToBinary(result, "'");
				return result;
			}
		}
		return sql;
	}

	private static String convertToBinary(String theSqlScript, String theReplacement) {
		// find convert_to_binary functions
		Pattern pattern = Pattern.compile("convert_to_binary\\((\\d+)\\)");
		Matcher matcher = pattern.matcher(theSqlScript);

		StringBuilder modifiedScript = new StringBuilder();

		// Iterate through all matches and replace them with the number as a string
		while (matcher.find()) {
			String number = matcher.group(1);
			String replacement = theReplacement + number + theReplacement;
			matcher.appendReplacement(modifiedScript, replacement);
		}
		matcher.appendTail(modifiedScript);
		return modifiedScript.toString();
	}

	public String getSqlFromResourceFile(String theFileName) {
		try {
			ourLog.info("Loading file: {}", theFileName);
			final URL resource = this.getClass().getClassLoader().getResource(theFileName);
			return Files.readString(Paths.get(resource.toURI()));
		} catch (Exception e) {
			throw new RuntimeException("Error loading file: " + theFileName, e);
		}
	}
}
