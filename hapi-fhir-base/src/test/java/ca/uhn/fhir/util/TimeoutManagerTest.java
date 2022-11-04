package ca.uhn.fhir.util;

import ca.uhn.fhir.system.HapiSystemProperties;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TimeoutManagerTest {
	public static final String TEST_SERVICE_NAME = "TEST TIMEOUT";
	final Duration myWarningTimeout = Duration.ofDays(1);
	final Duration myErrorTimeout = Duration.ofDays(10);
	@Mock
	private Appender<ILoggingEvent> myAppender;
	@Captor
	ArgumentCaptor<ILoggingEvent> myLoggingEvent;

	TimeoutManager mySvc = new TimeoutManager(TEST_SERVICE_NAME, myWarningTimeout, myErrorTimeout);

	@AfterAll
	public static void resetStopwatch() {
		StopWatch.setNowForUnitTest(null);
	}

	@BeforeEach
	void before() {
		ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(TimeoutManager.class);

		logger.addAppender(myAppender);
	}

	@AfterEach
	void after() {
		ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(TimeoutManager.class);

		logger.detachAppender(myAppender);
		verifyNoMoreInteractions(myAppender);
		System.clearProperty("unit_test");
	}

	@Test
	public void checkTimeout_noThreadholdHit_noLogging() {
		// execute
		assertFalse(mySvc.checkTimeout());
		// verify
		verifyNoInteractions(myAppender);
	}

	@Test
	public void checkTimeout_warningThreadholdHit_warningLogged() {
		// setup
		mySvc.addTimeForUnitTest(Duration.ofDays(2));
		// execute
		assertTrue(mySvc.checkTimeout());
		// verify
		verify(myAppender, times(1)).doAppend(myLoggingEvent.capture());
		ILoggingEvent event = myLoggingEvent.getValue();
		assertEquals(Level.WARN, event.getLevel());
		assertEquals(TEST_SERVICE_NAME + " has run for 2.0 days", event.getFormattedMessage());
	}

	@Test
	public void checkTimeout_errorThreadholdHit_errorLogged() {
		// setup
		mySvc.addTimeForUnitTest(Duration.ofDays(20));
		// execute
		mySvc.checkTimeout();
		// verify
		verify(myAppender, times(2)).doAppend(myLoggingEvent.capture());

		ILoggingEvent event1 = myLoggingEvent.getAllValues().get(0);
		assertEquals(Level.WARN, event1.getLevel());
		assertEquals(TEST_SERVICE_NAME + " has run for 20 days", event1.getFormattedMessage());

		ILoggingEvent event2 = myLoggingEvent.getAllValues().get(1);
		assertEquals(Level.ERROR, event2.getLevel());
		assertEquals(TEST_SERVICE_NAME + " has run for 20 days", event2.getFormattedMessage());
	}


	@Test
	public void checkTimeout_errorThreadholdHitInUnitTest_throwsException() {
		// setup
		HapiSystemProperties.enableUnitTestMode();
		mySvc.addTimeForUnitTest(Duration.ofDays(20));
		// execute
		try {
			mySvc.checkTimeout();
			fail();
		} catch (TimeoutException e) {
			assertEquals("HAPI-2133: TEST TIMEOUT timed out after running for 20 days", e.getMessage());
		}
		verify(myAppender, times(1)).doAppend(myLoggingEvent.capture());

		verify(myAppender, times(1)).doAppend(myLoggingEvent.capture());
		ILoggingEvent event = myLoggingEvent.getValue();
		assertEquals(Level.WARN, event.getLevel());
		assertEquals(TEST_SERVICE_NAME + " has run for 20 days", event.getFormattedMessage());
	}

}
