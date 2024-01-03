/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.test.concurrency;


import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

// This class is primarily used for testing.
public class PointcutLatch implements IAnonymousInterceptor, IPointcutLatch {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatch.class);

	private static final int DEFAULT_TIMEOUT_SECONDS = 10;

	private final String myName;
	private final IPointcut myPointcut;
	private boolean myStrict = true;
	private final AtomicLong myLastInvoke = new AtomicLong();
	private int myDefaultTimeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
	private final List<PointcutLatchException> myUnexpectedInvocations = new ArrayList<>();
	private final AtomicReference<PointcutLatchSession> myPointcutLatchSession = new AtomicReference<>();

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

	// Useful for debugging when you need more time to step through a method
	public void setDefaultTimeoutSeconds(int theDefaultTimeoutSeconds) {
		myDefaultTimeoutSeconds = theDefaultTimeoutSeconds;
	}

	@Override
	public void setExpectedCount(int theCount) {
		this.setExpectedCount(theCount, true);
	}

	public void setExpectedCount(int theCount, boolean theExactMatch) {
		if (myPointcutLatchSession.get() != null) {
			String previousStack = myPointcutLatchSession.get().getStackTrace();
			throw new PointcutLatchException(Msg.code(1480) + "setExpectedCount() called before previous awaitExpected() completed. Previous set stack:\n" + previousStack, myName);
		}
		startSession(theCount, theExactMatch);
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
		return myPointcutLatchSession.get() != null;
	}

	private void startSession(int theCount, boolean theExactMatch) {
		myPointcutLatchSession.set(new PointcutLatchSession(getName(), theCount, theExactMatch));
	}

	private String getName() {
		return myName + " " + this.getClass().getSimpleName();
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return awaitExpectedWithTimeout(myDefaultTimeoutSeconds);
	}

	public List<HookParams> awaitExpectedWithTimeout(int theTimeoutSecond) throws InterruptedException {
		PointcutLatchSession initialSession = myPointcutLatchSession.get();

		List<HookParams> retval;
		try {
			if (isSet()) {
				retval = myPointcutLatchSession.get().awaitExpectedWithTimeout(theTimeoutSecond);
				Validate.isTrue(initialSession.equals(myPointcutLatchSession.get()), "Concurrency error: Latch session switched while waiting.");
			} else {
				throw new PointcutLatchException("awaitExpected() called before setExpected() called.", myName);
			}
		} finally {
			clear();
		}
		return retval;
	}

	@Override
	public void clear() {
		ourLog.debug("Clearing latch {}", getName());
		checkExceptions();
		myPointcutLatchSession.set(null);
		myUnexpectedInvocations.clear();
	}

	private void checkExceptions() {
		if (!myStrict) {
			return;
		}

		if (myUnexpectedInvocations.size() > 0) {
			PointcutLatchException firstException = myUnexpectedInvocations.get(0);
			int size = myUnexpectedInvocations.size();
			if (firstException != null) {
				throw new AssertionError(Msg.code(2344) + getName() + " had " + size + " exceptions.  Throwing first one.", firstException);
			}
		}
	}

	@Override
	public void invoke(IPointcut thePointcut, HookParams theArgs) {
		myLastInvoke.set(System.currentTimeMillis());

		try {
			PointcutLatchSession session = myPointcutLatchSession.get();
			if (session == null) {
				throw new PointcutLatchException(Msg.code(1485) + "invoke() called outside of setExpectedCount() .. awaitExpected().  Probably got more invocations than expected or clear() was called before invoke().", myName, theArgs);
			}
			session.invoke(theArgs);
		} catch (PointcutLatchException e) {
			myUnexpectedInvocations.add(e);
			throw e;
		}
	}

	public void call(Object arg) {
		this.invoke(myPointcut, new HookParams(arg));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", myName)
			.append("pointCutLatchSession", myPointcutLatchSession)
			.toString();
	}

	public void setStrict(Boolean theStrict) {
		myStrict = theStrict;
	}

	public static <T> T getInvocationParameterOfType(List<HookParams> theHookParams, Class<T> theType) {
		Validate.notNull(theHookParams);
		Validate.isTrue(theHookParams.size() == 1, "Expected Pointcut to be invoked 1 time");
		HookParams hookParams = theHookParams.get(0);
		ListMultimap<Class<?>, Object> paramsForType = hookParams.getParamsForType();
		List<Object> objects = paramsForType.get(theType);
		Validate.isTrue(objects.size() == 1);
		return (T) objects.get(0);
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
