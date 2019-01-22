package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.interceptor.api.IAnonymousLambdaHook;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PointcutLatch implements IAnonymousLambdaHook {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatch.class);
	private static final int DEFAULT_TIMEOUT_SECONDS = 10;
	private final String name;

	private Semaphore mySemaphore = new Semaphore(1);
	private CountDownLatch myCountdownLatch;
	private AtomicReference<String> myFailure;
	private AtomicReference<List<HookParams>> myCalledWith;

	public PointcutLatch(Pointcut thePointcut) {
		this.name = thePointcut.name();
	}

	public PointcutLatch(String theName) {
		this.name = theName;
	}

	private void countdown() {
		if (myCountdownLatch == null) {
			myFailure.set(name + " latch countdown() called before expectedCount set.");
		} else if (myCountdownLatch.getCount() <= 0) {
			myFailure.set(name + " latch countdown() called "+ (1 - myCountdownLatch.getCount()) + " more times than expected.");
		}
		ourLog.info("{} counting down {}", name, myCountdownLatch);
		myCountdownLatch.countDown();
	}

	public void setExpectedCount(int count) throws InterruptedException {
		mySemaphore.acquire();
		if (myCountdownLatch != null) {
			myFailure.set(name + " latch setExpectedCount() called before previous awaitExpected() completed.");
		}
		myFailure = new AtomicReference<>();
		myCalledWith = new AtomicReference<>(new ArrayList<>());
		myCountdownLatch = new CountDownLatch(count);
	}

	public void awaitExpected() throws InterruptedException {
		awaitExpected(true);
	}

	public void awaitExpected(boolean release) throws InterruptedException {
		awaitExpectedWithTimeout(DEFAULT_TIMEOUT_SECONDS, release);
	}

	public void awaitExpectedWithTimeout(int timeoutSecond, boolean release) throws InterruptedException {
		try {
			assertNotNull(name + " latch awaitExpected() called before previous setExpected() called.", myCountdownLatch);
			assertTrue(name + " latch timed out waiting " + timeoutSecond + " seconds for latch to be triggered.", myCountdownLatch.await(timeoutSecond, TimeUnit.SECONDS));

			if (myFailure.get() != null) {
				String error = myFailure.get();
				error += "\nLatch called with values: " + myCalledWith.get().stream().map(Object::toString).collect(Collectors.joining(", "));
				throw new AssertionError(error);
			}
		} finally {
			if (release) {
				release();
			}
		}
	}

	public void release() {
		myCountdownLatch = null;
		mySemaphore.release();
	}

	@Override
	public void invoke(HookParams theArgs) {
		this.countdown();
		if (myCalledWith.get() != null) {
			myCalledWith.get().add(theArgs);
		}
	}
}
