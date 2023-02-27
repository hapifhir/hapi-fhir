package ca.uhn.test.concurrency;

import com.github.seregamorph.hamcrest.OrderMatchers;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.Matchers;
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
import static org.hamcrest.MatcherAssert.assertThat;
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
		assertThat("progress is ordered", myProgressEvents, OrderMatchers.softOrdered(myProgressStageComparator));
		assertThat("all progress logged", myProgressEvents, Matchers.hasSize(6));
	}

	private void recordProgress(int threadId) {
		myProgressEvents.add(Pair.of(threadId, myPhaser.getPhase()));
	}

	@Test
	void phaserWithTwoThreads_canAddThird_sequencContinues() throws InterruptedException, ExecutionException {
		// given
		myPhaser = new LockstepEnumPhaser<>(2, Stages.class);

		// run one simple schedule
		Callable<Integer> schedule1 = ()->{
			int threadId = 1;
			ourLog.info("Starting schedule1");
			myPhaser.assertInPhase(ONE);
			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(ONE);

			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(TWO);

			recordProgress(threadId);

			myPhaser.arriveAndAwaitSharedEndOf(THREE);

			ourLog.info("Finished schedule1");

			return 1;
		};
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

			return 1;
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

			return 1;
		};
		Future<Integer> result1 = myExecutorService.submit(schedule1);
		Future<Integer> result2 = myExecutorService.submit(schedule3);

		assertEquals(1, result1.get());
		assertEquals(1, result2.get());

		assertThat("progress is ordered", myProgressEvents, OrderMatchers.softOrdered(myProgressStageComparator));
		assertThat("all progress logged", myProgressEvents, Matchers.hasSize(8));

	}

}
