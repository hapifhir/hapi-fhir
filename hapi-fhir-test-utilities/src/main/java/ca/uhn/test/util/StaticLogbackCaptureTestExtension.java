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
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 * This is a static wrapper around LogbackTestExtension for use in IT tests when you need to assert on App
 * startup log entries
 * @deprecated use {@link StaticLogbackTestExtension}
 */
@Deprecated
public class StaticLogbackCaptureTestExtension implements BeforeAllCallback, AfterAllCallback {
	private final LogbackCaptureTestExtension myLogbackCaptureTestExtension;

	public StaticLogbackCaptureTestExtension(LogbackCaptureTestExtension theLogbackCaptureTestExtension) {
		myLogbackCaptureTestExtension = theLogbackCaptureTestExtension;
	}

	public StaticLogbackCaptureTestExtension() {
		myLogbackCaptureTestExtension = new LogbackCaptureTestExtension();
	}

	public static StaticLogbackCaptureTestExtension withThreshold(Level theLevel) {
		LogbackCaptureTestExtension logbackCaptureTestExtension = new LogbackCaptureTestExtension();
		logbackCaptureTestExtension.setUp(theLevel);
		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setLevel(theLevel.levelStr);
		logbackCaptureTestExtension.getAppender().addFilter(thresholdFilter);

		return new StaticLogbackCaptureTestExtension(logbackCaptureTestExtension);
	}

	@Override
	public void beforeAll(ExtensionContext theExtensionContext) throws Exception {
		if (myLogbackCaptureTestExtension.getAppender() == null) {
			myLogbackCaptureTestExtension.beforeEach(theExtensionContext);
		}
	}

	@Override
	public void afterAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackCaptureTestExtension.afterEach(theExtensionContext);
	}

	/**
	 * Returns a copy to avoid concurrent modification errors.
	 * @return A copy of the log events so far.
	 */
	public java.util.List<ILoggingEvent> getLogEvents() {
		return myLogbackCaptureTestExtension.getLogEvents();
	}

	/** Clear accumulated log events. */
	public void clearEvents() {
		myLogbackCaptureTestExtension.clearEvents();
	}

	public ListAppender<ILoggingEvent> getAppender() {
		return myLogbackCaptureTestExtension.getAppender();
	}

	public List<ILoggingEvent> filterLoggingEventsWithMessageEqualTo(String theMessageText){
		return myLogbackCaptureTestExtension.filterLoggingEventsWithMessageEqualTo(theMessageText);
	}

	public List<ILoggingEvent> filterLoggingEventsWithMessageContaining(String theMessageText){
		return myLogbackCaptureTestExtension.filterLoggingEventsWithMessageContaining(theMessageText);
	}

	public List<ILoggingEvent> filterLoggingEventsWithPredicate(Predicate<ILoggingEvent> theLoggingEventPredicate){
		return myLogbackCaptureTestExtension.filterLoggingEventsWithPredicate(theLoggingEventPredicate);
	}

	/**
	 * Extract the log messages from the logging events.
	 * @return a copy of the List of log messages
	 *
	 */
	@Nonnull
	public List<String> getLogMessages() {
		return myLogbackCaptureTestExtension.getLogMessages();
	}

}
