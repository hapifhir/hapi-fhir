package ca.uhn.test.util;

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

/**
 * Test helper to collect logback lines.
 *
 * The empty constructor will capture all log events, or you can name a log root to limit the noise.
 */
public class LogbackCaptureTestExtension implements BeforeEachCallback, AfterEachCallback {
	private final Logger myLogger;
	private final ListAppender<ILoggingEvent> myListAppender = new ListAppender<>();

	/**
	 * @param theLoggerName the log name root to capture
	 */
	public LogbackCaptureTestExtension(String theLoggerName) {
		myLogger = (Logger) LoggerFactory.getLogger(theLoggerName);
	}

	/**
	 * Capture the root logger - all lines.
	 */
	public LogbackCaptureTestExtension() {
		this(org.slf4j.Logger.ROOT_LOGGER_NAME);
	}

	/**
	 * Direct reference to the list of events.
	 * You may clear() it, or whatever.
	 * @return the modifiable List of events captured so far.
	 */
	public java.util.List<ILoggingEvent> getLogEvents() {
		return myListAppender.list;
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		myListAppender.start(); // 		SecurityContextHolder.getContext().setAuthentication(authResult);
		myLogger.addAppender(myListAppender);

	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myLogger.detachAppender(myListAppender);
	}

	public Matcher<ILoggingEvent> eventWithLevelAndMessageContains(Level theLevel, String thePartialMessage) {
		return new LogbackEventMatcher("log event", theLevel, thePartialMessage);
	}
	public static class LogbackEventMatcher extends CustomTypeSafeMatcher<ILoggingEvent> {

		private Level myLevel;
		private String myString;

		public LogbackEventMatcher(String description, Level theLevel, String theString) {
			super(description);
			myLevel = theLevel;
			myString = theString;
		}

		@Override
		protected boolean matchesSafely(ILoggingEvent item) {
			return (myLevel == null || item.getLevel().isGreaterOrEqual(myLevel)) &&
				item.getMessage().contains(myString);
		}
	}
}
