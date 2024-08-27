/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.logging;

import ca.uhn.fhir.i18n.Msg;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * When filtering active, filters hibernate SQL log lines which generating code stack trace
 * contains a line which starts with the defined string
 */
public class SqlLoggerStackTraceFilter extends BaseSqlLoggerFilterImpl implements ISqlLoggerFilter {
	private static final Logger ourLog = LoggerFactory.getLogger(SqlLoggerStackTraceFilter.class);
	public static final String PREFIX = "stack:";

	@Override
	public boolean match(String theStatement) {
		// safe copy to shorten synchronized time
		LinkedList<String> filterDefinitionsSafeCopy;
		synchronized (myFilterDefinitions) {
			filterDefinitionsSafeCopy = new LinkedList<>(myFilterDefinitions);
		}

		Set<String> cdrClassesInStack =
				getStackTraceStream().map(StackTraceElement::getClassName).collect(Collectors.toSet());

		if (cdrClassesInStack.isEmpty()) {
			ourLog.trace("No CDR or HAPI-FHIR class found in stack");
			return false;
		}

		boolean matched;
		try {
			matched = cdrClassesInStack.stream().anyMatch(clName -> filterDefinitionsSafeCopy.stream()
					.filter(Objects::nonNull)
					.anyMatch(clName::startsWith));

		} catch (Exception theE) {
			ourLog.debug(
					"myFilterDefinitions: {}",
					filterDefinitionsSafeCopy.stream()
							.map(String::valueOf)
							.collect(Collectors.joining("\n", "\n", "")));
			ourLog.debug(
					"cdrClassesInStack: {}",
					cdrClassesInStack.stream().map(String::valueOf).collect(Collectors.joining("\n", "\n", "")));
			throw new RuntimeException(Msg.code(2479) + theE);
		}

		return matched;
	}

	@VisibleForTesting // Thread can't be mocked
	public Stream<StackTraceElement> getStackTraceStream() {
		return Arrays.stream(Thread.currentThread().getStackTrace());
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}
}
