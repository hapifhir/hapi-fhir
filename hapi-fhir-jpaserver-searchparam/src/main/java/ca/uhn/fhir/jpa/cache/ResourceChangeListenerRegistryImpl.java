package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// FIXME KHS retool subscription to use this
@Component
public class ResourceChangeListenerRegistryImpl implements IResourceChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerRegistryImpl.class);

	static long LOCAL_REFRESH_INTERVAL_MS = 10 * DateUtils.MILLIS_PER_SECOND;
	static long REMOTE_REFRESH_INTERVAL_MS = DateUtils.MILLIS_PER_HOUR;
	private static final int MAX_RETRIES = 60; // 5 minutes
	private static volatile Map<String, Instant> myNextRefreshByResourceName = new HashMap<>();
	private static Instant ourNowForUnitTests;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private ResourceChangeListenerCache myResourceChangeListenerCache;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();

	/**
	 *
	 * @param theResourceName the name of the resource the listener should be notified about
	 * @param theSearchParamMap the listener will only be notified of changes to resources that match this map
	 * @param theResourceChangeListener the listener to be notified
	 * @throws ca.uhn.fhir.parser.DataFormatException if theResourceName is not valid
	 * @throws ca.uhn.fhir.parser.IllegalArgumentException if theSearchParamMap cannot be evaluated in-memory
	 */
	@Override
	public void registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParamMap, IResourceChangeListener theResourceChangeListener) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceName);
		InMemoryMatchResult inMemoryMatchResult = myInMemoryResourceMatcher.checkIfInMemorySupported(theSearchParamMap, resourceDef);
		if (!inMemoryMatchResult.supported()) {
			throw new IllegalArgumentException("SearchParameterMap " + theSearchParamMap + " cannot be evaluated in-memory: " + inMemoryMatchResult.getUnsupportedReason() + ".  Only search parameter maps that can be evaluated in-memory may be registered.");
		}
		myResourceChangeListenerCache.add(theResourceName, theResourceChangeListener, theSearchParamMap);
		myNextRefreshByResourceName.put(theResourceName, Instant.MIN);
	}

	@Override
	public void unregisterResourceResourceChangeListener(IResourceChangeListener theResourceChangeListener) {
		myResourceChangeListenerCache.remove(theResourceChangeListener);
		myResourceVersionCache.listenerRemoved(theResourceChangeListener);
	}

	@PostConstruct
	public void start() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(LOCAL_REFRESH_INTERVAL_MS, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IResourceChangeListenerRegistry myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshAllCachesIfNecessary();
		}
	}

	@Override
	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myResourceChangeListenerCache.clearListenersForUnitTest();
	}

	@Override
	@VisibleForTesting
	public void clearCacheForUnitTest() {
		myResourceVersionCache.clear();
		myNextRefreshByResourceName.clear();
	}

	@Override
	public ResourceChangeResult refreshAllCachesIfNecessary() {
		ResourceChangeResult retval =  new ResourceChangeResult();
		for (String resourceName : myResourceChangeListenerCache.resourceNames()) {
			retval = retval.plus(refreshCacheIfNecessary(resourceName));
		}
		// This will return true if at least 1 of the Resource Caches was refreshed
		return retval;
	}

	@Override
	public ResourceChangeResult refreshCacheIfNecessary(String theResourceName) {
		ResourceChangeResult retval =  new ResourceChangeResult();
		Instant nextRefresh = myNextRefreshByResourceName.computeIfAbsent(theResourceName, key -> Instant.MIN);
		if (nextRefresh.isBefore(now())) {
			retval = refreshCacheWithRetry(theResourceName);
		}
		return retval;
	}

	@Override
	public void requestRefresh(String theResourceName) {
		synchronized (this) {
			myNextRefreshByResourceName.put(theResourceName, Instant.MIN);
		}
	}

	@Override
	public ResourceChangeResult forceRefresh(String theResourceName) {
		requestRefresh(theResourceName);
		return refreshCacheWithRetry(theResourceName);
	}

	@Override
	public void requestRefreshIfWatching(IBaseResource theResource) {
		if (myResourceChangeListenerCache.hasListenerFor(theResource)) {
			requestRefresh(myFhirContext.getResourceType(theResource));
		}
	}

	@Override
	public ResourceChangeResult refreshCacheWithRetry(String theResourceName) {
		Retrier<ResourceChangeResult> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return doRefreshCachesAndNotifyListeners(theResourceName);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@Override
	public ResourceChangeResult refreshAllCachesImmediately() {
		StopWatch sw = new StopWatch();
		ResourceChangeResult retval = new ResourceChangeResult();
		for (String resourceType : myResourceChangeListenerCache.resourceNames()) {
			retval = retval.plus(doRefreshCachesAndNotifyListeners(resourceType));
		}
		ourLog.debug("Refreshed all caches in {}ms: {}", sw.getMillis(), retval);
		return retval;
	}

	private synchronized ResourceChangeResult doRefreshCachesAndNotifyListeners(String theResourceName) {
		ResourceChangeResult retval = new ResourceChangeResult();
		Set<ResourceChangeListenerWithSearchParamMap> listenerEntries = myResourceChangeListenerCache.getListenerEntries(theResourceName);
		if (listenerEntries.isEmpty()) {
			myResourceVersionCache.getMap(theResourceName).clear();
			return retval;
		}
		for (ResourceChangeListenerWithSearchParamMap listenerEntry : listenerEntries) {
			SearchParameterMap searchParamMap = listenerEntry.getSearchParameterMap();
			ResourceVersionMap newResourceVersionMap = myResourceVersionSvc.getVersionMap(theResourceName, searchParamMap);
			retval = retval.plus(myResourceChangeListenerCache.notifyListener(listenerEntry, myResourceVersionCache, newResourceVersionMap));
		}
		myNextRefreshByResourceName.put(theResourceName, now().plus(Duration.ofMillis(REMOTE_REFRESH_INTERVAL_MS)));
		return retval;
	}

	private static Instant now() {
		if (ourNowForUnitTests != null) {
			return ourNowForUnitTests;
		}
		return Instant.now();
	}

	@VisibleForTesting
	Instant getNextRefreshTimeForUnitTest(String theResourceName) {
		return myNextRefreshByResourceName.get(theResourceName);
	}

	@VisibleForTesting
	public int getResourceVersionCacheSizeForUnitTest(String theResourceName) {
		return myResourceVersionCache.getMap(theResourceName).size();
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
}
