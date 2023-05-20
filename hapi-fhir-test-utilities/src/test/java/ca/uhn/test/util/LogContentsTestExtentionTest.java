package ca.uhn.test.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class LogContentsTestExtentionTest {
	private static final Logger ourLog = LoggerFactory.getLogger(LogContentsTestExtentionTest.class);
	@RegisterExtension
	LogContentsTestExtention myExtention = new LogContentsTestExtention((ch.qos.logback.classic.Logger) ourLog);

	@Test
	void testExtensionCapturesMessage() {
		ourLog.info("Hello hello");

		assertThat(myExtention.getLogOutput(), containsString("Hello hello"));
	}

	@Test
	void testExtensionCapturesStackTraceAndExceptionMessage() {
		ourLog.info("Hello hello", new RuntimeException("exception message"));

		assertThat(myExtention.getLogOutput(), containsString("exception message"));
		assertThat(myExtention.getLogOutput(), containsString("testExtensionCapturesStackTraceAndExceptionMessage"));
	}
}
