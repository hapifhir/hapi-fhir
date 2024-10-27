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
package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.i18n.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

/**
 * Utility methods to be used by migrator functionality that needs to invoke JDBC directly.
 */
public class MigrationJdbcUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(MigrationJdbcUtils.class);

	public static boolean queryForSingleBooleanResultMultipleThrowsException(
			String theSql, JdbcTemplate theJdbcTemplate) {
		final RowMapper<Boolean> booleanRowMapper = (theResultSet, theRowNumber) -> theResultSet.getBoolean(1);
		return queryForSingle(theSql, theJdbcTemplate, booleanRowMapper).orElse(false);
	}

	private static <T> Optional<T> queryForSingle(
			String theSql, JdbcTemplate theJdbcTemplate, RowMapper<T> theRowMapper) {
		final List<T> results = queryForMultiple(theSql, theJdbcTemplate, theRowMapper);

		if (results.isEmpty()) {
			return Optional.empty();
		}

		if (results.size() > 1) {
			// Presumably other callers may want different behaviour but in this case more than one result should be
			// considered a hard failure distinct from an empty result, which is one expected outcome.
			throw new IllegalArgumentException(Msg.code(2474)
					+ String.format(
							"Failure due to query returning more than one result: %s for SQL: [%s].", results, theSql));
		}

		return Optional.ofNullable(results.get(0));
	}

	private static <T> List<T> queryForMultiple(
			String theSql, JdbcTemplate theJdbcTemplate, RowMapper<T> theRowMapper) {
		return theJdbcTemplate.query(theSql, theRowMapper);
	}
}
