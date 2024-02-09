package ca.uhn.test.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.assertj.core.api.AbstractAssert;

public class LogEventAssert extends AbstractAssert<LogEventAssert, ILoggingEvent> {

	protected LogEventAssert(ILoggingEvent actual) {
		super(actual, LogEventAssert.class);
	}

	// Example of a custom assertion for a single LogEvent
	// You can add methods for specific assertions here
}
