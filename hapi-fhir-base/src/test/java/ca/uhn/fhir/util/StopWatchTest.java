package ca.uhn.fhir.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class StopWatchTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StopWatchTest.class);

	@AfterEach
	public void after() {
		StopWatch.setNowForUnitTest(null);
	}

	private double calculateThroughput(int theMinutesElapsed, int theNumOperations) {
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -theMinutesElapsed));
		double throughput = sw.getThroughput(theNumOperations, TimeUnit.MINUTES);
		ourLog.info("{} operations in {}ms = {} ops / second", theNumOperations, sw.getMillis(), throughput);
		return throughput;
	}

	@Test
	public void testEstimatedTimeRemainingOutOfOne() {
		StopWatch.setNowForUnitTest(777777777L);
		StopWatch sw = new StopWatch();

		// Less than half
		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.1, 1.0)).isEqualTo("00:09:00");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(1, 10)).isEqualTo("00:09:00");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE + 100);
		assertThat(sw.getEstimatedTimeRemaining(0.1, 1.0)).isEqualTo("00:09:00");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.05, 1.0)).isEqualTo("00:19:00");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.025, 1.0)).isEqualTo("00:39:00");

		// More than half
		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.5, 1.0)).isEqualTo("00:01:00.000");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.501, 1.0)).isEqualTo("00:00:59.760");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.6, 1.0)).isEqualTo("00:00:40.000");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.9, 1.0)).isEqualTo("6666ms");

		StopWatch.setNowForUnitTest(777777777L + DateUtils.MILLIS_PER_MINUTE);
		assertThat(sw.getEstimatedTimeRemaining(0.999, 1.0)).isEqualTo("60ms");

	}

	@Test
	public void testEstimatedTimeRemainingOutOfOneHundred() {
		StopWatch.setNowForUnitTest(777777777L);
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTest(777777777L + (10 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(10, 100)).isEqualTo("01:30:00");

		StopWatch.setNowForUnitTest(777777777L + (DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(20, 100)).isEqualTo("00:04:00");

		StopWatch.setNowForUnitTest(777777777L + (30 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(30, 100)).isEqualTo("01:10:00");

		StopWatch.setNowForUnitTest(777777777L + (40 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(40, 100)).isEqualTo("01:00:00");

		StopWatch.setNowForUnitTest(777777777L + (50 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(50, 100)).isEqualTo("00:50:00");

		StopWatch.setNowForUnitTest(777777777L + (60 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(60, 100)).isEqualTo("00:40:00");

		StopWatch.setNowForUnitTest(777777777L + (60 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(99, 100)).isEqualTo("00:00:36.363");

		StopWatch.setNowForUnitTest(777777777L + (60 * DateUtils.MILLIS_PER_MINUTE));
		assertThat(sw.getEstimatedTimeRemaining(99.99, 100)).isEqualTo("360ms");

	}

	@Test
	public void testFormatMillis() {
		assertThat(StopWatch.formatMillis(0.1339d).replace(',', '.')).isEqualTo("0.134ms");
		assertThat(StopWatch.formatMillis(DateUtils.MILLIS_PER_SECOND)).isEqualTo("1000ms");
		assertThat(StopWatch.formatMillis(DateUtils.MILLIS_PER_MINUTE)).isEqualTo("00:01:00.000");
		assertThat(StopWatch.formatMillis(DateUtils.MILLIS_PER_MINUTE + DateUtils.MILLIS_PER_SECOND)).isEqualTo("00:01:01");
		assertThat(StopWatch.formatMillis(DateUtils.MILLIS_PER_HOUR)).isEqualTo("01:00:00");
		assertThat(StopWatch.formatMillis(DateUtils.MILLIS_PER_DAY).replace(',', '.')).isEqualTo("1.0 day");
		assertThat(StopWatch.formatMillis(DateUtils.MILLIS_PER_DAY * 2).replace(',', '.')).isEqualTo("2.0 days");
		assertThat(StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 2) + 1).replace(',', '.')).isEqualTo("2.0 days");
		assertThat(StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 2) + (10 * DateUtils.MILLIS_PER_HOUR)).replace(',', '.')).isEqualTo("2.4 days");
		assertThat(StopWatch.formatMillis((DateUtils.MILLIS_PER_DAY * 11) + (10 * DateUtils.MILLIS_PER_HOUR))).isEqualTo("11 days");
	}

	@Test
	public void testFormatTaskDurations() {
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTest(1000L);
		sw.startTask("TASK1");

		StopWatch.setNowForUnitTest(1500L);
		sw.startTask("TASK2");

		StopWatch.setNowForUnitTest(1600L);
		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertThat(taskDurations).isEqualTo("TASK1: 500ms\nTASK2: 100ms");
	}

	@Test
	public void testFormatTaskDurationsDelayBetweenTasks() {
		StopWatch sw = new StopWatch();

		StopWatch.setNowForUnitTest(1000L);
		sw.startTask("TASK1");

		StopWatch.setNowForUnitTest(1500L);
		sw.endCurrentTask();

		StopWatch.setNowForUnitTest(2000L);
		sw.startTask("TASK2");

		StopWatch.setNowForUnitTest(2100L);
		sw.endCurrentTask();

		StopWatch.setNowForUnitTest(2200L);
		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertThat(taskDurations).isEqualTo("TASK1: 500ms\n" +
				"Between: 500ms\n" +
				"TASK2: 100ms\n" +
				"After last task: 100ms");
	}

	@Test
	public void testFormatTaskDurationsLongDelayBeforeStart() {
		StopWatch sw = new StopWatch(0);

		StopWatch.setNowForUnitTest(1000L);
		sw.startTask("TASK1");

		StopWatch.setNowForUnitTest(1500L);
		sw.startTask("TASK2");

		StopWatch.setNowForUnitTest(1600L);
		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertThat(taskDurations).isEqualTo("Before first task: 1000ms\nTASK1: 500ms\nTASK2: 100ms");
	}

	@Test
	public void testFormatTaskDurationsNoTasks() {
		StopWatch sw = new StopWatch(0);

		String taskDurations = sw.formatTaskDurations();
		ourLog.info(taskDurations);
		assertThat(taskDurations).isEqualTo("No tasks");
	}

	@Test
	public void testFormatThroughput60Ops4Min() {
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -4));
		String throughput = sw.formatThroughput(60, TimeUnit.MINUTES).replace(',', '.');
		ourLog.info("{} operations in {}ms = {} ops / second", 60, sw.getMillis(), throughput);
		assertThat(throughput).isIn("14.9", "15.0", "15.1", "14,9", "15,0", "15,1");
	}

	@Test
	public void testMillisPerOperation() {
		int minutes = 60;
		StopWatch sw = new StopWatch(DateUtils.addMinutes(new Date(), -minutes));
		int numOperations = 60;
		long millis = sw.getMillisPerOperation(numOperations);
		ourLog.info("{} operations in {}ms = {}ms / operation", numOperations, minutes * DateUtils.MILLIS_PER_MINUTE, millis);

		assertThat(millis).isLessThan(62000L);
		assertThat(millis).isGreaterThan(58000L);
	}

	@Test
	public void testOperationThroughput30Ops1Min() {
		double throughput = calculateThroughput(1, 30);
		assertThat(throughput).isGreaterThan(29.0);
		assertThat(throughput).isLessThan(31.0);
	}

	@Test
	public void testOperationThroughput60Ops1Min() {
		double throughput = calculateThroughput(1, 60);
		assertThat(throughput).isGreaterThan(59.0);
		assertThat(throughput).isLessThan(61.0);
	}

	@Test
	public void testOperationThroughput60Ops4Min() {
		double throughput = calculateThroughput(4, 60);
		assertThat(throughput).isGreaterThan(14.0);
		assertThat(throughput).isLessThan(16.0);
	}

	@Test
	public void testRestart() throws InterruptedException {
		StopWatch sw = new StopWatch();
		Thread.sleep(500);
		sw.restart();
		assertThat(sw.getMillis()).isLessThan(100L);
	}

	@Test
	public void testStopwatch() throws Exception {
		StopWatch sw = new StopWatch();

		Thread.sleep(100);

		assertThat(sw.getMillis(new Date())).isGreaterThan(10L);
		assertThat(sw.getMillis()).isGreaterThan(10L);
		assertThat(sw.getStartedDate().getTime()).isLessThan(System.currentTimeMillis());
	}

	@Test
	public void testStopwatchWithDate() throws Exception {
		StopWatch sw = new StopWatch(new Date());

		Thread.sleep(100);

		assertThat(sw.getMillis(new Date())).isGreaterThan(10L);
		assertThat(sw.getMillis()).isGreaterThan(10L);
		assertThat(sw.getStartedDate().getTime()).isLessThan(System.currentTimeMillis());
	}

	@Test
	public void testToString() throws Exception {
		StopWatch sw = new StopWatch();

		Thread.sleep(100);

		String string = sw.toString();
		ourLog.info(string);
		assertThat(string).matches("^[0-9]{3,4}ms$");
	}


	@Test
	public void testAppendRightAlignedNumber() {
		StringBuilder b= new StringBuilder();

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 0, 100);
		assertThat(b.toString()).isEqualTo("PFX100");

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 1, 100);
		assertThat(b.toString()).isEqualTo("PFX100");

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 2, 100);
		assertThat(b.toString()).isEqualTo("PFX100");

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 3, 100);
		assertThat(b.toString()).isEqualTo("PFX100");

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 4, 100);
		assertThat(b.toString()).isEqualTo("PFX0100");

		b.setLength(0);
		StopWatch.appendRightAlignedNumber(b, "PFX", 10, 100);
		assertThat(b.toString()).isEqualTo("PFX0000000100");
	}

}
