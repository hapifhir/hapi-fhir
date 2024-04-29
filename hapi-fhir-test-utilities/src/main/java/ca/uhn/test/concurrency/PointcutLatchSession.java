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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This object exists from the time setExpectedCount() is called until awaitExpected() completes.  It tracks
 * invocations on the PointcutLatch and throws exceptions when incorrect states are detected.
 */
public class PointcutLatchSession {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatchSession.class);

	private final List<String> myFailures = Collections.synchronizedList(new ArrayList<>());
	private final List<HookParams> myCalledWith = Collections.synchronizedList(new ArrayList<>());
	private final CountDownLatch myCountdownLatch;
	private final String myStacktrace;

	private final String myName;
	private final int myInitialCount;
	private final boolean myExactMatch;

	PointcutLatchSession(String theName, int theCount, boolean theExactMatch) {
		myName = theName;
		myInitialCount = theCount;
		myCountdownLatch = new CountDownLatch(theCount);
		myExactMatch = theExactMatch;
		try {
			throw new Exception(Msg.code(1481));
		} catch (Exception e) {
			myStacktrace = ExceptionUtils.getStackTrace(e);
		}
	}

	String getStackTrace() {
		return myStacktrace;
	}

	List<HookParams> awaitExpectedWithTimeout(int theTimeoutSecond) throws InterruptedException {
		if (!myCountdownLatch.await(theTimeoutSecond, TimeUnit.SECONDS)) {
			if (!myFailures.isEmpty()) {
				ourLog.error(String.join(",", myFailures));
			}
			throw new LatchTimedOutError(Msg.code(1483) + myName + " timed out waiting " + theTimeoutSecond + " seconds for latch to countdown from " + myInitialCount + " to 0.  Is " + myCountdownLatch.getCount() + ".");
		}

		// Defend against ConcurrentModificationException
		String error = myName;
		if (myFailures.size() > 0) {
			List<String> failures = new ArrayList<>(myFailures);
			if (failures.size() > 1) {
				error += " ERRORS: \n";
			} else {
				error += " ERROR: ";
			}
			error += String.join("\n", failures);
			error += "\nLatch called " + myCalledWith.size() + " times with values:\n" + StringUtils.join(myCalledWith, "\n");
			throw new AssertionError(Msg.code(1484) + error);
		}
		return myCalledWith;
	}

	void invoke(HookParams theArgs) {
		if (myExactMatch) {
			if (myCountdownLatch.getCount() <= 0) {
				myFailures.add("invoke() called when countdown was zero.");
			}
		}

		myCalledWith.add(theArgs);
		ourLog.debug("Called {} {} with {}", myName, myCountdownLatch, theArgs);

		myCountdownLatch.countDown();
	}

	List<HookParams> getCalledWith() {
		return myCalledWith;
	}
}
