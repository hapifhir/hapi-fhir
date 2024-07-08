/*-
 * #%L
 * Prior Auth pas
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * All rights reserved.
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
