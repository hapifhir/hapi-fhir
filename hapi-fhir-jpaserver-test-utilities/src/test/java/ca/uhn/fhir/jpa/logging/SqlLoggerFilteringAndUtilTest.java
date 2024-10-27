package ca.uhn.fhir.jpa.logging;

import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
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
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static ca.uhn.fhir.jpa.logging.SqlLoggerFilteringUtil.FILTER_FILE_PATH;
import static ca.uhn.fhir.jpa.logging.SqlLoggerFilteringUtil.FILTER_UPDATE_INTERVAL_SECS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * This test includes tests for two classes SqlStatementFilteringLogger and  SqlLoggerFilteringUtil, because the later
 * uses threading in a way that would make these tests would collide with each other if run in parallel, as it could be
 * the case if set in different classes. They are separated by top level nested class names.
 */
@ExtendWith(MockitoExtension.class)
public class SqlLoggerFilteringAndUtilTest {

	@Nested
	public class SqlStatementFilteringLoggerTests {
		@Spy
		private SqlLoggerFilteringUtil myFilteringUtil;
		private SqlStatementFilteringLogger myTestedLogger;
		private ch.qos.logback.classic.Logger myLogger;

		@BeforeEach
		void setUp() {
			myTestedLogger = new SqlStatementFilteringLogger(myFilteringUtil);

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			myLogger = loggerContext.getLogger("org.hibernate.SQL");

			ch.qos.logback.classic.Logger myTestedclassLogger = loggerContext.getLogger("ca.cdr.api.logging.SqlLoggerFilteringUtil");
			myTestedclassLogger.setLevel(Level.toLevel("trace"));
		}

		@Nested
		public class ActivationTests {

			@Test
			void doesNotInvokeMustLogWhenLoggerNotDebug() {
				myLogger.setLevel(Level.toLevel("info"));

				myTestedLogger.logStatement("select * from Patients");

				verify(myFilteringUtil, never()).allowLog(any());
			}

			@Test
			void invokesMustLogWhenLoggerDebug() {
				myLogger.setLevel(Level.toLevel("debug"));

				myTestedLogger.logStatement("select * from Patients");

				verify(myFilteringUtil).allowLog("select * from Patients");
			}
		}


		@Nested
		public class FileFiltersTests {

			@RegisterExtension
			public LogbackTestExtension myLogCapture = new LogbackTestExtension("org.hibernate.SQL");

			@BeforeEach
			void setUp() {
				myLogger.setLevel(Level.toLevel("debug"));
				SqlLoggerFilteringUtil.setFilterUpdateIntervalSecs(1);
			}

			@AfterEach
			void tearDown() throws IOException {
				clearFilterFile();
			}

			@Test
			void testDynamicFiltersUpdate_forStartsWithFilters() throws IOException {
				// starts with empty filter list
				myTestedLogger.logStatement("1-must-log-this-statement");
				myTestedLogger.logStatement("2-must-log-this-statement");
				myTestedLogger.logStatement("3-must-log-this-statement");
				assertEquals(3, myLogCapture.getLogEvents().size());

				addLineToFilterFile("sw: 1-must-log");
				waitForFiltersRefresh();
				myLogCapture.clearEvents();

				// log again
				myTestedLogger.logStatement("1-must-log-this-statement");
				myTestedLogger.logStatement("2-must-log-this-statement");
				myTestedLogger.logStatement("3-must-log-this-statement");
				assertThat(myLogCapture.getLogEvents().stream().map(ILoggingEvent::getMessage).toList()).contains("2-must-log-this-statement", "3-must-log-this-statement");

				addLineToFilterFile("sw: 3-must-log");
				waitForFiltersRefresh();
				myLogCapture.clearEvents();

				// log again
				myTestedLogger.logStatement("1-must-log-this-statement");
				myTestedLogger.logStatement("2-must-log-this-statement");
				myTestedLogger.logStatement("3-must-log-this-statement");
				assertThat(myLogCapture.getLogEvents().stream().map(ILoggingEvent::getMessage).toList()).contains("2-must-log-this-statement");
			}

			@Test
			void testDynamicFiltersUpdate_forEqualsWithFilters() throws IOException {
				// starts with empty filter list
				myTestedLogger.logStatement("1-must-log-this-statement");
				myTestedLogger.logStatement("2-must-log-this-statement");
				myTestedLogger.logStatement("3-must-log-this-statement");
				assertEquals(3, myLogCapture.getLogEvents().size());

				addLineToFilterFile("eq: 1-must-log-this-statement");
				waitForFiltersRefresh();
				myLogCapture.clearEvents();

				// log again
				myTestedLogger.logStatement("1-must-log-this-statement");
				myTestedLogger.logStatement("2-must-log-this-statement");
				myTestedLogger.logStatement("3-must-log-this-statement");
				assertThat(myLogCapture.getLogEvents().stream().map(ILoggingEvent::getMessage).toList()).contains("2-must-log-this-statement", "3-must-log-this-statement");

				addLineToFilterFile("sw: 3-must-log-this-statement");
				waitForFiltersRefresh();
				myLogCapture.clearEvents();

				// log again
				myTestedLogger.logStatement("1-must-log-this-statement");
				myTestedLogger.logStatement("2-must-log-this-statement");
				myTestedLogger.logStatement("3-must-log-this-statement");
				assertThat(myLogCapture.getLogEvents().stream().map(ILoggingEvent::getMessage).toList()).contains("2-must-log-this-statement");
			}

		}

		private void waitForFiltersRefresh() {
			int beforeRefreshCount = SqlLoggerFilteringUtil.getRefreshCountForTests();
			await().atMost(Duration.of(SqlLoggerFilteringUtil.FILTER_UPDATE_INTERVAL_SECS + 1, ChronoUnit.SECONDS)).until(() -> SqlLoggerFilteringUtil.getRefreshCountForTests() > beforeRefreshCount);
		}

		private void addLineToFilterFile(String theFilterLine) throws IOException {
			File resource = new ClassPathResource(FILTER_FILE_PATH).getFile();
			assertNotNull(resource);
			Files.write(resource.toPath(), (theFilterLine + "\n").getBytes(), StandardOpenOption.APPEND);
		}

		private void clearFilterFile() throws IOException {
			File resource = new ClassPathResource(FILTER_FILE_PATH).getFile();
			assertNotNull(resource);
			Files.write(resource.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
		}
	}

	@Nested
	public class SqlLoggerFilteringUtilTests {
		@InjectMocks
		private SqlLoggerFilteringUtil myTestedUtil;
		@Spy
		private SqlLoggerFilteringUtil mySpiedUtil;

		@RegisterExtension
		public LogbackTestExtension myLogCapture = new LogbackTestExtension("org.hibernate.SQL");

		private ch.qos.logback.classic.Logger myHibernateLogger;

		@BeforeEach
		void setUp() {
			mySpiedUtil = spy(myTestedUtil);
			SqlLoggerFilteringUtil.setFilterUpdateIntervalSecs(1);

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			myHibernateLogger = loggerContext.getLogger(SqlLoggerFilteringUtil.class);
			myHibernateLogger = loggerContext.getLogger("org.hibernate.SQL");
		}

		@Nested
		public class MustLogTests {

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
		public class ExecutorActivationTests {

			@AfterEach
			void tearDown() {
				// shuts down executor if it was initialized
				myHibernateLogger.setLevel(Level.INFO);
				waitForRefreshCycle();
				assertExecutorState(false);
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
				myHibernateLogger.setLevel(Level.INFO);

				// wait for refresh cycle, which should stop executor
				waitForRefreshCycle();
				// validate it stopped refreshing
				assertExecutorState(false);

				// until log is back to DEBUG
				myHibernateLogger.setLevel(Level.DEBUG);
				// executor not reactivated until debug log is called
				mySpiedUtil.allowLog("sql statement");
				waitForRefreshCycle();
				// validate started refreshing again
				assertExecutorState(true);
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
				await().atMost(Duration.of(FILTER_UPDATE_INTERVAL_SECS + 1, ChronoUnit.SECONDS)).until(() -> beforeRefreshCount < SqlLoggerFilteringUtil.getRefreshCountForTests());
			} else {
				waitForRefreshCycle();
				int newCount = SqlLoggerFilteringUtil.getRefreshCountForTests();
				assertEquals(beforeRefreshCount, newCount);
			}
		}

	}


}
