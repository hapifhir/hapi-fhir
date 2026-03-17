package ca.uhn.test.concurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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

	// --- cascade-avoidance tests: invoke() outside a session never throws immediately ---

	@Test
	void testInvokeCalledBeforeExpect_DoesNotThrowImmediately() {
		// invoke() outside a session must NOT throw — the error is deferred to setExpectedCount()
		invoke();

		assertThat(myPointcutLatch.getLastInvoke()).isPositive();
		// @AfterEach clear() is safe since clear() no longer throws
	}

	@Test
	void testInvokeCalledBeforeExpect_ClearDoesNotThrow() {
		invoke();

		// clear() must NOT throw — error is deferred to the next setExpectedCount()
		myPointcutLatch.clear();

		assertThat(myPointcutLatch.isSet()).isFalse();
	}

	@Test
	void testInvokeCalledBeforeExpect_ErrorSurfacedAtNextSetExpectedCount() {
		invoke(); // unexpected invocation stored; clear() NOT called so it survives to setExpectedCount()

		try {
			myPointcutLatch.setExpectedCount(1);
			fail();
		} catch (AssertionError e) {
			assertThat(e.getMessage()).startsWith("HAPI-2344: " + TEST_LATCH_NAME + " PointcutLatch had 1 exceptions.  Throwing first one.");
		}
		// @AfterEach clear() handles cleanup
	}

	@Test
	void testInvokeInAsyncThread_DoesNotPropagateExceptionToAsyncThread()
			throws ExecutionException, InterruptedException {
		Future<Thread> future = myExecutorService.submit(this::invoke);

		// future.get() must NOT throw ExecutionException — the async thread must complete normally
		Thread asyncThread = future.get();
		assertThat(asyncThread).isNotEqualTo(Thread.currentThread());

		// error surfaces at next setExpectedCount() — clear() NOT called so invocation survives
		try {
			myPointcutLatch.setExpectedCount(1);
			fail();
		} catch (AssertionError e) {
			assertThat(e.getMessage()).startsWith("HAPI-2344: " + TEST_LATCH_NAME + " PointcutLatch had 1 exceptions.  Throwing first one.");
		}
		// @AfterEach clear() handles cleanup
	}

	@Test
	void testConcurrentUnexpectedInvocations_AllRecorded()
			throws ExecutionException, InterruptedException {
		// Invoke from multiple threads concurrently outside a session.
		// CopyOnWriteArrayList must not lose or corrupt any entries.
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<Future<Thread>> futures = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			futures.add(pool.submit(this::invoke));
		}
		for (Future<Thread> f : futures) {
			f.get(); // must not throw ExecutionException
		}
		pool.shutdown();

		try {
			myPointcutLatch.setExpectedCount(1);
			fail();
		} catch (AssertionError e) {
			assertThat(e.getMessage()).contains("had 4 exceptions");
		}
	}

}