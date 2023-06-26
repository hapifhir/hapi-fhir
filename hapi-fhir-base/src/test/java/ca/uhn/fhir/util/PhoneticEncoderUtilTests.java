package ca.uhn.fhir.util;

import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PhoneticEncoderUtilTests {

	private final Logger myLogger  = (Logger) LoggerFactory.getLogger(PhoneticEncoderUtil.class);

	private ListAppender<ILoggingEvent> myListAppender;

	@BeforeEach
	public void init() {
		myListAppender = Mockito.mock(ListAppender.class);
		myLogger.addAppender(myListAppender);
	}

	@Test
	public void getEncoder_withNumberProvided_parsesOutCorrectValue() {
		int num = 5;
		PhoneticEncoderEnum enumVal = PhoneticEncoderEnum.DOUBLE_METAPHONE;
		String enumString = enumVal.name() + "(" + num + ")";

		// test
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(enumString);

		assertNotNull(encoder);
		assertEquals(enumVal.name(), encoder.name());
	}

	@Test
	public void getEncoder_withNoNumber_parsesOutCorrectValue() {
		// test
		for (PhoneticEncoderEnum enumVal : PhoneticEncoderEnum.values()) {
			IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(enumVal.name());

			assertNotNull(encoder);
			assertEquals(enumVal.name(), encoder.name());
		}
	}

	@Test
	public void getEncoder_withInvalidNumber_returnsNullAndLogs() {
		// setup
		myLogger.setLevel(Level.ERROR);
		String num = "A";

		// test
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(
			PhoneticEncoderEnum.METAPHONE.name() + "(" + num + ")"
		);

		// verify
		assertNull(encoder);
		ArgumentCaptor<ILoggingEvent> loggingCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		Mockito.verify(myListAppender).doAppend(loggingCaptor.capture());
		assertEquals(1, loggingCaptor.getAllValues().size());
		ILoggingEvent event = loggingCaptor.getValue();
		assertEquals("Invalid encoder max character length: " + num,
			event.getMessage());
	}

	@Test
	public void getEncoder_unknownValue_returnsNull() {
		// setup
		myLogger.setLevel(Level.WARN);
		String theString = "Not a valid encoder value";

		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(theString);

		// verify
		assertNull(encoder);
		ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
		Mockito.verify(myListAppender)
			.doAppend(captor.capture());
		assertEquals(1, captor.getAllValues().size());
		ILoggingEvent event = captor.getValue();
		assertEquals("Invalid phonetic param string " + theString,
			event.getMessage());
	}

	@Test
	public void getEncoder_emptyNumberValue_returnsNull() {
		myLogger.setLevel(Level.ERROR);

		// test
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(PhoneticEncoderEnum.METAPHONE.name() + "()");

		verifyOutcome_getEncoder_NumberParseFailure(encoder, "");
	}

	@Test
	public void getEncoder_invalidNumberValue_returnsNull() {
		myLogger.setLevel(Level.ERROR);

		// test
		String num = "-1";
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(PhoneticEncoderEnum.METAPHONE.name() + "(" + num + ")");

		verifyOutcome_getEncoder_NumberParseFailure(encoder, num);
	}

	@Test
	public void getEncoder_incorrectBrackets_returnsNull() {
		myLogger.setLevel(Level.ERROR);

		// test
		String num = "(";
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(PhoneticEncoderEnum.METAPHONE.name() + "(" + num + ")");

		verifyOutcome_getEncoder_NumberParseFailure(encoder, num);
	}

	@Test
	public void getEncoder_maxInt_returnsWrapper() {
		// test
		IPhoneticEncoder encoder = PhoneticEncoderUtil.getEncoder(
			PhoneticEncoderEnum.METAPHONE.name() + "(" + Integer.MAX_VALUE + ")"
		);

		assertNotNull(encoder);
		assertEquals(PhoneticEncoderEnum.METAPHONE.name(), encoder.name());
	}

	/**
	 * Verifies the outcome encoder when an invalid string was passed in.
	 */
	private void verifyOutcome_getEncoder_NumberParseFailure(IPhoneticEncoder theEncoder, String theNumberParam) {
		assertNull(theEncoder);
		ArgumentCaptor<ILoggingEvent> loggingCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		Mockito.verify(myListAppender).doAppend(loggingCaptor.capture());
		assertEquals(1, loggingCaptor.getAllValues().size());
		ILoggingEvent event = loggingCaptor.getValue();
		assertEquals("Invalid encoder max character length: " + theNumberParam,
			event.getMessage());
	}
}
