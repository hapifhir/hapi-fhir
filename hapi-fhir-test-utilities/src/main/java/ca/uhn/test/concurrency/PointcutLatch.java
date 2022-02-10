package ca.uhn.test.concurrency;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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


import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

// This class is primarily used for testing.
public class PointcutLatch implements IAnonymousInterceptor, IPointcutLatch {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatch.class);
	private static final int DEFAULT_TIMEOUT_SECONDS = 10;
	private static final FhirObjectPrinter ourFhirObjectToStringMapper = new FhirObjectPrinter();

	private final String myName;
	private final AtomicLong myLastInvoke = new AtomicLong();
	private final AtomicReference<CountDownLatch> myCountdownLatch = new AtomicReference<>();
	private final AtomicReference<String> myCountdownLatchSetStacktrace = new AtomicReference<>();
	private final AtomicReference<List<String>> myFailures = new AtomicReference<>();
	private final AtomicReference<List<HookParams>> myCalledWith = new AtomicReference<>();
	private final IPointcut myPointcut;
	private int myDefaultTimeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
	private int myInitialCount;
	private boolean myExactMatch;

	public PointcutLatch(IPointcut thePointcut) {
		this.myName = thePointcut.name();
		myPointcut = thePointcut;
	}


	public PointcutLatch(String theName) {
		this.myName = theName;
		myPointcut = null;
	}

	public void runWithExpectedCount(int theExpectedCount, Runnable r) throws InterruptedException {
		this.setExpectedCount(theExpectedCount);
		r.run();
		this.awaitExpected();
	}

	public long getLastInvoke() {
		return myLastInvoke.get();
	}

	public PointcutLatch setDefaultTimeoutSeconds(int theDefaultTimeoutSeconds) {
		myDefaultTimeoutSeconds = theDefaultTimeoutSeconds;
		return this;
	}

	@Override
	public void setExpectedCount(int theCount) {
		this.setExpectedCount(theCount, true);
	}

	public void setExpectedCount(int theCount, boolean theExactMatch) {
		if (myCountdownLatch.get() != null) {
			String previousStack = myCountdownLatchSetStacktrace.get();
			throw new PointcutLatchException(Msg.code(1480) + "setExpectedCount() called before previous awaitExpected() completed. Previous set stack:\n" + previousStack);
		}
		myExactMatch = theExactMatch;
		createLatch(theCount);
		if (theExactMatch) {
			ourLog.info("Expecting exactly {} calls to {} latch", theCount, myName);
		} else {
			ourLog.info("Expecting at least {} calls to {} latch", theCount, myName);
		}
	}

	public void setExpectAtLeast(int theCount) {
		setExpectedCount(theCount, false);
	}

	public boolean isSet() {
		return myCountdownLatch.get() != null;
	}

	private void createLatch(int theCount) {
		myFailures.set(Collections.synchronizedList(new ArrayList<>()));
		myCalledWith.set(Collections.synchronizedList(new ArrayList<>()));
		myCountdownLatch.set(new CountDownLatch(theCount));
		try {
			throw new Exception(Msg.code(1481));
		} catch (Exception e) {
			myCountdownLatchSetStacktrace.set(ExceptionUtils.getStackTrace(e));
		}
		myInitialCount = theCount;
	}

	private void addFailure(String failure) {
		if (myFailures.get() != null) {
			myFailures.get().add(failure);
		} else {
			throw new PointcutLatchException(Msg.code(1482) + "trying to set failure on latch that hasn't been created: " + failure);
		}
	}

	private String getName() {
		return myName + " " + this.getClass().getSimpleName();
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return awaitExpectedWithTimeout(myDefaultTimeoutSeconds);
	}

	public List<HookParams> awaitExpectedWithTimeout(int timeoutSecond) throws InterruptedException {
		List<HookParams> retval = myCalledWith.get();
		try {
			CountDownLatch latch = myCountdownLatch.get();
			Validate.notNull(latch, getName() + " awaitExpected() called before setExpected() called.");
			if (!latch.await(timeoutSecond, TimeUnit.SECONDS)) {
				throw new AssertionError(Msg.code(1483) + getName() + " timed out waiting " + timeoutSecond + " seconds for latch to countdown from " + myInitialCount + " to 0.  Is " + latch.getCount() + ".");
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
				throw new AssertionError(Msg.code(1484) + error);
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
		myCountdownLatchSetStacktrace.set(null);
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
	public void invoke(IPointcut thePointcut, HookParams theArgs) {
		myLastInvoke.set(System.currentTimeMillis());
		
		CountDownLatch latch = myCountdownLatch.get();
		if (myExactMatch) {
			if (latch == null) {
				throw new PointcutLatchException(Msg.code(1485) + "invoke() for " + myName + " called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke() arrived with args: " + theArgs, theArgs);
			} else if (latch.getCount() <= 0) {
				addFailure("invoke() called when countdown was zero.");
			}
		} else if (latch == null || latch.getCount() <= 0) {
			return;
		}

		if (myCalledWith.get() != null) {
			myCalledWith.get().add(theArgs);
		}
		ourLog.info("Called {} {} with {}", myName, latch, hookParamsToString(theArgs));

		latch.countDown();
	}

	public void call(Object arg) {
		this.invoke(myPointcut, new HookParams(arg));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", myName)
			.append("myCountdownLatch", myCountdownLatch)
//			.append("myFailures", myFailures)
//			.append("myCalledWith", myCalledWith)
			.append("myInitialCount", myInitialCount)
			.toString();
	}

	public Object getLatchInvocationParameter() {
		return getLatchInvocationParameter(myCalledWith.get());
	}

	@SuppressWarnings("unchecked")
	public <T> T getLatchInvocationParameterOfType(Class<T> theType) {
		List<HookParams> hookParamsList = myCalledWith.get();
		Validate.notNull(hookParamsList);
		Validate.isTrue(hookParamsList.size() == 1, "Expected Pointcut to be invoked 1 time");
		HookParams hookParams = hookParamsList.get(0);
		ListMultimap<Class<?>, Object> paramsForType = hookParams.getParamsForType();
		List<Object> objects = paramsForType.get(theType);
		Validate.isTrue(objects.size() == 1);
		return (T) objects.get(0);
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
