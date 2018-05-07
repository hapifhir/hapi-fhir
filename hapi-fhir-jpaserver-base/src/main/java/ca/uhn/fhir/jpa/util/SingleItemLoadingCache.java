package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a simple cache for CapabilityStatement resources to
 * be returned as server metadata.
 */
public class SingleItemLoadingCache<T> {
	private static final Logger ourLog = LoggerFactory.getLogger(SingleItemLoadingCache.class);
	private static Long ourNowForUnitTest;
	private final Callable<T> myFetcher;
	private volatile long myCacheMillis;
	private AtomicReference<T> myCapabilityStatement = new AtomicReference<>();
	private long myLastFetched;

	/**
	 * Constructor
	 */
	public SingleItemLoadingCache(Callable<T> theFetcher) {
		myFetcher = theFetcher;
	}

	public synchronized void clear() {
		ourLog.info("Clearning cache");
		myCapabilityStatement.set(null);
		myLastFetched = 0;
	}

	public synchronized T get() {
		return myCapabilityStatement.get();
	}

	private T refresh() {
		T retVal;
		try {
			retVal = myFetcher.call();
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}

		myCapabilityStatement.set(retVal);
		myLastFetched = now();
		return retVal;
	}

	public void setCacheMillis(long theCacheMillis) {
		myCacheMillis = theCacheMillis;
	}

	@Scheduled(fixedDelay = 60000)
	public void update() {
		if (myCacheMillis > 0) {
			long now = now();
			long expiry = now - myCacheMillis;
			if (myLastFetched < expiry) {
				refresh();
			}
		}
	}

	private static long now() {
		if (ourNowForUnitTest != null) {
			return ourNowForUnitTest;
		}
		return System.currentTimeMillis();
	}

	@VisibleForTesting
	static void setNowForUnitTest(Long theNowForUnitTest) {
		ourNowForUnitTest = theNowForUnitTest;
	}

}
