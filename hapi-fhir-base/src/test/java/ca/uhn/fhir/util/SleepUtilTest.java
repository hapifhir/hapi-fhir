package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
		assertTrue(actualSleepDurationMs >= amountToSleepMs);
	}

	@Test
	public void testZeroMs() {
		// 0 is a valid input
		SleepUtil sleepUtil = new SleepUtil();
		sleepUtil.sleepAtLeast(0);
	}

}
