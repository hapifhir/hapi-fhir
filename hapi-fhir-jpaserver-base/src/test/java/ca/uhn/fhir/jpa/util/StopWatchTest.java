package ca.uhn.fhir.jpa.util;

import org.apache.commons.lang3.time.DateUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class StopWatchTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StopWatchTest.class);

	@After
	public void after() {
		StopWatch.setNowForUnitTestForUnitTest(null);
	}

	private double calculateThroughput(int theMinutesElapsed, int theNumOperations) {
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -theMinutesElapsed));
		double throughput = sw.getThroughput(theNumOperations, TimeUnit.MINUTES);
		ourLog.info("{} operations in {}ms = {} ops / second", theNumOperations, sw.getMillis(), throughput);
		return throughput;
	}

	@Test
	public void testEstimatedTimeRemaining() {
		StopWatch.setNowForUnitTestForUnitTest(777777777L);
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:00:00.600", sw.getEstimatedTimeRemaining(0.99, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:10:00", sw.getEstimatedTimeRemaining(0.1, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE + 100);
		assertEquals("00:10:01", sw.getEstimatedTimeRemaining(0.1, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:20:00", sw.getEstimatedTimeRemaining(0.05, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:40:00", sw.getEstimatedTimeRemaining(0.025, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("01:40:00", sw.getEstimatedTimeRemaining(0.01, 1.0));
	}

	@Test
	public void testFormatMillis() {
		assertEquals("00:00:01.000", StopWatch.formatMillis(DateUtils.MILLIS_PER_SECOND));
		assertEquals("00:01:00.000", StopWatch.formatMillis(DateUtils.MILLIS_PER_MINUTE));
		assertEquals("00:01:01", StopWatch.formatMillis(DateUtils.MILLIS_PER_MINUTE + DateUtils.MILLIS_PER_SECOND));
		assertEquals("01:00:00", StopWatch.formatMillis(DateUtils.MILLIS_PER_HOUR));
		assertEquals("1.0 day", StopWatch.formatMillis(DateUtils.MILLIS_PER_DAY));
		assertEquals("2.0 days", StopWatch.formatMillis(DateUtils.MILLIS_PER_DAY * 2));
		assertEquals("2.0 days", StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 2) + 1));
		assertEquals("2.4 days", StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 2) + (10 * DateUtils.MILLIS_PER_HOUR)));
		assertEquals("11 days", StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 11) + (10 * DateUtils.MILLIS_PER_HOUR)));
	}

	@Test
	public void testFormatThroughput60Ops4Min() {
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -4));
		String throughput = sw.formatThroughput(60, TimeUnit.MINUTES);
		ourLog.info("{} operations in {}ms = {} ops / second", 60, sw.getMillis(), throughput);
		assertThat(throughput, oneOf("14.9", "15.0", "15.1", "14,9", "15,0", "15,1"));
	}

	@Test
	public void testMillisPerOperation() {
		int minutes = 60;
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -minutes));
		int numOperations = 60;
		int millis = sw.getMillisPerOperation(numOperations);
		ourLog.info("{} operations in {}ms = {}ms / operation", numOperations, minutes * DateUtils.MILLIS_PER_MINUTE, millis);

		assertThat(millis, Matchers.lessThan(62000));
		assertThat(millis, Matchers.greaterThan(58000));
	}

	@Test
	public void testOperationThroughput30Ops1Min() {
		double throughput = calculateThroughput(1, 30);
		assertThat(throughput, greaterThan(29.0));
		assertThat(throughput, lessThan(31.0));
	}

	@Test
	public void testOperationThroughput60Ops1Min() {
		double throughput = calculateThroughput(1, 60);
		assertThat(throughput, greaterThan(59.0));
		assertThat(throughput, lessThan(61.0));
	}

	@Test
	public void testOperationThroughput60Ops4Min() {
		double throughput = calculateThroughput(4, 60);
		assertThat(throughput, greaterThan(14.0));
		assertThat(throughput, lessThan(16.0));
	}

	@Test
	public void testRestart() throws InterruptedException {
		StopWatch sw = new StopWatch();
		Thread.sleep(500);
		sw.restart();
		assertThat(sw.getMillis(), lessThan(100L));
	}

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
