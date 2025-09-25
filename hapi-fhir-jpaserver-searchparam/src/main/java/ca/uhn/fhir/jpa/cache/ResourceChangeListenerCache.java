/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class ResourceChangeListenerCache implements IResourceChangeListenerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCache.class);
	/**
	 * Max number of retries to do for cache refreshing
	 */
	private static final int MAX_RETRIES = 60;

	/**
	 * Timeout in milliseconds for acquiring the semaphore lock (1 minute)
	 */
	private static final long SEMAPHORE_TIMEOUT_MS = 60000;

	private static Instant ourNowForUnitTests;

	/**
	 * Semaphore used to control access to the cache refresh operation
	 */
	private final Semaphore myCacheSemaphore = new Semaphore(1);

	@Autowired
	IResourceChangeListenerCacheRefresher myResourceChangeListenerCacheRefresher;

	@Autowired
	SearchParamMatcher mySearchParamMatcher;

	private final String myResourceName;
	private final IResourceChangeListener myResourceChangeListener;
	private final SearchParameterMap mySearchParameterMap;
	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();
	private final long myRemoteRefreshIntervalMs;

	private boolean myInitialized = false;
	private Instant myNextRefreshTime = Instant.MIN;

	public ResourceChangeListenerCache(
			String theResourceName,
			IResourceChangeListener theResourceChangeListener,
			SearchParameterMap theSearchParameterMap,
			long theRemoteRefreshIntervalMs) {
		myResourceName = theResourceName;
		myResourceChangeListener = theResourceChangeListener;
		mySearchParameterMap = SerializationUtils.clone(theSearchParameterMap);
		myRemoteRefreshIntervalMs = theRemoteRefreshIntervalMs;
	}

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	@Override
	public void requestRefresh() {
		myNextRefreshTime = Instant.MIN;
	}

	/**
	 * Request that a cache be refreshed now, in the current thread
	 */
	@Override
	public ResourceChangeResult forceRefresh() {
		requestRefresh();
		return refreshCacheWithRetry();
	}

	/**
	 * Refresh the cache if theResource matches our SearchParameterMap
	 *
	 * @param theResource
	 */
	public void requestRefreshIfWatching(IBaseResource theResource) {
		if (matches(theResource)) {
			requestRefresh();
		}
	}

	public boolean matches(IBaseResource theResource) {
		InMemoryMatchResult result = mySearchParamMatcher.match(mySearchParameterMap, theResource);
		if (!result.supported()) {
			// This should never happen since we enforce only in-memory SearchParamMaps at registration time
			throw new IllegalStateException(Msg.code(483) + "Search Parameter Map " + mySearchParameterMap
					+ " cannot be processed in-memory: " + result.getUnsupportedReason());
		}
		return result.matched();
	}

	@Override
	public ResourceChangeResult refreshCacheIfNecessary() {
		ResourceChangeResult retval = new ResourceChangeResult();
		if (isTimeToRefresh()) {
			retval = refreshCacheWithRetry();
		}
		return retval;
	}

	protected boolean isTimeToRefresh() {
		return myNextRefreshTime.isBefore(now());
	}

	static Instant now() {
		if (ourNowForUnitTests != null) {
			return ourNowForUnitTests;
		}
		return Instant.now();
	}

	public ResourceChangeResult refreshCacheWithRetry() {
		ResourceChangeResult retval;
		try {
			retval = refreshCacheAndNotifyListenersWithRetry();
		} finally {
			myNextRefreshTime = now().plus(Duration.ofMillis(myRemoteRefreshIntervalMs));
		}
		return retval;
	}

	@VisibleForTesting
	public void setResourceChangeListenerCacheRefresher(
			IResourceChangeListenerCacheRefresher theResourceChangeListenerCacheRefresher) {
		myResourceChangeListenerCacheRefresher = theResourceChangeListenerCacheRefresher;
	}

	private ResourceChangeResult refreshCacheAndNotifyListenersWithRetry() {
		Retrier<ResourceChangeResult> refreshCacheRetrier =
				new Retrier<>(this::tryRefreshCacheAndNotifyListener, getMaxRetries());
		return refreshCacheRetrier.runWithRetry();
	}

	private ResourceChangeResult tryRefreshCacheAndNotifyListener() {
		boolean acquired = false;
		try {
			// Try to acquire the semaphore with a timeout
			acquired = myCacheSemaphore.tryAcquire(SEMAPHORE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
			if (acquired) {
				return myResourceChangeListenerCacheRefresher.refreshCacheAndNotifyListener(this);
			} else {
				ourLog.warn(
						"Timed out waiting {} ms to acquire lock for refreshing cache for resource {}",
						SEMAPHORE_TIMEOUT_MS,
						myResourceName);
				throw new InternalErrorException(
						Msg.code(2702) + "Timed out waiting to acquire lock for refreshing cache");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalErrorException(Msg.code(2703) + "Interrupted while waiting to refresh cache", e);
		} finally {
			if (acquired) {
				myCacheSemaphore.release();
			}
		}
	}

	@Override
	public Instant getNextRefreshTime() {
		return myNextRefreshTime;
	}

	@Override
	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	@Override
	public boolean isInitialized() {
		return myInitialized;
	}

	public ResourceChangeListenerCache setInitialized(boolean theInitialized) {
		myInitialized = theInitialized;
		return this;
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	public ResourceVersionCache getResourceVersionCache() {
		return myResourceVersionCache;
	}

	public IResourceChangeListener getResourceChangeListener() {
		return myResourceChangeListener;
	}

	/**
	 * @param theTime has format like "12:34:56" i.e. HH:MM:SS
	 */
	@VisibleForTesting
	public static void setNowForUnitTests(String theTime) {
		if (theTime == null) {
			ourNowForUnitTests = null;
			return;
		}
		String datetime = "2020-11-16T" + theTime + "Z";
		Clock clock = Clock.fixed(Instant.parse(datetime), ZoneId.systemDefault());
		ourNowForUnitTests = Instant.now(clock);
	}

	@VisibleForTesting
	Instant getNextRefreshTimeForUnitTest() {
		return myNextRefreshTime;
	}

	@VisibleForTesting
	public void clearForUnitTest() {
		requestRefresh();
		myResourceVersionCache.clear();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("myResourceName", myResourceName)
				.append("mySearchParameterMap", mySearchParameterMap)
				.append("myInitialized", myInitialized)
				.toString();
	}

	static int getMaxRetries() {
		return MAX_RETRIES;
	}
}
