package ca.uhn.fhir.jpa.batch.reader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BatchDateThresholdUpdaterTest {
	static Date LATE_DATE = new Date();
	static Date EARLY_DATE = new Date(LATE_DATE.getTime() - 1000);
	static Long PID1 = 1L;
	static Long PID2 = 2L;
	static Long PID3 = 3L;
	BatchDateThresholdUpdater mySvc = new BatchDateThresholdUpdater();

	@Test
	public void testEmptyList() {
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, Collections.emptySet(), Collections.emptyList());
		assertEquals(LATE_DATE, newThreshold);
	}

	@Test
	public void oneItem() {
		mySvc.setDateFromPid(pid -> LATE_DATE);
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Collections.singletonList(PID1));
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, contains(PID1));
	}

	@Test
	public void twoItemsSameDate() {
		mySvc.setDateFromPid(pid -> LATE_DATE);
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2));
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, contains(PID1, PID2));
	}

	@Test
	public void twoItemsDiffDate() {
		List<Date> dates = Arrays.asList(EARLY_DATE, LATE_DATE);
		mySvc.setDateFromPid(pid -> dates.get(pid.intValue() - 1));
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2));
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, contains(PID2));
	}

	@Test
	public void threeItemsSameDate() {
		mySvc.setDateFromPid(pid -> LATE_DATE);
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2, PID3));
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, contains(PID1, PID2, PID3));
	}

	@Test
	public void threeItemsDifferentEEL() {
		List<Date> dates = Arrays.asList(EARLY_DATE, EARLY_DATE, LATE_DATE);
		mySvc.setDateFromPid(pid -> dates.get(pid.intValue() - 1));
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2, PID3));
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, contains(PID3));
	}

	@Test
	public void threeItemsDifferentELL() {
		List<Date> dates = Arrays.asList(EARLY_DATE, LATE_DATE, LATE_DATE);
		mySvc.setDateFromPid(pid -> dates.get(pid.intValue() - 1));
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2, PID3));
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, contains(PID2, PID3));
	}


	@Test
	public void threeItemsDifferentLEE() {
		List<Date> dates = Arrays.asList(LATE_DATE, EARLY_DATE, EARLY_DATE);
		mySvc.setDateFromPid(pid -> dates.get(pid.intValue() - 1));
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2, PID3));
		assertEquals(EARLY_DATE, newThreshold);
		assertThat(seenPids, contains(PID2, PID3));
	}

	@Test
	public void threeItemsDifferentLLE() {
		List<Date> dates = Arrays.asList(LATE_DATE, LATE_DATE, EARLY_DATE);
		mySvc.setDateFromPid(pid -> dates.get(pid.intValue() - 1));
		Set<Long> seenPids = new HashSet<>();
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, Arrays.asList(PID1, PID2, PID3));
		assertEquals(EARLY_DATE, newThreshold);
		assertThat(seenPids, contains(PID3));
	}

	@Test
	public void oneHundredItemsSameDate() {
		mySvc.setDateFromPid(pid -> LATE_DATE);
		Set<Long> seenPids = new HashSet<>();
		List<Long> bigList = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			bigList.add((long) i);
		}
		Date newThreshold = mySvc.updateThresholdAndCache(LATE_DATE, seenPids, bigList);
		assertEquals(LATE_DATE, newThreshold);
		assertThat(seenPids, hasSize(100));
	}
}
