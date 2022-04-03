package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncUtil.class);

	/**
	 * Non instantiable
	 */
	private AsyncUtil() {
	}

	/**
	 * Calls Thread.sleep and if an InterruptedException occurs, logs a warning but otherwise continues
	 *
	 * @param theMillis The number of millis to sleep
	 * @return Did we sleep the whole amount
	 */
	public static boolean sleep(long theMillis) {
		try {
			Thread.sleep(theMillis);
			return true;
		} catch (InterruptedException theE) {
			Thread.currentThread().interrupt();
			ourLog.warn("Sleep for {}ms was interrupted", theMillis);
			return false;
		}
	}

	public static boolean awaitLatchAndThrowInternalErrorExceptionOnInterrupt(CountDownLatch theInitialCollectionLatch, long theTime, TimeUnit theTimeUnit) {
		try {
			return theInitialCollectionLatch.await(theTime, theTimeUnit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalErrorException(Msg.code(1805) + e);
		}
	}

	public static boolean awaitLatchAndIgnoreInterrupt(CountDownLatch theInitialCollectionLatch, long theTime, TimeUnit theTimeUnit) {
		try {
			return theInitialCollectionLatch.await(theTime, theTimeUnit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ourLog.warn("Interrupted while waiting for latch");
			return false;
		}
	}
}
