package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
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
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// FIXME KHS retool searchparam and subscription to use this
@Repository
public class VersionChangeListenerRegistryImpl implements IVersionChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeListenerRegistryImpl.class);

	static long LOCAL_REFRESH_INTERVAL_MS = 10 * DateUtils.MILLIS_PER_SECOND;
	static long REMOTE_REFRESH_INTERVAL_MS = DateUtils.MILLIS_PER_HOUR;
	private static final int MAX_RETRIES = 60; // 5 minutes
	private static volatile Map<String, Instant> myNextRefreshByResourceName = new HashMap<>();

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private FhirContext myFhirContext;

	private final ResourceVersionCache myResourceVersionCache = new ResourceVersionCache();
	private final VersionChangeListenerMap myListenerMap = new VersionChangeListenerMap();

	private RefreshVersionCacheAndNotifyListenersOnUpdate myInterceptor;

	/**
	 *
	 * @param theResourceName the name of the resource the listener should be notified about
	 * @param theSearchParamMap the listener will only be notified of changes to resources that match this map
	 * @param theVersionChangeListener the listener to be notified
	 * @throws ca.uhn.fhir.parser.DataFormatException if theResourceName is not valid
	 */
	@Override
	public void registerResourceVersionChangeListener(String theResourceName, SearchParameterMap theSearchParamMap, IVersionChangeListener theVersionChangeListener) {
		// validate the resource name
		myFhirContext.getResourceDefinition(theResourceName);
		myListenerMap.add(theResourceName, theVersionChangeListener, theSearchParamMap);
		myNextRefreshByResourceName.put(theResourceName, Instant.MIN);
	}

	@PostConstruct
	public void start() {
		myInterceptor = new RefreshVersionCacheAndNotifyListenersOnUpdate();
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(LOCAL_REFRESH_INTERVAL_MS, jobDetail);
	}

	@PreDestroy
	public void stop() {
		myInterceptorBroadcaster.unregisterInterceptor(myInterceptor);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IVersionChangeListenerRegistry myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshAllCachesIfNecessary();
		}
	}

	@Override
	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenerMap.clearListenersForUnitTest();
	}

	@Override
	@VisibleForTesting
	public void clearCacheForUnitTest() {
		myResourceVersionCache.clearForUnitTest();
		myNextRefreshByResourceName.clear();
	}

	@Override
	public long refreshAllCachesIfNecessary() {
		long retval = 0;
		for (String resourceName : myListenerMap.resourceNames()) {
			retval += refreshCacheIfNecessary(resourceName);
		}
		// This will return true if at least 1 of the Resource Caches was refreshed
		return retval;
	}

	@Override
	public long refreshCacheIfNecessary(String theResourceName) {
		long retval = 0;
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
	public long forceRefresh(String theResourceName) {
		requestRefresh(theResourceName);
		return refreshCacheWithRetry(theResourceName);
	}

	@Override
	public long refreshCacheWithRetry(String theResourceName) {
		Retrier<Long> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (this) {
				return doRefreshCachesAndNotifyListeners(theResourceName);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	@Override
	public long refreshAllCachesImmediately() {
		StopWatch sw = new StopWatch();
		long count = 0;
		for (String resourceType : myListenerMap.resourceNames()) {
			count += doRefreshCachesAndNotifyListeners(resourceType);
		}
		ourLog.debug("Refreshed all caches.  Updated {} entries in {}ms", count, sw.getMillis());
		return count;
	}

	private synchronized long doRefreshCachesAndNotifyListeners(String theResourceName) {
		Set<VersionChangeListenerEntry> listenerEntries = myListenerMap.getListenerEntries(theResourceName);
		if (listenerEntries.isEmpty()) {
			return 0;
		}
		long count = 0;
		for (VersionChangeListenerEntry listenerEntry : listenerEntries) {
			SearchParameterMap searchParamMap = listenerEntry.getSearchParameterMap();
			ResourceVersionMap newResourceVersionMap = myResourceVersionSvc.getVersionMap(theResourceName, searchParamMap);
			count += listenerEntry.notifyListener(myResourceVersionCache, newResourceVersionMap);
		}
		myNextRefreshByResourceName.put(theResourceName, Instant.now().plus(Duration.ofMillis(REMOTE_REFRESH_INTERVAL_MS)));
		return count;
	}

	@Interceptor
	public class RefreshVersionCacheAndNotifyListenersOnUpdate {

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void created(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
		public void deleted(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
		public void updated(IBaseResource theResource) {
			handle(theResource);
		}

		private void handle(IBaseResource theResource) {
			if (theResource == null) {
				return;
			}
			String resourceName = myFhirContext.getResourceType(theResource);
			synchronized (this) {
				requestRefresh(resourceName);
			}
		}
	}

	@VisibleForTesting
	Instant getNextRefreshTimeForUnitTest(String theResourceName) {
		return myNextRefreshByResourceName.get(theResourceName);
	}
}
