package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.model.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.model.interceptor.api.IAnonymousLambdaHook;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PointcutLatch implements IAnonymousLambdaHook {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatch.class);
	private static final int DEFAULT_TIMEOUT_SECONDS = 10;
	private static final FhirObjectPrinter ourFhirObjectToStringMapper = new FhirObjectPrinter();

	private final String name;

	private CountDownLatch myCountdownLatch;
	private AtomicReference<String> myFailure;
	private AtomicReference<List<HookParams>> myCalledWith;

	public PointcutLatch(Pointcut thePointcut) {
		this.name = thePointcut.name();
	}

	public PointcutLatch(String theName) {
		this.name = theName;
	}

	public void setExpectedCount(int count) {
		if (myCountdownLatch != null) {
			throw new PointcutLatchException("setExpectedCount() called before previous awaitExpected() completed.");
		}
		createLatch(count);
		ourLog.info("Expecting {} calls to {} latch", count, name);
	}

	private void createLatch(int count) {
		myFailure = new AtomicReference<>();
		myCalledWith = new AtomicReference<>(new ArrayList<>());
		myCountdownLatch = new CountDownLatch(count);
	}

	private void setFailure(String failure) {
		if (myFailure != null) {
			myFailure.set(failure);
		} else {
			throw new PointcutLatchException("trying to set failure on latch that hasn't been created: " + failure);
		}
	}

	private String getName() {
		return name + " " + this.getClass().getSimpleName();
	}

	public List<HookParams> awaitExpected() throws InterruptedException {
		return awaitExpectedWithTimeout(DEFAULT_TIMEOUT_SECONDS);
	}

	public List<HookParams> awaitExpectedWithTimeout(int timeoutSecond) throws InterruptedException {
		List<HookParams> retval = myCalledWith.get();
		try {
			assertNotNull(getName() + " awaitExpected() called before setExpected() called.", myCountdownLatch);
			assertTrue(getName() + " timed out waiting " + timeoutSecond + " seconds for latch to be triggered.", myCountdownLatch.await(timeoutSecond, TimeUnit.SECONDS));

			if (myFailure.get() != null) {
				String error = getName() + ": " + myFailure.get();
				error += "\nLatch called with values: " + myCalledWithString();
				throw new AssertionError(error);
			}
		} finally {
			clear();
		}
		assertEquals("Concurrency error: Latch switched while waiting.", retval, myCalledWith.get());
		return retval;
	}

	public void expectNothing() {
		clear();
	}

	public void clear() {
		myCountdownLatch = null;
	}

	private String myCalledWithString() {
		if (myCalledWith == null) {
			return "[]";
		}
		List<HookParams> calledWith = myCalledWith.get();
		if (calledWith.isEmpty()) {
			return "[]";
		}
		String retVal = "[ ";
		retVal += calledWith.stream().flatMap(hookParams -> hookParams.values().stream()).map(ourFhirObjectToStringMapper).collect(Collectors.joining(", "));
		return retVal + " ]";
	}


	@Override
	public void invoke(HookParams theArgs) {
		if (myCountdownLatch == null) {
			throw new PointcutLatchException("invoke() called before setExpectedCount() called.", theArgs);
		} else if (myCountdownLatch.getCount() <= 0) {
			setFailure("invoke() called " + (1 - myCountdownLatch.getCount()) + " more times than expected.");
		}

		if (myCalledWith.get() != null) {
			myCalledWith.get().add(theArgs);
		}
		ourLog.info("Called {} {} with {}", name, myCountdownLatch, hookParamsToString(theArgs));

		myCountdownLatch.countDown();
	}

	public void call(Object arg) {
		this.invoke(new HookParams(arg));
	}

	private class PointcutLatchException extends IllegalStateException {
		public PointcutLatchException(String message, HookParams theArgs) {
			super(getName() + ": " + message + " called with values: " + hookParamsToString(theArgs));
		}

		public PointcutLatchException(String message) {
			super(getName() + ": " + message);
		}
	}

	private static String hookParamsToString(HookParams hookParams) {
		return hookParams.values().stream().map(ourFhirObjectToStringMapper).collect(Collectors.joining(", "));
	}
}
