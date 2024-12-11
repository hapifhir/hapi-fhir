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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SqlUtil {

	/**
	 * Non instantiable
	 */
	private SqlUtil() {
		// nothing
	}

	@Nonnull
	public static List<String> splitSqlFileIntoStatements(String theSql) {
		String sqlWithoutComments = Arrays.stream(theSql.split("\n"))
				.filter(t -> !t.startsWith("--"))
				.collect(Collectors.joining("\n"));

		return Arrays.stream(sqlWithoutComments.split(";"))
				.filter(StringUtils::isNotBlank)
				.map(StringUtils::trim)
				.collect(Collectors.toList());
	}

	@Nonnull
	public static List<String> splitSqlFileIntoSqlStatementsUpperCase(String theSql) {
		return splitSqlFileIntoStatements(theSql).stream()
				.map(t -> t.toUpperCase(Locale.US))
				.collect(Collectors.toList());
	}
}
