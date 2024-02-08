package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SleepUtilTest {

	@Test
	public void testSleepAtLeast() {
		SleepUtil sleepUtil = new SleepUtil();
		long amountToSleepMs = 10;

		long start = System.currentTimeMillis();
		sleepUtil.sleepAtLeast(amountToSleepMs);
		long stop = System.currentTimeMillis();

		long actualSleepDurationMs = stop - start;
		assertThat(actualSleepDurationMs >= amountToSleepMs).isTrue();
	}

	@Test
	public void testZeroMs() {
		// 0 is a valid input
		SleepUtil sleepUtil = new SleepUtil();
		sleepUtil.sleepAtLeast(0);
	}

}
