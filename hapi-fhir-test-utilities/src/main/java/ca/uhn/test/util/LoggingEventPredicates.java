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

import java.util.List;
import java.util.function.Predicate;

public final class LoggingEventPredicates {
	private LoggingEventPredicates() {
	}

	@Nonnull
	public static Predicate<ILoggingEvent> makeLevelEquals(@Nonnull Level theLevel) {
		return loggingEvent -> loggingEvent.getLevel().equals(theLevel);
	}

	@Nonnull
	public static Predicate<ILoggingEvent> makeMessageEquals(@Nonnull String theMessage) {
		return loggingEvent -> loggingEvent.getFormattedMessage().equals(theMessage);
	}

	@Nonnull
	public static Predicate<ILoggingEvent> makeMessageContains(@Nonnull String theMessage) {
		return loggingEvent -> loggingEvent.getFormattedMessage().contains(theMessage);
	}

	@Nonnull
	public static Predicate<ILoggingEvent> makeExceptionMessageContains(@Nonnull String theExceptionMessage) {
		return loggingEvent -> loggingEvent.getThrowableProxy().getMessage().contains(theExceptionMessage);
	}

	@Nonnull
	public static Predicate<List<? extends ILoggingEvent>> makeAnyMatch(@Nonnull Predicate<ILoggingEvent> thePredicate) {
		return loggingEvents -> loggingEvents.stream().anyMatch(thePredicate);
	}

}
