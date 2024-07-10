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

public class LogbackTestExtensionAssert extends AbstractAssert<LogbackTestExtensionAssert, LogbackTestExtension> {
	@Nonnull
	public static LogbackTestExtensionAssert assertThat(@Nonnull LogbackTestExtension theLogbackTestExtension) {
		return new LogbackTestExtensionAssert(theLogbackTestExtension, LogbackTestExtensionAssert.class);
	}

	private LogbackTestExtensionAssert(LogbackTestExtension theLogbackTestExtension, Class<?> selfType) {
		super(theLogbackTestExtension, selfType);
	}

	@Nonnull
	public LogbackTestExtensionAssert hasError(@Nonnull String theMessage) {
		Assertions.assertThat(actual.getLogEvents()).has(eventWithLevelAndMessageContains(Level.ERROR, theMessage));
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasInfo(@Nonnull String theMessage) {
		Assertions.assertThat(actual.getLogEvents()).has(eventWithLevelAndMessageContains(Level.INFO, theMessage));
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert hasDebug(@Nonnull String theMessage) {
		Assertions.assertThat(actual.getLogEvents()).has(eventWithLevelAndMessageContains(Level.DEBUG, theMessage));
		return this;
	}

	@Nonnull
	public LogbackTestExtensionAssert isEmpty() {
		Assertions.assertThat(actual.getLogEvents()).isEmpty();
		return this;
	}

	@Nonnull
	private Condition<? super List<? extends ILoggingEvent>> eventWithLevelAndMessageContains(@Nonnull Level theLevel, @Nonnull String theExpected) {
		final Predicate<ILoggingEvent> loggingEvent = theLoggingEvent ->
			theLoggingEvent.getLevel().equals(theLevel) && theLoggingEvent.getFormattedMessage().contains(theExpected);
		final Predicate<List<? extends ILoggingEvent>> loggingEvents = theLoggingEvents ->
			theLoggingEvents.stream().anyMatch(loggingEvent);
		return new Condition<>(loggingEvents, "has error logging message with level %s and message containing %s", theLevel, theExpected);
	}

}
