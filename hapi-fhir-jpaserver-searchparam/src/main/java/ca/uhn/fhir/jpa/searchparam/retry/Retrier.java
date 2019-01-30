package ca.uhn.fhir.jpa.searchparam.retry;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Retrier<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(Retrier.class);

	private final Supplier<T> mySupplier;
	private final int myMaxRetries;
	private final int mySecondsBetweenRetries;
	private final String myDescription;

	public Retrier(Supplier<T> theSupplier, int theMaxRetries, int theSecondsBetweenRetries, String theDescription) {
		mySupplier = theSupplier;
		myMaxRetries = theMaxRetries;
		mySecondsBetweenRetries = theSecondsBetweenRetries;
		myDescription = theDescription;
	}

	public T runWithRetry() {
		RuntimeException lastException = new IllegalStateException("maxRetries must be above zero.");
		for (int retryCount = 1; retryCount <= myMaxRetries; ++retryCount) {
			try {
				return mySupplier.get();
			} catch(RuntimeException e) {
				ourLog.trace("Failure during retry: {}", e.getMessage(), e); // with stacktrace if it's ever needed
				ourLog.info("Failed to {}.  Attempt {} / {}: {}", myDescription, retryCount, myMaxRetries, e.getMessage());
				lastException = e;
				try {
					Thread.sleep(mySecondsBetweenRetries * DateUtils.MILLIS_PER_SECOND);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw lastException;
				}
			}
		}
		throw lastException;
	}
}
