package ca.uhn.fhir.jpa.model.concurrency;


import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

// This class is primarily used for testing.
public class PointcutLatch implements IAnonymousInterceptor, IPointcutLatch {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatch.class);
	private static final int DEFAULT_TIMEOUT_SECONDS = 10;
	private static final FhirObjectPrinter ourFhirObjectToStringMapper = new FhirObjectPrinter();

	private final String name;

	private CountDownLatch myCountdownLatch;
	private AtomicReference<List<String>> myFailures;
	private AtomicReference<List<HookParams>> myCalledWith;
	private int myInitialCount;
	private Pointcut myPointcut;

	public PointcutLatch(Pointcut thePointcut) {
		this.name = thePointcut.name();
		myPointcut = thePointcut;
	}

	public PointcutLatch(String theName) {
		this.name = theName;
	}

	@Override
	public void setExpectedCount(int count) {
		if (myCountdownLatch != null) {
			throw new PointcutLatchException("setExpectedCount() called before previous awaitExpected() completed.");
		}
		createLatch(count);
		ourLog.info("Expecting {} calls to {} latch", count, name);
	}

	private void createLatch(int count) {
		myFailures = new AtomicReference<>(new ArrayList<>());
		myCalledWith = new AtomicReference<>(new ArrayList<>());
		myCountdownLatch = new CountDownLatch(count);
		myInitialCount = count;
	}

	private void addFailure(String failure) {
		if (myFailures != null) {
			myFailures.get().add(failure);
		} else {
			throw new PointcutLatchException("trying to set failure on latch that hasn't been created: " + failure);
		}
	}

	private String getName() {
		return name + " " + this.getClass().getSimpleName();
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return awaitExpectedWithTimeout(DEFAULT_TIMEOUT_SECONDS);
	}

	public List<HookParams> awaitExpectedWithTimeout(int timeoutSecond) throws InterruptedException {
		List<HookParams> retval = myCalledWith.get();
		try {
			Validate.notNull(myCountdownLatch, getName() + " awaitExpected() called before setExpected() called.");
			if (!myCountdownLatch.await(timeoutSecond, TimeUnit.SECONDS)) {
				throw new AssertionError(getName() + " timed out waiting " + timeoutSecond + " seconds for latch to countdown from " + myInitialCount + " to 0.  Is " + myCountdownLatch.getCount() + ".");
			}

			List<String> failures = myFailures.get();
			String error = getName();
			if (failures != null && failures.size() > 0) {
				if (failures.size() > 1) {
					error += " ERRORS: \n";
				} else {
					error += " ERROR: ";
				}
				error += failures.stream().collect(Collectors.joining("\n"));
				error += "\nLatch called with values: " + myCalledWithString();
				throw new AssertionError(error);
			}
		} finally {
			clear();
		}
		Validate.isTrue(retval.equals(myCalledWith.get()), "Concurrency error: Latch switched while waiting.");
		return retval;
	}

	@Override
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
	public void invoke(Pointcut thePointcut, HookParams theArgs) {
		if (myCountdownLatch == null) {
			throw new PointcutLatchException("invoke() called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke() arrived.", theArgs);
		} else if (myCountdownLatch.getCount() <= 0) {
			addFailure("invoke() called when countdown was zero.");
		}

		if (myCalledWith.get() != null) {
			myCalledWith.get().add(theArgs);
		}
		ourLog.info("Called {} {} with {}", name, myCountdownLatch, hookParamsToString(theArgs));

		if (myCountdownLatch == null) {
			throw new PointcutLatchException("invoke() called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke() arrived.", theArgs);
		}
		myCountdownLatch.countDown();
	}

	public void call(Object arg) {
		this.invoke(myPointcut, new HookParams(arg));
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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", name)
			.append("myCountdownLatch", myCountdownLatch)
//			.append("myFailures", myFailures)
//			.append("myCalledWith", myCalledWith)
			.append("myInitialCount", myInitialCount)
			.toString();
	}

	public Object getLatchInvocationParameter() {
		return getLatchInvocationParameter(myCalledWith.get());
	}

	public static Object getLatchInvocationParameter(List<HookParams> theHookParams) {
		Validate.notNull(theHookParams);
		Validate.isTrue(theHookParams.size() == 1, "Expected Pointcut to be invoked 1 time");
		return getLatchInvocationParameter(theHookParams, 0);
	}

	public static Object getLatchInvocationParameter(List<HookParams> theHookParams, int index) {
		Validate.notNull(theHookParams);
		HookParams arg = theHookParams.get(index);
		Validate.isTrue(arg.values().size() == 1, "Expected pointcut to be invoked with 1 argument");
		return arg.values().iterator().next();
	}
}
