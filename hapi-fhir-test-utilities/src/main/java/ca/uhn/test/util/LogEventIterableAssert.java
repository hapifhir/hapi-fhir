package ca.uhn.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.annotation.Nonnull;
import org.assertj.core.api.AbstractIterableAssert;

import java.util.List;

public class LogEventIterableAssert extends AbstractIterableAssert<LogEventIterableAssert, List<? extends ILoggingEvent>, ILoggingEvent, LogEventAssert> {

	protected LogEventIterableAssert(List<ILoggingEvent> actual) {
		super(actual, LogEventIterableAssert.class);
	}

	public static LogEventIterableAssert assertThat(List<ILoggingEvent> actual) {
		return new LogEventIterableAssert(actual);
	}

	public LogEventIterableAssert hasNoFailureMessages() {
		isNotNull();

		matches(logEvents -> logEvents.stream()
				.map(ILoggingEvent::getMessage)
				.noneMatch(message -> message.contains("FAILURE")),
			"LogEvents should not contain messages with 'FAILURE'");
		return this;
	}

	public LogEventIterableAssert hasEventWithLevelAndMessageContains(@Nonnull Level theLevel, @Nonnull String thePartialMessage) {
		isNotNull();
		matches(logEvents -> logEvents.stream().anyMatch(event-> event.getMessage().contains(thePartialMessage) && event.getLevel().isGreaterOrEqual(theLevel)),
			"Log Events should have at least one message with `FAILURE` in it.");
		return this;
	}
	public LogEventIterableAssert hasAtLeastOneFailureMessage() {
		isNotNull();

		matches(logEvents -> logEvents.stream()
				.map(ILoggingEvent::getMessage)
				.anyMatch(message -> message.contains("FAILURE")),
			"Log Events should have at least one message with `FAILURE` in it.");
		return this;
	}

	public LogEventIterableAssert hasAtLeastOneEventWithMessage(String thePartial) {
		isNotNull();

		matches(logEvents -> logEvents.stream()
				.map(ILoggingEvent::getMessage)
				.anyMatch(message -> message.contains(thePartial)),
			"Log Events should have at least one message with "+ thePartial + " in it.");
		return this;
	}

	@Override
	protected LogEventAssert toAssert(ILoggingEvent value, String description) {
		return new LogEventAssert(value).as(description);
	}

	@Override
	protected LogEventIterableAssert newAbstractIterableAssert(Iterable<? extends ILoggingEvent> iterable) {
		return new LogEventIterableAssert((List<ILoggingEvent>) iterable);
	}
}
