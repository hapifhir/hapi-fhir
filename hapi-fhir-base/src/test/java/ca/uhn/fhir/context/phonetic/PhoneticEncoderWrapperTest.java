package ca.uhn.fhir.context.phonetic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

public class PhoneticEncoderWrapperTest {

	private final Logger myLogger  = (Logger) LoggerFactory.getLogger(PhoneticEncoderWrapper.class);

	private ListAppender<ILoggingEvent> myListAppender;

	@BeforeEach
	public void init() {
		myListAppender = Mockito.mock(ListAppender.class);
		myLogger.addAppender(myListAppender);
	}

	@Test
	public void getEncoderWrapper_withNumberProvided_parsesOutCorrectValue() {
		int num = 5;
		PhoneticEncoderEnum enumVal = PhoneticEncoderEnum.DOUBLE_METAPHONE;
		String enumString = enumVal.name() + "(" + num + ")";

		// test
		PhoneticEncoderWrapper wrapper = PhoneticEncoderWrapper.getEncoderWrapper(enumString);

		Assertions.assertNotNull(wrapper);
		Assertions.assertEquals(enumVal.name(), wrapper.getPhoneticEncoder().name());
	}

	@Test
	public void getEncoderWrapper_withNoNumber_parsesOutCorrectValue() {
		// test
		for (PhoneticEncoderEnum enumVal : PhoneticEncoderEnum.values()) {
			PhoneticEncoderWrapper wrapper = PhoneticEncoderWrapper.getEncoderWrapper(enumVal.name());

			Assertions.assertNotNull(wrapper);
			Assertions.assertEquals(enumVal.name(), wrapper.getPhoneticEncoder().name());
		}
	}

	@Test
	public void getEncoderWrapper_withInvalidNumber_returnsNullAndLogs() {
		// setup
		myLogger.setLevel(Level.ERROR);
		String num = "A";

		// test
		PhoneticEncoderWrapper wrapper = PhoneticEncoderWrapper.getEncoderWrapper(
			PhoneticEncoderEnum.METAPHONE.name() + "(" + num + ")"
		);

		// verify
		Assertions.assertNull(wrapper);
		ArgumentCaptor<ILoggingEvent> loggingCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		Mockito.verify(myListAppender).doAppend(loggingCaptor.capture());
		Assertions.assertEquals(1, loggingCaptor.getAllValues().size());
		ILoggingEvent event = loggingCaptor.getValue();
		Assertions.assertEquals("Invalid encoder max character length: " + num,
			event.getMessage());
	}

	@Test
	public void parseFromString_unknownValue_returnsNull() {
		// setup
		myLogger.setLevel(Level.WARN);
		String theString = "Not a valid encoder value";

		PhoneticEncoderWrapper retEnum = PhoneticEncoderWrapper.getEncoderWrapper(theString);

		// verify
		Assertions.assertNull(retEnum);
		ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
		Mockito.verify(myListAppender)
			.doAppend(captor.capture());
		Assertions.assertEquals(1, captor.getAllValues().size());
		ILoggingEvent event = captor.getValue();
		Assertions.assertEquals("Invalid phonetic param string " + theString,
			event.getMessage());
	}
}
