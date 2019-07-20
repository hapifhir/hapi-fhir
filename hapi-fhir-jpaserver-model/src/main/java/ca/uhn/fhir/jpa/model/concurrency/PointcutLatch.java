package ca.uhn.fhir.jpa.model.concurrency;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
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

	private final AtomicReference<CountDownLatch> myCountdownLatch = new AtomicReference<>();
	private final AtomicReference<List<String>> myFailures = new AtomicReference<>();
	private final AtomicReference<List<HookParams>> myCalledWith = new AtomicReference<>();
    private final Pointcut myPointcut;
	private int myInitialCount;

	public PointcutLatch(Pointcut thePointcut) {
		this.name = thePointcut.name();
		myPointcut = thePointcut;
	}

	public PointcutLatch(String theName) {
		this.name = theName;
        myPointcut = null;
	}

	@Override
	public void setExpectedCount(int count) {
		if (myCountdownLatch.get() != null) {
			throw new PointcutLatchException("setExpectedCount() called before previous awaitExpected() completed.");
		}
		createLatch(count);
		ourLog.info("Expecting {} calls to {} latch", count, name);
	}

	private void createLatch(int count) {
		myFailures.set(Collections.synchronizedList(new ArrayList<>()));
		myCalledWith.set(Collections.synchronizedList(new ArrayList<>()));
		myCountdownLatch.set(new CountDownLatch(count));
		myInitialCount = count;
	}

	private void addFailure(String failure) {
		if (myFailures.get() != null) {
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
			CountDownLatch latch = myCountdownLatch.get();
            Validate.notNull(latch, getName() + " awaitExpected() called before setExpected() called.");
			if (!latch.await(timeoutSecond, TimeUnit.SECONDS)) {
				throw new AssertionError(getName() + " timed out waiting " + timeoutSecond + " seconds for latch to countdown from " + myInitialCount + " to 0.  Is " + latch.getCount() + ".");
			}

			// Defend against ConcurrentModificationException
			String error = getName();
			if (myFailures.get() != null && myFailures.get().size() > 0) {
				List<String> failures = new ArrayList<>(myFailures.get());
				if (failures.size() > 1) {
					error += " ERRORS: \n";
				} else {
					error += " ERROR: ";
				}
				error += String.join("\n", failures);
				error += "\nLatch called with values: " + toCalledWithString();
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
		myCountdownLatch.set(null);
	}

	private String toCalledWithString() {
		if (myCalledWith.get() == null) {
			return "[]";
		}
		// Defend against ConcurrentModificationException
		List<HookParams> calledWith = new ArrayList<>(myCalledWith.get());
		if (calledWith.isEmpty()) {
			return "[]";
		}
		String retVal = "[ ";
		retVal += calledWith.stream().flatMap(hookParams -> hookParams.values().stream()).map(ourFhirObjectToStringMapper).collect(Collectors.joining(", "));
		return retVal + " ]";
	}


	@Override
	public void invoke(Pointcut thePointcut, HookParams theArgs) {
        CountDownLatch latch = myCountdownLatch.get();
		if (latch == null) {
			throw new PointcutLatchException("invoke() called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke() arrived.", theArgs);
		} else if (latch.getCount() <= 0) {
			addFailure("invoke() called when countdown was zero.");
		}

		if (myCalledWith.get() != null) {
			myCalledWith.get().add(theArgs);
		}
		ourLog.info("Called {} {} with {}", name, latch, hookParamsToString(theArgs));

		latch.countDown();
	}

	public void call(Object arg) {
		this.invoke(myPointcut, new HookParams(arg));
	}

	private class PointcutLatchException extends IllegalStateException {
		private static final long serialVersionUID = 1372636272233536829L;

		PointcutLatchException(String message, HookParams theArgs) {
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
