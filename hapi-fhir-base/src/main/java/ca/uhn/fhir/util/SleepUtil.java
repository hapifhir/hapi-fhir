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

/**
 * A utility class for thread sleeps.
 * Uses non-static methods for easier mocking and unnecessary waits in unit tests
 */
public class SleepUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SleepUtil.class);

	public void sleepAtLeast(long theMillis) {
		sleepAtLeast(theMillis, true);
	}

	@SuppressWarnings("BusyWait")
	public void sleepAtLeast(long theMillis, boolean theLogProgress) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() <= start + theMillis) {
			try {
				long timeSinceStarted = System.currentTimeMillis() - start;
				long timeToSleep = Math.max(0, theMillis - timeSinceStarted);
				if (theLogProgress) {
					ourLog.info("Sleeping for {}ms", timeToSleep);
				}
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				ourLog.error("Interrupted", e);
			}
		}
	}
}
