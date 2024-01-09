/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.system.HapiSystemProperties;
import com.google.common.annotations.VisibleForTesting;
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
			if (HapiSystemProperties.isUnitTestModeEnabled()) {
				throw new TimeoutException(
						Msg.code(2133) + myServiceName + " timed out after running for " + myStopWatch);
			} else {
				ourLog.error(myServiceName + " has run for {}", myStopWatch);
				errored = true;
				retval = true;
			}
		}
		return retval;
	}

	@VisibleForTesting
	void addTimeForUnitTest(Duration theDuration) {
		myStopWatch.setNowForUnitTest(myStopWatch.getStartedDate().getTime() + theDuration.toMillis());
	}
}
