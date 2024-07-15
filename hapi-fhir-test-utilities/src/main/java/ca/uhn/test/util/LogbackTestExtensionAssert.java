/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.annotation.Nonnull;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;

import java.util.List;
import java.util.function.Predicate;

import static ca.uhn.test.util.LoggingEventPredicates.makeAnyMatch;
import static ca.uhn.test.util.LoggingEventPredicates.makeLevelEquals;
import static ca.uhn.test.util.LoggingEventPredicates.makeMessageContains;


public class LogbackTestExtensionAssert extends AbstractAssert<LogbackTestExtensionAssert, LogbackTestExtension> {

	private static final String HAS_ERROR_MSG_1 = "has logging message with message containing %s";
	private static final String HAS_ERROR_MSG_2 = "has logging message with level %s and message containing %s";

	@Nonnull
	public static LogbackTestExtensionAssert assertThat(@Nonnull LogbackTestExtension theLogbackTestExtension) {
		return new LogbackTestExtensionAssert(theLogbackTestExtension, LogbackTestExtensionAssert.class);
	}

	private LogbackTestExtensionAssert(LogbackTestExtension theLogbackTestExtension, Class<?> selfType) {
		super(theLogbackTestExtension, selfType);
	}

	/**
	 * Verifies that messages contains exactly the given values and nothing else, in order.
	 */
	@Nonnull
	public LogbackTestExtensionAssert containsExactMessages(@Nonnull String ... theMessages) {
		isNotNull();
		Assertions.assertThat(actual.getLogMessages()).containsExactly(theMessages);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeMessageContains(theMessage);
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_1, theMessage);
		Assertions.assertThat(actual.getLogEvents()).has(condition);
		return this;
	}

	public LogbackTestExtensionAssert doesNotHaveMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeMessageContains(theMessage);
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_1, theMessage);
		Assertions.assertThat(actual.getLogEvents()).doesNotHave(condition);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasErrorMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeLevelEquals(Level.ERROR)
			.and(makeMessageContains(theMessage));
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_2, Level.ERROR, theMessage);
		Assertions.assertThat(actual.getLogEvents()).has(condition);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasInfoMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeLevelEquals(Level.INFO)
			.and(makeMessageContains(theMessage));
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_2, Level.INFO, theMessage);
		Assertions.assertThat(actual.getLogEvents()).has(condition);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert doesNotHaveInfoMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeLevelEquals(Level.INFO).and(makeMessageContains(theMessage));
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_2, Level.INFO, theMessage);
		Assertions.assertThat(actual.getLogEvents()).doesNotHave(condition);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasWarnMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeLevelEquals(Level.WARN)
			.and(makeMessageContains(theMessage));
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_2, Level.WARN, theMessage);
		Assertions.assertThat(actual.getLogEvents()).has(condition);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasDebugMessage(@Nonnull String theMessage) {
		isNotNull();
		final Predicate<ILoggingEvent> predicate = makeLevelEquals(Level.DEBUG)
			.and(makeMessageContains(theMessage));
		final Condition<List<? extends ILoggingEvent>> condition =
			new Condition<>(makeAnyMatch(predicate), HAS_ERROR_MSG_2, Level.DEBUG, theMessage);
		Assertions.assertThat(actual.getLogEvents()).has(condition);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert anyMatch(@Nonnull Predicate<ILoggingEvent> thePredicate) {
		isNotNull();
		Assertions.assertThat(actual.getLogEvents()).anyMatch(thePredicate);
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert isEmpty() {
		Assertions.assertThat(actual.getLogEvents()).isEmpty();
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert isNotEmpty() {
		isNotNull();
		Assertions.assertThat(actual.getLogEvents()).isNotEmpty();
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasSize(int theExpected) {
		isNotNull();
		Assertions.assertThat(actual.getLogEvents()).hasSize(theExpected);
		return this;
	}

}
