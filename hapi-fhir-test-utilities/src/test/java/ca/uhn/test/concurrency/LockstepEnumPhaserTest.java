package ca.uhn.test.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.test.concurrency.LockstepEnumPhaserTest.Stages.*;
import static org.junit.jupiter.api.Assertions.*;

class LockstepEnumPhaserTest {
	private static final Logger ourLog = LoggerFactory.getLogger(LockstepEnumPhaserTest.class);

	enum Stages {
		ONE, TWO, THREE, FINISHED
	}

	LockstepEnumPhaser<Stages> myPhaser;

	@Timeout(1)
	@Test
	void phaserWithOnePariticpant_worksFine() {
	    // given
		myPhaser = new LockstepEnumPhaser<>(1, Stages.class);

		myPhaser.assertInPhase(ONE);

		myPhaser.arriveAtMyEndOf(ONE);

		myPhaser.arriveAndAwaitSharedEndOf(TWO);

		myPhaser.arriveAndAwaitSharedEndOf(THREE);

		myPhaser.assertInPhase(FINISHED);
	}

	@Timeout(5)
	@Test
	void phaserWithTwoThreads_runsInLockStep() throws InterruptedException, ExecutionException {
		// given
		myPhaser = new LockstepEnumPhaser<>(2, Stages.class);
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		// run two copies of the same schedule
		Callable<Integer> schedule = ()->{
			myPhaser.assertInPhase(ONE);
			ourLog.info("Starting");

			myPhaser.arriveAndAwaitSharedEndOf(ONE);

			myPhaser.assertInPhase(TWO);

			myPhaser.arriveAndAwaitSharedEndOf(TWO);

			myPhaser.assertInPhase(THREE);

			Stages nextStage = myPhaser.awaitAdvance(TWO);
			assertEquals(THREE, nextStage);

			myPhaser.arriveAtMyEndOf(THREE);

			ourLog.info("Finished");

			return 1;
		};
		Future<Integer> result1 = executorService.submit(schedule);
		Future<Integer> result2 = executorService.submit(schedule);

		assertEquals(1, result1.get());
		assertEquals(1, result2.get());
	}

}
