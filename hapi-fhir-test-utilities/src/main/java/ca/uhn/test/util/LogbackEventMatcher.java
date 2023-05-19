/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import org.hamcrest.CustomTypeSafeMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Hamcrest matcher for junit assertions.
 * Matches on level, partial message, and/or a portion of the message contained by a throwable, if present.
 */
public class LogbackEventMatcher extends CustomTypeSafeMatcher<ILoggingEvent> {
	@Nullable
	private final Level myLevel;
	@Nullable
	private final String myLogMessage;
	@Nullable
	private final String myThrownMessage;

	public LogbackEventMatcher(@Nullable Level theLevel, @Nullable String thePartialString) {
		this("log event", theLevel, thePartialString, null);
	}

	public LogbackEventMatcher(@Nullable Level theLevel, @Nullable String thePartialString, @Nullable String theThrownMessage) {
		this("log event", theLevel, thePartialString, theThrownMessage);
	}

	private LogbackEventMatcher(@Nonnull String description, Level theLevel,
										 String thePartialString, String theThrownMessage)
	{
		super(makeDescription(description, theLevel, thePartialString, theThrownMessage));
		myLevel = theLevel;
		myLogMessage = thePartialString;
		myThrownMessage = theThrownMessage;
	}

	@Nonnull
	private static String makeDescription(String description, Level theLevel, String thePartialString, String theThrownMessage) {
		String msg = description;
		if (theLevel != null) {
			msg = msg + " with level at least " + theLevel;
		}
		if (thePartialString != null) {
			msg = msg + " containing string \"" + thePartialString + "\"";

		}
		if (thePartialString != null) {
			msg = msg + " and throwable with error message containing string \"" + theThrownMessage + "\"";

		}
		return msg;
	}

	@Override
	protected boolean matchesSafely(ILoggingEvent item) {
		return (myLevel == null || item.getLevel().isGreaterOrEqual(myLevel)) &&
			(myLogMessage == null || item.getFormattedMessage().contains(myLogMessage)) &&
			(myThrownMessage == null || item.getThrowableProxy() == null || item.getThrowableProxy().getMessage().contains(myThrownMessage));
	}
}
