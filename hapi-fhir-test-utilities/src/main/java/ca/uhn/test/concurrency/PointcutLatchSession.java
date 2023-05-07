package ca.uhn.test.concurrency;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PointcutLatchSession {
	private static final Logger ourLog = LoggerFactory.getLogger(PointcutLatchSession.class);
	private static final FhirObjectPrinter ourFhirObjectToStringMapper = new FhirObjectPrinter();

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
			error += "\nLatch called with values: " + toCalledWithString();
			throw new AssertionError(Msg.code(1484) + error);
		}
		return myCalledWith;
	}

	private String toCalledWithString() {
		// Defend against ConcurrentModificationException
		List<HookParams> calledWith = new ArrayList<>(myCalledWith);
		if (calledWith.isEmpty()) {
			return "[]";
		}
		String retVal = "[ ";
		retVal += calledWith.stream().flatMap(hookParams -> hookParams.values().stream()).map(ourFhirObjectToStringMapper).collect(Collectors.joining(", "));
		return retVal + " ]";
	}

	void invoke(HookParams theArgs) {
		if (myExactMatch) {
			if (myCountdownLatch.getCount() <= 0) {
				myFailures.add("invoke() called when countdown was zero.");
			}
		}

		myCalledWith.add(theArgs);
		ourLog.debug("Called {} {} with {}", myName, myCountdownLatch, hookParamsToString(theArgs));

		myCountdownLatch.countDown();
	}

	static String hookParamsToString(HookParams hookParams) {
		return hookParams.values().stream().map(ourFhirObjectToStringMapper).collect(Collectors.joining(", "));
	}

	List<HookParams> getCalledWith() {
		return myCalledWith;
	}
}
