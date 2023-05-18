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
