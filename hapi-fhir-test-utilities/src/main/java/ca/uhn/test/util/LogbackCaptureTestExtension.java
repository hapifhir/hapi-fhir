package ca.uhn.test.util;

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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Test helper to collect logback lines.
 *
 * The empty constructor will capture all log events, or you can name a log root to limit the noise.
 */
public class LogbackCaptureTestExtension implements BeforeEachCallback, AfterEachCallback {
	private final Logger myLogger;
	private final Level myLevel;
	private ListAppender<ILoggingEvent> myListAppender = null;
	private Level mySavedLevel;

	/**
	 *
	 * @param theLogger the log to capture
	 */
	public LogbackCaptureTestExtension(Logger theLogger) {
		myLogger = theLogger;
		myLevel = null;
	}

	/**
	 *
	 * @param theLogger the log to capture
	 * @param theTestLogLevel the Level to set for the duration of the test
	 */
	public LogbackCaptureTestExtension(Logger theLogger, Level theTestLogLevel) {
		myLogger = theLogger;
		myLevel = theTestLogLevel;
	}

	/**
	 * @param theLoggerName the log name to capture
	 */
	public LogbackCaptureTestExtension(String theLoggerName) {
		this((Logger) LoggerFactory.getLogger(theLoggerName));
	}

	/**
	 * Capture the root logger - all lines.
	 */
	public LogbackCaptureTestExtension() {
		this(org.slf4j.Logger.ROOT_LOGGER_NAME);
	}

	/**
	 * Returns a copy to avoid concurrent modification errors.
	 * @return A copy of the log events so far.
	 */
	public java.util.List<ILoggingEvent> getLogEvents() {
		// copy to avoid concurrent mod errors
		return new ArrayList<>(myListAppender.list);
	}

	/** Clear accumulated log events. */
	public void clearEvents() {
		myListAppender.list.clear();
	}

	public ListAppender<ILoggingEvent> getAppender() {
		return myListAppender;
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		myListAppender = new ListAppender<>();
		myListAppender.start();
		myLogger.addAppender(myListAppender);
		if (myLevel != null) {
			mySavedLevel = myLogger.getLevel();
			myLogger.setLevel(myLevel);
		}
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myLogger.detachAppender(myListAppender);
		myListAppender.stop();
		if (myLevel != null) {
			myLogger.setLevel(mySavedLevel);
		}
	}


	public List<ILoggingEvent> filterLoggingEventsWithMessageEqualTo(String theMessageText){
		return filterLoggingEventsWithPredicate(loggingEvent -> loggingEvent.getFormattedMessage().equals(theMessageText));
	}

	public List<ILoggingEvent> filterLoggingEventsWithMessageContaining(String theMessageText){
		return filterLoggingEventsWithPredicate(loggingEvent -> loggingEvent.getFormattedMessage().contains(theMessageText));
	}

	public List<ILoggingEvent> filterLoggingEventsWithPredicate(Predicate<ILoggingEvent> theLoggingEventPredicate){
		return getLogEvents()
			.stream()
			.filter(theLoggingEventPredicate)
			.collect(Collectors.toList());
	}

	//  Hamcrest matcher support
	public static Matcher<ILoggingEvent> eventWithLevelAndMessageContains(@Nonnull Level theLevel, @Nonnull String thePartialMessage) {
		return new LogbackEventMatcher(theLevel, thePartialMessage);
	}

	public static Matcher<ILoggingEvent> eventWithLevel(@Nonnull Level theLevel) {
		return new LogbackEventMatcher(theLevel, null);
	}

	public static Matcher<ILoggingEvent> eventWithMessageContains(@Nonnull String thePartialMessage) {
		return new LogbackEventMatcher(null, thePartialMessage);
	}

	/**
	 * A Hamcrest matcher for junit assertions.
	 * Matches on level and/or partial message.
	 */
	public static class LogbackEventMatcher extends CustomTypeSafeMatcher<ILoggingEvent> {
		@Nullable
		private final Level myLevel;
		@Nullable
		private final String myString;

		public LogbackEventMatcher(@Nullable Level theLevel, @Nullable String thePartialString) {
			this("log event", theLevel, thePartialString);
		}

		public LogbackEventMatcher(String description, @Nullable Level theLevel, @Nullable String thePartialString) {
			super(makeDescription(description, theLevel, thePartialString));
			myLevel = theLevel;
			myString = thePartialString;
		}
		@Nonnull
		private static String makeDescription(String description, Level theLevel, String thePartialString) {
			String msg = description;
			if (theLevel != null) {
				msg = msg + " with level at least " + theLevel;
			}
			if (thePartialString != null) {
				msg = msg + " containing string \"" + thePartialString + "\"";

			}
			return msg;
		}

		@Override
		protected boolean matchesSafely(ILoggingEvent item) {
			return (myLevel == null || item.getLevel().isGreaterOrEqual(myLevel)) &&
				(myString == null || item.getFormattedMessage().contains(myString));
		}
	}
}
