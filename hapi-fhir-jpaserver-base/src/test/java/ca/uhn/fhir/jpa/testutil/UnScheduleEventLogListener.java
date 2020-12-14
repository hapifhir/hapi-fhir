package ca.uhn.fhir.jpa.testutil;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.opentest4j.AssertionFailedError;
import org.slf4j.event.LoggingEvent;

import java.util.Locale;

/**
 * Catches "entity un-schedule delete" events, which should never happen
 *
 * @see org.hibernate.event.internal.DefaultPersistEventListener to see where the logging actually happens
 */
public class UnScheduleEventLogListener<E> extends UnsynchronizedAppenderBase<E> {
	@Override
	protected void append(E eventObject) {
		LoggingEvent event = (LoggingEvent) eventObject;
		String message = event.getMessage();
		if (message.toLowerCase(Locale.US).contains("un-scheduling")) {
			throw new AssertionFailedError("Entity deletion has been unscheduled - This should never happen");
		}
	}
}
