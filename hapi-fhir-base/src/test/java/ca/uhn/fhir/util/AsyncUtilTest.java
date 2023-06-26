package ca.uhn.fhir.util;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.awaitility.Awaitility.await;

public class AsyncUtilTest {

	@Test
	public void testSleep() {
		AsyncUtil.sleep(10);
	}

	@Test
	public void testSleepWithInterrupt() {
		AtomicBoolean outcomeHolder = new AtomicBoolean(true);
		Thread thread = new Thread(() -> {
			boolean outcome = AsyncUtil.sleep(10000);
			outcomeHolder.set(outcome);
		});
		thread.start();
		sleepAtLeast(1000);
		thread.interrupt();
		await().until(()-> outcomeHolder.get() == false);
	}

	@Test
	public void testAwaitLatchAndThrowInternalErrorException() {
		AtomicBoolean outcomeHolder = new AtomicBoolean(false);

		CountDownLatch latch = new CountDownLatch(1);
		Thread thread = new Thread(() -> {
			try {
				AsyncUtil.awaitLatchAndThrowInternalErrorExceptionOnInterrupt(latch, 10, TimeUnit.SECONDS);
			} catch (InternalErrorException e) {
				outcomeHolder.set(true);
			}
		});
		thread.start();
		thread.interrupt();
		await().until(()-> outcomeHolder.get());
	}

	@Test
	public void testAwaitLatchIgnoreInterruption() {
		AtomicBoolean outcomeHolder = new AtomicBoolean(true);

		CountDownLatch latch = new CountDownLatch(1);
		Thread thread = new Thread(() -> {
			boolean outcome = AsyncUtil.awaitLatchAndIgnoreInterrupt(latch, 10, TimeUnit.SECONDS);
			outcomeHolder.set(outcome);
		});
		thread.start();
		thread.interrupt();
		await().until(()-> outcomeHolder.get() == false);
	}

}
