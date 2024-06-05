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
import jakarta.annotation.Nullable;
import org.assertj.core.api.AbstractAssert;

/**
 * An assertj matcher for junit assertions.
 * Matches on level, partial message, and/or a portion of the message contained by a throwable, if present.
 */
public class LogbackEventAssert extends AbstractAssert<LogbackEventAssert, ILoggingEvent> {
	@Nullable
	private final Level myLevel;
	@Nullable
	private final String myLogMessage;
	@Nullable
	private final String myThrownMessage;

	public LogbackEventAssert(@Nullable Level theLevel, @Nullable String thePartialString) {
		this(theLevel, thePartialString, null);
	}

	public LogbackEventAssert(@Nullable Level theLevel, @Nullable String thePartialString, @Nullable String theThrownMessage) {
		super(null, LogbackEventAssert.class);
		myLevel = theLevel;
		myLogMessage = thePartialString;
		myThrownMessage = theThrownMessage;
	}

	public static LogbackEventAssert assertThat(@Nullable Level theLevel, @Nullable String thePartialString) {
		return new LogbackEventAssert(theLevel, thePartialString);
	}

	public static LogbackEventAssert assertThat(@Nullable Level theLevel, @Nullable String thePartialString, @Nullable String theThrownMessage) {
		return new LogbackEventAssert(theLevel, thePartialString, theThrownMessage);
	}

	public LogbackEventAssert matches(ILoggingEvent item) {
		isNotNull();

		if (myLevel != null && !item.getLevel().isGreaterOrEqual(myLevel)) {
			failWithMessage("Expected level to be at least <%s> but was <%s>", myLevel, item.getLevel());
		}

		if (myLogMessage != null && !item.getFormattedMessage().contains(myLogMessage)) {
			failWithMessage("Expected log message to contain <%s> but was <%s>", myLogMessage, item.getFormattedMessage());
		}

		if (myThrownMessage != null && (item.getThrowableProxy() == null || !item.getThrowableProxy().getMessage().contains(myThrownMessage))) {
			failWithMessage("Expected throwable message to contain <%s> but was <%s>", myThrownMessage, item.getThrowableProxy() != null ? item.getThrowableProxy().getMessage() : "null");
		}

		return this;
	}
}
