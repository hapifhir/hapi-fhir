package ca.uhn.fhir.jpa.logging;

import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ca.uhn.fhir.jpa.logging.SqlLoggerFilteringUtil.FILTER_UPDATE_INTERVAL_SECS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class SqlLoggerFilteringUtilTest {
	@InjectMocks
	private SqlLoggerFilteringUtil myTestedUtil;
	@Spy
	private SqlLoggerFilteringUtil mySpiedUtil;

	@RegisterExtension
	public LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension("org.hibernate.SQL");

	private ch.qos.logback.classic.Logger myTestLogger;
	private ch.qos.logback.classic.Logger myHibernateLogger;

	@BeforeEach
	void setUp() {
		mySpiedUtil = spy(myTestedUtil);
		SqlLoggerFilteringUtil.setFilterUpdateIntervalSecs(1);

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		myTestLogger = loggerContext.getLogger(SqlLoggerFilteringUtil.class);
		myHibernateLogger = loggerContext.getLogger("org.hibernate.SQL");
		myHibernateLogger.setLevel(Level.INFO);
	}

	@Nested
	public class TestMustLog {

		@BeforeEach
		void setUp() throws IOException {
			lenient().doNothing().when(mySpiedUtil).refreshFilters(any());
			myHibernateLogger.setLevel(Level.DEBUG);
		}

		@Test
		void whenNoFilterMatches_mustReturnTrue() {
			boolean result = mySpiedUtil.allowLog("sql statement 1 with params a , BBB and ccccc");

			assertTrue(result);
		}

		@Test
		void whenStartsWithFilterMatches_mustReturnFalse() {
			setFilter("sw: sql statement 1");

			boolean result = mySpiedUtil.allowLog("sql statement 1 with params a , BBB and ccccc");

			assertFalse(result);
		}

		@Test
		void whenFragmentFilterMatches_mustReturnFalse() {
			setFilter("frag: with params a, BBB");

			boolean result = mySpiedUtil.allowLog("sql statement 1 with params a, BBB and ccccc");

			assertFalse(result);
		}
		private void setFilter(String theFilterDefinition) {
			mySpiedUtil.getSqlLoggerFilters().forEach(f -> f.evaluateFilterLine(theFilterDefinition));
		}
	}

	@Nested
	public class TestExecutorActivation {

		@AfterEach
		void tearDown() {
			// shuts down executor if it was initialized
			myHibernateLogger.setLevel(Level.INFO);
			waitForRefreshCycle();
		}

		@Test
		void testRefreshIsNotExecutedUntilMustLogIsCalled() {
			myHibernateLogger.setLevel(Level.INFO);

			assertExecutorState(false);
		}

		@Test
		void testRefreshIsExecutedWhenMustLogIsCalled() {
			myHibernateLogger.setLevel(Level.DEBUG);

			mySpiedUtil.allowLog("sql statement");

			assertExecutorState(true);
		}

		@Test
		void testRefreshIsStoppedWhenDebugLogIsStopped() {
			// refresh executor is stopped until mustLog is invoked

			assertExecutorState(false);

			// refresh executor starts once mustLog is called with logger in DEBUG mode
			myHibernateLogger.setLevel(Level.DEBUG);

			mySpiedUtil.allowLog("sql statement");
			assertExecutorState(true);

			// refresh executor stops once mustLog is called when logger DEBUG mode is reset

			// make logger INFO
			myTestLogger.info("Hibernate SQL logger level: INFO");
			myHibernateLogger.setLevel(Level.INFO);

			// wait for refresh cycle, which should stop executor
			waitForRefreshCycle();
			// validate it stopped refreshing
			assertExecutorState(false);

			// until log is back to DEBUG
			myTestLogger.info("Hibernate SQL logger level: DEBUG");
			myHibernateLogger.setLevel(Level.DEBUG);
			// executor not reactivated until debug log is called
			mySpiedUtil.allowLog("sql statement");
			waitForRefreshCycle();
			// validate started refreshing again
			assertExecutorState(true);

			// and so on...
		}

	}

	private void waitForRefreshCycle() {
		try {
			Thread.sleep(((long) FILTER_UPDATE_INTERVAL_SECS + 1) * 1_000);
		} catch (InterruptedException ignored) {
			// ignore
		}
	}


	private void assertExecutorState(boolean isActive) {
		int beforeRefreshCount = SqlLoggerFilteringUtil.getRefreshCountForTests();
		if (isActive) {
			await().atMost(Duration.of(FILTER_UPDATE_INTERVAL_SECS + 1, ChronoUnit.SECONDS))
				.until(() -> beforeRefreshCount < SqlLoggerFilteringUtil.getRefreshCountForTests());
		} else {
			waitForRefreshCycle();
			int newCount = SqlLoggerFilteringUtil.getRefreshCountForTests();
			assertEquals(beforeRefreshCount, newCount);
		}
	}


}
