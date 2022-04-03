package ca.uhn.fhir.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.util.MessageSupplier.msg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageSupplierTest {

	private static final Logger ourLog = LoggerFactory.getLogger(MessageSupplierTest.class);
	private Appender myMockAppender;
	private ch.qos.logback.classic.Logger myLoggerRoot;

	@AfterEach
	public void after() {
		myLoggerRoot.detachAppender(myMockAppender);
	}

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void before() {

		/*
		 * This is a bit funky, but it's useful for verifying that the headers actually get logged
		 */
		myMockAppender = mock(Appender.class);
		when(myMockAppender.getName()).thenReturn("MOCK");

		Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

		myLoggerRoot = (ch.qos.logback.classic.Logger) logger;
		myLoggerRoot.addAppender(myMockAppender);
	}


	@Test
	public void testLog() {

		ourLog.info("Hello: {}", msg(() -> "Goodbye"));

		verify(myMockAppender, times(1)).doAppend(argThat((ArgumentMatcher<ILoggingEvent>) argument -> {
			String formattedMessage = argument.getFormattedMessage();
			System.out.flush();
			System.out.println("** Got Message: " + formattedMessage);
			System.out.flush();
			return formattedMessage.equals("Hello: Goodbye");
		}));


	}


}
