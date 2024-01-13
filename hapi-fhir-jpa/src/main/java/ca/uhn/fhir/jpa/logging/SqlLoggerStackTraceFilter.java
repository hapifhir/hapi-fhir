package ca.uhn.fhir.jpa.logging;

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
			throw new RuntimeException(theE);
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
