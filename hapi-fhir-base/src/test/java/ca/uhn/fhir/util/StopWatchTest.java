package ca.uhn.fhir.util;

import org.apache.commons.lang3.time.DateUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StopWatchTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StopWatchTest.class);

	@AfterEach
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
	public void testEstimatedTimeRemainingOutOfOne() {
		StopWatch.setNowForUnitTestForUnitTest(777777777L);
		StopWatch sw = new StopWatch();

		// Less than half
		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:09:00", sw.getEstimatedTimeRemaining(0.1, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:09:00", sw.getEstimatedTimeRemaining(1, 10));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE + 100);
		assertEquals("00:09:00", sw.getEstimatedTimeRemaining(0.1, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:19:00", sw.getEstimatedTimeRemaining(0.05, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:39:00", sw.getEstimatedTimeRemaining(0.025, 1.0));

		// More than half
		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:01:00.000", sw.getEstimatedTimeRemaining(0.5, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:00:59.760", sw.getEstimatedTimeRemaining(0.501, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("00:00:40.000", sw.getEstimatedTimeRemaining(0.6, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("6666ms", sw.getEstimatedTimeRemaining(0.9, 1.0));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertEquals("60ms", sw.getEstimatedTimeRemaining(0.999, 1.0));

	}

	@Test
	public void testEstimatedTimeRemainingOutOfOneHundred() {
		StopWatch.setNowForUnitTestForUnitTest(777777777L);
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (10 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("01:30:00", sw.getEstimatedTimeRemaining(10, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (DateUtils.MILLIS_PER_MINUTE));
		assertEquals("00:04:00", sw.getEstimatedTimeRemaining(20, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (30 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("01:10:00", sw.getEstimatedTimeRemaining(30, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (40 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("01:00:00", sw.getEstimatedTimeRemaining(40, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (50 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("00:50:00", sw.getEstimatedTimeRemaining(50, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (60 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("00:40:00", sw.getEstimatedTimeRemaining(60, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (60 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("00:00:36.363", sw.getEstimatedTimeRemaining(99, 100));

		StopWatch.setNowForUnitTestForUnitTest(777777777L + (60 * DateUtils.MILLIS_PER_MINUTE));
		assertEquals("360ms", sw.getEstimatedTimeRemaining(99.99, 100));

	}

	@Test
	public void testFormatMillis() {
		assertEquals("0.134ms", StopWatch.formatMillis(0.1339d).replace(',', '.'));
		assertEquals("1000ms", StopWatch.formatMillis(DateUtils.MILLIS_PER_SECOND));
		assertEquals("00:01:00.000", StopWatch.formatMillis(DateUtils.MILLIS_PER_MINUTE));
		assertEquals("00:01:01", StopWatch.formatMillis(DateUtils.MILLIS_PER_MINUTE + DateUtils.MILLIS_PER_SECOND));
		assertEquals("01:00:00", StopWatch.formatMillis(DateUtils.MILLIS_PER_HOUR));
		assertEquals("1.0 day", StopWatch.formatMillis(DateUtils.MILLIS_PER_DAY).replace(',', '.'));
		assertEquals("2.0 days", StopWatch.formatMillis(DateUtils.MILLIS_PER_DAY * 2).replace(',', '.'));
		assertEquals("2.0 days", StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 2) + 1).replace(',', '.'));
		assertEquals("2.4 days", StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 2) + (10 * DateUtils.MILLIS_PER_HOUR)).replace(',', '.'));
		assertEquals("11 days", StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 11) + (10 * DateUtils.MILLIS_PER_HOUR)));
	}

	@Test
	public void testFormatTaskDurations() {
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTestForUnitTest(1000L);
		sw.startTask("TASK1");

		StopWatch.setNowForUnitTestForUnitTest(1500L);
		sw.startTask("TASK2");

		StopWatch.setNowForUnitTestForUnitTest(1600L);
		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertEquals("TASK1: 500ms\nTASK2: 100ms", taskDurations);
	}

	@Test
	public void testFormatTaskDurationsDelayBetweenTasks() {
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTestForUnitTest(1000L);
		sw.startTask("TASK1");

		StopWatch.setNowForUnitTestForUnitTest(1500L);
		sw.endCurrentTask();

		StopWatch.setNowForUnitTestForUnitTest(2000L);
		sw.startTask("TASK2");

		StopWatch.setNowForUnitTestForUnitTest(2100L);
		sw.endCurrentTask();

		StopWatch.setNowForUnitTestForUnitTest(2200L);
		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertEquals("TASK1: 500ms\n" +
			"Between: 500ms\n" +
			"TASK2: 100ms\n" +
			"After last task: 100ms", taskDurations);
	}

	@Test
	public void testFormatTaskDurationsLongDelayBeforeStart() {
		StopWatch sw = new StopWatch(0);

		StopWatch.setNowForUnitTestForUnitTest(1000L);
		sw.startTask("TASK1");

		StopWatch.setNowForUnitTestForUnitTest(1500L);
		sw.startTask("TASK2");

		StopWatch.setNowForUnitTestForUnitTest(1600L);
		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertEquals("Before first task: 1000ms\nTASK1: 500ms\nTASK2: 100ms", taskDurations);
	}

	@Test
	public void testFormatTaskDurationsNoTasks() {
		StopWatch sw = new StopWatch(0);

		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertEquals("No tasks", taskDurations);
	}

	@Test
	public void testFormatThroughput60Ops4Min() {
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -4));
		String throughput = sw.formatThroughput(60, TimeUnit.MINUTES).replace(',', '.');
		ourLog.info("{} operations in {}ms = {} ops / second", 60, sw.getMillis(), throughput);
		assertThat(throughput, oneOf("14.9", "15.0", "15.1", "14,9", "15,0", "15,1"));
	}

	@Test
	public void testMillisPerOperation() {
		int minutes = 60;
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -minutes));
		int numOperations = 60;
		long millis = sw.getMillisPerOperation(numOperations);
		ourLog.info("{} operations in {}ms = {}ms / operation", numOperations, minutes * DateUtils.MILLIS_PER_MINUTE, millis);

		assertThat(millis, Matchers.lessThan(62000L));
		assertThat(millis, Matchers.greaterThan(58000L));
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
		assertThat(string, matchesPattern("^[0-9]{3,4}ms$"));
	}


	@Test
	public void testAppendRightAlignedNumber() {
		StringBuilder b= new StringBuilder();

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 0, 100);
		assertEquals("PFX100", b.toString());

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 1, 100);
		assertEquals("PFX100", b.toString());

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 2, 100);
		assertEquals("PFX100", b.toString());

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 3, 100);
		assertEquals("PFX100", b.toString());

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 4, 100);
		assertEquals("PFX0100", b.toString());

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 10, 100);
		assertEquals("PFX0000000100", b.toString());
	}

}
