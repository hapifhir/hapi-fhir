package ca.uhn.test.concurrency;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.test.concurrency.LockstepEnumPhaserTest.Stages.FINISHED;
import static ca.uhn.test.concurrency.LockstepEnumPhaserTest.Stages.ONE;
import static ca.uhn.test.concurrency.LockstepEnumPhaserTest.Stages.THREE;
import static ca.uhn.test.concurrency.LockstepEnumPhaserTest.Stages.TWO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

// All of these should run pretty quickly - 5s should be lots.
// But if they deadlock, they will hang forever.  Need @Timeout.
@Timeout(5)
class LockstepEnumPhaserTest {
	private static final Logger ourLog = LoggerFactory.getLogger(LockstepEnumPhaserTest.class);
	final ExecutorService myExecutorService = Executors.newFixedThreadPool(10);
	final List<Pair<Integer, Stages>> myProgressEvents = Collections.synchronizedList(new ArrayList<>());
	/** Compare progress records by stage */
	final Comparator<Pair<Integer, Stages>> myProgressStageComparator = Comparator.comparing(Pair::getRight);

	enum Stages {
		ONE, TWO, THREE, FINISHED
	}

	LockstepEnumPhaser<Stages> myPhaser;

	@Test
	void phaserWithOnePariticpant_worksFine() {
	    // given
		myPhaser = new LockstepEnumPhaser<>(1, Stages.class);

		myPhaser.assertInPhase(ONE);

		myPhaser.arriveAndAwaitSharedEndOf(ONE);

		myPhaser.arriveAndAwaitSharedEndOf(TWO);

		myPhaser.arriveAndAwaitSharedEndOf(THREE);

		myPhaser.assertInPhase(FINISHED);
	}

	@Test
	void phaserWithTwoThreads_runsInLockStep() throws InterruptedException, ExecutionException {
		// given
		myPhaser = new LockstepEnumPhaser<>(2, Stages.class);

		// run two copies of the same schedule
		AtomicInteger i = new AtomicInteger(0);
		Callable<Integer> schedule = ()->{
			// get unique ids for each thread.
			int threadId = i.getAndIncrement();

			myPhaser.assertInPhase(ONE);
			ourLog.info("Starting");
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(ONE);

			myPhaser.assertInPhase(TWO);
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(TWO);

			myPhaser.assertInPhase(THREE);
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(THREE);

			ourLog.info("Finished");

			return 1;
		};
		Future<Integer> result1 = myExecutorService.submit(schedule);
		Future<Integer> result2 = myExecutorService.submit(schedule);

		assertEquals(1, result1.get());
		assertEquals(1, result2.get());
		assertEventsAreOrdered();
		assertThat(myProgressEvents).as("all progress logged").hasSize(6);
	}

	private void recordProgress(int threadId) {
		myProgressEvents.add(Pair.of(threadId, myPhaser.getPhase()));
	}

	@Test
	void phaserWithTwoThreads_canAddThird_sequencContinues() throws InterruptedException, ExecutionException {
		// given
		myPhaser = new LockstepEnumPhaser<>(2, Stages.class);

		// run one simple schedule
		Callable<Integer> schedule1 = buildSimpleCountingSchedule(1);
		// this schedule will start half-way in
		Callable<Integer> schedule2 = ()->{
			int threadId = 2;
			ourLog.info("Starting schedule2");

			myPhaser.assertInPhase(TWO);
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(TWO);

			myPhaser.assertInPhase(THREE);

			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(THREE);

			ourLog.info("Finished schedule2");

			return 2;
		};
		// this schedule will start schedule 2 half-way
		Callable<Integer> schedule3 = ()->{
			int threadId = 3;
			myPhaser.assertInPhase(ONE);
			ourLog.info("Starting schedule3");
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(ONE);

			recordProgress(threadId);

			// add a new thread to the mix
			myPhaser.register(); // tell the phaser to expect one more
			myExecutorService.submit(schedule2);

			myPhaser.arriveAndAwaitSharedEndOf(TWO);

			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(THREE);

			ourLog.info("Finished schedule3");

			return 3;
		};
		Future<Integer> result1 = myExecutorService.submit(schedule1);
		Future<Integer> result3 = myExecutorService.submit(schedule3);

		assertEquals(1, result1.get());
		assertEquals(3, result3.get());

		assertEventsAreOrdered();
		assertThat(myProgressEvents).as("all progress logged").hasSize(8);

	}

	@Nonnull
	private Callable<Integer> buildSimpleCountingSchedule(int theThreadId) {
		Callable<Integer> schedule = ()->{
			ourLog.info("Starting schedule - {}", theThreadId);

			myPhaser.assertInPhase(ONE);
			recordProgress(theThreadId);

			myPhaser.arriveAndAwaitSharedEndOf(ONE);

			recordProgress(theThreadId);

			myPhaser.arriveAndAwaitSharedEndOf(TWO);

			recordProgress(theThreadId);

			myPhaser.arriveAndAwaitSharedEndOf(THREE);

			ourLog.info("Finished schedule1");

			return theThreadId;
		};
		return schedule;
	}

	@Test
	void aShortScheduleDeregister_allowsRemainingParticipantsToContinue() throws ExecutionException, InterruptedException {
		// given
		myPhaser = new LockstepEnumPhaser<>(3, Stages.class);

		// Three schedules, but with one that leaves early
		// sched 1,2 counting
		// sched 3 start, but end with 2.
		Callable<Integer> schedule1 = buildSimpleCountingSchedule(1);
		Callable<Integer> schedule2 = buildSimpleCountingSchedule(2);
		Callable<Integer> schedule3 = () -> {
			int threadId = 3;
			ourLog.info("Starting schedule - {}", threadId);

			myPhaser.assertInPhase(ONE);
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(ONE);

			recordProgress(threadId);

			ourLog.info("Leaving schedule - {}", threadId);

			Stages deregisterPhase = myPhaser.arriveAndDeregister();
			assertEquals(TWO, deregisterPhase);

			return threadId;
		};
		Future<Integer> result1 = myExecutorService.submit(schedule1);
		Future<Integer> result2 = myExecutorService.submit(schedule2);
		Future<Integer> result3 = myExecutorService.submit(schedule3);

		assertEquals(1, result1.get());
		assertEquals(2, result2.get());
		assertEquals(3, result3.get());

		assertEventsAreOrdered();
		assertThat(myProgressEvents).as("all progress logged").hasSize(2 * 3 + 2);

	}

	private void assertEventsAreOrdered() {
		assertThat(myProgressEvents)
			.as("progress is ordered")
			.usingElementComparator(myProgressStageComparator)
			.isSorted();
	}

}
