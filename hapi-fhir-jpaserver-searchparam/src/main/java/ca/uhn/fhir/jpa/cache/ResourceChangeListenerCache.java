package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import com.google.common.annotations.VisibleForTesting;
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

@Component
@Scope("prototype")
public class ResourceChangeListenerCache implements IResourceChangeListenerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCache.class);
	private static final int MAX_RETRIES = 60;

	private static Instant ourNowForUnitTests;

	@Autowired
	IResourceChangeListenerCacheRefresher myResourceChangeListenerCacheRefresher;
	@Autowired
	SearchParamMatcher mySearchParamMatcher;

	private final String myResourceName;
	private final IResourceChangeListener myResourceChangeListener;
	private final SearchParameterMap mySearchParameterMap;
	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();
	private boolean myInitialized = false;
	private Instant myNextRefreshTime = Instant.MIN;
	private final long myRemoteRefreshIntervalMs;

	public ResourceChangeListenerCache(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
		myResourceName = theResourceName;
		myResourceChangeListener = theResourceChangeListener;
		// FIXME KHS clone
		mySearchParameterMap = theSearchParameterMap;
		myRemoteRefreshIntervalMs = theRemoteRefreshIntervalMs;
	}

	public IResourceChangeListener getResourceChangeListener() {
		return myResourceChangeListener;
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

	public boolean matches(IBaseResource theResource) {
		InMemoryMatchResult result = mySearchParamMatcher.match(mySearchParameterMap, theResource);
		if (!result.supported()) {
			// This should never happen since we enforce only in-memory SearchParamMaps at registration time
			throw new IllegalStateException("Search Parameter Map " + mySearchParameterMap + " cannot be processed in-memory: " + result.getUnsupportedReason());
		}
		return result.matched();
	}


	public void clear() {
		requestRefresh();
		myResourceVersionCache.clear();
	}

	@Override
	public Instant getNextRefreshTime() {
		return myNextRefreshTime;
	}

	/**
	 * Request that the cache be refreshed at the next convenient time (in a different thread)
	 */
	@Override
	public void requestRefresh() {
		myNextRefreshTime = Instant.MIN;
	}

	public boolean isTimeToRefresh() {
		return myNextRefreshTime.isBefore(now());
	}

	private static Instant now() {
		if (ourNowForUnitTests != null) {
			return ourNowForUnitTests;
		}
		return Instant.now();
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

	/**
	 * Request that a cache be refreshed now, in the current thread
	 */
	@Override
	public ResourceChangeResult forceRefresh() {
		requestRefresh();
		return refreshCacheWithRetry();
	}

	public void requestRefreshIfWatching(IBaseResource theResource) {
		if (matches(theResource)) {
			requestRefresh();
		}
	}

	@Override
	public ResourceChangeResult refreshCacheIfNecessary() {
		ResourceChangeResult retval = new ResourceChangeResult();
		if (isTimeToRefresh()) {
			retval = refreshCacheWithRetry();
		}
		return retval;
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

	private ResourceChangeResult refreshCacheAndNotifyListenersWithRetry() {
		Retrier<ResourceChangeResult> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return myResourceChangeListenerCacheRefresher.refreshCacheAndNotifyListener(this);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myResourceName", myResourceName)
			.append("mySearchParameterMap", mySearchParameterMap)
			.append("myInitialized", myInitialized)
			.toString();
	}
}
