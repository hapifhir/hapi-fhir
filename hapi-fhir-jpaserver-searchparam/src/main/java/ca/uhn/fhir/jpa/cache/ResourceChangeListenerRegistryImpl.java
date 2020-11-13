package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
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
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// FIXME KHS retool searchparam and subscription to use this
@Component
public class ResourceChangeListenerRegistryImpl implements IResourceChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerRegistryImpl.class);

	static long LOCAL_REFRESH_INTERVAL_MS = 10 * DateUtils.MILLIS_PER_SECOND;
	static long REMOTE_REFRESH_INTERVAL_MS = DateUtils.MILLIS_PER_HOUR;
	private static final int MAX_RETRIES = 60; // 5 minutes
	private static volatile Map<String, Instant> myNextRefreshByResourceName = new HashMap<>();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private ResourceChangeListenerCache myResourceChangeListenerCache;

	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();

	/**
	 *
	 * @param theResourceName the name of the resource the listener should be notified about
	 * @param theSearchParamMap the listener will only be notified of changes to resources that match this map
	 * @param theResourceChangeListener the listener to be notified
	 * @throws ca.uhn.fhir.parser.DataFormatException if theResourceName is not valid
	 */
	@Override
	public void registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParamMap, IResourceChangeListener theResourceChangeListener) {
		// validate the resource name
		myFhirContext.getResourceDefinition(theResourceName);
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
		if (nextRefresh.isBefore(Instant.now())) {
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

	// FIXME KHS test
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
		// FIXME KHS Should we assume that if COUNT == 0 then we do NOT have any Listeners for this ResourceType ?
		//           If so, then we can clear() the Cache for that ResourceType...
		myNextRefreshByResourceName.put(theResourceName, Instant.now().plus(Duration.ofMillis(REMOTE_REFRESH_INTERVAL_MS)));
		return retval;
	}

	@VisibleForTesting
	Instant getNextRefreshTimeForUnitTest(String theResourceName) {
		return myNextRefreshByResourceName.get(theResourceName);
	}

	@VisibleForTesting
	public int getResourceVersionCacheSizeForUnitTest(String theResourceName) {
		return myResourceVersionCache.getMap(theResourceName).size();
	}
}
