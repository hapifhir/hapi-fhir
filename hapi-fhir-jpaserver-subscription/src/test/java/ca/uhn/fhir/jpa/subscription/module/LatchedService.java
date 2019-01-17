package ca.uhn.fhir.jpa.subscription.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LatchedService implements Predicate<Object> {
	private static final Logger ourLog = LoggerFactory.getLogger(LatchedService.class);
	private final String name;

	private CountDownLatch myCountdownLatch;
	private AtomicReference<String> myFailure;
	private AtomicReference<List<Object>> myCalledWith;

	public LatchedService(String name) {
		this.name = name;
	}

	public void countdown() {
		if (myCountdownLatch == null) {
			myFailure.set(name + " latch countdown() called before expectedCount set.");
		} else if (myCountdownLatch.getCount() <= 0) {
			myFailure.set(name + " latch countdown() called "+ (1 - myCountdownLatch.getCount()) + " more times than expected.");
		}
		ourLog.info("{} counting down {}", name, myCountdownLatch);
		myCountdownLatch.countDown();
	}

	public void setExpectedCount(int count) {
		myFailure = new AtomicReference<>();
		myCalledWith = new AtomicReference<>(new ArrayList<>());
		myCountdownLatch = new CountDownLatch(count);
	}

	public void awaitExpected() throws InterruptedException {
		awaitExpectedWithTimeout(10);
	}

	public void awaitExpectedWithTimeout(int timeoutSecond) throws InterruptedException {
		assertTrue(name+" latch timed out waiting "+timeoutSecond+" seconds for latch to be triggered.", myCountdownLatch.await(timeoutSecond, TimeUnit.SECONDS));

		if (myFailure.get() != null) {
			String error = myFailure.get();
			error += "\nLatch called with values: "+myCalledWith.get().stream().map(Object::toString).collect(Collectors.joining(", "));
			throw new AssertionError(error);
		}
	}

	@Override
	public boolean test(Object object) {
		this.countdown();
		if (myCalledWith.get() != null) {
			myCalledWith.get().add(object);
		}
		return true;
	}
}
