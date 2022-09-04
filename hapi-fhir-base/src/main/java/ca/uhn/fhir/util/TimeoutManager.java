package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class TimeoutManager {
	private static final Logger ourLog = LoggerFactory.getLogger(TimeoutManager.class);

	private final StopWatch myStopWatch = new StopWatch();
	private final String myServiceName;
	private final Duration myWarningTimeout;
	private final Duration myErrorTimeout;

	private boolean warned = false;
	private boolean errored = false;

	public TimeoutManager(String theServiceName, Duration theWarningTimeout, Duration theErrorTimeout) {
		myServiceName = theServiceName;
		myWarningTimeout = theWarningTimeout;
		myErrorTimeout = theErrorTimeout;
	}

	/**
	 *
	 * @return true if a message was logged
	 */
	public boolean checkTimeout() {
		boolean retval = false;
		if (myStopWatch.getMillis() > myWarningTimeout.toMillis() && !warned) {
			ourLog.warn(myServiceName + " has run for {}", myStopWatch);
			warned = true;
			retval = true;
		}
		if (myStopWatch.getMillis() > myErrorTimeout.toMillis() && !errored) {
			if ("true".equalsIgnoreCase(System.getProperty("unit_test_mode"))) {
				throw new TimeoutException(Msg.code(2133) + myServiceName + " timed out after running for " + myStopWatch);
			} else {
				ourLog.error(myServiceName + " has run for {}", myStopWatch);
				errored = true;
				retval = true;
			}
		}
		return retval;
	}

	public void addTimeForUnitTest(Duration theDuration) {
		myStopWatch.setNowForUnitTest(myStopWatch.getStartedDate().getTime() + theDuration.toMillis());
	}
}
