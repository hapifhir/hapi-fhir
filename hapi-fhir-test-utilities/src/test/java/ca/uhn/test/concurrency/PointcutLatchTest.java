package ca.uhn.test.concurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PointcutLatchTest {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatchTest.class);
	public static final String TEST_LATCH_NAME = "test-latch-name";
	private final ExecutorService myExecutorService = Executors.newSingleThreadExecutor();
	private final PointcutLatch myPointcutLatch = new PointcutLatch(TEST_LATCH_NAME);

	@AfterEach
	void after() {
		myPointcutLatch.clear();
		myPointcutLatch.setStrict(true);
	}

	@Test
	public void testInvokeSameThread() throws InterruptedException {
		myPointcutLatch.setExpectedCount(1);
		Thread thread = invoke();
		assertEquals(thread, Thread.currentThread());
		myPointcutLatch.awaitExpected();
	}

	private Thread invoke() {
		ourLog.info("invoke");
		myPointcutLatch.call(this);
		return Thread.currentThread();
	}

	@Test
	public void testInvokeDifferentThread() throws InterruptedException, ExecutionException {
		myPointcutLatch.setExpectedCount(1);
		Future<Thread> future = myExecutorService.submit(this::invoke);
		myPointcutLatch.awaitExpected();
		assertThat(future.get()).isNotEqualTo(Thread.currentThread());
	}

	@Test
	public void testDoubleExpect() {
		myPointcutLatch.setExpectedCount(1);
		try {
			myPointcutLatch.setExpectedCount(1);
			fail();
		} catch (PointcutLatchException e) {
			assertThat(e.getMessage()).startsWith(TEST_LATCH_NAME + ": HAPI-1480: setExpectedCount() called before previous awaitExpected() completed. Previous set stack:");
		}
	}

	@Test
	public void testNotCalled() throws InterruptedException {
		myPointcutLatch.setExpectedCount(1);
		try {
			myPointcutLatch.awaitExpectedWithTimeout(1);
			fail();
		} catch (LatchTimedOutError e) {
			assertEquals("HAPI-1483: test-latch-name PointcutLatch timed out waiting 1 seconds for latch to countdown from 1 to 0.  Is 1.", e.getMessage());
		}
	}

	@Test
	public void testAwaitExpectedCalledBeforeExpect() throws InterruptedException {
		try {
			myPointcutLatch.awaitExpected();
			fail();
		} catch (PointcutLatchException e) {
			assertEquals(TEST_LATCH_NAME + ": awaitExpected() called before setExpected() called.", e.getMessage());
		}
	}

	@Test
	public void testInvokeCalledBeforeExpect() {
		try {
			invoke();
			fail();
		} catch (PointcutLatchException e) {
			assertThat(e.getMessage()).startsWith(TEST_LATCH_NAME + ": HAPI-1485: invoke() called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke().");
		}
		// Don't blow up in the clear() called by @AfterEach
		myPointcutLatch.setStrict(false);
	}

	@Test
	public void testDoubleInvokeInexact() throws InterruptedException {
		myPointcutLatch.setExpectedCount(1, false);
		invoke();
		invoke();
		myPointcutLatch.awaitExpected();
	}

	@Test
	public void testDoubleInvokeExact() throws InterruptedException {
		myPointcutLatch.setExpectedCount(1);
		invoke();
		try {
			invoke();
			myPointcutLatch.awaitExpected();
			fail();
		} catch (AssertionError e) {
			assertThat(e.getMessage()).startsWith("HAPI-1484: test-latch-name PointcutLatch ERROR: invoke() called when countdown was zero.");
		}
	}

	@Test
	public void testInvokeThenClear() throws ExecutionException, InterruptedException {
		Future<Thread> future = myExecutorService.submit(this::invoke);
		try {
			future.get();
		} catch (ExecutionException e) {
			// This is the exception thrown on the invocation thread
			assertThat(e.getMessage()).startsWith("ca.uhn.test.concurrency.PointcutLatchException: " + TEST_LATCH_NAME + ": HAPI-1485: invoke() called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke().");
		}
		try {
			myPointcutLatch.clear();
		} catch (AssertionError e) {
			// This is the exception the test thread gets
			assertThat(e.getMessage()).startsWith("HAPI-2344: " + TEST_LATCH_NAME + " PointcutLatch had 1 exceptions.  Throwing first one.");
		}

		// Don't blow up in the clear() called by @AfterEach
		myPointcutLatch.setStrict(false);
	}

}
