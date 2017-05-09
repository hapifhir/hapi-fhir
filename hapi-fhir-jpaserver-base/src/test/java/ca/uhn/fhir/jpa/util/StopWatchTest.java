package ca.uhn.fhir.jpa.util;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

public class StopWatchTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StopWatchTest.class);

	@Test
	public void testStopwatch() throws Exception {
		StopWatch sw = new StopWatch();

		Thread.sleep(100);

		assertThat(sw.getMillis(new Date()), greaterThan(10L));
		assertThat(sw.getMillis(), greaterThan(10L));
		assertThat(sw.getStartedDate().getTime(), lessThan(System.currentTimeMillis()));
	}

	@Test
	public void testStopwatchWithDate() throws Exception {
		StopWatch sw = new StopWatch(new Date());

		Thread.sleep(100);

		assertThat(sw.getMillis(new Date()), greaterThan(10L));
		assertThat(sw.getMillis(), greaterThan(10L));
		assertThat(sw.getStartedDate().getTime(), lessThan(System.currentTimeMillis()));
	}

	@Test
	public void testToString() throws Exception {
		StopWatch sw = new StopWatch();

		Thread.sleep(100);

		String string = sw.toString();
		ourLog.info(string);
		assertThat(string, startsWith("00:00"));
	}

}
