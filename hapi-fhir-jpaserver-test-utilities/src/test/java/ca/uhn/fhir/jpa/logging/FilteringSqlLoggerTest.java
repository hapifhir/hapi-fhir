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
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FilteringSqlLoggerTest {
	@Spy
	private SqlLoggerFilteringUtil myFilteringUtil;
	private SqlStatementFilteringLogger myTestedLogger;
	private ch.qos.logback.classic.Logger myLogger;

	@BeforeEach
	void setUp() {
		myTestedLogger= new SqlStatementFilteringLogger(myFilteringUtil);

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		myLogger = loggerContext.getLogger("org.hibernate.SQL");

		ch.qos.logback.classic.Logger myTestedclassLogger = loggerContext.getLogger("ca.cdr.api.logging.SqlLoggerFilteringUtil");
		myTestedclassLogger.setLevel(Level.toLevel("trace"));
	}

	@Nested
	public class TestActivation {

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
		public LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension("org.hibernate.SQL");

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
			assertEquals(3, myLogCapture.getLogEvents().size() );

			addLineToFilterFile("sw: 1-must-log");
			waitForFiltersRefresh();
			myLogCapture.clearEvents();

			// log again
			myTestedLogger.logStatement("1-must-log-this-statement");
			myTestedLogger.logStatement("2-must-log-this-statement");
			myTestedLogger.logStatement("3-must-log-this-statement");
			assertThat(
				myLogCapture.getLogEvents().stream().map(Object::toString).toList(),
				hasItems(
					containsString("2-must-log-this-statement"),
					containsString("3-must-log-this-statement")));

			addLineToFilterFile("sw: 3-must-log");
			waitForFiltersRefresh();
			myLogCapture.clearEvents();

			// log again
			myTestedLogger.logStatement("1-must-log-this-statement");
			myTestedLogger.logStatement("2-must-log-this-statement");
			myTestedLogger.logStatement("3-must-log-this-statement");
			assertThat(
				myLogCapture.getLogEvents().stream().map(Object::toString).toList(),
				hasItems(containsString("2-must-log-this-statement")));
		}

		@Test
		void testDynamicFiltersUpdate_forEqualsWithFilters() throws IOException {
			// starts with empty filter list
			myTestedLogger.logStatement("1-must-log-this-statement");
			myTestedLogger.logStatement("2-must-log-this-statement");
			myTestedLogger.logStatement("3-must-log-this-statement");
			assertEquals(3, myLogCapture.getLogEvents().size() );

			addLineToFilterFile("eq: 1-must-log-this-statement");
			waitForFiltersRefresh();
			myLogCapture.clearEvents();

			// log again
			myTestedLogger.logStatement("1-must-log-this-statement");
			myTestedLogger.logStatement("2-must-log-this-statement");
			myTestedLogger.logStatement("3-must-log-this-statement");
			assertThat(
				myLogCapture.getLogEvents().stream().map(Object::toString).toList(),
				hasItems(
					containsString("2-must-log-this-statement"),
					containsString("3-must-log-this-statement")));

			addLineToFilterFile("sw: 3-must-log-this-statement");
			waitForFiltersRefresh();
			myLogCapture.clearEvents();

			// log again
			myTestedLogger.logStatement("1-must-log-this-statement");
			myTestedLogger.logStatement("2-must-log-this-statement");
			myTestedLogger.logStatement("3-must-log-this-statement");
			assertThat(
				myLogCapture.getLogEvents().stream().map(Object::toString).toList(),
				hasItems(containsString("2-must-log-this-statement")));
		}

	}

	private void waitForFiltersRefresh() {
		int beforeRefreshCount = SqlLoggerFilteringUtil.getRefreshCountForTests();
		await().atMost(Duration.of(SqlLoggerFilteringUtil.FILTER_UPDATE_INTERVAL_SECS + 1, ChronoUnit.SECONDS))
			.until(() -> SqlLoggerFilteringUtil.getRefreshCountForTests() > beforeRefreshCount);
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
